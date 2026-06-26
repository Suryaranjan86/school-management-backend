package com.srs.school.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeePaymentResponseDto {

    private String id;
    private String studentAccRecId;
    private String academicYear;
    private String clsId;
    private String clsName;
    private String payMonth;
    private String payYear;
    private Date paymentDate;
    private String transactionId;
    private String slipNo;
    private ModeOfPay modeOfPay;
    private Double amount;
    private String receiptNo;
    private String academicRollNo;
    private String fatherName;
    private String studentName;
}
