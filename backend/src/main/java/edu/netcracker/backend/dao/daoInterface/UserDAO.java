package edu.netcracker.backend.dao.daoInterface;

import edu.netcracker.backend.model.User;

import java.util.Optional;

public interface UserDAO {

    void save(User user);

    Optional<User> find(Number id);

    void delete(User user);

    Optional<User> findByUsername(String userName);

    Optional<User> findByEmail(String email);

    Optional<User> findCarrierByUsername(String userName);

    Optional<User> findCarrierByEmail(String email);
}
