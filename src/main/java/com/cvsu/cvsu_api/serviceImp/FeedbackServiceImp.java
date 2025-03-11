package com.cvsu.cvsu_api.serviceImp;

import com.cvsu.cvsu_api.entity.FeedbackEntity;
import com.cvsu.cvsu_api.model.ResponseModel;
import com.cvsu.cvsu_api.repository.FeedbackRepository;
import com.cvsu.cvsu_api.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class FeedbackServiceImp implements FeedbackService {
    @Autowired
    FeedbackRepository feedbackRepository;
    @Override
    public List<FeedbackEntity> feedbackList() {
        try{
            return feedbackRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseModel submitFeedBack(FeedbackEntity feedbackEntity) {
        ResponseModel res = new ResponseModel();
        try{
            feedbackRepository.save(feedbackEntity);
            res.setMessage("Feedback submitted successfully.");
            res.setStatusCode(200);
            res.setSuccess(true);
            return res;

        }catch(Exception e){
           res.setSuccess(false);
           res.setStatusCode(500);
           res.setMessage("Failed to submit feedback. Error occured: "+e.getMessage());
           return res;
        }
    }
}
