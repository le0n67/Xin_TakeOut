package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Date：2024/9/10  15:33
 * Description：TODO
 *
 * @author Leon
 * @version 1.0
 */

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private static final String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";
    private Map<String, String> params = new HashMap<>();
    @Autowired
    private UserMapper userMapper;

    @Autowired
    WeChatProperties properties;

    /**
     * 用户登录
     *
     * @param userLoginDTO
     * @return
     */
    public User login(UserLoginDTO userLoginDTO) {
        log.info("微信登录:{}", userLoginDTO);

        String openid = getOpenid(userLoginDTO.getCode());
        if (openid == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        User user = userMapper.getByOpenId(openid);
        if (user == null) {
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }
        return user;
    }

    private String getOpenid(String code) {
        params.put("appid", properties.getAppid());
        params.put("secret", properties.getSecret());
        params.put("js_code", code);
        params.put("grant_type", "authorization_code");
        String res = HttpClientUtil.doGet(WX_LOGIN_URL, params);

        JSONObject jsonObject = JSON.parseObject(res);
        return jsonObject.getString("openid");
    }
}
