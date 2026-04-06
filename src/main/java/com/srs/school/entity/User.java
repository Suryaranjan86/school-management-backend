package com.srs.school.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(generator = "usr_id_gen")
    @GenericGenerator(name = "usr_id_gen", strategy = "com.srs.school.entity.idgenerator.UsrIdGenerator")
    private String id;

    private String name;
    private String email;
    private String username;
    private String password;
    private String role;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;
}
