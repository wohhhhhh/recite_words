package com.feidian.service.impl;

import com.feidian.domain.entity.LoginUser;
import com.feidian.domain.entity.ResponseResult;
import com.feidian.domain.entity.User;
import com.feidian.domain.vo.UserInfoVO;
import com.feidian.domain.vo.UserLoginVO;
import com.feidian.service.LoginService;
import com.feidian.utils.BeanCopyUtils;
import com.feidian.utils.JwtUtil;
import com.feidian.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;


    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public ResponseResult login(User user) {
        String password = user.getPassword();
        String encode = passwordEncoder.encode(password);
//        user.setPassword(encode);
        System.out.println(encode);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
            Authentication authenticate = authenticationManager.authenticate(authenticationToken);
            // 判断是否认证通过
            if(Objects.isNull(authenticate)){
                throw new RuntimeException("用户名或密码错误");
            }
            // 获取userid 生成token
            LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
            String userId = loginUser.getUser().getId().toString();
            String jwt = JwtUtil.createJWT(userId);
            // 把用户信息存入redis
            redisCache.setCacheObject("login:"+userId,loginUser);

            // 把token和userinfo封装 返回
            // 把User转换成UserInfoVo
            UserInfoVO userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVO.class);
            UserLoginVO vo = new UserLoginVO(jwt,userInfoVo);
            return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult logout() {
        //获取token 解析获取userid
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //获取userid
        Integer userId = loginUser.getUser().getId();
        //删除redis中的用户信息
        redisCache.deleteObject("login:"+userId);
        return ResponseResult.okResult();
    }

}
