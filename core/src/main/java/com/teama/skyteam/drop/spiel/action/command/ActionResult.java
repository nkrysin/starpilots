package com.teama.skyteam.drop.spiel.action.command;

import com.teama.skyteam.drop.spiel.action.validator.ValidationResult;

public class ActionResult {
    private final boolean valid;
    private final String msg;

    private ActionResult(boolean valid, String msg) {
        this.valid = valid;
        this.msg = msg;
    }

    public static ActionResult success() {
        return new ActionResult(true, null);
    }
    public static ActionResult fail(String msg) {
        return new ActionResult(false, msg);
    }
    public boolean isValid() {
        return valid;
    }
    public String getErrorMsg() {
        return msg;
    }
}
