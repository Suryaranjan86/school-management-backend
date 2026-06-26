package com.srs.school.controller;

import com.srs.school.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/receipt")
public class ReceiptController {

    private static final Logger logger = LoggerFactory.getLogger(ReceiptController.class);

    @Autowired
    private ReceiptService pdfService;

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getReceipt(@PathVariable String id) throws Exception {
        try {
            logger.info("Receipt request received for payment ID: {}", id);
            
            byte[] pdf = pdfService.generateReceipt(id);
            
            logger.info("PDF generated successfully, size: {} bytes", pdf.length);
            
            if (pdf.length == 0) {
                logger.error("Generated PDF is empty for payment ID: {}", id);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(null);
            }
            
            logger.info("Returning PDF response with proper headers");
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=receipt-" + id + ".pdf")
                    .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                    .header("Pragma", "no-cache")
                    .header(HttpHeaders.EXPIRES, "0")
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(pdf.length)
                    .body(pdf);
        } catch (Exception e) {
            logger.error("Error generating receipt for payment ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
}
