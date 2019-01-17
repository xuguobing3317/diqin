package com.diqin.gateway.enums;

import com.diqin.gateway.common.InvokeHelper;

import java.util.Map;


public enum ResponseCodeEnum {
	SUCCESS("000000", "请求处理成功"),
	PROCESSING("000001", "请求已受理"),
	PROCESS_ERROR("000002", "请求受理失败"),
	PROCESS_CANCEL("000003", "请求受理撤销"),
	USER_NOT_EXISTS("100001", "用户不存在"),
	LOGIN_PWD_ERROR("100002", "密码不正确"),
	TOKEN_NULL("100003", "TOKEN为空"),
	TOKEN_ERROR("100004", "TOKEN错误"),
	TOKEN_INVALID("100005", "TOKEN已失效"),
	PARAM_ERROR("100006", "参数错误"),
	_404("000404", "页面不存在"),
	_500("000500", "服务内部错误"),
	BUSI_ERROR("999999", "业务异常"),
	;
	private String code;
	private String desc;

	ResponseCodeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	public static ResponseCodeEnum getResponseCodeEnum(String value) {
		if (value != null) {
			for (ResponseCodeEnum nameEnum : values()) {
				if (nameEnum.getCode().equals(value)) {
					return nameEnum;
				}
			}
		}
		return null;
	}

	public static boolean isResponseCodeEnum(String value) {
		if (value != null) {
			for (ResponseCodeEnum nameEnum : values()) {
				if (nameEnum.getCode().equals(value)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static Map<String, Object> setRespMap(Map<String, Object> map,String code,String desc){
		if(map != null){
			map.put(InvokeHelper.RESP_CODE, code);
			map.put(InvokeHelper.RESP_MSG, desc);
		}
		return map;
	}
	
	public static Map<String, Object> setRespMap(Map<String, Object> map,ResponseCodeEnum rcEnum){
		if(rcEnum != null){
			map = setRespMap(map, rcEnum.getCode(), rcEnum.getDesc());
		}
		return map;
	}
	
	public static Map<String, Object> setRespMap(Map<String, Object> map,ResponseCodeEnum rcEnum,String desc){
		if(rcEnum != null){
			map = setRespMap(map, rcEnum.getCode(), desc);
		}
		return map;
	}

	public boolean isSuccess() {
		if (ResponseCodeEnum.SUCCESS.getCode().equals(this.getCode())) {
			return true;
		}
		return false;
	}
	

}
