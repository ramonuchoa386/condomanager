package com.condocam.condomanager.infra.config.classes;

import org.springframework.http.HttpStatus;

public class APIResponse<T> {
    private Control control;
    private String message;
    private T data;

    public APIResponse(HttpStatus status, String message, T data) {
        this.control = new Control(status);
        this.message = message;
        this.data = data;
    }

    public Control getControl() {
        return control;
    }

    public void setControl(Control control) {
        this.control = control;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static class Control {
        private int status;

        public Control(HttpStatus status) {
            this.status = status.value();
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
