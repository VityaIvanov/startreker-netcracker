package edu.netcracker.backend.service.serviceInterface;



import edu.netcracker.backend.dto.request.UserCreateForm;
import edu.netcracker.backend.dto.request.UserUpdateForm;
import edu.netcracker.backend.dto.response.UserDTO;

import java.util.List;


public interface CarrierService {

    UserDTO getCarrierByUsername(String username);

    UserDTO getCarrierByEmail(String email);

    UserDTO getCarrierById(Number userId);

    List<UserDTO> getAllCarrier();

    List<UserDTO> getAllCarrier(Number startId, Number endId);

    UserDTO createCarrier(UserCreateForm createForm);

    UserDTO deleteCarrier(Number userId);

    UserDTO updateCarrier(UserUpdateForm updateFrom);
}
