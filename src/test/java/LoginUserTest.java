import com.github.javafaker.Faker;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LoginUserTest {

    private UserClient userClient;
    private User user;
    private String accessToken;
    private String refreshToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.base();
        userClient.addUser(user);

    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }

    @Test
    public void loginExistedUser() {

        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));
        accessToken =loginResponse.extract().path("accessToken");
        refreshToken = loginResponse.extract().path("refreshToken");
        int loginStatusCode = loginResponse.extract().statusCode();
        assertNotNull(accessToken);
        assertNotNull(refreshToken);
        assertEquals(loginStatusCode, 200);
    }

    @Test
    public void loginWithIncorrectEmail() {
        UserCredentials userCredentials = new UserCredentials();

        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));
        accessToken =loginResponse.extract().path("accessToken");

        Faker faker = new Faker();
        userCredentials.setEmail(faker.internet().emailAddress());
        ValidatableResponse response = userClient.login(userCredentials);
        boolean success = response.extract().path("success");
        assertEquals(401, response.extract().statusCode());
        assertFalse(success);
        assertEquals("email or password are incorrect", response.extract().path("message"));
    }

    @Test
    public void loginWithIncorrectPassword() {
        UserCredentials userCredentials = new UserCredentials();

        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));
        accessToken =loginResponse.extract().path("accessToken");

        Faker faker = new Faker();
        userCredentials.setPassword(faker.internet().password());
        ValidatableResponse response = userClient.login(userCredentials);
        boolean success = response.extract().path("success");
        assertEquals(401, response.extract().statusCode());
        assertFalse(success);
        assertEquals("email or password are incorrect", response.extract().path("message"));
    }

}
