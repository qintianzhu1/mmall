package com.mmall.vo;

import java.math.BigDecimal;

public class ProductDetailVo {

  private Integer id;

  private Integer categoryId;

  private String name;

  private String subtitle;

  private String mainImage;

  private String subImages;

  private String detail;

  private BigDecimal price;

  private Integer stock;

  private Integer status;

  private String createTime;

  private String updateTime;


  //图片服务器 url前缀
  private String imageHost;

  private Integer parentCategoryId;

  public Integer getId() {
    return id;
  }

  public void setId(final Integer id) {
    this.id = id;
  }

  public Integer getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(final Integer categoryId) {
    this.categoryId = categoryId;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getSubtitle() {
    return subtitle;
  }

  public void setSubtitle(final String subtitle) {
    this.subtitle = subtitle;
  }

  public String getMainImage() {
    return mainImage;
  }

  public void setMainImage(final String mainImage) {
    this.mainImage = mainImage;
  }

  public String getSubImages() {
    return subImages;
  }

  public void setSubImages(final String subImages) {
    this.subImages = subImages;
  }

  public String getDetail() {
    return detail;
  }

  public void setDetail(final String detail) {
    this.detail = detail;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(final BigDecimal price) {
    this.price = price;
  }

  public Integer getStock() {
    return stock;
  }

  public void setStock(final Integer stock) {
    this.stock = stock;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(final Integer status) {
    this.status = status;
  }

  public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(final String createTime) {
    this.createTime = createTime;
  }

  public String getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(final String updateTime) {
    this.updateTime = updateTime;
  }

  public String getImageHost() {
    return imageHost;
  }

  public void setImageHost(final String imageHost) {
    this.imageHost = imageHost;
  }

  public Integer getParentCategoryId() {
    return parentCategoryId;
  }

  public void setParentCategoryId(final Integer parentCategoryId) {
    this.parentCategoryId = parentCategoryId;
  }
}
