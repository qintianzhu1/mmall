package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

  @Autowired
  private IUserService iUserService;

  @Autowired
  private ICategoryService iCategoryService;

  /**
   * 新增类目
   * @param session
   * @param categoryName
   * @param parentId
   * @return
   */
  @RequestMapping(value = "/add_category.do",method = RequestMethod.POST)
  @ResponseBody
  public ServerResponse addCategory(HttpSession session,String categoryName,
          @RequestParam(value = "parentId", defaultValue = "0") int parentId){

    User user = (User)session.getAttribute(Const.CURRENT_USER);
    if(user == null){
      return ServerResponse.createByErroCoderMessage("当前用户未登录，请登录",
              ResponseCode.NEED_LOGIN.getCode());
    }

    //检验是否是管理员
    ServerResponse response = iUserService.checkAdminRole(user);
    if(response.isSuccess()){
      return iCategoryService.addCategory(categoryName,parentId);

    }else {
      return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
    }
  }

  /**
   * 更新类目
   * @param session
   * @param categoryName
   * @param categoryId
   * @return
   */
  @RequestMapping(value = "/set_category_name.do",method = RequestMethod.POST)
  @ResponseBody
  public ServerResponse setCategoryName(HttpSession session,String categoryName,
                        int categoryId) {

    User user = (User) session.getAttribute(Const.CURRENT_USER);
    if (user == null) {
      return ServerResponse.createByErroCoderMessage("当前用户未登录，请登录",
              ResponseCode.NEED_LOGIN.getCode());
    }

    //检验是否是管理员
    ServerResponse response = iUserService.checkAdminRole(user);
    if(response.isSuccess()){
      //更新类目
      return iCategoryService.updateCategory(categoryName,categoryId);

    }else {
      return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
    }

  }


  /**
   * 查询子节点category信息
   * @param session
   * @param categoryId
   * @return
   */
  @RequestMapping(value = "/get_category.do",method = RequestMethod.GET)
  @ResponseBody
  public ServerResponse getChildrenParallelCategory(HttpSession session,
          @RequestParam(value = "categoryId" ,defaultValue = "0") int categoryId) {

    User user = (User) session.getAttribute(Const.CURRENT_USER);
    if (user == null) {
      return ServerResponse.createByErroCoderMessage("当前用户未登录，请登录",
              ResponseCode.NEED_LOGIN.getCode());
    }

    if(iUserService.checkAdminRole(user).isSuccess()){
      //查询子节点category信息，并且不递归，保持平级
      return iCategoryService.getChildrenParallelCategory(categoryId);
    }else {
      return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
    }

  }


  /**
   * 递归查询本节点id以及子节点id
   * @param session
   * @param categoryId
   * @return
   */
  @RequestMapping(value = "/get_deep_category.do",method = RequestMethod.GET)
  @ResponseBody
  public ServerResponse getCategoryAndDeepChildren(HttpSession session,
          @RequestParam(value = "categoryId" ,defaultValue = "0") int categoryId) {

    User user = (User) session.getAttribute(Const.CURRENT_USER);
    if (user == null) {
      return ServerResponse.createByErroCoderMessage("当前用户未登录，请登录",
              ResponseCode.NEED_LOGIN.getCode());
    }

    if(iUserService.checkAdminRole(user).isSuccess()){
      //查询子节点category和递归节点
      return iCategoryService.selectCategoryAndChildrenById(categoryId);
    }else {
      return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
    }

  }


}
