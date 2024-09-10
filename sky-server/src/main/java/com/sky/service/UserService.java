package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.vo.UserLoginVO;

/**
 * Date：2024/9/10  15:32
 * Description：TODO
 *
 * @author Leon
 * @version 1.0
 */
public interface UserService {
    User login(UserLoginDTO userLoginDTO);
}
