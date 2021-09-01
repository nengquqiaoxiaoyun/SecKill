package com.huakai.service.impl;

import com.huakai.controller.dto.UserDto;
import com.huakai.mapper.UserDOMapper;
import com.huakai.mapper.UserPasswordDOMapper;
import com.huakai.mapper.dataobject.UserDO;
import com.huakai.mapper.dataobject.UserPasswordDO;
import com.huakai.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void register(UserDto userDto) {

    }


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
