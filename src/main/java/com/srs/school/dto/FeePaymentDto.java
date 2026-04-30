package com.srs.school.dto;

import lombok.Data;

import java.util.Date;

@Data
public class FeePaymentDto {
    private String studentId;
    private String classId;
    private String batch;
    private String payMonth;
    private String payYear;
    private Date paymentDate;
    private String transactionId;
    private String modeOfPay;
    private double amount;
}
