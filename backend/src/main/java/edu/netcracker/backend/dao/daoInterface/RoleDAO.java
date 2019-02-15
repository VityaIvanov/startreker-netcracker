package edu.netcracker.backend.dao.daoInterface;

import edu.netcracker.backend.model.Role;

import java.util.Optional;

public interface RoleDAO {

    void save(Role role);

    Optional<Role> get(Number id);

    Optional<Role> get(String roleName);

    void delete(Role role);

    void update(Role role);

}
