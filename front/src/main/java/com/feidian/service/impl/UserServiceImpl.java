package com.feidian.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feidian.domain.entity.ResponseResult;
import com.feidian.domain.entity.User;
import com.feidian.domain.vo.UserInfoVO;
import com.feidian.enums.AppHttpCodeEnum;
import com.feidian.handler.exception.SystemException;
import com.feidian.mapper.UserMapper;
import com.feidian.service.UserService;
import com.feidian.utils.BeanCopyUtils;
import com.feidian.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.regex.Pattern;


@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    final static Pattern partern = Pattern.compile("[a-zA-Z0-9]+[\\.]{0,1}[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z]+");

    final static Pattern regex = Pattern.compile("0\\d{2,3}[-]?\\d{7,8}|0\\d{2,3}\\s?\\d{7,8}|13[0-9]\\d{8}|15[1089]\\d{8}");
    @Override
    public ResponseResult userInfo() {
        //获取当前用户id
        Integer userId = SecurityUtils.getUserId();
        //根据用户id查询用户信息
        User user = getById(userId);
        //封装成UserInfoVo
        UserInfoVO vo = BeanCopyUtils.copyBean(user,UserInfoVO.class);
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        User updateUser=getById(user.getId());
        updateUser.setPhone(user.getPhone());
        updateUser.setEmail(user.getEmail());
        updateUser.setGender(user.getGender());
        //获取当前时间
        Date now = new Date();
        updateUser.setGmtModified(now);
        updateById(updateUser);
        return ResponseResult.okResult();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public ResponseResult register(User user) {
        //对数据进行非空判断
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if (user.getGender() == null) {
            throw new SystemException(AppHttpCodeEnum.GENDER_NOT_NULL);
        }
        if (user.getPhone() == null) {
            throw new SystemException(AppHttpCodeEnum.PHONE_NOT_NULL);
        }
        //对数据进行是否存在的判断
        if(isUserNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(isEmailExist(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        if(isEmailExist(user.getPhone())){
            throw new SystemException(AppHttpCodeEnum.PHONE_EXIST);
        }

        if(!IsPasswordStandardized(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_STANDARDIZED);
        }
        //获取当前时间
        Date now = new Date();


        if (emailFormat(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_FORMAT);
        }
        if (phoneFormat(user.getPhone())) {
            throw new SystemException(AppHttpCodeEnum.PHONE_NOT_FORMAT);
        }


        //设置gmt_create和gmt_modified字段值
        user.setGmtCreate(now);
        user.setGmtModified(now);

        //对密码进行加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        //存入数据库
        save(user);
        return ResponseResult.okResult();
    }

    // 1.密码长度至少8位以上<br/>
    // 2.密码应至少包含大写字母，小写字母，数字
    private boolean IsPasswordStandardized(String password) {
            //数字
            String REG_NUMBER = ".*\\d+.*";
            //小写字母
            String REG_UPPERCASE = ".*[A-Z]+.*";
            //大写字母
            String REG_LOWERCASE = ".*[a-z]+.*";
            //密码为空或者长度小于8位则返回false
            if (password == null || password.length() <8 ) {
                return false;
            }
            int j = 0;
            if (password.matches(REG_NUMBER)) j++;
            if (password.matches(REG_LOWERCASE))j++;
            if (password.matches(REG_UPPERCASE)) j++;
            if (j  < 3 )  return false;

            return true;
    }


    @Autowired
    UserMapper userMapper;

    private boolean isEmailExist(String email) {
        return userMapper.selectCount(new QueryWrapper<User>().eq("email", email)) > 0;
    }

    private boolean isUserNameExist(String userName) {
        return userMapper.selectCount(new QueryWrapper<User>().eq("user_name", userName)) > 0;
    }

    private boolean isPhoneExist(Integer phone) {
        return userMapper.selectCount(new QueryWrapper<User>().eq("phone", phone)) > 0;
    }

    /**
     * 验证输入的邮箱格式是否符合
     * @param email
     * @return 是否合法
     */
    public static boolean emailFormat(String email){
        boolean isMatch = partern.matcher(email).matches();
        return isMatch;
    }
    public static boolean phoneFormat(String phone){
        boolean isMatch = regex.matcher(phone).matches();
        return isMatch;
    }

}
