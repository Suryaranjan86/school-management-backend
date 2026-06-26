package com.srs.school.entity;

import com.srs.school.dto.PaymentMode;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "school")
public class School {

    @Id
    @GeneratedValue(generator = "sch_id_gen")
    @GenericGenerator(name = "sch_id_gen", strategy = "com.srs.school.entity.idgenerator.SchIdGenerator")
    private String id;

    private String name;
    private String address;
    private String state;
    private String district;
    private String pin;
    private String phone;
    private String logoUrl;

    @Column(name = "payment_mode")
    private String paymentMode;

    @Column(name = "session_start_month")
    private String sessionStartMonth;

    @Column(name = "session_end_month")
    private String sessionEndMonth;
}
