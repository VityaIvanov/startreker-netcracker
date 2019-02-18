package edu.netcracker.backend.carrierREST;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.netcracker.backend.BackendApplication;
import edu.netcracker.backend.dto.response.UserDTO;
import edu.netcracker.backend.model.Role;
import edu.netcracker.backend.model.User;
import edu.netcracker.backend.service.serviceInterface.UserService;
import edu.netcracker.backend.utils.AuthorityUtils;
import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BackendApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CarrierRESTTest {

    private static final String CREATE_TEST_DB_SCRIPT = "createTestDB.sql";
    private static final String DELETE_TEST_DB_SCRIPT = "deleteTestDB.sql";

    @LocalServerPort
    private int port;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private TestRestTemplate restTemplate = new TestRestTemplate();
    private HttpHeaders headers;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DataSource dataSource;

    @Before
    public void beforeTest() throws SQLException {
        startDB();
        headers = new HttpHeaders();
    }

    @After
    public void afterTest() throws SQLException {
        deleteDB();
        headers = new HttpHeaders();
    }

    @Test
    public void getCarrierByUsernameTestGood() throws Exception {
        String actual = getResponse("/api/carrier-by-username?username=vitya", HttpMethod.GET, null);

        UserDTO userDTO = UserDTO.from(userService.findByUsernameWithRole("vitya", AuthorityUtils.ROLE_CARRIER));

        String expected = objectMapper.writeValueAsString(userDTO);

        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    public void getCarrierByEmailTestGood() throws Exception {
        String actual = getResponse("/api/carrier-by-email?email=a@gmail.com", HttpMethod.GET, null);

        UserDTO userDTO = UserDTO.from(userService.findByEmailWithRole("a@gmail.com", AuthorityUtils.ROLE_CARRIER));

        String expected = objectMapper.writeValueAsString(userDTO);

        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    public void getCarrierByIdTestGood() throws Exception {
        User user = userService.findByUsernameWithRole("e", AuthorityUtils.ROLE_CARRIER);

        String actual = getResponse("/api/carrier/" + user.getUserId(), HttpMethod.GET, null);

        String expected = objectMapper.writeValueAsString(UserDTO.from(user));

        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    public void getAllCarrierTestGood() throws Exception {
        List<User> users = userService.findAllWithRole(AuthorityUtils.ROLE_CARRIER);

        List<UserDTO> userDTOS = users.stream().map(UserDTO::from).collect(Collectors.toList());

        String expected = objectMapper.writeValueAsString(userDTOS);

        String actual = getResponse("/api/carrier", HttpMethod.GET, null);

        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    public void getAllCarrierInRangeTestGood() throws Exception {
        User firstUser = userService.findByUsernameWithRole("a", AuthorityUtils.ROLE_CARRIER);
        User lastUser = userService.findByUsernameWithRole("e", AuthorityUtils.ROLE_CARRIER);

        List<User> users = userService.findByRangeIdWithRole(firstUser.getUserId(),
                lastUser.getUserId(),
                AuthorityUtils.ROLE_CARRIER);

        List<UserDTO> userDTOS = users.stream().map(UserDTO::from).collect(Collectors.toList());

        String expected = objectMapper.writeValueAsString(userDTOS);

        String actual = getResponse("/api/carrier-in-range-id?startId="+
                firstUser.getUserId()+
                "&endId=" + lastUser.getUserId(), HttpMethod.GET, null);

        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    public void createCarrierTestGood() throws Exception {
        String body = "{\n" +
                "\"username\":\"masha\",\n" +
                "\"password\":\"qwe123asd\",\n" +
                "\"email\":\"mq@gmail.com\",\n" +
                "\"telephone_number\":\"12344\",\n" +
                "\"is_activated\":\"false\"\n" +
                "}";

        headers.setContentType(MediaType.APPLICATION_JSON);

        String actual = getResponse("/api/carrier", HttpMethod.POST, body);

        User user = userService.findByUsernameWithRole("masha", AuthorityUtils.ROLE_CARRIER);

        if (user == null) {
            throw new RuntimeException();
        }

        String expected = objectMapper.writeValueAsString(UserDTO.from(user));

        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    public void deleteCarrierTestGood() throws JSONException, JsonProcessingException {
        User user = userService.findByUsernameWithRole("d", AuthorityUtils.ROLE_CARRIER);

        String actual = getResponse("/api/carrier/" + user.getUserId(), HttpMethod.DELETE, null);

        User deletedUser = userService.findByUsernameWithRole("d", AuthorityUtils.ROLE_CARRIER);

        if (deletedUser != null) {
            throw new RuntimeException();
        }

        String expected = objectMapper.writeValueAsString(UserDTO.from(user));

        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    public void updateCarrierTestGood() throws JSONException, JsonProcessingException {
        User user = userService.findByUsernameWithRole("vitya", AuthorityUtils.ROLE_CARRIER);

        String body = "{\n" +
                "\"user_id\":\"" + user.getUserId() + "\",\n" +
                "\"username\":\"masha\",\n" +
                "\"email\":\"masha@gmail.com\",\n" +
                "\"telephone_number\":\"12344\",\n" +
                "\"is_activated\":\"false\"\n" +
                "}";

        headers.setContentType(MediaType.APPLICATION_JSON);

        String actual = getResponse("/api/carrier", HttpMethod.PUT, body);

        User updatedUser = userService.findByIdWithRole(user.getUserId(), AuthorityUtils.ROLE_CARRIER);

        String expected = objectMapper.writeValueAsString(UserDTO.from(updatedUser));

        JSONAssert.assertEquals(expected, actual, false);
    }

    private String getResponse(String uri, HttpMethod httpMethod, String body) {
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(uri), httpMethod, entity, String.class);

        return response.getBody();
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    private void startDB() throws SQLException {
        executeScript(CREATE_TEST_DB_SCRIPT);

        userService.save(createUser(
                "vitya",
                "vitya",
                "vitya@gmail.ua",
                "093",
                true,
                Arrays.asList(AuthorityUtils.ROLE_USER, AuthorityUtils.ROLE_CARRIER)
        ));

        userService.save(createUser(
                "a",
                "a",
                "a@gmail.com",
                "093",
                true,
                Arrays.asList(AuthorityUtils.ROLE_USER, AuthorityUtils.ROLE_CARRIER)
        ));

        userService.save(createUser(
                "b",
                "b",
                "b@gmail.com",
                "093",
                true,
                Arrays.asList(AuthorityUtils.ROLE_USER)
        ));

        userService.save(createUser(
                "c",
                "c",
                "c@gmail.com",
                "093",
                true,
                Arrays.asList(AuthorityUtils.ROLE_USER)
        ));

        userService.save(createUser(
                "d",
                "d",
                "d@gmail.com",
                "093",
                true,
                Arrays.asList(AuthorityUtils.ROLE_USER, AuthorityUtils.ROLE_CARRIER)
        ));

        userService.save(createUser(
                "e",
                "e",
                "e@gmail.com",
                "093",
                true,
                Arrays.asList(AuthorityUtils.ROLE_USER, AuthorityUtils.ROLE_CARRIER)
        ));

        userService.save(createUser(
                "f",
                "f",
                "f@gmail.com",
                "093",
                true,
                Arrays.asList(AuthorityUtils.ROLE_USER, AuthorityUtils.ROLE_ADMIN)
        ));
    }

    private User createUser(String userName,
                            String password,
                            String mail,
                            String telephone,
                            boolean isActivated,
                            List<Role> roles) {
        User user = new User();
        user.setUserName(userName);
        user.setUserIsActivated(isActivated);
        user.setUserPassword(passwordEncoder.encode(password));
        user.setUserEmail(mail);
        user.setUserTelephone(telephone);
        user.setUserCreatedDate(LocalDate.now());
        user.setUserRoles(roles);

        return user;
    }

    private void deleteDB() throws SQLException {
        executeScript(DELETE_TEST_DB_SCRIPT);
    }

    private void executeScript(String scriptName) throws SQLException {
        ScriptUtils.executeSqlScript(dataSource.getConnection(),
                new EncodedResource(new ClassPathResource(scriptName),
                        StandardCharsets.UTF_8));
    }
}
