package com.diqin.gateway.common;

import com.alibaba.fastjson.JSON;
import com.diqin.gateway.enums.ResponseCodeEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: guobing
 * @Date: 2018-12-12 11:25
 * @Description:
 */
@Setter
@Getter
public class ResponseDto {

    private String responseCode;

    private String responseDesc;

    private Object data;

    public ResponseDto(String responseCode, String responseDesc, Object data) {
        this.responseCode = responseCode;
        this.responseDesc = responseDesc;
        this.data = data;
    }

    public static ResponseDto doSuccess() {
        return new ResponseDto(ResponseCodeEnum.SUCCESS);
    }

    public static ResponseDto doSuccess(Object data) {
        ResponseDto commonResponseDto =  new ResponseDto(ResponseCodeEnum.SUCCESS);
        commonResponseDto.setData(data);
        return commonResponseDto;
    }

    public static ResponseDto doFail() {
        return new ResponseDto(ResponseCodeEnum.PROCESS_ERROR);
    }

    public static ResponseDto doRet(String responseCode, String responseDesc) {
        return new ResponseDto(responseCode, responseDesc);
    }



    public static ResponseDto doRet(ResponseCodeEnum resp) {
        return new ResponseDto(resp);
    }


    public ResponseDto(Exception ex) {
        this.responseCode = ResponseCodeEnum.BUSI_ERROR.getCode();
        this.responseDesc = ResponseCodeEnum.BUSI_ERROR.getDesc();
    }

    public ResponseDto(ResponseCodeEnum respCode) {
        this.responseCode = respCode.getCode();
        this.responseDesc = respCode.getDesc();
    }

    public ResponseDto(String responseCode, String responseDesc) {
        this.responseCode = responseCode;
        this.responseDesc = responseDesc;
    }

    public ResponseDto(ResponseCodeEnum respCode, String responseDesc) {
        this.responseCode = respCode.getCode();
        this.responseDesc = responseDesc;
    }

    public boolean success() {
        if (ResponseCodeEnum.SUCCESS.getCode().equals(this.responseCode)) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        Map map = new HashMap();
        map.put(InvokeHelper.RESP_CODE, this.getResponseCode());
        map.put(InvokeHelper.RESP_MSG, this.getResponseDesc());
        map.put(InvokeHelper.DATA, null==this.getData()?"":this.getData());
        return JSON.toJSONString(map);
    }
}
