import com.github.javafaker.Faker;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class AddOrderTest {

    private UserClient userClient;
    private User user;
    private String accessToken;
    private String refreshToken;
    private OrderClient orderClient;


    @Before
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();
        user = UserGenerator.base();
        userClient.addUser(user);
        accessToken = userClient.login(UserCredentials.from(user)).extract().path("accessToken");
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }

    @Test
    public void addOrderWithAuthorizationTest() {

        ValidatableResponse responseIngredients = orderClient.getIngredients();
        List<String> ingredients = new ArrayList<>(responseIngredients.extract().path("data._id"));
        Order order = new Order(ingredients.subList(0, 10));
        ValidatableResponse response = orderClient.addOrder(order, accessToken);
        int statusCode = response.extract().statusCode();
        boolean success = response.extract().path("success");
        assertEquals(200, statusCode);
        assertTrue(success);

    }

    @Test
    public void addOrderWithAuthorizationWithoutIngredientsTest() {
        Order order = new Order(new ArrayList<>());
        ValidatableResponse response = orderClient.addOrder(order, accessToken);
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");
        assertFalse(response.extract().path("success"));
        assertEquals(400, statusCode);
        assertEquals("Ingredient ids must be provided", message);
    }

    @Test
    public void addOrderWithAuthorizationAndIncorrectHashTest() {

        Faker faker = new Faker();
        List<String> ingredients = Arrays.asList(faker.food().ingredient(), faker.food().ingredient(), faker.food().ingredient());
        Order order = new Order(ingredients.subList(0, ingredients.size()));
        ValidatableResponse response = orderClient.addOrder(order, accessToken);
        int statusCode = response.extract().statusCode();
        assertEquals(500, statusCode);

    }

    @Test
    public void addOrderWithoutAuthorizationTest() {

        refreshToken = userClient.login(UserCredentials.from(user)).extract().path("refreshToken");
        userClient.logout(user, accessToken, refreshToken);
        ValidatableResponse responseIngredients = orderClient.getIngredients();
        List<String> ingredients = new ArrayList<>(responseIngredients.extract().path("data._id"));
        Order order = new Order(ingredients.subList(0, ingredients.size()));
        ValidatableResponse response = orderClient.addOrderWithoutAuthorization(order);
        int statusCode = response.extract().statusCode();
        boolean success = response.extract().path("success");
        assertEquals(200, statusCode);
        assertTrue(success);
    }
}

