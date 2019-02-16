package edu.netcracker.backend.service.serviceImplementation;


import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.dao.daoInterface.UserDAO;
import edu.netcracker.backend.dto.request.SignUpForm;
import edu.netcracker.backend.model.Role;
import edu.netcracker.backend.model.User;
import edu.netcracker.backend.security.UserInformationHolder;
import edu.netcracker.backend.service.serviceInterface.UserService;
import edu.netcracker.backend.utils.AuthorityUtils;
import edu.netcracker.backend.utils.PasswordGeneratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private PasswordEncoder passwordEncoder;


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

    public boolean ifUsernameExist(String username) {
        return userDAO.findByUsername(username).isPresent();
    }

    public boolean ifEmailExist(String email) {
        return userDAO.findByEmail(email).isPresent();
    }

    public String changePasswordForUser(User user) {
        String newPassword = PasswordGeneratorUtils.generatePassword();

        user.setUserPassword(passwordEncoder.encode(newPassword));
        userDAO.save(user);

        return newPassword;
    }

    public User createUser(SignUpForm signUpForm, boolean isActivated, List<Role> roles) {
        User user = new User(signUpForm.getUsername(),
                passwordEncoder.encode(signUpForm.getPassword()),
                signUpForm.getEmail());
        user.setUserIsActivated(isActivated);
        user.setUserRoles(roles);
        user.setUserTelephone(signUpForm.getTelephoneNumber());
        user.setUserCreatedDate(LocalDate.now());

        userDAO.save(user);

        return user;
    }

    public UserDetails createUserDetails(UserInformationHolder userInformationHolder) {
        if (userInformationHolder == null) {
            return null;
        }

        return new org.springframework.security.core.userdetails.User(userInformationHolder.getUsername(),
                userInformationHolder.getPassword(),
                mapRolesToAuthorities(userInformationHolder.getRoles()));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAO.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(username + " not found"));

        return user;
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<String> roles) {
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}