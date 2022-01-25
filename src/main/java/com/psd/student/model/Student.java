package com.psd.student.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;

@Entity
//@Getter
//@Setter
//@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor

@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stud_seq")
    @SequenceGenerator(name = "stud_seq", sequenceName = "stud_seq", initialValue = 10, allocationSize = 50)
    private int studId;
    private String firstName;
    private String lastName;
    private int year;
    private String dob;
    private String doj;
}
