package com.BrainFlux.AutoInput.config;

/**
 * @Author：whl
 * @Version：1.0
 * @Date：2021/7/2-17:23
 * @Since:jdk1.8
 */
public enum EnumStatus {
    SUCCESS(20000,"请求成功"),
    NO_CONTENT(20004,"没有内容"),
    UN_AUTHORIZED(40001,"没有授权"),
    FORBIDDEN(40003,"禁止"),
    ERROR(50000,"请求失败");
    private int status;
    private String message;


    EnumStatus(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
