package com.mmall.controller.protal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

  @Autowired
  private IUserService iUserService;

  /**
   * 用户登录
   * @param username
   * @param password
   * @param session
   * @return
   */
  @RequestMapping(value = "/login.do",method = RequestMethod.POST)
  @ResponseBody
  public ServerResponse<User> login(String username,String password,HttpSession session){
    System.out.println(username);
    ServerResponse<User> response = iUserService.login(username,password);
    if(response.isSuccess()){
      session.setAttribute(Const.CURRENT_USER,response.getData());
    }
    return response;
  }

  /**
   * 用户登出
   * @param session
   * @return
   */
  @RequestMapping(value = "/logout.do",method = RequestMethod.POST)
  @ResponseBody
  public ServerResponse<String> logout(HttpSession session){
    session.removeAttribute(Const.CURRENT_USER);
    return ServerResponse.createBySuccess();
  }

  /**
   * 用户注册
   * @param user
   * @return
   */
  @RequestMapping(value = "/register.do",method = RequestMethod.POST)
  @ResponseBody
  public ServerResponse<String> register(User user){
    return iUserService.register(user);
  }

  /**
   * 校验用户名和邮箱
   * @param str
   * @param type
   * @return
   */
  @RequestMapping(value = "/check_valid.do",method = RequestMethod.POST)
  @ResponseBody
  public ServerResponse<String> checkValid(String str,String type){
    return iUserService.checkValid(str,type);
  }

  /**
   * 获取用户信息
   * @param session
   * @return
   */
  @RequestMapping(value = "/get_user_info.do",method = RequestMethod.POST)
  @ResponseBody
  public ServerResponse<User> userInfo(HttpSession session){
    User user = (User) session.getAttribute(Const.CURRENT_USER);
    if(user != null){
      return ServerResponse.createBySuccess(user);
    }
    return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
  }

  /**
   * 获取密码提示问题
   * @param username
   * @return
   */
  @RequestMapping(value = "/forget_get_question.do",method = RequestMethod.POST)
  @ResponseBody
  public ServerResponse<String> forgetGetQuestion(String username){
    return iUserService.forgetGetQuestion(username);
  }

  /**
   * 验证用户名 提示问题 答案是否正确
   * @param userName
   * @param question
   * @param answer
   * @return
   */
  @RequestMapping(value = "/forget_check_answer.do",method = RequestMethod.POST)
  @ResponseBody
  public ServerResponse<String> checkAnswer(String userName,String question,String answer){
    return iUserService.checkAnswer(userName, question, answer);
  }

  /**
   * 忘记密码重置密码
   * @param userName
   * @param passwordNew
   * @param forgetToken
   * @return
   */
  @RequestMapping(value = "/forget_reset_password.do",method = RequestMethod.POST)
  @ResponseBody
  public ServerResponse<String> forgetResetPassword(String userName,String passwordNew,String forgetToken){
    return iUserService.forgetResetPassword(userName, passwordNew, forgetToken);
  }


  /**
   * 用户登录状态修改密码
   * @param session
   * @param passwordOld
   * @param passwordNew
   * @return
   */
  @RequestMapping(value = "/reset_password.do",method = RequestMethod.POST)
  @ResponseBody
  public ServerResponse<String> resetPassword(HttpSession session, String passwordOld,String passwordNew){
    User user = (User) session.getAttribute(Const.CURRENT_USER);
    if(user == null){
      return ServerResponse.createByErrorMessage("用户未登录");
    }

    return iUserService.resetPassword(passwordOld,passwordNew,user);
  }

  /**
   * 更新个人用户信息
   * @param session
   * @param user
   * @return
   */
  @RequestMapping(value = "/update_information.do",method = RequestMethod.POST)
  @ResponseBody
  public ServerResponse<User> updateInformation(HttpSession session, User user){
    User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
    if(currentUser == null){
      return ServerResponse.createByErrorMessage("用户未登录");
    }

    user.setId(currentUser.getId());
    user.setUsername(currentUser.getUsername());

    ServerResponse response = iUserService.updateInformation(user);
    if(response.isSuccess()){
      session.setAttribute(Const.CURRENT_USER,response.getData());
    }
    return response;
  }

  /**
   * 查询当前用户的信息
   * @param session
   * @return
   */
  @RequestMapping(value = "/get_information.do",method = RequestMethod.POST)
  @ResponseBody
  public ServerResponse<User> getInformation(HttpSession session){
    User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
    if(currentUser == null){
      return ServerResponse.createByErroCoderMessage("用户未登录需要强制登录",
              ResponseCode.NEED_LOGIN.getCode());
    }
    return iUserService.getInformation(currentUser.getId());
  }
}
