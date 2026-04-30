package com.srs.school.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.srs.school.entity.FeePayment;
import com.srs.school.entity.School;
import com.srs.school.entity.Student;
import com.srs.school.repository.FeePaymentRepository;
import com.srs.school.repository.SequenceRepository;
import com.srs.school.utils.ImageUtil;
import com.srs.school.utils.NumberToWords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;

@Service
public class ReceiptService {

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

    public byte[] generateReceipt(String id) throws Exception {
        FeePayment payment = feePaymentRepository.findById(id).get();
        return this.generate(payment);
    }
    private byte[] generate(FeePayment payment) throws Exception {

        School school=schoolService.getCurrentSchool();
        Student student=studentService.getStudentById(payment.getStudent().getId());
        Context ctx = new Context();

        ctx.setVariable("schoolName", school.getName());
        ctx.setVariable("address", school.getAddress());
        ctx.setVariable("phone", school.getPhone());

        String logo = ImageUtil.getBase64FromStatic(school.getLogoUrl());

        ctx.setVariable("logo", logo);

        ctx.setVariable("receiptNo", payment.getReceiptNo());
        ctx.setVariable("date", payment.getPaymentDate());

        ctx.setVariable("studentName", student.getName());
        ctx.setVariable("fatherName", student.getFatherName());
        ctx.setVariable("className", student.getCls().getName());
        ctx.setVariable("batch", payment.getBatch());

        ctx.setVariable("month", payment.getPayMonth());
        ctx.setVariable("amount", payment.getAmount());

        ctx.setVariable("amountWords",
                NumberToWords.convert(payment.getAmount().longValue()) + " Rupees Only");

        ctx.setVariable("mode", payment.getModeOfPay());
        ctx.setVariable("txnId", payment.getTransactionId());

        String html = templateEngine.process("receipt", ctx);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.withHtmlContent(html, null);
        builder.toStream(out);
        builder.run();

        return out.toByteArray();
    }
    public String generateReceiptNo() {

        Long seq = sequenceRepository.getNextReceipt();

        int year = LocalDate.now().getYear();

        return "RCPT-" + year + "-" + String.format("%05d", seq);
    }
}