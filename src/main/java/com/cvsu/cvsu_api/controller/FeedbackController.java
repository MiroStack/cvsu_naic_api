package com.cvsu.cvsu_api.controller;

import com.cvsu.cvsu_api.entity.FeedbackEntity;
import com.cvsu.cvsu_api.model.ResponseModel;
import com.cvsu.cvsu_api.serviceImp.FeedbackServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cvsu")
public class FeedbackController {
    @Autowired
    FeedbackServiceImp feedbackServiceImp;

    @GetMapping("displayAllFeedback")
    public ResponseEntity<List<FeedbackEntity>> displayAllFeedback(){
        try{
            return new ResponseEntity<List<FeedbackEntity>>(feedbackServiceImp.feedbackList(), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("submitFeedback")
    public ResponseEntity<ResponseModel> submitFeedback(@RequestBody FeedbackEntity entity){
        try{
            return new ResponseEntity<ResponseModel>(feedbackServiceImp.submitFeedBack(entity), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseModel>(HttpStatus.BAD_REQUEST);
        }
    }
}
