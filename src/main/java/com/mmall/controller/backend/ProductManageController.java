package com.mmall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

  @Autowired
  private IUserService iUserService;

  @Autowired
  private IProductService iProductService;

  @Autowired
  private IFileService iFileService;

  /**
   * 保存或者更新产品信息
   * @param session
   * @param product
   * @return
   */
  @RequestMapping(value = "/save.do")
  @ResponseBody
  public ServerResponse<String> saveOrUpdateProduct(HttpSession session,
          Product product){

    User user = (User)session.getAttribute(Const.CURRENT_USER);
    if(user == null){
      return ServerResponse.createByErroCoderMessage("用户未登录,请登录管理员",
              ResponseCode.NEED_LOGIN.getCode());
    }

    if(iUserService.checkAdminRole(user).isSuccess()){
      return iProductService.saveOrUpdateProduct(product);
    }else {
      return ServerResponse.createByErrorMessage("无权限操作");
    }
  }


  /**
   * 更新产品状态
   * @param session
   * @param productId
   * @param status
   * @return
   */
  @RequestMapping(value = "/set_sale_status.do")
  @ResponseBody
  public ServerResponse<String> setSaleStatus(HttpSession session,
          Integer productId, Integer status){

    User user = (User)session.getAttribute(Const.CURRENT_USER);
    if(user == null){
      return ServerResponse.createByErroCoderMessage("用户未登录,请登录管理员",
              ResponseCode.NEED_LOGIN.getCode());
    }

    if(iUserService.checkAdminRole(user).isSuccess()){
      return iProductService.setSaleStatus(productId, status);
    }else {
      return ServerResponse.createByErrorMessage("无权限操作");
    }
  }

  /**
   * 产品详情
   * @param session
   * @param productId
   * @return
   */
  @RequestMapping(value = "/detail.do")
  @ResponseBody
  public ServerResponse<ProductDetailVo> getDetail(HttpSession session, Integer productId){

    User user = (User)session.getAttribute(Const.CURRENT_USER);
    if(user == null){
      return ServerResponse.createByErroCoderMessage("用户未登录,请登录管理员",
              ResponseCode.NEED_LOGIN.getCode());
    }

    if(iUserService.checkAdminRole(user).isSuccess()){
      return iProductService.manageProductDetail(productId);
    }else {
      return ServerResponse.createByErrorMessage("无权限操作");
    }
  }


  /**
   * 查询产品列表(分页)
   * @param session
   * @param pageNum
   * @param pageSize
   * @return
   */
  @RequestMapping(value = "/list.do")
  @ResponseBody
  public ServerResponse<PageInfo> getList(HttpSession session,
          @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
          @RequestParam(value = "pageSize", defaultValue = "10")Integer pageSize){

    User user = (User)session.getAttribute(Const.CURRENT_USER);
    if(user == null){
      return ServerResponse.createByErroCoderMessage("用户未登录,请登录管理员",
              ResponseCode.NEED_LOGIN.getCode());
    }

    if(iUserService.checkAdminRole(user).isSuccess()){
      return iProductService.getProductList(pageNum,pageSize);
    }else {
      return ServerResponse.createByErrorMessage("无权限操作");
    }
  }

  /**
   * 搜索产品列表（分页）
   * @param session
   * @param pageNum
   * @param pageSize
   * @param productName
   * @param productId
   * @return
   */
  @RequestMapping(value = "/search.do")
  @ResponseBody
  public ServerResponse<PageInfo> productSearch(HttpSession session,
          @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
          @RequestParam(value = "pageSize", defaultValue = "10")Integer pageSize,
          String productName, Integer productId){

    User user = (User)session.getAttribute(Const.CURRENT_USER);
    if(user == null){
      return ServerResponse.createByErroCoderMessage("用户未登录,请登录管理员",
              ResponseCode.NEED_LOGIN.getCode());
    }

    if(iUserService.checkAdminRole(user).isSuccess()){
      return iProductService.searchProduct(pageNum,pageSize,productName,productId);
    }else {
      return ServerResponse.createByErrorMessage("无权限操作");
    }
  }

  @RequestMapping(value = "/upload.do")
  @ResponseBody
  public ServerResponse upload(HttpSession session,@RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request){

    Map map = Maps.newHashMap();
    User user = (User)session.getAttribute(Const.CURRENT_USER);
    if(user == null){
      return ServerResponse.createByErroCoderMessage("用户未登录,请登录管理员",
              ResponseCode.NEED_LOGIN.getCode());
    }

    if(iUserService.checkAdminRole(user).isSuccess()){

      String path = request.getSession().getServletContext().getRealPath("upload");
      String targetFileName = iFileService.upload(file,path);
      String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;


      map.put("uri",targetFileName);
      map.put("url",url);
      return ServerResponse.createBySuccess(map);
    }else {
      return ServerResponse.createByErrorMessage("无权限操作");
    }
  }

  @RequestMapping(value = "/richtext_img_upload.do")
  @ResponseBody
  public Map richtextImgUpload(HttpSession session,@RequestParam(value = "upload_file",
                                                                 required = false) MultipartFile file, HttpServletRequest request,HttpServletResponse response){

    //富文本返回格式
    // 'success' => true,
    //    'msg' => '图片上传错误信息',
    //    'file_path' => '/upload/2018_10_11/1.jpg'
    Map resultMap = Maps.newHashMap();
    User user = (User)session.getAttribute(Const.CURRENT_USER);
    if(user == null){
      resultMap.put("success",false);
      resultMap.put("msg","请登录管理员");

    }

    if(iUserService.checkAdminRole(user).isSuccess()){

      String path = request.getSession().getServletContext().getRealPath("upload");
      String targetFileName = iFileService.upload(file,path);
      if(StringUtils.isBlank(targetFileName)){
        resultMap.put("success",false);
        resultMap.put("msg","上传失败");
      }
      String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
      resultMap.put("success",true);
      resultMap.put("msg","上传成功");
      resultMap.put("file_path",url);
      response.addHeader("Access-Control-Allow-Headers","X-File-Name");
    }else {
      resultMap.put("success",false);
      resultMap.put("msg","无权限操作");
    }
    return resultMap;
  }

}
