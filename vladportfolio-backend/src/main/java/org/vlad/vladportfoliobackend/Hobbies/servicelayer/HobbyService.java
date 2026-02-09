package org.vlad.vladportfoliobackend.Hobbies.servicelayer;

import org.vlad.vladportfoliobackend.Hobbies.datalayer.HobbyRequestModel;
import org.vlad.vladportfoliobackend.Hobbies.datalayer.HobbyResponseDTO;

import java.util.List;

public interface HobbyService {

    public List<HobbyResponseDTO> getAllHobbies();

    public HobbyResponseDTO addHobby(HobbyRequestModel hobby);

    public HobbyResponseDTO editHobby(Long id, HobbyRequestModel hobby);

    public void deleteHobby(Long id);
}
