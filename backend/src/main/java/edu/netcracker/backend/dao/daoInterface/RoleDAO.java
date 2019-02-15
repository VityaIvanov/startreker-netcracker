package edu.netcracker.backend.dao.daoInterface;

import edu.netcracker.backend.model.Role;
import edu.netcracker.backend.model.User;

import java.util.List;
import java.util.Optional;

public interface RoleDAO {

    void save(Role role);

    Optional<Role> find(Number id);

    void delete(Role role);
}
