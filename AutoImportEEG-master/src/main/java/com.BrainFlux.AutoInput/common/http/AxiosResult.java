package com.BrainFlux.AutoInput.common.http;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @Author：whl
 * @Version：1.0
 * @Date：2021/7/2-17:22
 * @Since:jdk1.8
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AxiosResult<T> {
    private int status;
    private String message;
    private T data;

    private AxiosResult(EnumStatus enumStatus, T data) {
        this.status = enumStatus.getStatus();
        this.message = enumStatus.getMessage();
        this.data = data;
    }
/*    private AxiosResult(EnumStatus enumStatus) {
        this.status = enumStatus.getStatus();
        this.message = enumStatus.getMessage();
    }*/

    public static<T> AxiosResult<T> getAxiosResult(EnumStatus enumStatus, T data){
        return new AxiosResult<T>(enumStatus,data);
    }
   /* public static<T> AxiosResult<T> getAxiosResult(EnumStatus enumStatus){
        return new AxiosResult<T>(enumStatus);
    }*/

    public static <T> AxiosResult<T> success(){
        return getAxiosResult(EnumStatus.SUCCESS,null);
    }

    public static <T> AxiosResult<T> success(T data){
        return getAxiosResult(EnumStatus.SUCCESS,data);
    }

    public static <T> AxiosResult<T> error(){
        return getAxiosResult(EnumStatus.ERROR,null);
    }

    public static <T> AxiosResult<T> error(T data){
        return getAxiosResult(EnumStatus.ERROR,data);
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
