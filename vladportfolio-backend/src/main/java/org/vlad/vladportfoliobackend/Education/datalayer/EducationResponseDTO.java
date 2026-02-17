package org.vlad.vladportfoliobackend.Education.datalayer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EducationResponseDTO {
    private String id;
    private String institutionName;
    private String degree;
    private boolean active;
    private int displayOrder;

    public static EducationResponseDTO from(Education education) {
        return new EducationResponseDTO(
                String.valueOf(education.getId()),
                education.getInstitutionName(),
                education.getDegree(),
                education.isActive(),
                education.getDisplayOrder()
        );
    }
}
