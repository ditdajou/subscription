package ezypay.subscription.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.math.BigDecimal;

import static ezypay.subscription.constant.Constant.REGEX_SUBSCRIPTION_TYPE;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class SubscriptionRequest implements Serializable {
    @NotNull
    private String chargeAmount;

    @Pattern(regexp = REGEX_SUBSCRIPTION_TYPE)
    private String subscriptionType;

    private String subscriptionValue;

    private String startDate;

    private String endDate;

}
