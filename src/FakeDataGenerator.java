import com.github.javafaker.Faker;
import java.util.Date;

public class FakeDataGenerator {

    private static final Faker faker = new Faker();

    public static User generateFakeUser() {
        String id = faker.idNumber().valid();
        String name = faker.name().fullName();
        String password = faker.internet().password();
        String role = "USER";
        return new User(id, name, password, role);
    }

    public static TrainingPlan generateFakeTrainingPlan(String userId) {
        String id = faker.idNumber().valid();
        Date startDate = faker.date().past(30, java.util.concurrent.TimeUnit.DAYS);
        Date endDate = faker.date().future(30, java.util.concurrent.TimeUnit.DAYS);
        String description = faker.lorem().sentence();
        return new TrainingPlan(id, startDate, endDate, userId, description);
    }

    public static Workout generateFakeWorkout(String planId) {
        String id = faker.idNumber().valid();
        String type = "Cardio";
        String description = faker.lorem().sentence();
        int duration = faker.number().numberBetween(30, 90);
        Date date = faker.date().future(30, java.util.concurrent.TimeUnit.DAYS);
        return new Workout(id, type, description, duration, date);
    }

    public static Exercise generateFakeExercise(String workoutId) {
        String id = faker.idNumber().valid();
        String name = faker.lorem().word();
        String description = faker.lorem().sentence();
        int reps = faker.number().numberBetween(10, 30);
        int sets = faker.number().numberBetween(3, 5);
        return new Exercise(id, name, description, reps, sets, workoutId);
    }
}
