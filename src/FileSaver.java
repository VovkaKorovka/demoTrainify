import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileSaver {

    public static void saveUsersToJSON(List<User> users, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            Gson gson = new GsonBuilder().setPrettyPrinting()
                .create();
            gson.toJson(users, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveTrainingPlansToJSON(List<TrainingPlan> trainingPlans, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            Gson gson = new GsonBuilder().setPrettyPrinting()
                .create();
            gson.toJson(trainingPlans, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveWorkoutsToJSON(List<Workout> workouts, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            Gson gson = new GsonBuilder().setPrettyPrinting()
                .create();
            gson.toJson(workouts, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveExercisesToJSON(List<Exercise> exercises, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            Gson gson = new GsonBuilder().setPrettyPrinting()
                .create();
            gson.toJson(exercises, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
