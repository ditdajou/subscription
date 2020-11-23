package ezypay.subscription.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

import static ezypay.subscription.constant.Constant.ERROR_CODE;
import static ezypay.subscription.constant.Constant.FAILURE_MESSAGE;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubscriptionResponse implements Serializable {

    private String responseCode;
    private String responseMessage;

    private String amount;
    private String subscriptionType;
    private String invoiceDates;

    public SubscriptionResponse() {
        //initialize with error
        this.responseCode = ERROR_CODE;
        this.responseMessage = FAILURE_MESSAGE;
    }

}
