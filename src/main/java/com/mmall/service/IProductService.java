package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;

public interface IProductService {

  ServerResponse<String> saveOrUpdateProduct(Product product);

  ServerResponse<String> setSaleStatus(Integer productId,Integer status);

  ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);

  ServerResponse<PageInfo> getProductList(Integer pageNum,Integer pageSize);

  ServerResponse<PageInfo> searchProduct(Integer pageNum,Integer pageSize,
          String productName, Integer productId);

  ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

  ServerResponse<PageInfo> getProductByKeywordCategory(Integer pageNum,Integer pageSize,
          String keyword,Integer categoryId,String orderBy);
}
