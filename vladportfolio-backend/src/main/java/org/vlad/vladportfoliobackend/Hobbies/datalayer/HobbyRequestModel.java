package org.vlad.vladportfoliobackend.Hobbies.datalayer;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class HobbyRequestModel {
    private String hobbyName;
    private String description;
    private List<String> tags;
}
