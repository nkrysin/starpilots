package com.teama.skyteam.drop.spiel.action.validator;

public class ValidationResult {
    private final boolean valid;
    private final String msg;

    private ValidationResult(boolean valid, String msg) {
        this.valid = valid;
        this.msg = msg;
    }

    public static ValidationResult success() {
        return new ValidationResult(true, null);
    }
    public static ValidationResult fail(String msg) {
        return new ValidationResult(false, msg);
    }
    public boolean isValid() {
        return valid;
    }
    public String getErrorMsg() {
        return msg;
    }

}
