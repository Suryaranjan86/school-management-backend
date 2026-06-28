package com.srs.school.repository;

import com.srs.school.dto.ModeOfPay;
import com.srs.school.dto.FeePaymentResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Repository
public class FeePaymentRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger log = LoggerFactory.getLogger(FeePaymentRepository.class);

    /**
     * RowMapper for FeePayment entity
     */
    private final RowMapper<FeePaymentResponseDto> feePaymentRowMapper = (rs, rowNum) -> FeePaymentResponseDto.builder()
            .id(rs.getString("id"))
            .studentAccRecId(rs.getString("stu_aca_rec_id"))
            .academicYear(rs.getString("academic_year"))
            .clsId(rs.getString("cls_id"))
            .clsName(rs.getString("cls_name"))
            .payMonth(rs.getString("pay_month"))
            .payYear(rs.getString("pay_year"))
            .paymentDate(rs.getTimestamp("payment_date") != null ? new java.util.Date(rs.getTimestamp("payment_date").getTime()) : null)
            .transactionId(rs.getString("transaction_id"))
            .slipNo(rs.getString("offline_slip_no"))
            .modeOfPay(rs.getString("mode_of_pay") != null ? ModeOfPay.valueOf(rs.getString("mode_of_pay")) : null)
            .amount(rs.getDouble("amount"))
            .receiptNo(rs.getString("receipt_no"))
            .studentName(rs.getString("student_name"))
            .academicRollNo(rs.getString("academic_roll_no"))
            .build();

    /**
     * Find all paid months by academic year for a specific student
     * SQL Query: SELECT CONCAT(pay_month,'-',pay_year) FROM fee_payment 
     * INNER JOIN student_academic_record ON stu_aca_rec_id = student_academic_record.id
     * INNER JOIN student ON student_academic_record.student_id = student.id
     * WHERE student_id = ? AND academic_year = ? AND student.is_deleted='N'
     */
    public List<String> findStudentPaidMonthsByAcademicYear(
            String studentId,
            String academicYear,
            String schoolId) {
        log.info("Entering findStudentPaidMonthsByAcademicYear - studentId: {}, academicYear: {}, schoolId: {}", studentId, academicYear, schoolId);
        String sql = "SELECT CONCAT(f.pay_month, '-', f.pay_year) as paid_months " +
                "FROM fee_payment f " +
                "INNER JOIN student_academic_record sar ON f.stu_aca_rec_id = sar.id " +
                "INNER JOIN student s ON sar.student_id = s.id " +
                "WHERE sar.student_id = ? AND sar.academic_year = ? AND s.is_deleted='N' AND s.school_id = ? " +
                "ORDER BY f.pay_year DESC, f.pay_month DESC";

        return jdbcTemplate.queryForList(sql, String.class, studentId, academicYear, schoolId);
    }

    /**
     * Find fee payments by student academic record ID with optional academic year filter
     * SQL Query: SELECT f.id, f.stu_aca_rec_id, f.pay_month, f.pay_year, f.payment_date, 
     * f.transaction_id, f.offline_slip_no, f.mode_of_pay, f.amount, f.receipt_no, 
     * sar.academic_year, c.id as cls_id, c.name as cls_name, s.name as student_name FROM fee_payment f 
     * INNER JOIN student_academic_record sar ON f.stu_aca_rec_id = sar.id 
     * INNER JOIN student s ON sar.student_id = s.id
     * LEFT JOIN classes c ON sar.cls_id = c.id
     * WHERE sar.academic_year = ? AND (sar.student_id = ? or ? is null)
     */
    public List<FeePaymentResponseDto> findByAcademicYear(
            String studentId,
            String academicYear,
            String schoolId) {
        log.info("Entering findByAcademicYear - academicYear: {}, studentId: {}, schoolId: {}", academicYear, studentId, schoolId);
        StringBuilder sql = new StringBuilder(
                "SELECT f.id, f.stu_aca_rec_id, f.pay_month, f.pay_year, f.payment_date, " +
                "f.transaction_id, f.offline_slip_no, f.mode_of_pay, f.amount, f.receipt_no, " +
                "sar.academic_year, COALESCE(c.id, '') as cls_id, COALESCE(c.name, '') as cls_name, " +
                "COALESCE(s.name, '') as student_name,sar.class_roll_no  as academic_roll_no " +
                "FROM fee_payment f " +
                "INNER JOIN student_academic_record sar ON f.stu_aca_rec_id = sar.id " +
                "INNER JOIN student s ON sar.student_id = s.id " +
                "LEFT JOIN classes c ON sar.cls_id = c.id " +
                "WHERE sar.academic_year = ? AND s.is_deleted='N' AND s.school_id = ?");

        Object[] params;

        if (studentId != null) {
            sql.append(" AND sar.student_id = ?");
            params = new Object[]{academicYear, schoolId, studentId};
        } else {
            params = new Object[]{academicYear, schoolId};
        }

        return jdbcTemplate.query(sql.toString(), feePaymentRowMapper, params);
    }

    /**
     * Save a fee payment record to the database
     * INSERT INTO fee_payment (id, stu_aca_rec_id, pay_month, pay_year, 
     * payment_date, transaction_id, offline_slip_no, mode_of_pay, amount, receipt_no) 
     * VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
     */
    public int saveFeePayment(FeePaymentResponseDto feePayment) {
        log.info("Entering saveFeePayment - feePayment id: {}", feePayment != null ? feePayment.getId() : null);
        String sql = "INSERT INTO fee_payment (id, stu_aca_rec_id, pay_month, pay_year, " +
                "payment_date, transaction_id, offline_slip_no, mode_of_pay, amount, receipt_no) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        return jdbcTemplate.update(sql,
                feePayment.getId(),
                feePayment.getStudentAccRecId(),
                feePayment.getPayMonth(),
                feePayment.getPayYear(),
                feePayment.getPaymentDate(),
                feePayment.getTransactionId(),
                feePayment.getSlipNo(),
                feePayment.getModeOfPay() != null ? feePayment.getModeOfPay().toString() : null,
                feePayment.getAmount(),
                feePayment.getReceiptNo()
        );
    }


    /**
     * Find a fee payment by ID
     * SELECT f.id, f.stu_aca_rec_id, f.pay_month, f.pay_year, f.payment_date, 
     * f.transaction_id, f.offline_slip_no, f.mode_of_pay, f.amount, f.receipt_no, s.name as student_name
     * FROM fee_payment f 
     * INNER JOIN student_academic_record sar ON f.stu_aca_rec_id = sar.id
     * INNER JOIN student s ON sar.student_id = s.id
     * WHERE f.id = ? AND s.is_deleted='N'
     */
    public FeePaymentResponseDto findFeePaymentById(String id, String schoolId) {
        log.info("Entering findFeePaymentById - id: {}, schoolId: {}", id, schoolId);
        String sql = "SELECT f.id, f.stu_aca_rec_id, f.pay_month, f.pay_year, f.payment_date, " +
                "f.transaction_id, f.offline_slip_no, f.mode_of_pay, f.amount, f.receipt_no, " +
                "'' as academic_year, '' as cls_id, '' as cls_name, COALESCE(s.name, '') as student_name, " +
                "'' as academic_roll_no " +
                "FROM fee_payment f " +
                "INNER JOIN student_academic_record sar ON f.stu_aca_rec_id = sar.id " +
                "INNER JOIN student s ON sar.student_id = s.id " +
                "WHERE f.id = ? AND s.is_deleted='N' AND s.school_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, feePaymentRowMapper, id, schoolId);
        } catch (Exception e) {
            return null;
        }
    }

    
}
