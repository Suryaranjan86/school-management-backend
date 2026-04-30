package com.srs.school.service;

import com.srs.school.dto.FeePaymentDto;
import com.srs.school.entity.Classes;
import com.srs.school.entity.FeePayment;
import com.srs.school.entity.ModeOfPay;
import com.srs.school.entity.Student;
import com.srs.school.repository.ClassRepository;
import com.srs.school.repository.FeePaymentRepository;
import com.srs.school.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeePaymentService {

    @Autowired
    private FeePaymentRepository feePaymentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private ReceiptService receiptService;

    public FeePayment saveFeePayment(FeePaymentDto feePaymentDto) {
        Student student = studentRepository.findById(feePaymentDto.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Classes cls = classRepository.findById(feePaymentDto.getClassId())
                .orElseThrow(() -> new RuntimeException("Class not found"));
        var feePayment = FeePayment.builder()
                .student(student)
                .cls(cls)
                .batch(feePaymentDto.getBatch())
                .payMonth(feePaymentDto.getPayMonth())
                .payYear(feePaymentDto.getPayYear())
                .paymentDate(feePaymentDto.getPaymentDate())
                .transactionId(feePaymentDto.getTransactionId())
                .amount(feePaymentDto.getAmount())
                .receiptNo(receiptService.generateReceiptNo())
                .modeOfPay(ModeOfPay.valueOf(feePaymentDto.getModeOfPay().toUpperCase()))
                .build();
        return feePaymentRepository.save(feePayment);
    }

    public List<FeePayment> getAllFeePayments() {
        return feePaymentRepository.findAll();
    }

    public FeePayment getFeePaymentById(String id) {
        Optional<FeePayment> feePayment = feePaymentRepository.findById(id);
        return feePayment.orElse(null);
    }

    public void deleteFeePayment(String id) {
        feePaymentRepository.deleteById(id);
    }
}
