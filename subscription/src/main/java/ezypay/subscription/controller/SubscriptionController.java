package ezypay.subscription.controller;


import ezypay.subscription.exception.BusinessException;
import ezypay.subscription.model.SubscriptionRequest;
import ezypay.subscription.model.SubscriptionResponse;
import ezypay.subscription.service.SubscriptionServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static ezypay.subscription.constant.Constant.SUCCESS_CODE;
import static ezypay.subscription.constant.Constant.SUCCESS_MSG;

@RestController
public class SubscriptionController {

    @Autowired
    SubscriptionServices services;

    @PostMapping(value = "/v1/subscription/subscribe/recurring", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SubscriptionResponse> subscribeRecurring(
            @Valid @RequestBody SubscriptionRequest request
    ) {
        SubscriptionResponse response = new SubscriptionResponse();
        try {
            response = services.subscribeRecurring(request);
            response.setResponseCode(SUCCESS_CODE);
            response.setResponseMessage(SUCCESS_MSG);
        } catch (BusinessException ex) {
            response.setResponseCode(ex.getCode());
            response.setResponseMessage(ex.getMessage());
        } catch (Exception e) {
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
