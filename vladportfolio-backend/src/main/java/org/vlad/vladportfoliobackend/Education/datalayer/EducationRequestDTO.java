package org.vlad.vladportfoliobackend.Education.datalayer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EducationRequestDTO {
    private String institutionName;
    private String degree;
    private String years;
}
