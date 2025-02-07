package com.jabibim.admin.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseAndStudyHistVO {

  private List<PurchasedCourseVO> purchasedCourses;
  private LastStudyHistoryVO studyHistory;

}
