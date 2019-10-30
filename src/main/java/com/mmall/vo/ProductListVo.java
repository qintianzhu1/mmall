package com.mmall.vo;

import java.math.BigDecimal;

public class ProductListVo {

  private Integer id;

  private Integer categoryId;

  private String name;

  private String subtitle;

  private String mainImage;

  private BigDecimal price;

  private Integer status;

  private String imageHost;

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

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(final BigDecimal price) {
    this.price = price;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(final Integer status) {
    this.status = status;
  }

  public String getImageHost() {
    return imageHost;
  }

  public void setImageHost(final String imageHost) {
    this.imageHost = imageHost;
  }
}
