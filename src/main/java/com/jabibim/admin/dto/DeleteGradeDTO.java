package com.jabibim.admin.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DeleteGradeDTO {
    private String academyId;
    private String gradeId;
    private String newGradeId;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public DeleteGradeDTO(String academyId, String gradeId, String newGradeId) {
        this.academyId = academyId;
        this.gradeId = gradeId;
        this.newGradeId = newGradeId;
    }
}
