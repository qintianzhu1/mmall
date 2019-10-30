package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IUserServiceImpl implements IUserService {

  @Autowired
  private UserMapper userMapper;

  @Override
  public ServerResponse<User> login(String userName,  String password) {
    int resultCount = userMapper.checkUserName(userName);
    if(resultCount == 0){
      return ServerResponse.createByErrorMessage("用户名不存在");
    }

    //密码登录  MD5
    String md5Password = MD5Util.MD5EncodeUtf8(password);
    User user = userMapper.selectLogin(userName,md5Password);
    if(user == null){
      return ServerResponse.createByErrorMessage("密码错误");
    }
    user.setPassword(StringUtils.EMPTY);
    return ServerResponse.createBySuccess("登录成功",user);
  }

  @Override
  public ServerResponse<String> register( User user) {

    ServerResponse<String> validResponse = checkValid(user.getUsername(),Const.USERNAME);
    if(!validResponse.isSuccess()){
      return validResponse;
    }

    validResponse = checkValid(user.getUsername(),Const.EMAIL);
    if(!validResponse.isSuccess()){
      return validResponse;
    }
    user.setRole(Const.Role.ROLE_CUSTOMER);

    //MD5加密
    user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

    int resultUser = userMapper.insert(user);
    if(resultUser == 0 ){
      return ServerResponse.createByErrorMessage("注册失败");
    }

    return ServerResponse.createBySuccessMessage("注册成功");
  }

  @Override
  public ServerResponse<String> checkValid(String str, String type) {

    if(StringUtils.isNotBlank(str)){
      //检验邮箱
      if(Const.EMAIL.equals(type)){
        int emailCount = userMapper.checkEmail(str);
        if(emailCount > 0){
          return ServerResponse.createByErrorMessage("邮箱已存在");
        }
      }

      //校验用户名
      if(Const.USERNAME.equals(type)){
      int userCount = userMapper.checkUserName(str);
      if(userCount > 0){
        return ServerResponse.createByErrorMessage("用户已存在");
        }
      }
    }else {
      ServerResponse.createByErrorMessage("参数错误");
    }
    return ServerResponse.createBySuccessMessage("校验成功");
  }

  @Override
  public ServerResponse<String> forgetGetQuestion(String username) {

    ServerResponse<String> validResponse = this.checkValid(username,Const.USERNAME);
    if(!validResponse.isSuccess()){
      return ServerResponse.createByErrorMessage("用户不存在");
    }

    String question = userMapper.selectQuestionByUserName(username);
    if(StringUtils.isNotBlank(question)){
      return ServerResponse.createBySuccess(question);
    }

    return ServerResponse.createByErrorMessage("找回密码的问题是空的");
  }

  @Override
  public ServerResponse<String> checkAnswer(String userName, String question, String answer) {

    int resultCount = userMapper.checkAnswer(userName,question,answer);
    if(resultCount > 0){
      String forgetToken = UUID.randomUUID().toString();
      TokenCache.setKey(TokenCache.TOKEN_PREFIX+userName,forgetToken);
      return ServerResponse.createBySuccess(forgetToken);
    }
    return ServerResponse.createByErrorMessage("问题的答案错误");
  }

  @Override
  public ServerResponse<String> forgetResetPassword(String userName,
          String newPassword, String forgetToken) {

    if(StringUtils.isBlank(forgetToken)){
      return ServerResponse.createByErrorMessage("参数错误，token需要传送");
    }

    ServerResponse<String> validResponse = this.checkValid(userName,Const.USERNAME);
    if(!validResponse.isSuccess()){
      return ServerResponse.createByErrorMessage("用户不存在");
    }

    String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX+userName);
    if(StringUtils.isBlank(token)){
      return ServerResponse.createByErrorMessage("token无效或者已过期");
    }
    if(StringUtils.equals(forgetToken,token)){
      String md5Password = MD5Util.MD5EncodeUtf8(newPassword);
      int rowCount = userMapper.updatePasswordByUserName(userName,md5Password);
      if(rowCount > 0){
        return ServerResponse.createBySuccessMessage("修改密码成功");
      }
    }else {
      return ServerResponse.createByErrorMessage("token不正确，请重置密码的token");
    }
    return ServerResponse.createByErrorMessage("修改密码失败");
  }

  @Override
  public ServerResponse<String> resetPassword(String passwordOld,
          String passwordNew, User user) {

    int rowCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld),user.getId());
    if(rowCount == 0){
      return ServerResponse.createByErrorMessage("用户密码错误");
    }

    user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
    int updateCount = userMapper.updateByPrimaryKeySelective(user);
    if(updateCount > 0){
      return ServerResponse.createBySuccessMessage("密码更新成功");
    }
    return ServerResponse.createByErrorMessage("密码更新失败");
  }

  @Override
  public ServerResponse<User> updateInformation(User user) {
    //userName不能被更新
    //email校验 email是否已经存在并且不属于当前用户
    int result = userMapper.checkEmail(user.getEmail());
    if(result > 0 ){
      return ServerResponse.createByErrorMessage("email已存在,请更换email再次尝试");
    }

    User updateUser = new User();
    BeanUtils.copyProperties(user, updateUser);
    int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
    if(updateCount > 0){
      return ServerResponse.createBySuccess("更新个人信息成功",updateUser);
    }

    return ServerResponse.createByErrorMessage("更新个人信息失败");
  }

  @Override
  public ServerResponse<User> getInformation(Integer userId) {
    User user = userMapper.selectByPrimaryKey(userId);
    if(user == null){
      return ServerResponse.createByErrorMessage("找不到当前用户");
    }

    user.setPassword(StringUtils.EMPTY);

    return ServerResponse.createBySuccess(user);
  }

  //验证是否是管理员
  @Override
  public ServerResponse<User> checkAdminRole(User user) {
    if(user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN){
      return ServerResponse.createBySuccess();
    }
    return ServerResponse.createByError();
  }


}
