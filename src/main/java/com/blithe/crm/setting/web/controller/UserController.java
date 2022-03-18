package com.blithe.crm.setting.web.controller;


import com.blithe.crm.setting.domain.User;
import com.blithe.crm.setting.service.UserService;
import com.blithe.crm.utils.MD5Util;
import com.blithe.crm.utils.PrintJson;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Author:  blithe.xwj
 * Date:    2022/3/17 09:31
 * Description:
 */

@Controller
@RequestMapping("settings/user")
public class UserController {

    @Resource
    private UserService userService;

    // @RequestMapping("/login.do")
    // public ModelAndView login(String loginAct,String loginPwd){
    //     ModelAndView mv = new ModelAndView();
    //     int result = userService.loginUser(loginAct,loginPwd);
    //     System.out.println(result);
    //     mv.addObject("result",result);
    //     return mv;
    // }

    // 当返回值是void，执行ajax请求可以
    @RequestMapping("/login.do")
    public void login(String loginAct, String loginPwd, HttpServletRequest request, HttpServletResponse response){
        // 将前端的密码转换为md5加密之后的密文
        loginPwd = MD5Util.getMD5(loginPwd);
        // 接收ip地址
        String ip = request.getRemoteAddr();
        System.out.println("ip:" + ip);

        try{
            User user = userService.login(loginAct,loginPwd,ip);

            // 如果程序执行到此处，说明业务层没有为controller抛出任何异常
            // 表示登陆成功
            /*
                {"success":true}
             */
            request.getSession().setAttribute("user",user);
            PrintJson.printJsonFlag(response,true);
        }catch (Exception e){
            e.printStackTrace();
            // 一旦程序执行了catch块的信息，说明业务层为我们验证登陆失败，为Controller抛出了异常
            // 表示登陆失败
            /*
                {"success":false,"msg":错误消息}
             */
            String msg =e.getMessage();
            /*
                现在作为控制器，需要为ajax提供多项信息，
                两种方法：
                    1）将多项信息打包成为map，将map解析为json串
                    2）创建一个对象
                        private boolean success;
                        private String msg;

                如果对于展现的信息将来还会大规模的使用，我们就创建一个vo类，使用方便，
                但是本项目中，仅仅需要使用map。
             */
            Map<String,Object> map = new HashMap<>();
            map.put("success",false);
            map.put("msg",msg);
            PrintJson.printJsonObj(response,map);
        }
    }
}
