package com.srs.school.controller;

import com.srs.school.dto.DefaulterStudentDto;
import com.srs.school.dto.FeePaymentRequestDto;
import com.srs.school.dto.FeePaymentResponseDto;
import com.srs.school.service.FeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fee-payments")
public class FeeController {

    @Autowired
    private FeeService feePaymentService;

    @PostMapping
    public ResponseEntity<FeePaymentResponseDto> addFeePayment(@RequestBody FeePaymentRequestDto feePaymentDto) {
        FeePaymentResponseDto savedFeePayment = feePaymentService.saveFeePayment(feePaymentDto);
        return ResponseEntity.ok(savedFeePayment);
    }


    @GetMapping("/{id}")
    public ResponseEntity<FeePaymentResponseDto> getFeePaymentById(@PathVariable String id) {
        FeePaymentResponseDto feePayment = feePaymentService.getFeePaymentById(id);
        if (feePayment != null) {
            return ResponseEntity.ok(feePayment);
        }
        return ResponseEntity.notFound().build();
    }


  @GetMapping("/defaulters/{academicYear}")
    public List<DefaulterStudentDto> getDefaulters(@PathVariable  String academicYear) {
        return feePaymentService.getDefaulters(academicYear);
    }


    @GetMapping("/student/{academicYear}")
    public ResponseEntity<List<FeePaymentResponseDto>> getFeePaymentsByStudentAndAcademicYear(
            @PathVariable String academicYear,
            @RequestParam(required = false) String studentId) {
        List<FeePaymentResponseDto> feePayments = feePaymentService.getFeePaymentsByAcademicYear(studentId, academicYear);
        return ResponseEntity.ok(feePayments);
    }
}
