package edu.netcracker.backend.dao.rowMapper;

import edu.netcracker.backend.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new User();
        user.setUserId(resultSet.getLong("user_id"));
        user.setUserName(resultSet.getString("user_name"));
        user.setUserPassword(resultSet.getString("user_password"));
        user.setUserEmail(resultSet.getString("user_email"));
        user.setUserTelephone(resultSet.getString("user_telephone"));
        user.setUserCreatedDate(resultSet.getDate("user_created").toLocalDate());
        user.setUserIsActivated(resultSet.getBoolean("user_activated"));
        user.setUserRefreshToken(resultSet.getString("user_token"));
        return user;
    }
}
