package com.ironhack.ironbank.setting;

public class Settings {

    private static final String BANK_NAME = "IronBank";

    private static final String BANK_ACCOUNT_START_NAME = "IB";

    private static final String DAY_TO_APPLY_INTERESTS = "01";

    private static final String DAY_TO_APPLY_MAINTENANCE = "01";


    public static String getBANK_NAME() {
        return BANK_NAME;
    }

    public static String getBANK_ACCOUNT_START() {
        return BANK_ACCOUNT_START_NAME;
    }

    public static String getDAY_TO_APPLY_INTERESTS() {
        return DAY_TO_APPLY_INTERESTS;
    }

    public static String getDAY_TO_APPLY_MAINTENANCE() {
        return DAY_TO_APPLY_MAINTENANCE;
    }

}
