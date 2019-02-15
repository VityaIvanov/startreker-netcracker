package edu.netcracker.backend.service.serviceInterface;

import edu.netcracker.backend.dto.request.SignUpForm;
import edu.netcracker.backend.model.User;

public interface CarrierService {

    void save(User user);

    void delete(User user);

    User findByUsername(String userName);

    User findByEmail(String email);

    User createCarrier(SignUpForm signUpForm);
}
