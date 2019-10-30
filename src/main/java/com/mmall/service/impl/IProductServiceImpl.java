package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.ICategoryService;
import com.mmall.service.IProductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductListVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IProductServiceImpl implements IProductService {

  @Autowired
  private ProductMapper productMapper;

  @Autowired
  private CategoryMapper categoryMapper;

  @Autowired
  private ICategoryService iCategoryService;

  @Override
  public ServerResponse<String> saveOrUpdateProduct(Product product) {
    if(product != null){
      if(StringUtils.isNotBlank(product.getSubImages())){
        String[] subImages = product.getSubImages().split(",");
        if(subImages.length > 0){
          product.setMainImage(subImages[0]);
        }
      }
      if(product.getId() == null){
        int resultCount = productMapper.insert(product);
        if(resultCount > 0){
          ServerResponse.createBySuccess("新增产品成功");
        }
      }else {
        int resultCount = productMapper.updateByPrimaryKey(product);
        if(resultCount > 0){
          ServerResponse.createBySuccess("更新产品成功");
        }
      }
    }
    return ServerResponse.createByErrorMessage("新增或者更新产品参数不正确");
  }

  @Override
  public ServerResponse<String> setSaleStatus(Integer productId, Integer status) {
    if(productId == null || status == null){
      ServerResponse.createByErroCoderMessage(ResponseCode.ILLEGAL_ARGUMENT.getDesc(),
              ResponseCode.ILLEGAL_ARGUMENT.getCode());
    }

    Product product = new Product();
    product.setId(productId);
    product.setStatus(status);
    int resultCount =productMapper.updateByPrimaryKey(product);
    if(resultCount > 0){
      ServerResponse.createBySuccess("更新产品状态成功");
    }

    return ServerResponse.createByErrorMessage("更新产品状态失败");
  }

  @Override
  public ServerResponse<ProductDetailVo> manageProductDetail(Integer productId) {

    if(productId == null){
      return ServerResponse.createByErroCoderMessage(ResponseCode.ILLEGAL_ARGUMENT.getDesc(),
              ResponseCode.ILLEGAL_ARGUMENT.getCode());
    }

    Product product = productMapper.selectByPrimaryKey(productId);
    if(product == null){
      return ServerResponse.createByErrorMessage("产品下架或者已删除");
    }

    ProductDetailVo productDetailVo = assembleProductDetailVo(product);
    return ServerResponse.createBySuccess(productDetailVo);
  }

  @Override
  public ServerResponse<PageInfo> getProductList(Integer pageNum,
          Integer pageSize) {
    PageHelper.startPage(pageNum,pageSize);
    List<Product> products = productMapper.getProductList();
    List<ProductListVo> productListVos = Lists.newArrayList();
    for(Product product : products){
      ProductListVo productListVo = assembleProductListVo(product);
      productListVos.add(productListVo);
    }

    PageInfo pageInfo = new PageInfo(products);
    pageInfo.setList(productListVos);
    return ServerResponse.createBySuccess(pageInfo);
  }

  @Override
  public ServerResponse<PageInfo> searchProduct(Integer pageNum, Integer pageSize,
          String productName, Integer productId) {
    PageHelper.startPage(pageNum,pageSize);
    if(StringUtils.isNotBlank(productName)){
      productName = new StringBuilder().append("%").append(productName).append("%").toString();
    }
    List<Product> products = productMapper.searchListByProductNameAndProductId(productName,productId);
    List<ProductListVo> productListVos = Lists.newArrayList();
    for(Product product : products){
      ProductListVo productListVo = assembleProductListVo(product);
      productListVos.add(productListVo);
    }
    PageInfo pageInfo = new PageInfo(products);
    pageInfo.setList(productListVos);
    return ServerResponse.createBySuccess(pageInfo);
  }

  @Override
  public ServerResponse<ProductDetailVo> getProductDetail(Integer productId) {
    if(productId == null){
      return ServerResponse.createByErroCoderMessage(ResponseCode.ILLEGAL_ARGUMENT.getDesc(),
              ResponseCode.ILLEGAL_ARGUMENT.getCode());
    }

    Product product = productMapper.selectByPrimaryKey(productId);
    if(product == null){
        return ServerResponse.createByErrorMessage("产品下架或者已删除");
    }

    if(!product.getStatus().equals(Const.ProductStatusEnum.ON_SALE)){
      return ServerResponse.createByErrorMessage("产品下架或者已删除");
    }

    return ServerResponse.createBySuccess(assembleProductDetailVo(product));
  }

  @Override
  public ServerResponse<PageInfo> getProductByKeywordCategory(Integer pageNum,
          Integer pageSize, String keyword, Integer categoryId,String orderBy) {

    List<Integer> categoryIdList = Lists.newArrayList();
    if(StringUtils.isBlank(keyword) && categoryId == null){
      return ServerResponse.createByErroCoderMessage(ResponseCode.ILLEGAL_ARGUMENT.getDesc(),ResponseCode.ILLEGAL_ARGUMENT.getCode());
    }

    if(categoryId != null){
      Category category = categoryMapper.selectByPrimaryKey(categoryId);
      if(category == null && StringUtils.isBlank(keyword)){
        PageHelper.startPage(pageNum,pageSize);
        List<ProductListVo> productListVos = Lists.newArrayList();
        PageInfo pageInfo = new PageInfo(productListVos);
        return ServerResponse.createBySuccess(pageInfo);
      }

      categoryIdList = iCategoryService.selectCategoryAndChildrenById(category.getId()).getData();
    }
    if(StringUtils.isNotBlank(keyword)){
      keyword = new StringBuffer().append("%").append(keyword).append("%").toString();
    }

    PageHelper.startPage(pageNum,pageSize);
    //排序处理
    if(StringUtils.isNotBlank(orderBy)){
      if(Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)){
        String [] orderByArray = orderBy.split("_");
        PageHelper.orderBy(orderByArray[0]+" "+orderByArray[1]);
      }
    }

    List<Product> productList = productMapper.selectByNameAndCategoryIds(StringUtils.isBlank(keyword)?null:keyword,categoryIdList.size()==0?null:categoryIdList);
    List<ProductListVo> productListVos = Lists.newArrayList();
    for(Product product : productList){
      ProductListVo productListVo = assembleProductListVo(product);
      productListVos.add(productListVo);
    }

    PageInfo pageInfo = new PageInfo(productList);
    pageInfo.setList(productListVos);
    return ServerResponse.createBySuccess(pageInfo);
  }

  private ProductListVo assembleProductListVo(Product product){
    ProductListVo productListVo = new ProductListVo();
    productListVo.setId(product.getId());
    productListVo.setCategoryId(product.getCategoryId());
    productListVo.setName(product.getName());
    productListVo.setMainImage(product.getMainImage());
    productListVo.setPrice(product.getPrice());
    productListVo.setStatus(product.getStatus());
    productListVo.setSubtitle(product.getSubtitle());
    productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
    return productListVo;
  }

  private ProductDetailVo assembleProductDetailVo(Product product){
    ProductDetailVo productDetailVo = new ProductDetailVo();
    BeanUtils.copyProperties(product,productDetailVo);

    // imageHost
    productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));

    // parentCategoryId
    Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
    if(category == null){
      productDetailVo.setParentCategoryId(0);
    }
    productDetailVo.setParentCategoryId(category.getParentId());

    // createTime
    productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
    // updateTime
    productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
    return productDetailVo;
  }
}
