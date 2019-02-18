package edu.netcracker.backend.service.serviceImplementation;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.dto.request.EmailFrom;
import edu.netcracker.backend.dto.request.SignInForm;
import edu.netcracker.backend.dto.request.SignUpForm;
import edu.netcracker.backend.dto.response.JwtResponse;
import edu.netcracker.backend.dto.response.Message;
import edu.netcracker.backend.model.User;
import edu.netcracker.backend.security.JwtProvider;
import edu.netcracker.backend.service.serviceInterface.AuthenticationService;
import edu.netcracker.backend.service.serviceInterface.UserService;
import edu.netcracker.backend.utils.AuthorityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final int ERROR_USER_ALREADY_EXISTS = -1;
    private static final int ERROR_MAIL_ALREADY_EXISTS = -2;
    private static final int ERROR_NO_SUCH_USER = -3;

    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtProvider jwtProvider;


    @Override
    public User signUp(SignUpForm signUpForm, HttpServletRequest request) {
        if (userService.ifUsernameExist(signUpForm.getUsername())) {
            throw new RequestException("Username already exist");
        }

        if (userService.ifEmailExist(signUpForm.getEmail())) {
            throw new RequestException("Email already exist");
        }

        User user = userService.createUser(signUpForm, false, Collections.singletonList(AuthorityUtils.ROLE_USER));

        emailService.sendRegistrationMessage(signUpForm.getEmail(),
                getContextPath(request),
                jwtProvider.generateMailRegistrationToken(user.getUsername()));

        return user;
    }

    @Override
    public void passwordRecovery(EmailFrom emailFrom) {
        User user = userService.findByEmail(emailFrom.getEmail());

        if (user == null) {
            throw new RequestException("No such user");
        }

        String newUserPassword = userService.changePasswordForUser(user);

        emailService.sendPasswordRecoveryMessage(emailFrom.getEmail(),
                user.getUsername(),
                newUserPassword);
    }

    @Override
    public JwtResponse signIn(SignInForm signInForm) {
        Authentication authentication = authenticationManager.
                authenticate(new UsernamePasswordAuthenticationToken(
                        signInForm.getUsername(),
                        signInForm.getPassword()));

        String accessToken = jwtProvider.generateAccessToken((UserDetails) authentication.getPrincipal());
        String refreshToken = jwtProvider.generateRefreshToken(signInForm.getUsername());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User user = userService.findByUsername(signInForm.getUsername());
        user.setUserRefreshToken(refreshToken);
        userService.save(user);

        return new JwtResponse(accessToken, refreshToken, "Bearer", userDetails.getUsername(),
                userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors
                        .toList()));
    }

    @Override
    public Message confirmPassword(String token) {

        if (!jwtProvider.validateToken(token))
            throw new RequestException("Invalid token");

        User user = userService.findByUsername(jwtProvider.retrieveSubject(token));

        if (user == null) {
            throw new RequestException("Invalid token");
        }

        user.setUserIsActivated(true);
        userService.save(user);

        return new Message(200, "password is confirmed");
    }

    @Override
    public Message logOut() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new RequestException("User is not authenticated!");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User user = userService.findByUsername(userDetails.getUsername());

        if (user == null)
            throw new RequestException("No such user!");


        user.setUserRefreshToken(null);
        userService.save(user);

        return new Message(200, "You are logged out");
    }

    private String getContextPath(HttpServletRequest request) {
        // ne robet
        return request.getRequestURL().toString().replace(request.getRequestURI(), "");
    }
}
