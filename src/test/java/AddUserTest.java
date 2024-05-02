import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class AddUserTest {
    private UserClient userClient;
    private User user;
    private String accessToken;
    private String refreshToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.base();
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }

    @Test
    public void addUserTest() {

    ValidatableResponse response = userClient.addUser(user);
    int statusCode = response.extract().statusCode();
    boolean success = response.extract().path("success");
    assertEquals(statusCode, 200);
    assertTrue(success);

    ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));
    accessToken =loginResponse.extract().path("accessToken");
    refreshToken = loginResponse.extract().path("refreshToken");
    int loginStatusCode = loginResponse.extract().statusCode();
    assertNotNull(accessToken);
    assertNotNull(refreshToken);
    assertEquals(200, loginStatusCode);

}

    @Test
    public void addExistedUserTest() {

        userClient.addUser(user);
        ValidatableResponse response = userClient.addUser(user);

        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));
        accessToken =loginResponse.extract().path("accessToken");

        int statusCode = response.extract().statusCode();
        boolean success = response.extract().path("success");
        String message = response.extract().path("message");
        assertEquals(403, statusCode);
        assertEquals( "User already exists", message);
        assertFalse(success);

    }

    @Test
    public void addUserWithoutEmailTest() {

        user.setEmail(null);
        ValidatableResponse response = userClient.addUser(user);

        int statusCode = response.extract().statusCode();
        boolean success = response.extract().path("success");
        assertEquals(403, statusCode);
        assertFalse(success);
        assertEquals("Email, password and name are required fields", response.extract().path("message"));

    }

    @Test
    public void addUserWithoutPasswordTest() {

        user.setPassword(null);
        ValidatableResponse response = userClient.addUser(user);

        int statusCode = response.extract().statusCode();
        boolean success = response.extract().path("success");
        assertEquals(403, statusCode);
        assertFalse(success);
        assertEquals("Email, password and name are required fields", response.extract().path("message"));

    }

    @Test
    public void addUserWithoutNameTest() {

        user.setName(null);
        ValidatableResponse response = userClient.addUser(user);

        int statusCode = response.extract().statusCode();
        boolean success = response.extract().path("success");
        assertEquals(403, statusCode);
        assertFalse(success);
        assertEquals("Email, password and name are required fields", response.extract().path("message"));

    }

    }

