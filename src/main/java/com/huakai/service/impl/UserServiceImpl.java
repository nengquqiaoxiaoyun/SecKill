package com.huakai.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.huakai.controller.dto.UserDto;
import com.huakai.error.BussinesssError;
import com.huakai.error.ErrorEnum;
import com.huakai.mapper.UserDOMapper;
import com.huakai.mapper.UserPasswordDOMapper;
import com.huakai.mapper.dataobject.UserDO;
import com.huakai.mapper.dataobject.UserPasswordDO;
import com.huakai.service.UserService;
import com.huakai.valiator.ValidationResult;
import com.huakai.valiator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author: huakaimay
 * @since: 2021-08-30
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDOMapper userDOMapper;
    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;

    @Override
    public UserDto getUserById(Integer id) {
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);
        if (userDO == null)
            return null;
        String password = userPasswordDOMapper.getPasswordByUid(userDO.getId());
        return convertUserDo(userDO, password);
    }

    @Override
    @Transactional
    public void register(UserDto userDto) {

        UserDO userDO = converUerDto(userDto);
        // insert to user_info
        userDOMapper.insertSelective(userDO);

        /*
         此处需要注意两点：
         1. userDO的id可能为空，因为采用的insertSelective方法
         如果没有在xml中设置主键自增则插入的数据UserDo不会有id
         2. 加密时不能简单地使用Java自带的MD5，其只支持16位
         */
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setUserId(userDO.getId());
        userPasswordDO.setEncryptPassword(encodeByMyd(userDto.getEncryptPassword()));
        // insert to user_password
        userPasswordDOMapper.insertSelective(userPasswordDO);
    }

    @Override
    public UserDO getUserByTelephone(String telephone) {
        return userDOMapper.getUserByTelephone(telephone);
    }

    @Override
    public boolean userExist(UserDO userDO, String password) {
        String inTablePwd = userPasswordDOMapper.getPasswordByUid(userDO.getId());

        if (StringUtils.equals(encodeByMyd(password), inTablePwd))
            return true;

        return false;
    }

    /**
     * Java自带的MD5只支持16位
     */
    private String encodeByMyd(String str) {
        String encode = null;
        try {
            BASE64Encoder base64Encoder = new BASE64Encoder();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            encode = base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encode;
    }


    /**
     * UserDto to UserDO
     */
    private UserDO converUerDto(UserDto userDto) {
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userDto, userDO);
        userDO.setRegisterMode("byalipay");
        return userDO;
    }


    /**
     * userDO to UserDto
     */
    private UserDto convertUserDo(UserDO userDO, String password) {

        if (userDO == null)
            return null;

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDO, userDto);

        if (password != null)
            userDto.setEncryptPassword(password);

        return userDto;
    }
}
