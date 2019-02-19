package edu.netcracker.backend.dto.response;

import edu.netcracker.backend.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserDTO {

    private Integer userId;
    private String userName;
    private String userEmail;
    private String userTelephone;
    private boolean userIsActivated;
    private String userCreatedDate;
    private List<String> roles;

    private UserDTO(Integer userId,
                    String userName,
                    String userEmail,
                    String userTelephone,
                    boolean userIsActivated,
                    LocalDate userCreatedDate,
                    Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userTelephone = userTelephone;
        this.userIsActivated = userIsActivated;
        this.userCreatedDate = userCreatedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.roles = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    }

    public static UserDTO from(User user){
        return new UserDTO(user.getUserId(),
                user.getUsername(),
                user.getUserEmail(),
                user.getUserTelephone(),
                user.isUserIsActivated(),
                user.getUserCreatedDate(),
                user.getAuthorities());
    }
}
