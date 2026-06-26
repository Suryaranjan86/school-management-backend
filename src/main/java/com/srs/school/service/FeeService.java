package com.srs.school.service;

import com.srs.school.context.SchoolContext;
import com.srs.school.dto.*;
import com.srs.school.entity.*;
import com.srs.school.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.IntStream;
import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FeeService {

    private final FeePaymentRepository feePaymentRepository;

    private final StudentAcademicRecordRepository studentAcademicRecordRepository;

    private final SchoolService schoolService;

    private final ReceiptService receiptService;
    private final SequenceRepository sequenceRepository;
    private final StudentRepository studentRepository;
    private final ClassService classService;
    
    private static final String[] MONTHS_ARRAY = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

    public FeePaymentResponseDto saveFeePayment(FeePaymentRequestDto feePaymentDto) {
        String schoolId = SchoolContext.getSchoolId();
        if (schoolId == null || schoolId.isEmpty()) {
            throw new RuntimeException("School ID is required");
        }

        // Get the studentAccRecId using studentId, academicYear, and clsId
        var studentAccRecord = studentAcademicRecordRepository.findByStudentIdAndAcademicYearAndClsId(
                feePaymentDto.getStudentId(),
                feePaymentDto.getAcademicYear(),
                feePaymentDto.getClsId(),
                schoolId
        );

        if (studentAccRecord.isEmpty()) {
            throw new RuntimeException("Student Academic Record not found for the given student, academic year, and class");
        }

        String studentAccRecId = studentAccRecord.get().getId();

        var feePayment = FeePaymentResponseDto.builder()
                .id(sequenceRepository.generateFeeId())
                .studentAccRecId(studentAccRecId)
                .payMonth(feePaymentDto.getPayMonth().toUpperCase())
                .payYear(feePaymentDto.getPayYear())
                .paymentDate(feePaymentDto.getPaymentDate())
                .transactionId(feePaymentDto.getTransactionId())
                .amount(feePaymentDto.getAmount())
                .receiptNo(receiptService.generateReceiptNo())
                .modeOfPay(ModeOfPay.valueOf(feePaymentDto.getModeOfPay().toUpperCase()))
                .build();
        int result = feePaymentRepository.saveFeePayment(feePayment);
        if (result > 0) {
            return feePayment;
        } else {
            throw new RuntimeException("Failed to save fee payment");
        }
    }


    public List<FeePaymentResponseDto> getFeePaymentsByAcademicYear(String studentId, String academicYear) {
        return feePaymentRepository.findByAcademicYear(studentId, academicYear);
    }


    public FeePaymentResponseDto getFeePaymentById(String id) {
        return feePaymentRepository.findFeePaymentById(id);
    }


    public List<DefaulterStudentDto> getDefaulters(String academicYear) {

        String schoolId = SchoolContext.getSchoolId();
        if (schoolId == null || schoolId.isEmpty()) {
            return List.of();
        }

        var school = schoolService.getSchoolById(schoolId);
        if (school == null) return List.of();

        // Only MONTHLY is supported for defaulter calculation as per requirement
        String paymentMode = school.getPaymentMode();
        if (paymentMode == null || paymentMode.isEmpty()) return List.of();

        if (!paymentMode.equalsIgnoreCase("MONTHLY")) {
            throw new RuntimeException("Payment mode not supported for defaulter calculation: " + paymentMode);
        }

        // Build session months list (month abbreviation + year) for the given academicYear
        // Parse academic year into startYear and endYear
        int[] years = parseAcademicYear(academicYear);
        if (years == null) return List.of();
        int sessionStartYear = years[0]; // academic start year

        int startIdx = getMonthIndex(school.getSessionStartMonth());
        int endIdx = getMonthIndex(school.getSessionEndMonth());
        if (startIdx == -1 || endIdx == -1) return List.of();

        // build full session month -> year mapping
        List<String> sessionMonths = new ArrayList<>();
        int idx = startIdx;
        int year = sessionStartYear;
        while (true) {
            String monYear = MONTHS_ARRAY[idx] + "-" + year;
            sessionMonths.add(monYear);
            if (idx == endIdx) break;
            idx = (idx + 1) % 12;
            if (idx == 0) year++;
        }

        // Now limit to months up to current month (system date)
        LocalDate now = LocalDate.now();
        int currentMonthIdx = now.getMonthValue() - 1;
        int currentYear = now.getYear();

        List<String> expectedMonths = sessionMonths.stream()
                .filter(m -> {
                    String[] parts = m.split("-");
                    if (parts.length != 2) return false;
                    String mon = parts[0];
                    int yr = Integer.parseInt(parts[1]);
                    int monIdx = getMonthIndex(mon);
                    if (yr < currentYear) return true;
                    if (yr > currentYear) return false;
                    return monIdx <= currentMonthIdx;
                })
                .collect(Collectors.toList());

        if (expectedMonths.isEmpty()) return List.of();

        List<StudentAcademicRecord> records = studentAcademicRecordRepository.findByAcademicYear(academicYear, schoolId);
        List<DefaulterStudentDto> defaulters = new ArrayList<>();

        for (StudentAcademicRecord rec : records) {
            // get paid months for this student in this academic year
            List<String> paid = feePaymentRepository.findStudentPaidMonthsByAcademicYear(rec.getStudentId(), academicYear);
            Set<String> paidSet = paid == null ? Collections.emptySet() : new HashSet<>(paid);

            List<String> pending = expectedMonths.stream()
                    .filter(em -> !paidSet.contains(em))
                    .collect(Collectors.toList());

            if (!pending.isEmpty()) {
                List<String> paidWithinExpected = expectedMonths.stream()
                        .filter(paidSet::contains)
                        .collect(Collectors.toList());

                String studentName = "";
                try {
                    var s = studentRepository.findById(rec.getStudentId());
                    if (s.isPresent()) studentName = s.get().getName();
                } catch (Exception ignored) {}

                DefaulterStudentDto dto = new DefaulterStudentDto();
                dto.setStudentId(rec.getStudentId());
                dto.setClassRollNo(rec.getClassRollNo());
                dto.setClassName(classService.getClassById(rec.getClsId()).getName());
                dto.setAcademicYear(academicYear);
                dto.setStudentName(studentName);
                dto.setExpectedInstallments(expectedMonths.size());
                dto.setPaidInstallments(paidWithinExpected);
                dto.setPendingInstallments(pending);
                defaulters.add(dto);
            }
        }

        return defaulters;
    }

    /**
     * Parse academic year string into start year and end year.
     * Supports formats like "2025-2026", "2025-26", "2025" (treated as start year)
     * Returns int[] {startYear, endYear} or null if cannot parse.
     */
    private int[] parseAcademicYear(String academicYear) {
        if (academicYear == null || academicYear.trim().isEmpty()) return null;
        String t = academicYear.trim();
        String[] parts = t.split("[^0-9]");
        List<String> nums = new ArrayList<>();
        for (String p : parts) if (!p.isEmpty()) nums.add(p);
        try {
            if (nums.size() >= 2) {
                int a = Integer.parseInt(nums.get(0));
                int b = Integer.parseInt(nums.get(1));
                if (nums.get(1).length() == 2) {
                    int century = (a / 100) * 100;
                    b = century + b;
                    if (b <= a) b += 100;
                }
                return new int[]{a, b};
            } else if (nums.size() == 1) {
                int a = Integer.parseInt(nums.get(0));
                return new int[]{a, a + 1};
            }
        } catch (NumberFormatException ignored) {}
        return null;
    }

    private int getMonthIndex(String monthStr) {
        if (monthStr == null) return -1;
        String t = monthStr.trim();
        if (t.isEmpty()) return -1;
        
        String key = t.length() >= 3 
            ? t.substring(0, 3).toUpperCase(Locale.ENGLISH) 
            : t.toUpperCase(Locale.ENGLISH);
        
        for (int i = 0; i < 12; i++) {
            if (MONTHS_ARRAY[i].equals(key)) {
                return i;
            }
        }
        
        // Fallback: try numeric
        try {
            int v = Integer.parseInt(t);
            if (v >= 1 && v <= 12) return v - 1;
        } catch (NumberFormatException ignored) {}
        
        return -1;
    }


}
