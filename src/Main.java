import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<User> users = new ArrayList<>();
        List<TrainingPlan> trainingPlans = new ArrayList<>();
        List<Workout> workouts = new ArrayList<>();
        List<Exercise> exercises = new ArrayList<>();

        User user = FakeDataGenerator.generateFakeUser();
        users.add(user);

        TrainingPlan trainingPlan = FakeDataGenerator.generateFakeTrainingPlan(user.getId());
        trainingPlans.add(trainingPlan);

        Workout workout = FakeDataGenerator.generateFakeWorkout(trainingPlan.getId());
        workouts.add(workout);

        Exercise exercise = FakeDataGenerator.generateFakeExercise(workout.getId());
        exercises.add(exercise);

        FileSaver.saveUsersToJSON(users, "users.json");
        FileSaver.saveTrainingPlansToJSON(trainingPlans, "training_plans.json");
        FileSaver.saveWorkoutsToJSON(workouts, "workouts.json");
        FileSaver.saveExercisesToJSON(exercises, "exercises.json");
        System.out.println("Дані збережено в JSON файли!!!");
    }
}
