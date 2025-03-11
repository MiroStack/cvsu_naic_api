package com.cvsu.cvsu_api.entity;

import jakarta.persistence.*;
import lombok.Data;

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

    @Column(name = "rating")
    int rating;
}
