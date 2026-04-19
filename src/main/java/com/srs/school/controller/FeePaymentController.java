package com.srs.school.controller;

import com.srs.school.dto.FeePaymentDto;
import com.srs.school.entity.FeePayment;
import com.srs.school.service.FeePaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fee-payments")
public class FeePaymentController {

    @Autowired
    private FeePaymentService feePaymentService;

    @PostMapping
    public ResponseEntity<FeePayment> addFeePayment(@RequestBody FeePaymentDto feePaymentDto) {
        FeePayment savedFeePayment = feePaymentService.saveFeePayment(feePaymentDto);
        return ResponseEntity.ok(savedFeePayment);
    }

    @GetMapping
    public ResponseEntity<List<FeePayment>> getAllFeePayments() {
        List<FeePayment> feePayments = feePaymentService.getAllFeePayments();
        return ResponseEntity.ok(feePayments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeePayment> getFeePaymentById(@PathVariable String id) {
        FeePayment feePayment = feePaymentService.getFeePaymentById(id);
        if (feePayment != null) {
            return ResponseEntity.ok(feePayment);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeePayment(@PathVariable String id) {
        feePaymentService.deleteFeePayment(id);
        return ResponseEntity.noContent().build();
    }
}
