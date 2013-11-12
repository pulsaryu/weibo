package me.yuxing.weibo.request;

/**
 * Created by yuxing on 13-11-2.
 */
public class ApiError extends Throwable {
    public String error;
    public int error_code;
    public String request;

    public ApiError(){
        this(null);
    }

    public ApiError(String message) {
        super();
        error = message;
    }

    @Override
    public String getMessage() {
        return error;
    }

    public int getCode() {
        return error_code;
    }

    public String getRequest() {
        return request;
    }
}
