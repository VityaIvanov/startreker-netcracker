package edu.netcracker.backend.service.serviceInterface;

import edu.netcracker.backend.dto.request.EmailFrom;
import edu.netcracker.backend.dto.request.SignInForm;
import edu.netcracker.backend.dto.request.SignUpForm;
import edu.netcracker.backend.dto.response.JwtResponse;
import edu.netcracker.backend.dto.response.Message;
import edu.netcracker.backend.model.User;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationService {

    User signUp(SignUpForm signUpForm, HttpServletRequest request);

    void passwordRecovery(EmailFrom emailFrom);

    JwtResponse signIn(SignInForm signInForm);

    Message confirmPassword(String token);

    Message logOut();
}
