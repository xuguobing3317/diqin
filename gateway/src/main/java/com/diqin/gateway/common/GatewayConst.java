package com.diqin.gateway.common;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Auther: guobing
 * @Date: 2019-1-15 11:05
 * @Description:
 */
@Data
public class GatewayConst {

    private Logger log = LoggerFactory.getLogger(GatewayConst.class);


    public static final String LOGIN_TOKEN = "token";


}
