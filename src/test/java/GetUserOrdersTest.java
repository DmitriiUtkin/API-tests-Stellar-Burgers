import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GetUserOrdersTest {
    private OrderClient orderClient;
    private UserClient userClient;
    private String accessToken;
    private String refreshToken;
    private User user;



    @Before
    public void setUp() {
        orderClient = new OrderClient();
        user = UserGenerator.base();
        userClient = new UserClient();
        ValidatableResponse response = new UserClient().addUser(user);

        ValidatableResponse loginResponse = new UserClient().login(UserCredentials.from(user));
        accessToken = loginResponse.extract().path("accessToken");
        refreshToken = loginResponse.extract().path("refreshToken");

    }

    @After
    public void cleanUp () {
        userClient.deleteUser(accessToken);
    }

    @Test
    public void getOrdersListWithAuthorizationTest() {

        ValidatableResponse response = orderClient.getUserOrders(accessToken);

        assertEquals(200, response.extract().statusCode());
        assertTrue(response.extract().path("success"));
    }

    @Test
    public void getOrdersListWithoutAuthorizationTest() {

        userClient.logout(user, accessToken, refreshToken);
        ValidatableResponse response = orderClient.getNonAuthorizedUserOrders();

        assertEquals(401, response.extract().statusCode());
        assertFalse(response.extract().path("success"));
        assertEquals("You should be authorised", response.extract().path("message"));




    }



}
