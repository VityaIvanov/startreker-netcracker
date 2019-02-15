package edu.netcracker.backend.dao.daoImplementation;

import edu.netcracker.backend.dao.daoInterface.RoleDAO;
import edu.netcracker.backend.dao.rowMapper.RoleRowMapper;
import edu.netcracker.backend.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RoleDAOImpl implements RoleDAO {

    private static final String insertSQL = "INSERT INTO role (role_name) VALUES (:role_name) ";
    private static final String selectSQLId = "SELECT role_id, role_name FROM role WHERE role_id = :role_id";
    private static final String selectSQLName = "SELECT role_id, role_name FROM role WHERE role_name = :role_name";
    private static final String deleteSQL = "DELETE FROM role WHERE role_id = :role_id";
    private static final String updateSQL = "UPDATE role SET role_name = :role_name WHERE role_id = :role_id";

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void save(Role role) {
        jdbcTemplate.update(insertSQL, new MapSqlParameterSource("role_name", role.getRoleName()));
    }

    @Override
    public Optional<Role> get(Number id) {
        try {
            Role role = jdbcTemplate.queryForObject(selectSQLId,
                    new MapSqlParameterSource("role_id", id),
                    new RoleRowMapper());
            return Optional.of(role);

        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Role> get(String roleName) {
        try {
            Role role = jdbcTemplate.queryForObject(selectSQLName,
                    new MapSqlParameterSource("role_name", roleName),
                    new RoleRowMapper());
            return Optional.of(role);

        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void delete(Role role) {
        jdbcTemplate.update(deleteSQL, new MapSqlParameterSource("role_id", role.getRoleId()));
    }

    @Override
    public void update(Role role) {
        jdbcTemplate.update(updateSQL,
                new MapSqlParameterSource("role_id", role.getRoleId()).
                addValue("role_name", role.getRoleName()));
    }

    @Autowired
    public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}

