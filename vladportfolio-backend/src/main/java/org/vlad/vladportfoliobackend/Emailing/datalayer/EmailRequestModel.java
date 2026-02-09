package org.vlad.vladportfoliobackend.Emailing.datalayer;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

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
