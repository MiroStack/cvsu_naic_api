package com.cvsu.cvsu_api.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "tbl_feedback")
public class FeedbackEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    Long id;

    @Column(name = "submitted_dt")
    String submittedDate;

    @Column(name = "respondent_role")
    String respondentRole;

    @Column(name = "purpose")
    String purpose;

    @Column(precision = 3, scale = 1, name = "rating")
    BigDecimal rating;

    @Column(name = "comment")
    String comment;
}
