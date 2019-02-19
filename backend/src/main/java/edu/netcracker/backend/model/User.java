package edu.netcracker.backend.model;

import edu.netcracker.backend.dao.annotation.Attribute;
import edu.netcracker.backend.dao.annotation.PrimaryKey;
import edu.netcracker.backend.dao.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Table("usr")
public class User implements UserDetails {

    @PrimaryKey("user_id")
    @EqualsAndHashCode.Include
    private Integer userId;
    @Attribute("user_name")
    private String userName;
    @Attribute("user_password")
    private String userPassword;
    @Attribute("user_email")
    private String userEmail;
    @Attribute("user_telephone")
    private String userTelephone;
    @Attribute("user_token")
    private String userRefreshToken;
    @Attribute("user_activated")
    private boolean userIsActivated;
    @Attribute("user_created")
    private LocalDate userCreatedDate;

    private List<Role> userRoles = new ArrayList<>();

    public User(String userName, String userPassword, String userEmail) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(
                userRoles.stream()
                        .map(Role::getRoleName)
                        .collect(Collectors.toList())
                        .toArray(new String[userRoles.size()]));
    }

    public void addRole(Role role) {
        if (userRoles == null) {
            userRoles = new ArrayList<>();
        }

        userRoles.add(role);
    }

    @Override
    public String getPassword() {
        return userPassword;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return userIsActivated;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean isUserIsActivated() {
        return userIsActivated;
    }
}
