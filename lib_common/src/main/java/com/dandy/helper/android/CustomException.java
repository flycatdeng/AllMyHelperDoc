package com.dandy.helper.android;

public class CustomException extends Exception {

    private static final long serialVersionUID = 1L;

    public CustomException() {
    }

    public CustomException(String detailMessage) {
        super(detailMessage);
    }

    public CustomException(Throwable throwable) {
        super(throwable);
    }

    public CustomException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
