import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient extends RestClient {

    private static final String USER_PATH = "/api/auth/register";
    private static final String LOGIN_PATH = "/api/auth/login";
    private static final String DELETE_PATH = "/api/auth/user";
    private static final String UPDATE_PATH = "/api/auth/user";
    private static final String LOGOUT_PATH = "/api/auth/logout";



    @Step("Создание пользователя")
    public ValidatableResponse addUser(User user) {
        return given()
                .spec(getRequestSpec())
                .body(user)
                .when()
                .post(USER_PATH)
                .then()
                .spec(getResponseSpec());
    }

    @Step("Авторизация")
    public ValidatableResponse login(UserCredentials credentials) {
        return given()
                .spec(getRequestSpec())
                .body(credentials)
                .post(LOGIN_PATH)
                .then()
                .spec(getResponseSpec());
    }

    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(String accessToken) {
        return given()
                .spec(getRequestSpec())
                .header("Authorization", accessToken)
                .delete(DELETE_PATH)
                .then()
                .spec(getResponseSpec());
    }

    @Step("Изменение данных пользователя без авторизации")
    public ValidatableResponse updateUserWithoutAuthorization (User user) {
        return given()
                    .spec(getRequestSpec())
                    .body(user)
                    .when()
                    .patch(UPDATE_PATH)
                    .then()
                    .spec(getResponseSpec());
        }

    @Step("Изменение данных пользователя после авторизации")
        public ValidatableResponse updateUserAfterAuthorization (User user, String accessToken) {
            return given()
                .spec(getRequestSpec())
                .header("Authorization", accessToken)
                .body(user)
                .when()
                .patch(UPDATE_PATH)
                .then()
                .spec(getResponseSpec());
    }

    @Step("Выход из системы")
    public ValidatableResponse logout (User user, String accessToken, String refreshToken) {
//        Map<String, String> requestBody = new HashMap<>();
//        requestBody.put("token", refreshToken);
        return given()
                .spec(getRequestSpec())
                .header("Authorization", accessToken)
                .body(refreshToken)
                .when()
                .post(LOGOUT_PATH)
                .then()
                .spec(getResponseSpec());

    }
}
