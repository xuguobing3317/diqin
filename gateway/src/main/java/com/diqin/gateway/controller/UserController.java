package com.diqin.gateway.controller;

import com.diqin.gateway.common.ResponseDto;
import com.diqin.gateway.dto.PersonDto;
import com.diqin.gateway.dto.PersonPageQueryDto;
import com.diqin.gateway.enums.ResponseCodeEnum;
import com.diqin.gateway.mapper.Person;
import com.diqin.gateway.service.AccessService;
import com.diqin.gateway.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: guobing
 * @Date: 2018-12-12 11:19
 * @Description:
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {

    private Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AccessService accessService;

    @RequestMapping(value="/login",method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto login (@RequestParam("userName") String userName, @RequestParam("password") String password){
        return userService.login(userName, password);
    }


    @RequestMapping(value="/showUser",method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto showUser (HttpServletRequest request){
        String userId = request.getAttribute("userId").toString();
        return userService.getUser(userId);
    }


    @RequestMapping(value="/getPerson",method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto getPerson (HttpServletRequest request){
        String userId = request.getAttribute("userId").toString();
        PersonPageQueryDto queryDto = new PersonPageQueryDto();
        queryDto.setUserId(userId);
        String accessTime = request.getParameter("accessTime");
        queryDto.setAccessTime(accessTime);
        String createTime = request.getParameter("createTime");
        queryDto.setCreateTime(createTime);
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));
        int pageNum = Integer.parseInt(request.getParameter("pageNum"));
        if (pageNum <= 0) {
            return ResponseDto.doRet(ResponseCodeEnum.PARAM_ERROR);
        }

        return accessService.getPersonPage(queryDto, pageSize, pageNum-1);
    }



    @RequestMapping(value="/getPerson2",method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto getPerson2 (HttpServletRequest request){
        String userId = request.getAttribute("userId").toString();
        PersonPageQueryDto queryDto = new PersonPageQueryDto();
        queryDto.setUserId(userId);
        String accessTime = request.getParameter("accessTime");
        queryDto.setAccessTime(accessTime);
        String createTime = request.getParameter("createTime");
        queryDto.setCreateTime(createTime);
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));
        int pageNum = Integer.parseInt(request.getParameter("pageNum"));
        if (pageNum <= 0) {
            return ResponseDto.doRet(ResponseCodeEnum.PARAM_ERROR);
        }

        return accessService.getPersonPage2(queryDto, pageSize, pageNum-1);
    }


    @RequestMapping(value="/getCount",method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto getCount (HttpServletRequest request){
        String userId = request.getAttribute("userId").toString();
        return accessService.getCount(userId);
    }


    @RequestMapping(value="/findList",method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto findList (HttpServletRequest request){
        String userId = request.getAttribute("userId").toString();
        String accessTime = request.getParameter("accessTime");
        return accessService.findList(userId,accessTime);
    }

}
