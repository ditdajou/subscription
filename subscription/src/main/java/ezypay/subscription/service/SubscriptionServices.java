package ezypay.subscription.service;


import ezypay.subscription.exception.BusinessException;
import ezypay.subscription.model.DateRange;
import ezypay.subscription.model.SubscriptionRequest;
import ezypay.subscription.model.SubscriptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ezypay.subscription.constant.Constant.ERROR_CODE_3001;
import static ezypay.subscription.constant.Constant.ERROR_CODE_3002;
import static ezypay.subscription.constant.Constant.ERROR_CODE_3003;
import static ezypay.subscription.constant.Constant.ERROR_MSG_3001;
import static ezypay.subscription.constant.Constant.ERROR_MSG_3002;
import static ezypay.subscription.constant.Constant.ERROR_MSG_3003;
import static ezypay.subscription.constant.Constant.TYPE_MONTHLY;
import static ezypay.subscription.constant.Constant.TYPE_WEEKLY;


@Service
@Slf4j
public class SubscriptionServices {

    public SubscriptionResponse subscribeRecurring(SubscriptionRequest request) {

        DateRange dateRange = validateAndReturnDateRange(request.getStartDate(), request.getEndDate());
        LocalDate startDate = dateRange.getStartDate();
        LocalDate endDate = dateRange.getEndDate();

        String subscriptionType = request.getSubscriptionType();
        String subscriptionValue = request.getSubscriptionValue();

        SubscriptionResponse response = new SubscriptionResponse();
        response.setAmount(request.getChargeAmount());
        response.setSubscriptionType(subscriptionType);

        if (TYPE_MONTHLY.equals(subscriptionType)) {
            if (StringUtils.isNotEmpty(subscriptionValue)) {
                response.setInvoiceDates(returnMonthlyInvoiceDates(startDate, endDate, subscriptionValue));
            } else {
                log.error("SubscriptionValue can't be empty when subscriptionType is WEEKLY or MONTHLY");
                throw new BusinessException(ERROR_CODE_3002, ERROR_MSG_3002);
            }
        } else if (TYPE_WEEKLY.equals(subscriptionType)) {
            if (StringUtils.isNotEmpty(subscriptionValue)) {
                response.setInvoiceDates(returnWeeklyInvoiceDates(startDate, endDate, subscriptionValue));
            } else {
                log.error("SubscriptionValue can't be empty when subscriptionType is WEEKLY or MONTHLY");
                throw new BusinessException(ERROR_CODE_3002, ERROR_MSG_3002);
            }
        } else {
            response.setInvoiceDates(returnDailyInvoiceDates(startDate, endDate));
        }
        return response;
    }

    private DateRange validateAndReturnDateRange(String startDateStr, String endDateStr) {
        LocalDate startDate;
        LocalDate endDate;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            startDate = LocalDate.parse(startDateStr, formatter);
            endDate = LocalDate.parse(endDateStr, formatter);
        } catch (Exception e) {
            log.error("The date format is incorrect , please use date format dd/MM/yyyy");
            throw new BusinessException(ERROR_CODE_3001, ERROR_MSG_3001);
        }

        if (startDate.isBefore(endDate)) {
            Period diff = Period.between(startDate, endDate.plusDays(1));
            if (diff.getMonths() >= 3) {
                log.error("Date range is invalid");
                throw new BusinessException(ERROR_CODE_3003, ERROR_MSG_3003);
            }
        } else {
            log.error("Date range is invalid");
            throw new BusinessException(ERROR_CODE_3003, ERROR_MSG_3003);
        }

        return new DateRange(startDate, endDate);
    }


    private String returnMonthlyInvoiceDates(LocalDate startDate, LocalDate endDate, String monthlyDate) {
        List<LocalDate> invoiceDates = new ArrayList<>();
        LocalDate originalDayOfMonth = startDate.withDayOfMonth(Integer.valueOf(monthlyDate));
        int monthCounter = 1;
        LocalDate dayOfMonth = originalDayOfMonth;
        while ((dayOfMonth != null) & (!dayOfMonth.isAfter(endDate))) {
            invoiceDates.add(dayOfMonth);
            dayOfMonth = originalDayOfMonth.plusMonths(monthCounter);
            monthCounter += 1;
        }
        ;
        return invoiceDates.toString();
    }

    private String returnWeeklyInvoiceDates(LocalDate startDate, LocalDate endDate, String weeklyDay) {
        List<LocalDate> invoiceDates = new ArrayList<>();
        LocalDate dayOfWeek = startDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.valueOf(weeklyDay)));
        while ((dayOfWeek != null) & (!dayOfWeek.isAfter(endDate))) {
            invoiceDates.add(dayOfWeek);
            dayOfWeek = dayOfWeek.plusWeeks(1);
        }
        ;
        return invoiceDates.toString();
    }

    private String returnDailyInvoiceDates(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> invoiceDates = Stream.iterate(startDate, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(startDate, endDate.plusDays(1)))
                .collect(Collectors.toList());
        return invoiceDates.toString();
    }

}
