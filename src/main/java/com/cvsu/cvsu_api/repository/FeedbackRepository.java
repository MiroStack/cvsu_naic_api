package com.cvsu.cvsu_api.repository;

import com.cvsu.cvsu_api.entity.FeedbackEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<FeedbackEntity, Long> {

}
