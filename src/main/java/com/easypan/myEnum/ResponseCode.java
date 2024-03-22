package com.easypan.myEnum;
public enum ResponseCode {
    CODE_200(200, "成功"), CODE_404(404, "请求地址并不存在"), CODE_600(600, "请求参数错误")
    ,CODE_500(500, "服务器错误"), CODE_901(901,"登录超时，重新登录" ), CODE_904(904, "网盘空间不足");
    private Integer code;

    private String info;

    private ResponseCode(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public Integer getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
