package com.seckill.service.impl;

import com.seckill.dao.UserDOMapper;
import com.seckill.dao.UserPasswordDOMapper;
import com.seckill.dataobject.UserDO;
import com.seckill.dataobject.UserPasswordDO;
import com.seckill.error.BusinessException;
import com.seckill.error.EmBusinessError;
import com.seckill.service.UserService;
import com.seckill.service.model.UserModel;
import com.seckill.validator.ValidationResult;
import com.seckill.validator.ValidatorImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDOMapper userDOMapper;

    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;

    @Autowired
    private ValidatorImpl validator;

    @Override
    public UserModel getUserById(Integer id) {
        // 调用 userdomapper 获取到对应的用户 dataobject
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);
        if (userDO == null) {
            return null;
        }
        // 通过用户id获取对应的用户加密密码信息
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        return  convertFromDataObject(userDO, userPasswordDO);
    }

    @Override
    @Transactional  // 声明为一个事务
    public void register(UserModel userModel) throws BusinessException {
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
//        if (StringUtils.isEmpty(userModel.getName()) ||
//            userModel.getGender() == null ||
//            userModel.getAge() == null ||
//            StringUtils.isEmpty(userModel.getTelephone())) {
//            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
//        }
        ValidationResult validationResult = validator.validate(userModel);
        if (validationResult.isHasErrors()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, validationResult.getErrMsg());
        }
        // 实现 model->dataobject 方法
        UserDO userDO = convertFromModel(userModel);
        try {
            userDOMapper.insertSelective(userDO);
        } catch (Exception ex) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "手机号已重复注册");
        }
        userModel.setId(userDO.getId());
        UserPasswordDO userPasswordDO = convertPasswordFromModel(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);
        return;
    }

    @Override
    public UserModel validateLogin(String telephone, String encrptPassword) throws BusinessException {
        // 通过用户手机获取用户信息
        UserDO userDO = userDOMapper.selectByTelephone(telephone);
        if (userDO == null) {
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        UserModel userModel = convertFromDataObject(userDO, userPasswordDO);
        // 比对用户信息内加密密码是否和传输进来的密码匹配
        if (!StringUtils.equals(userPasswordDO.getEncrptPassword(), encrptPassword)) {
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        return userModel;
    }

    private UserModel convertFromDataObject(UserDO userDO, UserPasswordDO userPasswordDO) {
        if (userDO == null) {
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDO, userModel);
        if (userPasswordDO != null) {
            userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());
        }
        return userModel;
    }

    public UserDO convertFromModel(UserModel userModel) {
        if (userModel == null) {
            return null;
        }
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel, userDO);
        return userDO;
    }
    public UserPasswordDO convertPasswordFromModel(UserModel userModel) {
        if (userModel == null) {
            return null;
        }
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setEncrptPassword(userModel.getEncrptPassword());
        userPasswordDO.setUserId(userModel.getId());
        return userPasswordDO;
    }
}