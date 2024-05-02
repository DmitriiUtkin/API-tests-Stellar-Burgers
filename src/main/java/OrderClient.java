import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestClient {

    private static final String ORDER_PATH = "/api/orders";
    private static final String INGREDIENTS_PATH = "/api/ingredients";

    @Step("Создание заказа")
    public ValidatableResponse addOrder(Order order, String accessToken) {
        return given()
                .spec(getRequestSpec())
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then()
                .spec(getResponseSpec());
    }

    @Step("Создание заказа без авторизации")
    public ValidatableResponse addOrderWithoutAuthorization(Order order) {
        return given()
                .spec(getRequestSpec())
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then()
                .spec(getResponseSpec());
    }

    @Step("Получение данных об ингредиентах")
    public ValidatableResponse getIngredients() {
        return given()
                .spec(getRequestSpec())
                .when()
                .get(INGREDIENTS_PATH)
                .then()
                .spec(getResponseSpec());
    }

    @Step("Получение списка заказов авторизованного пользователя")
    public ValidatableResponse getUserOrders(String accessToken) {
        return given()
                .spec(getRequestSpec())
                .header("Authorization", accessToken)
                .when()
                .get(ORDER_PATH)
                .then()
                .spec(getResponseSpec());
    }

    @Step("Получение списка заказов неавторизованного пользователя")
    public ValidatableResponse getNonAuthorizedUserOrders() {
        return given()
                .spec(getRequestSpec())
                .when()
                .get(ORDER_PATH)
                .then()
                .spec(getResponseSpec());
    }

   }
