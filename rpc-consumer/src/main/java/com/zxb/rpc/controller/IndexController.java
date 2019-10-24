package com.zxb.rpc.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zxb.rpc.entity.InfoUser;
import com.zxb.rpc.service.InfoUserService;
import com.zxb.rpc.util.IdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    InfoUserService userService;

    @RequestMapping("now")
    @ResponseBody
    public String index(){
        return  new Date().toString();
    }

    @RequestMapping("insert/{name}/{area}")
    @ResponseBody
    public List<InfoUser> insert(@PathVariable("name")String name, @PathVariable("area")String area) throws InterruptedException {
        InfoUser infoUser = new InfoUser(IdUtil.getId(), name, area);
        List<InfoUser> users = userService.insertInfoUser(infoUser);
        logger.info("返回用户信息记录:{}", JSON.toJSONString(users));
        return users;
    }

    @RequestMapping("getById/{userid}")
    @ResponseBody
    public InfoUser getById(@PathVariable("userid") String id){
        logger.info("根据ID查询用户信息:{}",id);
        return userService.getInfoUserById(id);
    }

    @RequestMapping("getNameById/{userid}")
    @ResponseBody
    public String getNameById(@PathVariable("userid") String id){
        logger.info("根据ID查询用户名称:{}",id);
        return userService.getNameById(id);
    }

    @RequestMapping("getAllUser")
    @ResponseBody
    public Map<String,InfoUser> getAllUser() throws InterruptedException {
        Map<String, InfoUser> allUser = userService.getAllUser();
        logger.info("查询所有用户信息：{}",JSONObject.toJSONString(allUser));
        return allUser;
    }
}
