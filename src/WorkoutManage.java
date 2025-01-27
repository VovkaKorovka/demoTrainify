import com.google.gson.Gson;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

public class WorkoutManage {

    public static void manageWorkouts(TrainingPlan plan, File jsonFile, Gson gson,
        TrainingPlans trainingPlans) {
        boolean exit = false;
        Scanner input = new Scanner(System.in);

        while (!exit) {
            System.out.println("\n=== Робота з планом ===");
            System.out.println("Опис: " + plan.getDescription());
            System.out.println("Початок: " + plan.getStartDate());
            System.out.println("Кінець: " + plan.getEndDate());
            System.out.println("Тренування:");
            for (int i = 0; i < plan.getWorkouts().size(); i++) {
                System.out.println((i + 1) + ". " + plan.getWorkouts().get(i).getDescription());
            }

            System.out.println("\nОберіть дію:");
            System.out.println("+ - додати тренування");
            System.out.println("<номер> - переглянути тренування");
            System.out.println("-<номер> - видалити тренування");
            System.out.println("?<номер> - редагувати тренування");
            System.out.println("0 - вихід");

            String action = input.next();

            if (action.equals("0")) {
                exit = true;
            } else if (action.equals("+")) {
                addWorkout(plan);
            } else if (action.startsWith("-")) {
                int workoutIndex = Integer.parseInt(action.substring(1)) - 1;
                deleteWorkout(plan, workoutIndex);
            } else if (action.startsWith("?")) {
                int workoutIndex = Integer.parseInt(action.substring(1)) - 1;
                editWorkout(plan, workoutIndex);
            } else {
                int workoutIndex = Integer.parseInt(action) - 1;
                viewWorkout(plan, workoutIndex);
            }

            // Після кожної зміни зберігаємо всі плани тренувань
            savePlansToFile(trainingPlans, jsonFile, gson);
        }
    }

    public static void savePlansToFile(TrainingPlans trainingPlans, File jsonFile, Gson gson) {
        try (FileWriter writer = new FileWriter(jsonFile)) {
            gson.toJson(trainingPlans, writer);  // Зберігаємо всі плани тренувань
            System.out.println("Зміни успішно збережено.");
        } catch (IOException e) {
            System.out.println("Помилка при збереженні планів тренувань у файл: " + e.getMessage());
        }
    }

    private static void addWorkout(TrainingPlan plan) {
        Scanner input = new Scanner(System.in);
        System.out.println("Додати нове тренування:");
        System.out.print("Тип тренування: ");
        String type = input.nextLine();
        System.out.print("Опис: ");
        String description = input.nextLine();
        System.out.print("Тривалість (хвилини): ");
        int duration = input.nextInt();

        // Визначення ID як порядкового номеру
        String id = String.valueOf(plan.getWorkouts().size() + 1);

        // Створення об'єкта тренування
        Workout newWorkout = new Workout(id, type, description, duration, new Date());
        plan.getWorkouts().add(newWorkout);

        System.out.println("Тренування успішно додано.");
    }

    private static void deleteWorkout(TrainingPlan plan, int index) {
        if (index < 0 || index >= plan.getWorkouts().size()) {
            System.out.println("Невірний номер тренування.");
            return;
        }
        plan.getWorkouts().remove(index);
        System.out.println("Тренування успішно видалено.");
    }

    private static void editWorkout(TrainingPlan plan, int index) {
        if (index < 0 || index >= plan.getWorkouts().size()) {
            System.out.println("Невірний номер тренування.");
            return;
        }
        Workout workout = plan.getWorkouts().get(index);
        Scanner input = new Scanner(System.in);

        System.out.println("Редагування тренування:");
        System.out.print("Нова назва (поточна: " + workout.getDescription() + "): ");
        String newName = input.nextLine();
        System.out.print("Новий опис (поточний: " + workout.getDescription() + "): ");
        String newDescription = input.nextLine();

        workout.setName(newName.isEmpty() ? workout.getDescription() : newName);
        workout.setDescription(
            newDescription.isEmpty() ? workout.getDescription() : newDescription);

        System.out.println("Тренування успішно оновлено.");
    }

    private static void viewWorkout(TrainingPlan plan, int index) {
        if (index < 0 || index >= plan.getWorkouts().size()) {
            System.out.println("Невірний номер тренування.");
            return;
        }
        Workout workout = plan.getWorkouts().get(index);
        System.out.println("\n=== Інформація про тренування ===");
        System.out.println("Назва: " + workout.getDescription());
        System.out.println("Опис: " + workout.getDescription());
    }
}
