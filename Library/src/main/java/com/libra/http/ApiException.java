package com.libra.http;

import android.content.Context;
import com.libra.R;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by libra on 16/3/21 上午10:12.
 */
public class ApiException extends Exception {
    /**
     * 定义异常类型
     */
    public final static byte TYPE_NETWORK = 0x01;
    public final static byte TYPE_SOCKET = 0x02;
    public final static byte TYPE_HTTP_CODE = 0x03;
    public final static byte TYPE_HTTP_ERROR = 0x04;
    public final static byte TYPE_JSON = 0x05;
    public final static byte TYPE_IO = 0x06;
    public final static byte TYPE_RUN = 0x07;
    public final static byte TYPE_HttpResponseException = 0x08;
    private byte type;
    private int code;


    public ApiException() {
        super();
    }


    public ApiException(int code) {
        super();
        this.code = code;
    }


    public ApiException(Throwable e) {
        super(e);
    }


    private ApiException(byte type, int code, Exception excp) {
        super(excp);
        this.type = type;
        this.code = code;
    }


    private ApiException(byte type, int code, Throwable throwable) {
        super(throwable);
        this.type = type;
        this.code = code;
    }


    public int getCode() {
        return this.code;
    }


    public int getType() {
        return this.type;
    }


    /**
     * 提示友好的错误信息
     */
    public String getErrorMessage(Context ctx) {
        String err = ctx.getString(R.string.http_exception_error);
        switch (this.getType()) {
            case TYPE_HTTP_CODE:
                err = ctx.getString(R.string.http_status_code_error,
                        this.getCode());
                break;
            case TYPE_HTTP_ERROR:
                err = ctx.getString(R.string.http_exception_error);
                break;
            case TYPE_SOCKET:
                err = ctx.getString(R.string.socket_exception_error);
                break;
            case TYPE_NETWORK:
                err = ctx.getString(R.string.network_not_connected);
                break;
            case TYPE_JSON:
                err = ctx.getString(R.string.json_parser_failed);
                break;
            case TYPE_IO:
                err = ctx.getString(R.string.io_exception_error);
                break;
            case TYPE_RUN:
                err = ctx.getString(R.string.app_run_code_error);
                break;
            case TYPE_HttpResponseException:
                err = getMessage();
                break;
        }
        return err;
    }


    public static ApiException e(Throwable e) {
        if (e instanceof UnknownHostException ||
                e instanceof ConnectException) {
            return new ApiException(TYPE_NETWORK, 0, e);
        }
        else if (e instanceof HttpException ||
                e instanceof SocketTimeoutException) {
            return new ApiException(TYPE_HTTP_ERROR, 0, e);
        }
        else if (e instanceof SocketException) {
            return new ApiException(TYPE_SOCKET, 0, e);
        }
        return new ApiException(TYPE_HttpResponseException, 0, e);
    }
}
