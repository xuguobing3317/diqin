package com.diqin.gateway.enums;

import com.diqin.gateway.common.InvokeHelper;

import java.util.Map;


public enum AccessStateEnum {
	NORMAL("1", "正常"),
	SYSTEM("2", "系统"),
	;
	private String code;
	private String desc;

	AccessStateEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	public static AccessStateEnum getResponseCodeEnum(String value) {
		if (value != null) {
			for (AccessStateEnum nameEnum : values()) {
				if (nameEnum.getCode().equals(value)) {
					return nameEnum;
				}
			}
		}
		return null;
	}

	public static boolean isResponseCodeEnum(String value) {
		if (value != null) {
			for (AccessStateEnum nameEnum : values()) {
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
	
	public static Map<String, Object> setRespMap(Map<String, Object> map,AccessStateEnum rcEnum){
		if(rcEnum != null){
			map = setRespMap(map, rcEnum.getCode(), rcEnum.getDesc());
		}
		return map;
	}
	
	public static Map<String, Object> setRespMap(Map<String, Object> map, AccessStateEnum rcEnum, String desc){
		if(rcEnum != null){
			map = setRespMap(map, rcEnum.getCode(), desc);
		}
		return map;
	}

}
