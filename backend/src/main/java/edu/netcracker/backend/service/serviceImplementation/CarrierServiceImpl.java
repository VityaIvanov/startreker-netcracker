package edu.netcracker.backend.service.serviceImplementation;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.dto.request.UserCreateForm;
import edu.netcracker.backend.dto.request.UserUpdateForm;
import edu.netcracker.backend.dto.response.UserDTO;
import edu.netcracker.backend.model.Role;
import edu.netcracker.backend.model.User;
import edu.netcracker.backend.service.serviceInterface.CarrierService;
import edu.netcracker.backend.service.serviceInterface.UserService;
import edu.netcracker.backend.utils.AuthorityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarrierServiceImpl implements CarrierService {

    @Autowired
    private UserService userService;

    @Override
    public UserDTO getCarrierByUsername(String username) {

        User user = userService.findByUsernameWithRole(username, AuthorityUtils.ROLE_CARRIER);

        if (user == null) {
            throw new RequestException("Carrier " + username + " not exist");
        }

        return UserDTO.from(user);
    }

    @Override
    public UserDTO getCarrierByEmail(String email) {
        User user = userService.findByEmailWithRole(email, AuthorityUtils.ROLE_CARRIER);

        if (user == null) {
            throw new RequestException("Carrier with email" + email + " not exist");
        }

        return UserDTO.from(user);
    }

    @Override
    public UserDTO getCarrierById(Number userId) {
        User user = userService.findByIdWithRole(userId, AuthorityUtils.ROLE_CARRIER);

        if (user == null) {
            throw new RequestException("No such user");
        }

        return UserDTO.from(user);
    }

    @Override
    public List<UserDTO> getAllCarrier() {
        List<User> users = userService.findAllWithRole(AuthorityUtils.ROLE_CARRIER);

        if (users.isEmpty()) {
            throw new RequestException("No carriers");
        }

        return users.stream().map(UserDTO::from).collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getAllCarrier(Number startId, Number endId) {
        List<User> users = userService.findByRangeIdWithRole(startId, endId, AuthorityUtils.ROLE_CARRIER);

        if (users.isEmpty()) {
            throw new RequestException("No carriers in range");
        }

        return users.stream().map(UserDTO::from).collect(Collectors.toList());
    }

    @Override
    public UserDTO createCarrier(UserCreateForm createForm) {
        User user = userService.createUser(createForm, carrierRoles());
        return UserDTO.from(user);
    }

    @Override
    public UserDTO deleteCarrier(Number userId) {
        User user = userService.findByIdWithRole(userId, AuthorityUtils.ROLE_CARRIER);

        if (user == null) {
            throw new RequestException("No carriers with this id " + userId);
        }

        userService.delete(user);
        return UserDTO.from(user);
    }

    @Override
    public UserDTO updateCarrier(UserUpdateForm updateFrom) {
        User user = userService.findByIdWithRole(updateFrom.getUserId(), AuthorityUtils.ROLE_CARRIER);

        if (user == null) {
            throw new RequestException("No carriers with this id " + updateFrom.getUserId());
        }

        if (userService.ifUsernameExist(updateFrom.getUsername())) {
            throw new RequestException("Username already exist");
        }

        if (userService.ifEmailExist(updateFrom.getEmail())) {
            throw new RequestException("Email already exist");
        }

        user.setUserEmail(updateFrom.getEmail());
        user.setUserTelephone(updateFrom.getTelephoneNumber());
        user.setUserName(updateFrom.getUsername());
        user.setUserIsActivated(updateFrom.getIsActivated());

        userService.save(user);

        return UserDTO.from(user);
    }

    private List<Role> carrierRoles(){
        return Arrays.asList(AuthorityUtils.ROLE_USER, AuthorityUtils.ROLE_CARRIER);
    }

}