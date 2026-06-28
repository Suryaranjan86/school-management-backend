package com.srs.school.controller;

import com.srs.school.dto.DefaulterStudentDto;
import com.srs.school.dto.FeePaymentRequestDto;
import com.srs.school.dto.FeePaymentResponseDto;
import com.srs.school.service.FeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/fee-payments")
public class FeeController {

    private static final Logger log = LoggerFactory.getLogger(FeeController.class);

    @Autowired
    private FeeService feePaymentService;

    @PostMapping
    public ResponseEntity<FeePaymentResponseDto> addFeePayment(@RequestBody FeePaymentRequestDto feePaymentDto) {
        log.info("Entering addFeePayment - feePaymentDto: {}", feePaymentDto);
        FeePaymentResponseDto savedFeePayment = feePaymentService.saveFeePayment(feePaymentDto);
        return ResponseEntity.ok(savedFeePayment);
    }


    @GetMapping("/{id}")
    public ResponseEntity<FeePaymentResponseDto> getFeePaymentById(@PathVariable String id) {
        log.info("Entering getFeePaymentById - id: {}", id);
        FeePaymentResponseDto feePayment = feePaymentService.getFeePaymentById(id);
        if (feePayment != null) {
            return ResponseEntity.ok(feePayment);
        }
        return ResponseEntity.notFound().build();
    }


  @GetMapping("/defaulters/{academicYear}")
    public List<DefaulterStudentDto> getDefaulters(@PathVariable  String academicYear) {
        log.info("Entering getDefaulters - academicYear: {}", academicYear);
        return feePaymentService.getDefaulters(academicYear);
    }


    @GetMapping("/student/{academicYear}")
    public ResponseEntity<List<FeePaymentResponseDto>> getFeePaymentsByStudentAndAcademicYear(
            @PathVariable String academicYear,
            @RequestParam(required = false) String studentId) {
        log.info("Entering getFeePaymentsByStudentAndAcademicYear - academicYear: {}, studentId: {}", academicYear, studentId);
        List<FeePaymentResponseDto> feePayments = feePaymentService.getFeePaymentsByAcademicYear(studentId, academicYear);
        return ResponseEntity.ok(feePayments);
    }
}
