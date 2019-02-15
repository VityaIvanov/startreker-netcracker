package edu.netcracker.backend.dao.daoImplementation;

import edu.netcracker.backend.dao.daoInterface.CrudDAO;
import edu.netcracker.backend.dao.daoInterface.RoleDAO;
import edu.netcracker.backend.dao.daoInterface.UserDAO;
import edu.netcracker.backend.dao.rowMapper.UserRowMapper;
import edu.netcracker.backend.model.Role;
import edu.netcracker.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserDAOImpl extends CrudDAO<User> implements UserDAO {

    private final RoleDAO roleDAO;
    private final String findByUsernameSql = "SELECT * FROM usr WHERE user_name = ?";
    private final String findByEmailSql = "SELECT * FROM usr WHERE user_email = ?";
    private final String findAllRolesSql = "SELECT role_id FROM assigned_role WHERE user_id = ?";
    private final String removeAllUserRolesSql = "DELETE FROM assigned_role WHERE user_id = ?";
    private final String addRoleSql = "INSERT INTO assigned_role (user_id, role_id) VALUES (?, ?)";
    private final String removeRoleSql = "DELETE FROM assigned_role WHERE user_id = ? AND role_id = ?";

    private final String findByUsernameCarrierSql = "SELECT DISTINCT * FROM usr\n" +
            "  INNER JOIN assigned_role ON assigned_role.user_id = usr.user_id\n" +
            "  INNER JOIN role ON assigned_role.role_id = role.role_id WHERE role_name = 'ROLE_CARRIER' and user_name = ?;";

    private final String findByEmailCarrierSql = "SELECT DISTINCT * FROM usr\n" +
            "  INNER JOIN assigned_role ON assigned_role.user_id = usr.user_id\n" +
            "  INNER JOIN role ON assigned_role.role_id = role.role_id WHERE role_name = 'ROLE_CARRIER' and user_email = ?;";

    private final String findAllCarrierSql = "SELECT DISTINCT * FROM usr\n" +
            "  INNER JOIN assigned_role ON assigned_role.user_id = usr.user_id\n" +
            "  INNER JOIN role ON assigned_role.role_id = role.role_id WHERE role_name = 'ROLE_CARRIER'";

    @Autowired
    public UserDAOImpl(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    @Override
    public Optional<User> find(Number id) {
        Optional<User> userOpt = super.find(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return attachRoles(user);
        }
        return Optional.empty();
    }

    public Optional<User> findByUsername(String userName) {
        try{
            User user = getJdbcTemplate().queryForObject(
                    findByUsernameSql,
                    new Object[]{userName},
                    new UserRowMapper());
            return user != null ? attachRoles(user) : Optional.empty();
        }catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    public Optional<User> findByEmail(String email) {
        try{
            User user = getJdbcTemplate().queryForObject(
                    findByEmailSql,
                    new Object[]{email},
                    new UserRowMapper());
            return user != null ? attachRoles(user) : Optional.empty();
        }catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findCarrierByUsername(String userName) {
        try{
            User user = getJdbcTemplate().queryForObject(
                    findByUsernameCarrierSql,
                    new Object[]{userName},
                    new UserRowMapper());
            return user != null ? attachRoles(user) : Optional.empty();
        }catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findCarrierByEmail(String email) {
        try{
            User user = getJdbcTemplate().queryForObject(
                    findByEmailCarrierSql,
                    new Object[]{email},
                    new UserRowMapper());
            return user != null ? attachRoles(user) : Optional.empty();
        }catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<User>> findAllCarriers() {
        try{
            List<User> users = getJdbcTemplate().query(
                    findAllCarrierSql,
                    new UserRowMapper());

            if (users == null) {
                return Optional.empty();
            }

            for (User user: users) {
                attachRoles(user);
            }

            return Optional.of(users);
        } catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    @Override
    public void save(User user) {
        super.save(user);
        updateRoles(user);
    }

    @Override
    public void delete(User user) {
        getJdbcTemplate().update(removeAllUserRolesSql, user.getUserId());
        super.delete(user);
    }

    private Optional<User> attachRoles(User user) {
        List<Long> rows = getJdbcTemplate().queryForList(findAllRolesSql, Long.class, user.getUserId());
        List<Role> roles = new ArrayList<>();
        for (Long role_id : rows) {
            roles.add(roleDAO.find(role_id).orElse(null));
        }
        user.setUserRoles(roles);
        return Optional.of(user);
    }

    private void updateRoles(User user) {
        List<Long> dbRoleIds = getJdbcTemplate().queryForList(findAllRolesSql, Long.class, user.getUserId());
        List<Long> userRoleIds = user.getUserRoles()
                .stream()
                .map(Role::getRoleId)
                .collect(Collectors.toList());
        for (Long role_id : userRoleIds) {
            if (!dbRoleIds.contains(role_id)) {
                getJdbcTemplate().update(addRoleSql, user.getUserId(), role_id);
            }
        }
        for (Long db_role : dbRoleIds) {
            if (!userRoleIds.contains(db_role)) {
                getJdbcTemplate().update(removeRoleSql, user.getUserId(), db_role);
            }
        }
    }
}
