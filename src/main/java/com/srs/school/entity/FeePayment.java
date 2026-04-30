package com.srs.school.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "fee_payment")
public class FeePayment {

    @Id
    @GeneratedValue(generator = "fee_id_gen")
    @GenericGenerator(name = "fee_id_gen", strategy = "com.srs.school.entity.idgenerator.FeeIdGenerator")
    private String id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "cls_id")
    private Classes cls;

    private String batch;
    private String payMonth;
    private String payYear;
    private Date paymentDate;
    private String transactionId;
    @Enumerated(EnumType.STRING)
    private ModeOfPay modeOfPay;
    private Double amount;
    private String receiptNo;
}
