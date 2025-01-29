package com.jabibim.admin.front.dto;

import org.springframework.stereotype.Component;

@Component
public class OrderApiVO {
  private String orderNumber;
  private int totalAmount;
  private String cartIds;
  private String academyId;
  private String studentId;

  public OrderApiVO() {
  }

  public String getOrderNumber() {
    return this.orderNumber;
  }

  public void setOrderNumber(String orderNumber) {
    this.orderNumber = orderNumber;
  }

  public int getTotalAmount() {
    return this.totalAmount;
  }

  public void setTotalAmount(int totalAmount) {
    this.totalAmount = totalAmount;
  }

  public String getCartIds() {
    return this.cartIds;
  }

  public void setCartIds(String cartIds) {
    this.cartIds = cartIds;
  }

  public String getAcademyId() {
    return this.academyId;
  }

  public void setAcademyId(String academyId) {
    this.academyId = academyId;
  }

  public String getStudentId() {
    return this.studentId;
  }

  public void setStudentId(String studentId) {
    this.studentId = studentId;
  }

}
