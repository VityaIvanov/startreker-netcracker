package edu.netcracker.backend.service.serviceImplementation;

import edu.netcracker.backend.dao.daoInterface.UserDAO;
import edu.netcracker.backend.dto.request.SignUpForm;
import edu.netcracker.backend.model.User;
import edu.netcracker.backend.security.UserInformationHolder;
import edu.netcracker.backend.service.serviceInterface.CarrierService;
import edu.netcracker.backend.service.serviceInterface.UserService;
import edu.netcracker.backend.utils.AuthorityUtils;
import edu.netcracker.backend.utils.PasswordGeneratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class CarrierServiceImpl implements CarrierService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserService userService;

    @Override
    public void save(User user) {
        userDAO.save(user);
    }

    @Override
    public void delete(User user) {
        userDAO.delete(user);
    }

    @Override
    public User findByUsername(String userName) {
        return userDAO.findByUsername(userName).orElse(null);
    }

    @Override
    public User findByEmail(String email) {
        return userDAO.findByEmail(email).orElse(null);
    }

    @Override
    public User createCarrier(SignUpForm signUpForm) {
        return userService.createUser(signUpForm,
                true,
                Arrays.asList(AuthorityUtils.ROLE_CARRIER, AuthorityUtils.ROLE_USER));
    }

    @Override
    public List<User> findAllCarriers() {
        return userDAO.findAll("ROLE_CARRIER");
    }


}
