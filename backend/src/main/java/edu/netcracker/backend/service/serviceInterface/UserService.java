package edu.netcracker.backend.service.serviceInterface;

import edu.netcracker.backend.dto.request.SignUpForm;
import edu.netcracker.backend.model.User;
import edu.netcracker.backend.security.UserInformationHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    void save(User user);

    void delete(User user);

    User findByUsername(String userName);

    User findByEmail(String email);

    UserDetails createUserDetails(UserInformationHolder userInformationHolder);

    boolean ifUsernameExist(String username);

    boolean ifEmailExist(String email);

    String changePasswordForUser(User user);

    User createUser(SignUpForm signUpForm, boolean isActivated);
}
