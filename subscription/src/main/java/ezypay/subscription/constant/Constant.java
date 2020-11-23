package ezypay.subscription.constant;

public class Constant {

    public static final String REGEX_SUBSCRIPTION_TYPE = "(DAILY|WEEKLY|MONTHLY)";
    public static final String TYPE_WEEKLY = "WEEKLY";
    public static final String TYPE_MONTHLY = "MONTHLY";

    public static final String FAILURE_MESSAGE = "Failed with unexpected error";

    public static final String ERROR_CODE = "9999";
    public static final String SUCCESS_CODE = "0000";
    public static final String SUCCESS_MSG = "Success";

    public static final int ERROR_CODE_3001 = 3001;
    public static final int ERROR_CODE_3002 = 3002;
    public static final int ERROR_CODE_3003 = 3003;


    public static final String ERROR_MSG_3001 = "Invalid date format";
    public static final String ERROR_MSG_3002 = "Missing or invalid subscription type";
    public static final String ERROR_MSG_3003 = "Invalid date range";
}
