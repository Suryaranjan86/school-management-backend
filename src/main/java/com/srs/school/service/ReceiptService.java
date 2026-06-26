package com.srs.school.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.srs.school.dto.StudentRequestDto;
import com.srs.school.dto.FeePaymentResponseDto;
import com.srs.school.entity.School;
import com.srs.school.dto.StudentAcademicRecord;
import com.srs.school.repository.ClassRepository;
import com.srs.school.repository.FeePaymentRepository;
import com.srs.school.repository.SequenceRepository;
import com.srs.school.repository.StudentAcademicRecordRepository;
import com.srs.school.repository.StudentRepository;
import com.srs.school.utils.ImageUtil;
import com.srs.school.utils.NumberToWords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;

@Service
public class ReceiptService {

    private static final Logger logger = LoggerFactory.getLogger(ReceiptService.class);

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private SequenceRepository sequenceRepository;

    @Autowired
    private FeePaymentRepository feePaymentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentAcademicRecordRepository studentAcademicRecordRepository;

    @Autowired
    private ClassRepository classRepository;

    public byte[] generateReceipt(String id) throws Exception {
        try {
            logger.info("Generating receipt for payment ID: {}", id);
            
            FeePaymentResponseDto payment = feePaymentRepository.findFeePaymentById(id);
            if (payment == null) {
                logger.error("Fee payment not found with id: {}", id);
                throw new RuntimeException("Fee payment not found with id: " + id);
            }
            
            logger.info("Found payment record. Student Acc Rec ID: {}", payment.getStudentAccRecId());
            byte[] pdf = this.generate(payment);
            logger.info("Successfully generated PDF of size: {} bytes", pdf.length);
            
            return pdf;
        } catch (Exception e) {
            logger.error("Error generating receipt for payment ID: {}", id, e);
            throw e;
        }
    }

    private byte[] generate(FeePaymentResponseDto payment) throws Exception {
        try {
            logger.info("Starting PDF generation process");
            
            School school = schoolService.getCurrentSchool();
            logger.info("Retrieved school: {}", school.getName());
            
            // Fetch StudentAcademicRecord using studentAccRecId
            StudentAcademicRecord studentAcademicRecord = studentAcademicRecordRepository.findById(payment.getStudentAccRecId())
                    .orElseThrow(() -> new RuntimeException("Student Academic Record not found with id: " + payment.getStudentAccRecId()));
            
            logger.info("Retrieved student academic record for student ID: {}", studentAcademicRecord.getStudentId());
            
            StudentRequestDto studentDto = studentService.getStudentById(studentAcademicRecord.getStudentId());
            logger.info("Retrieved student: {}", studentDto.getName());
            
            // Fetch class name using clsId
            String className = classRepository.findById(studentAcademicRecord.getClsId())
                    .map(cls -> cls.getName())
                    .orElseThrow(() -> new RuntimeException("Class not found with id: " + studentAcademicRecord.getClsId()));
            
            logger.info("Retrieved class: {}", className);

            Context ctx = new Context();

            ctx.setVariable("schoolName", school.getName());
            ctx.setVariable("address", school.getAddress());
            ctx.setVariable("phone", school.getPhone());

            String logo = ImageUtil.getBase64FromStatic(school.getLogoUrl());
            logger.info("Retrieved logo image, size: {} bytes", logo != null ? logo.length() : 0);

            ctx.setVariable("logo", logo);

            ctx.setVariable("receiptNo", payment.getReceiptNo());
            ctx.setVariable("date", payment.getPaymentDate());

            ctx.setVariable("studentName", studentDto.getName());
            ctx.setVariable("fatherName", studentDto.getFatherName());
            ctx.setVariable("className", className);
            ctx.setVariable("batch", studentAcademicRecord.getAcademicYear());

            ctx.setVariable("month", payment.getPayMonth());
            ctx.setVariable("amount", payment.getAmount());

            ctx.setVariable("amountWords",
                    NumberToWords.convert(payment.getAmount().longValue()) + " Rupees Only");

            ctx.setVariable("mode", payment.getModeOfPay());
            ctx.setVariable("txnId", payment.getTransactionId());

            logger.info("Processing HTML template");
            String html = templateEngine.process("receipt", ctx);
            logger.info("HTML template processed successfully, size: {} characters", html.length());

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            logger.info("Building PDF from HTML");
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            builder.toStream(out);
            builder.run();

            byte[] pdfBytes = out.toByteArray();
            logger.info("PDF generated successfully, size: {} bytes", pdfBytes.length);
            
            return pdfBytes;
        } catch (Exception e) {
            logger.error("Error in PDF generation process", e);
            throw e;
        }
    }

    public String generateReceiptNo() {

        Long seq = sequenceRepository.getNextReceipt();

        int year = LocalDate.now().getYear();

        return "RCPT-" + year + "-" + String.format("%05d", seq);
    }
}


