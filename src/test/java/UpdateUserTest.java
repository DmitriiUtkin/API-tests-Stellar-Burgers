import com.github.javafaker.Faker;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UpdateUserTest {
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
    public void updateAuthorizedUserTest() {
        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));
        accessToken =loginResponse.extract().path("accessToken");
        Faker faker = new Faker();
        ValidatableResponse response = userClient.updateUserAfterAuthorization((new User(faker.internet().emailAddress(), faker.internet().password(), faker.name().username())), accessToken);
        int statusCode = response.extract().statusCode();
        boolean success = response.extract().path("success");
        assertEquals(200, statusCode);
        assertTrue(success);
    }

    @Test
    public void updateNonAuthorizedUserTest() {

        Faker faker = new Faker();
        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));



        accessToken =loginResponse.extract().path("accessToken");
        ValidatableResponse response = userClient.updateUserWithoutAuthorization((new User(faker.internet().emailAddress(), faker.internet().password(), faker.internet().password())));
        int statusCode = response.extract().statusCode();
        boolean success = response.extract().path("success");
        assertEquals(401, statusCode);
        assertFalse(success);
        assertEquals("You should be authorised", response.extract().path("message"));

    }

}
