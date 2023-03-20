package com.xzp.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 时间未到，资格未够，继续努力！
 *
 * @Author xuezhanpeng
 * @Date 2022/10/25 18:42
 * @Version 1.0
 */
@Data
@ApiModel("返回结果实体")
@AllArgsConstructor
public class Result<T> {

    @ApiModelProperty(value = "返回码")
    private Integer code;

    @ApiModelProperty(value = "返回消息")
    private String message;

    @ApiModelProperty(value = "返回数据")
    private T data;

    public Result(){}

    protected static <T> Result<T> build(T data) {
        Result<T> result = new Result<T>();
        if (data != null) {
            result.setData(data);
        }
        return result;
    }

    protected static <T> Result<T> build(String message) {
        Result<T> result = new Result<T>();
        result.setMessage(message);
        return result;
    }

    public static <T> Result<T> build(T body, ResultCode resultCode) {
        Result<T> result = build(body);
        result.setCode(resultCode.getCode());
        result.setMessage(resultCode.getMessage());
        return result;
    }

    public static <T> Result<T> build(Integer code, String message) {
        Result<T> result = build(null);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public static<T> Result<T> success(){
        return Result.success(null);
    }

    /**
     * 操作成功
     * @param data
     * @param <T>
     * @return
     */
    public static<T> Result<T> success(T data){
        Result<T> result = build(data);
        return build(data, ResultCode.SUCCESS);
    }

    public static<T> Result<T> fail(){
        return Result.fail(null);
    }

    /**
     * 操作失败
     * @param data
     * @param <T>
     * @return
     */
    public static<T> Result<T> fail(T data){
        Result<T> result = build(data);
        return build(data, ResultCode.FAIL);
    }

    public static<T> Result<T> fail(Integer code,String message){
        Result<T> result = build(code,message);
        return result;
    }


    public static<T> Result<T> fail(String message){
        Result<T> result = build(ResultCode.FAIL.getCode(),message);
        return result;
    }

    public  Result<T> message(String msg){
        this.setMessage(msg);
        return this;
    }

    public Result<T> code(Integer code){
        this.setCode(code);
        return this;
    }

    public boolean isOk() {
        if(this.getCode().intValue() == ResultCode.SUCCESS.getCode().intValue()) {
            return true;
        }
        return false;
    }
}
