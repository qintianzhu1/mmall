package com.mmall.controller.backend;

import com.mmall.common.Const;
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
@RequestMapping("/manage/user")
public class UserManageController {

  @Autowired
  private IUserService iUserService;

  /**
   * 用户后台登录
   * @param session
   * @param userName
   * @param password
   * @return
   */
  @RequestMapping(value = "/login.do" ,method = RequestMethod.POST)
  @ResponseBody
  public ServerResponse<User> login(HttpSession session,String userName, String password){
    ServerResponse<User> response = iUserService.login(userName,password);
    if(response.isSuccess()){
      User user = response.getData();
      if(user.getRole() == Const.Role.ROLE_ADMIN){
        session.setAttribute(Const.CURRENT_USER,user);
      }else {
        return ServerResponse.createByErrorMessage("不是管理员无法登录");
      }
    }
    return response;
  }
}
