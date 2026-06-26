package com.srs.school.dto;

import lombok.Data;

import java.util.Date;

@Data
public class FeePaymentRequestDto {
    private String studentAccRecId;
    private String payMonth;
    private String payYear;
    private Date paymentDate;
    private String transactionId;
    private String modeOfPay;
    private double amount;
    private String slipId;
    private String studentId;
    private String clsId;
    private String academicYear;
}
