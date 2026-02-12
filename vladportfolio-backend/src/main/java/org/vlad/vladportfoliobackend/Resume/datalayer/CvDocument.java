package org.vlad.vladportfoliobackend.Resume.datalayer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("cv_documents")
public class CvDocument {
    @Id
    private Long id;
    private String language;
    private String filename;
    private byte[] data;
    private String contentType;
    private LocalDateTime uploadedAt;
}
