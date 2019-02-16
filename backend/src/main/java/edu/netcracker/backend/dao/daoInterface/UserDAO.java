package edu.netcracker.backend.dao.daoInterface;

import edu.netcracker.backend.model.Role;
import edu.netcracker.backend.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {

    void save(User user);

    Optional<User> find(Number id);

    void delete(User user);

    Optional<User> findByUsername(String userName);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    List<User> findAll(String roleName);
}
