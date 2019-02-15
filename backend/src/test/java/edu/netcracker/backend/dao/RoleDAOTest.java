package edu.netcracker.backend.dao;

import edu.netcracker.backend.dao.daoInterface.RoleDAO;
import edu.netcracker.backend.model.Role;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles(profiles = "test")
public class RoleDAOTest {

    @Autowired
    private RoleDAO roleDAO;

    @Before
    public void setup() {
        EmbeddedDatabase db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .build();

        JdbcTemplate jdbcTemplate = new JdbcTemplate(db);

        jdbcTemplate.update("INSERT INTO role (role_name) VALUES ('asfas')");

        System.out.println();
    }

    @Test
    public void daoTest(){
        // Basically a CrudDAO test
        Role role = new Role();
        role.setRoleName("testrole");
        roleDAO.save(role);
        Role check = roleDAO.find(role.getRoleId()).get();
        assertThat(check.getRoleName(), equalTo("testrole"));

        role.setRoleName("updatedname");
        roleDAO.save(role);
        check = roleDAO.find(role.getRoleId()).get();
        assertThat(check.getRoleName(), equalTo("updatedname"));

        roleDAO.delete(check);
        check = roleDAO.find(role.getRoleId()).orElse(null);
        assertThat(check, equalTo(null));
    }
}
