package com.seckill.service;

import com.seckill.error.BusinessException;
import com.seckill.service.model.UserModel;

public interface UserService {
    // 通过用户 Id 获取用户对象的方法
    public UserModel getUserById(Integer id);

    // 用户注册
    public void register(UserModel userModel) throws BusinessException;

    /**
     * 用户登陆
     * @param telephone 用户注册的手机号
     * @param password 用户加密后的密码
     * @throws BusinessException
     */
    public UserModel validateLogin(String telephone, String encrptPassword) throws BusinessException;
}
