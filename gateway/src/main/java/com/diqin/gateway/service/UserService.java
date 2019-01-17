package com.diqin.gateway.service;

import com.alibaba.fastjson.JSON;
import com.diqin.gateway.common.GatewayConst;
import com.diqin.gateway.common.ResponseDto;
import com.diqin.gateway.enums.ResponseCodeEnum;
import com.diqin.gateway.mapper.User;
import com.diqin.gateway.repository.UserRepository;
import com.diqin.gateway.utils.AesUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 用户管理，主要用于用户相关业务
 * @Auther: guobing
 * @Date: 2019-1-15 10:59
 * @Description:
 */
@Data
@Service
public class UserService {

    private Logger log = LoggerFactory.getLogger(UserService.class);

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    @Autowired
    private UserRepository userRepository;

    public ResponseDto login(String userName, String password) {
        User user = userRepository.findByUserName(userName);
        if (null == user) {
            return ResponseDto.doRet(ResponseCodeEnum.USER_NOT_EXISTS);
        }
        if (!password.equals(user.getPassword())) {
            return ResponseDto.doRet(ResponseCodeEnum.LOGIN_PWD_ERROR);
        }

        log.info("login success,put token into redis...");
        String userId = user.getUserId();
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("userId", userId);
        tokenMap.put("uuid", UUID.randomUUID().toString());
        String token = AesUtil.AESEncode(JSON.toJSONString(tokenMap));

        redisTemplate.opsForValue().set(GatewayConst.LOGIN_TOKEN + userId, token,1, TimeUnit.DAYS);
        Map retMap = new HashMap();
        retMap.put("token", token);

        return ResponseDto.doSuccess(retMap);
    }


    public ResponseDto getUser(String  userId) {
        User user = userRepository.findOne(userId);
        if (null == user) {
            return ResponseDto.doRet(ResponseCodeEnum.USER_NOT_EXISTS);
        }
        return ResponseDto.doSuccess(user);

    }


}
