package com.mmall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * @author kong
 * 服务端响应对象
 * @param <T>
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> implements Serializable {

  private String msg;

  private T data;

  private Integer status;

  private ServerResponse(final String msg, final T data, final Integer status) {
    this.msg = msg;
    this.data = data;
    this.status = status;
  }

  private ServerResponse(final T data, final Integer status) {
    this.data = data;
    this.status = status;
  }

  private ServerResponse(final String msg, final Integer status) {
    this.msg = msg;
    this.status = status;
  }

  private ServerResponse(final Integer status) {
    this.status = status;
  }

  @JsonIgnore
  public boolean isSuccess(){
    return this.status == ResponseCode.SUCCESS.getCode();
  }

  public String getMsg() {
    return msg;
  }

  public T getData() {
    return data;
  }

  public Integer getStatus() {
    return status;
  }

  public static <T> ServerResponse createBySuccess(){
    return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
  }

  public static <T> ServerResponse createBySuccessMessage(String msg){
    return new ServerResponse<T>(msg,ResponseCode.SUCCESS.getCode());
  }

  public static <T> ServerResponse createBySuccess(T data){
    return new ServerResponse<T>(data,ResponseCode.SUCCESS.getCode());
  }

  public static <T> ServerResponse createBySuccess(String msg,T data){
    return new ServerResponse<T>(msg,data,ResponseCode.SUCCESS.getCode());
  }

  public static <T> ServerResponse createByError(){
    return new ServerResponse<T>(ResponseCode.ERROR.getDesc(),ResponseCode.ERROR.getCode());
  }

  public static <T> ServerResponse createByErrorMessage(String message){
    return new ServerResponse<T>(message,ResponseCode.ERROR.getCode());
  }

  public static <T> ServerResponse createByErroCoderMessage(String message,int errorCode){
    return new ServerResponse<T>(message,errorCode);
  }
}
