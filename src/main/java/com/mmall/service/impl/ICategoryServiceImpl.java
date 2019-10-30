package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;


@Service
public class ICategoryServiceImpl implements ICategoryService {


  private Logger logger = LoggerFactory.getLogger(ICategoryServiceImpl.class);

  @Autowired
  private CategoryMapper categoryMapper;

  @Override
  public ServerResponse addCategory(String categoryName, Integer parentId) {

    if(parentId == null && StringUtils.isBlank(categoryName)){
      return ServerResponse.createByErrorMessage("添加品类参数错误");
    }

    Category category = new Category();
    category.setName(categoryName);
    category.setParentId(parentId);
    //这个分类是可用的
    category.setStatus(true);
    int resultCount = categoryMapper.insert(category);
    if(resultCount > 0){
      return ServerResponse.createBySuccessMessage("添加品类成功");
    }

    return ServerResponse.createByErrorMessage("添加品类失败");
  }

  @Override
  public ServerResponse updateCategory(String categoryName, Integer categoryId){

    if(categoryId == null && StringUtils.isBlank(categoryName)){
      return ServerResponse.createByErrorMessage("更新品类参数错误");
    }

    Category category = new Category();
    category.setName(categoryName);
    category.setId(categoryId);

    int resultCount =categoryMapper.updateByPrimaryKeySelective(category);
    if(resultCount > 0){
      return ServerResponse.createBySuccessMessage("更新品类成功");
    }

    return ServerResponse.createByErrorMessage("更新品类失败");
  }

  @Override
  public ServerResponse<List<Category>> getChildrenParallelCategory(
          Integer categoryId) {

    List<Category> categories = categoryMapper.selectCategoryChildrenByParentId(categoryId);
    if(CollectionUtils.isEmpty(categories)){
      logger.info("未找到当前分类的子分类");
    }
    return ServerResponse.createBySuccess(categories);
  }

  /**
   * 递归查询本节点id以及子节点id
   * @param categoryId
   * @return
   */
  @Override
  public ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId) {
    Set<Category> categorySet = Sets.newHashSet();
    findChildCategory(categorySet,categoryId);

    List<Integer> categoryList = Lists.newArrayList();
    if(categoryId != null){
      for (Category category : categorySet){
        categoryList.add(category.getId());
      }
    }

    return ServerResponse.createBySuccess(categoryList);
  }


  //递归算法，算出子节点
  private Set<Category> findChildCategory(Set<Category> categorySet, Integer categoryId){

    Category category = categoryMapper.selectByPrimaryKey(categoryId);
    if(category != null){
      categorySet.add(category);
    }

    List<Category> categories = categoryMapper.selectCategoryChildrenByParentId(categoryId);
    for (Category category1 : categories){
      findChildCategory(categorySet,category1.getId());
    }
    return categorySet;
  }
}
