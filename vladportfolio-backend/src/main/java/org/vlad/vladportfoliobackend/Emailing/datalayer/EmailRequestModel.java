package org.vlad.vladportfoliobackend.Emailing.datalayer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequestModel {
    @NotNull
    private String name;

    @Email
    @NotNull
    private String email;

    @NotNull
    private String message;
}
