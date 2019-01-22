package com.diqin.gateway.interceptor;

import com.alibaba.fastjson.JSON;
import com.diqin.gateway.common.GatewayConst;
import com.diqin.gateway.common.ResponseDto;
import com.diqin.gateway.enums.ResponseCodeEnum;
import com.diqin.gateway.utils.AesUtil;
import com.sun.istack.internal.Nullable;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Auther: guobing
 * @Date: 2019-1-15 10:56
 * @Description:
 */
@Data
@Component
@ConfigurationProperties
public class AuthInterceptor implements HandlerInterceptor {
    private Logger log = LoggerFactory.getLogger(AuthInterceptor.class);
    private List<String> interceptUrl;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("Auth verify interceptor executing ...");
        boolean flag = contains(request);

        if (flag) {
            log.info("开始验证TOKEN");
            String token = request.getHeader(GatewayConst.LOGIN_TOKEN);
            if (StringUtils.isEmpty(token)) {
                ResponseDto responseDto = ResponseDto.doRet(ResponseCodeEnum.TOKEN_NULL);
                response.getWriter().println(responseDto);
                log.info(responseDto.toString());
                return false;
            }

            log.info(token);

            String redisToken = AesUtil.AESDecode(token);
            if (StringUtils.isEmpty(redisToken)) {
                ResponseDto responseDto = ResponseDto.doRet(ResponseCodeEnum.TOKEN_ERROR);
                response.getWriter().println(responseDto);
                log.info(responseDto.toString());
                return false;
            }
            try {
                Map<String, String> tokenMap = JSON.parseObject(redisToken, Map.class);
                String userId = tokenMap.get("userId");
                String encodeToken = (String) redisTemplate.opsForValue().get(GatewayConst.LOGIN_TOKEN + userId);
                if (StringUtils.isEmpty(encodeToken)) {
                    ResponseDto responseDto = ResponseDto.doRet(ResponseCodeEnum.TOKEN_INVALID);
                    response.getWriter().println(responseDto);
                    log.info(responseDto.toString());
                    return false;
                }
                request.setAttribute("userId",userId);
            } catch (Exception ex) {
                log.error("", ex);
                ResponseDto responseDto = ResponseDto.doRet(ResponseCodeEnum.TOKEN_ERROR);
                response.getWriter().println(responseDto);
                log.info(responseDto.toString());
                return false;
            }
        }

        return true;
    }

    public boolean contains(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        if (requestUri.startsWith(request.getContextPath())) {
            requestUri = requestUri.substring(request.getContextPath().length(), requestUri.length());
        }

        log.info("requestUri={}",requestUri);
        //系统根目录
        if ("/".equals(requestUri)) {
            return false;
        }

        for (String url : interceptUrl) {
            if (url.endsWith("/**")) {
                String sUrl = url.substring(0, url.length() - 3);
                if (requestUri.startsWith(sUrl)) {
                    return true;
                }
            } else if (requestUri.startsWith(url)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
