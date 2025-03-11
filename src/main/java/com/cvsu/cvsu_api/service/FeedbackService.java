package com.cvsu.cvsu_api.service;

import com.cvsu.cvsu_api.entity.FeedbackEntity;
import com.cvsu.cvsu_api.model.ResponseModel;

import java.util.List;

public interface FeedbackService {
    List<FeedbackEntity> feedbackList();
    ResponseModel submitFeedBack(FeedbackEntity feedbackEntity);
}
