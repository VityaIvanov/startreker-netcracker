package edu.netcracker.backend.service.serviceInterface;

import edu.netcracker.backend.dto.request.SignUpForm;
import edu.netcracker.backend.model.User;

import java.util.List;

public interface CarrierService {

    void save(User user);

    void delete(User user);

    User findByUsername(String userName);

    User findByEmail(String email);

    User createCarrier(SignUpForm signUpForm);

    List<User> findAllCarriers();
}
