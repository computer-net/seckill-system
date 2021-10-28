package com.seckill.controller;

import com.alibaba.druid.util.StringUtils;
import com.seckill.controller.viewobject.UserVO;
import com.seckill.error.BusinessException;
import com.seckill.error.EmBusinessError;
import com.seckill.response.CommonReturnType;
import com.seckill.service.UserService;
import com.seckill.service.model.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Random;


@Controller("user")
@RequestMapping("/user")
@Slf4j
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    // 用户登陆接口
    @RequestMapping(value = "/login", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType login(@RequestParam(name = "telephone")String telephone,
                                  @RequestParam(name = "password")String password) throws BusinessException {
        // 入参校验
        if (org.apache.commons.lang3.StringUtils.isEmpty(telephone) || org.apache.commons.lang3.StringUtils.isEmpty(password)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        // 用户登陆服务，校验登陆是否合法
        UserModel userModel = userService.validateLogin(telephone, this.EncodeByMd5(password));

        // 将登陆凭证加入到用户登陆成功的 session 内
        request.getSession().setAttribute("IS_LOGIN", true);
        request.getSession().setAttribute("LOGIN_USER", userModel);
        log.info("login: " + request.getSession().getAttribute("IS_LOGIN"));

        ResponseCookie cookie = ResponseCookie.from("JSESSIONID", request.getSession().getId() ) // key & value
                .httpOnly(true)       // 禁止js读取
                .secure(true)     // 在http下也传输
                .domain("localhost")// 域名
                .path("/")       // path
                .sameSite("None")  // 大多数情况也是不发送第三方 Cookie，但是导航到目标网址的 Get 请求除外
                .build()
                ;
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return CommonReturnType.create(userModel);
    }

    // 用户注册接口
    @RequestMapping(value = "/register", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType register(HttpServletRequest httpServletRequest,
                                     @RequestParam(name = "telephone")String telephone,
                                     @RequestParam(name = "optCode")String optCode,
                                     @RequestParam(name = "name")String name,
                                     @RequestParam(name = "gender")Integer gender,
                                     @RequestParam(name = "age")Integer age,
                                     @RequestParam(name = "password")String password) throws BusinessException {
        // 验证手机号和对应的 optCode 相符合
        String inSessionOptCode = (String) httpServletRequest.getSession().getAttribute(telephone);
        if (!StringUtils.equals(optCode, inSessionOptCode)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "短信验证码不符合");
        }
        // 用户注册
        UserModel userModel = new UserModel();
        userModel.setAge(age);
        userModel.setTelephone(telephone);
        userModel.setName(name);
        userModel.setGender(new Byte(String.valueOf(gender.intValue())));
        userModel.setRegisterMode("byphone");
        userModel.setEncrptPassword(this.EncodeByMd5(password));

        userService.register(userModel);
        return CommonReturnType.create(null);
    }

    public String EncodeByMd5(String str) {
        // 确定计算方法
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        Base64.Encoder encoder = Base64.getEncoder();
        String encodedText = encoder.encodeToString(md5.digest(str.getBytes(StandardCharsets.UTF_8)));
        return encodedText;
    }


    // 用户获取 opt 短信接口
    @RequestMapping(value = "/getopt", method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType getOpt(HttpServletRequest httpServletRequest,
                                   HttpServletResponse httpServletResponse,
                                   @RequestParam(name = "telephone") String telephone) {
        // 需要按照一定的规则生成 opt 验证码
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt += 10000;
        String optCode = String.valueOf(randomInt);
        // 将验证码同对应用户的手机号关联，使用 httpsession 的方式绑定他的手机号与 OPTCODE
        httpServletRequest.getSession().setAttribute(telephone, optCode);
        // 将 opt 验证码通过短信通道发送给用户，此处省略
        log.info("telephone: " + telephone + " & optCode: " + optCode);

        ResponseCookie cookie = ResponseCookie.from("JSESSIONID", httpServletRequest.getSession().getId() ) // key & value
                .httpOnly(true)       // 禁止js读取
                .secure(true)     // 在http下也传输
                .domain("localhost")// 域名
                .path("/")       // path
                .sameSite("None")  // 大多数情况也是不发送第三方 Cookie，但是导航到目标网址的 Get 请求除外
                .build()
                ;
        httpServletResponse.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return CommonReturnType.create(null);
    }

    @GetMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name = "id") Integer id) throws BusinessException {
        // 调用 service 服务，获取对应 id 的用户并返回给前端
        UserModel userModel = userService.getUserById(id);
        // 若获取的对应用户信息不存在
        if (userModel == null) {
//            userModel.setEncrptPassword("dabk");
            throw new BusinessException(EmBusinessError.USER_NOT_EXIT);
        }
        // 将核心领域模型用户对象转化为可供 UI 使用的 viewobject
        UserVO userVO = convertFromModel(userModel);
        // 返回通用对象
        return CommonReturnType.create(userVO);
    }

    private UserVO convertFromModel(UserModel userModel) {
        if (userModel == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel, userVO);
        return userVO;
    }

}
