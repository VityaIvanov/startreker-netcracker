package edu.netcracker.backend.controller;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.dto.request.EmailFrom;
import edu.netcracker.backend.dto.request.SignUpForm;
import edu.netcracker.backend.dto.request.SignInForm;
import edu.netcracker.backend.dto.response.JwtResponse;
import edu.netcracker.backend.model.User;
import edu.netcracker.backend.security.JwtProvider;
import edu.netcracker.backend.service.serviceImplementation.EmailService;
import edu.netcracker.backend.service.serviceInterface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
public class AuthController {

    private static final int ERROR_USER_ALREADY_EXISTS = -1;
    private static final int ERROR_MAIL_ALREADY_EXISTS = -2;
    private static final int ERROR_NO_SUCH_USER = -3;

    private AuthenticationManager authenticationManager;
    private JwtProvider jwtProvider;
    private UserService userService;
    private EmailService emailService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          JwtProvider jwtProvider,
                          UserService userService,
                          EmailService emailService) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.userService = userService;
        this.emailService = emailService;
    }

    @PostMapping(path = "/api/auth/sign-up")
    public User signUp(@Valid @RequestBody SignUpForm signUpForm, HttpServletRequest request){
        if (userService.ifUsernameExist(signUpForm.getUsername())) {
            throw new RequestException("Username already exist", ERROR_USER_ALREADY_EXISTS);
        }

        if (userService.ifEmailExist(signUpForm.getEmail())) {
            throw new RequestException("Email already exist", ERROR_MAIL_ALREADY_EXISTS);
        }

        User user = userService.createUser(signUpForm, false);

        emailService.sendRegistrationMessage(signUpForm.getEmail(),
                getContextPath(request),
                jwtProvider.generateMailRegistrationToken(user.getUsername()));

        return user;
    }

    @PostMapping(path = "/api/auth/password-recovery")
    public EmailFrom passwordRecovery(@Valid @RequestBody EmailFrom emailFrom) {
        User user = userService.findByEmail(emailFrom.getEmail());

        if (user == null) {
            throw new RequestException("No such user", ERROR_NO_SUCH_USER);
        }

        String newUserPassword = userService.changePasswordForUser(user);

        emailService.sendPasswordRecoveryMessage(emailFrom.getEmail(),
                user.getUsername(),
                newUserPassword);

        return emailFrom;
    }

    @PostMapping(path = "/api/log-out")
    public String logOut() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new RequestException("User is not authenticated!", 1);
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User user = userService.findByUsername(userDetails.getUsername());

        if (user == null)
            throw new RequestException("No such user!", 2);


        user.setUserRefreshToken(null);
        userService.save(user);

        return "OK";
    }

    @GetMapping(path = "api/auth/confirm-password")
    public ResponseEntity<String> confirmPassword(@Valid @RequestParam("token") String token) {

        if (!jwtProvider.validateToken(token))
            return ResponseEntity.created(null).body("NOT OK1");

        User user = userService.findByUsername(jwtProvider.retrieveSubject(token));

        if (user == null)
            return ResponseEntity.created(null).body("NOT OK2");

        user.setUserIsActivated(true);
        userService.save(user);

        return ResponseEntity.created(null).body("OK");
    }

    @PostMapping("/api/auth/sign-in")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInForm signInForm) {

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

        return ResponseEntity.ok(new JwtResponse(accessToken, refreshToken, "Bearer", userDetails.getUsername(), userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors
                .toList())));
    }

    private String getContextPath(HttpServletRequest request) {
        // ne robet
        return request.getRequestURL().toString().replace(request.getRequestURI(), "");
    }
}
