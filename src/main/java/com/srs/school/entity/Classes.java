package com.srs.school.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "classes")
public class Classes {

    @Id
    @GeneratedValue(generator = "cls_id_gen")
    @GenericGenerator(name = "cls_id_gen", strategy = "com.srs.school.entity.idgenerator.ClsIdGenerator")
    private String id;

    private String name;
}
