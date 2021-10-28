package com.seckill.response;

public class CommonReturnType {
    // 表明对应请求的返回处理结果 "success" 或 "fail"
    private String status;
    // 若 status == "success", 则 data 返回前端需要的json数据
    // 若 status == "fail", 则 data 使用通用的错误码格式
    private Object data;

    public static CommonReturnType create(Object result) {
        return CommonReturnType.create(result, "success");
    }

    public static CommonReturnType create(Object result, String status) {
        CommonReturnType type = new CommonReturnType();
        type.setData(result);
        type.setStatus(status);
        return type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
