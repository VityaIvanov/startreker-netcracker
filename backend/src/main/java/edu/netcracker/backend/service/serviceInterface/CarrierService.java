package edu.netcracker.backend.service.serviceInterface;

import edu.netcracker.backend.dto.request.UserCreateForm;
import edu.netcracker.backend.dto.request.UserUpdateForm;
import edu.netcracker.backend.dto.response.UserDTO;

import java.util.List;

public interface CarrierService {

    UserDTO getCarrierByUsername(String username);

    UserDTO getCarrierByEmail(String email);

    UserDTO getCarrierById(Integer userId);

    List<UserDTO> getAllCarrier();

    List<UserDTO> getAllCarrier(Integer startId, Integer endId);

    UserDTO createCarrier(UserCreateForm createForm);

    UserDTO deleteCarrier(Integer userId);

    UserDTO updateCarrier(UserUpdateForm updateFrom);

    List<UserDTO> getPagination(Integer start, Integer number);
}
