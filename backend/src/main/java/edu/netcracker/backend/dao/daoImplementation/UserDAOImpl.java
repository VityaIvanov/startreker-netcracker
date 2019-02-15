package edu.netcracker.backend.dao.daoImplementation;

import edu.netcracker.backend.dao.daoInterface.RoleDAO;
import edu.netcracker.backend.dao.daoInterface.UserDAO;
import edu.netcracker.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDAOImpl implements UserDAO {

    private final RoleDAO roleDAO;
    private final String findByUsernameSql = "SELECT * FROM usr WHERE user_name = ?";
    private final String findByEmailSql = "SELECT * FROM usr WHERE user_email = ?";
    private final String findAllRolesSql = "SELECT role_id FROM assigned_role WHERE user_id = ?";
    private final String removeAllUserRolesSql = "DELETE FROM assigned_role WHERE user_id = ?";
    private final String addRoleSql = "INSERT INTO assigned_role (user_id, role_id) VALUES (?, ?)";
    private final String removeRoleSql = "DELETE FROM assigned_role WHERE user_id = ? AND role_id = ?";

    @Autowired
    public UserDAOImpl(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    @Override
    public Optional<User> find(Number id) {
        return null;
    }

    public Optional<User> findByUsername(String userName) {
        return null;
    }

    public Optional<User> findByEmail(String email) {
        return null;
    }

    @Override
    public void save(User user) {

    }

    @Override
    public void delete(User user) {
    }

    private Optional<User> attachRoles(User user) {
        return null;
    }

    private void updateRoles(User user) {

    }
}
