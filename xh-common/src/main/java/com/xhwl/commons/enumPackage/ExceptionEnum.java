package com.xhwl.commons.enumPackage;

import lombok.Getter;

@Getter
public enum ExceptionEnum {


    DELETE_FAIL(500,"删除失败"),
    INVALID_EMAIL(400, "邮箱格式不正确"),
    INVALID_REQUEST_PARAM(400,"请求参数有误"),
    DATA_SAVE_ERROR(500, "数据库保存异常"),
    DATA_TRANSFER_ERROR(500, "数据转换异常"),
    INVALID_USERNAME_PASSWORD(400, "账号或密码错误"),
    PARAMS_ERROR(400, "参数错误"),
    HAVE_TOKEN_ERROR(500,"申请token失败"),
    INVALID_USERNAME(500, "查询失败"),
    HAS_JTI_ERROR(500, "获取jti失败"),
    CONVERSION_ERROR(500, "转换异常"),
    NAME_NOT_ISNULL(400,"姓名不能为空"),
    GET_ACCESS_TOKEN_FAIL(500,"获取华为access_token失败"),
    DATA_WRITING_FAILED(500,"表格数据写入失败"),
    DATA_DOWNLOAD_FAILED(500, "导出数据到excel失败")
    ;


    private int status;
    private String message;

    ExceptionEnum(int status, String message) {
        this.status = status;
        this.message = message;
    }

}
