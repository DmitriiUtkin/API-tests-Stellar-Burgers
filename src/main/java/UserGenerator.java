import com.github.javafaker.Faker;

public class UserGenerator {
    public static User base() {
        Faker faker = new Faker();
        return new User(faker.internet().emailAddress(), faker.internet().password(6,12), faker.name().username());

    }
}