package com.lalaalal.droni;

import androidx.annotation.Nullable;

public class LoginException extends Exception {
    public static final int EMPTY_FIELD = 1;
    public static final int NOT_LOGGED_IN = 2;

    private int errNo;

    public LoginException(int errNo) {
        this.errNo = errNo;
    }

    @Nullable
    @Override
    public String getMessage() {
        if (errNo == EMPTY_FIELD)
            return "아이디, 비밀번호를 입력해 주세요!";
        return super.getMessage();
    }
}
