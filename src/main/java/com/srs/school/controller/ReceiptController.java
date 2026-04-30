package com.srs.school.controller;

import com.srs.school.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/receipt")
public class ReceiptController {

    @Autowired
    private ReceiptService pdfService;

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getReceipt(@PathVariable String id) throws Exception {

        byte[] pdf = pdfService.generateReceipt(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=receipt.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}