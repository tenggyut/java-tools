package com.tenggyut.exception;

public class RetryFailedException extends Exception {

    private String msg;

    public RetryFailedException(String msg, int maxRetry) {
        this.msg = String.format("failed to do %s after retrying for %d times", msg, maxRetry);
    }

    @Override
    public String getMessage() {
        return msg;
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
