

package ezypay.subscription;

import ezypay.subscription.controller.SubscriptionController;
import ezypay.subscription.model.SubscriptionRequest;
import ezypay.subscription.model.SubscriptionResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SubscriptionControllerTest {

    @Autowired
    SubscriptionController subject;

    @Test
    public void subscribeRecurring_weekly_success() {
        SubscriptionRequest request = new SubscriptionRequest();
        request.setChargeAmount("10.03");
        request.setStartDate("11/11/2020");
        request.setEndDate("09/02/2021");
        request.setSubscriptionType("WEEKLY");
        request.setSubscriptionValue("FRIDAY");
        ResponseEntity<SubscriptionResponse> responseEntity = subject.subscribeRecurring(request);

        Assert.assertThat(responseEntity.getBody().getResponseCode(), equalTo("0000"));
        Assert.assertThat(responseEntity.getBody().getAmount(), equalTo("10.03"));
        Assert.assertThat(responseEntity.getBody().getSubscriptionType(), equalTo("WEEKLY"));
        Assert.assertThat(responseEntity.getBody().getInvoiceDates(), equalTo("[2020-11-13, 2020-11-20, 2020-11-27, 2020-12-04, 2020-12-11, 2020-12-18, 2020-12-25, 2021-01-01, 2021-01-08, 2021-01-15, 2021-01-22, 2021-01-29, 2021-02-05]"));
    }

    @Test
    public void subscribeRecurring_daily_success() {
        SubscriptionRequest request = new SubscriptionRequest();
        request.setChargeAmount("52.33");
        request.setStartDate("11/12/2020");
        request.setEndDate("01/01/2021");
        request.setSubscriptionType("DAILY");
        ResponseEntity<SubscriptionResponse> responseEntity = subject.subscribeRecurring(request);

        Assert.assertThat(responseEntity.getBody().getResponseCode(), equalTo("0000"));
        Assert.assertThat(responseEntity.getBody().getAmount(), equalTo("52.33"));
        Assert.assertThat(responseEntity.getBody().getSubscriptionType(), equalTo("DAILY"));
        Assert.assertThat(responseEntity.getBody().getInvoiceDates(), equalTo("[2020-12-11, 2020-12-12, 2020-12-13, 2020-12-14, 2020-12-15, 2020-12-16, 2020-12-17, 2020-12-18, 2020-12-19, 2020-12-20, 2020-12-21, 2020-12-22, 2020-12-23, 2020-12-24, 2020-12-25, 2020-12-26, 2020-12-27, 2020-12-28, 2020-12-29, 2020-12-30, 2020-12-31, 2021-01-01]"));
    }

    @Test
    public void subscribeRecurring_monthly_success() {
        SubscriptionRequest request = new SubscriptionRequest();
        request.setChargeAmount("52.33");
        request.setStartDate("29/01/2021");
        request.setEndDate("01/04/2021");
        request.setSubscriptionType("MONTHLY");
        request.setSubscriptionValue("30");
        ResponseEntity<SubscriptionResponse> responseEntity = subject.subscribeRecurring(request);

        Assert.assertThat(responseEntity.getBody().getResponseCode(), equalTo("0000"));
        Assert.assertThat(responseEntity.getBody().getAmount(), equalTo("52.33"));
        Assert.assertThat(responseEntity.getBody().getSubscriptionType(), equalTo("MONTHLY"));
        Assert.assertThat(responseEntity.getBody().getInvoiceDates(), equalTo("[2021-01-30, 2021-02-28, 2021-03-30]"));
    }

    @Test
    public void subscribeRecurring_monthly_fail_invalidDateFormat() {
        SubscriptionRequest request = new SubscriptionRequest();
        request.setChargeAmount("52.33");
        request.setStartDate("29-01-2021");
        request.setEndDate("01-04-2021");
        request.setSubscriptionType("MONTHLY");
        request.setSubscriptionValue("30");
        ResponseEntity<SubscriptionResponse> responseEntity = subject.subscribeRecurring(request);

        Assert.assertThat(responseEntity.getBody().getResponseCode(), equalTo("3001"));
    }

    @Test
    public void subscribeRecurring_monthly_fail_missingSubscriptionValue() {
        SubscriptionRequest request = new SubscriptionRequest();
        request.setChargeAmount("52.33");
        request.setStartDate("29/01/2021");
        request.setEndDate("01/03/2021");
        request.setSubscriptionType("MONTHLY");
        ResponseEntity<SubscriptionResponse> responseEntity = subject.subscribeRecurring(request);

        Assert.assertThat(responseEntity.getBody().getResponseCode(), equalTo("3002"));
    }

    @Test
    public void subscribeRecurring_monthly_fail_invalidDateRange() {
        SubscriptionRequest request = new SubscriptionRequest();
        request.setChargeAmount("52.33");
        request.setStartDate("29/01/2021");
        request.setEndDate("01/08/2021");
        request.setSubscriptionType("MONTHLY");
        request.setSubscriptionValue("30");
        ResponseEntity<SubscriptionResponse> responseEntity = subject.subscribeRecurring(request);

        Assert.assertThat(responseEntity.getBody().getResponseCode(), equalTo("3003"));
    }
}

