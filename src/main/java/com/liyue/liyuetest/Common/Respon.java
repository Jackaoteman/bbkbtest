package com.liyue.liyuetest.Common;

public class Respon {
    private Object data;
    private  String msg;
    private  String code;

    public Respon() {
    }

    public Respon(Object data, String msg, String code) {
        this.data = data;
        this.msg = msg;
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

