package com.jabibim.admin.dto;

import java.time.LocalDateTime;

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
public class LastStudyHistoryVO {
  private String studyHistoryId;
  private LocalDateTime lastAccessedDate;
  private String lastAccessedClassId;
  private String lastAccessedClassName;
}
