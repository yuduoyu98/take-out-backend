package com.sky.service.impl;

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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * 用户相关服务
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private static final String WX_CODE_2_SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登录
     * - 新用户自动完成注册
     * @param userLoginDTO
     * @return
     */
    @Override
    public User wxlogin(UserLoginDTO userLoginDTO) {
        // 1. 请求微信服务 获取openid
        String openid = getOpenId(userLoginDTO.getCode());
        if (StringUtils.isBlank(openid)) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        log.debug("微信用户openid: {}", openid);
        // 2. 根据openid查询用户信息
        User user = userMapper.getByOpenId(openid);
        // 3. 如果用户不存在，则自动注册
        if (user == null) {
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
            log.debug("新用户，自动注册，user_id={}", user.getId());
        }
        // 4. 返回用户信息
        log.debug("用户登录: {}", user.getId());
        return user;
    }

    /**
     * 请求微信开放平台 获取用户openid（用户唯一标识）
     * <a href="https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/user-login/code2Session.html">...</a>
     * @param code
     * @return
     */
    private String getOpenId(String code) {
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("appid", weChatProperties.getAppid());
        paramMap.put("secret", weChatProperties.getSecret());
        paramMap.put("js_code", code);
        paramMap.put("grant_type", "authorization_code");
        try {
            String res = HttpClientUtil.doGet(WX_CODE_2_SESSION_URL, paramMap);
            JSONObject jsonObj = JSONObject.parseObject(res);
            return jsonObj.getString("openid");
        } catch (Exception e) {
            e.printStackTrace();
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
    }
}
