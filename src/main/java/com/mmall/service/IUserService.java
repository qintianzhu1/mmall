package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

public interface IUserService {

  ServerResponse<User> login (String userName,String password);

  ServerResponse<String> register(User user);

  ServerResponse<String> checkValid(String str,String type);

  ServerResponse<String> forgetGetQuestion(String username);

  ServerResponse<String> checkAnswer(String userName,String question,String answer);

  ServerResponse<String> forgetResetPassword(String userName,String newPassword,String forgetToken);

  ServerResponse<String> resetPassword(String passwordOld,String passwordNew,User user);

  ServerResponse<User> updateInformation(User user);

  ServerResponse<User> getInformation(Integer userId);

  ServerResponse<User> checkAdminRole(User user);

}
