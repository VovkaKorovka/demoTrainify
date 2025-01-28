package main.cods.Trainify.menu;

import static main.cods.Trainify.service.ClearConsoleService.clearConsole;

import java.text.SimpleDateFormat;
import main.cods.Trainify.model.TrainingPlan;
import main.cods.Trainify.model.Workout;

public class WorkoutMenu {

    public static void displayTrainingPlanMenu(TrainingPlan plan) {
        clearConsole();
        System.out.println("\n=== Робота з планом ===");
        System.out.println("Опис: " + plan.getDescription());
        System.out.println("Початок: " + plan.getStartDate());
        System.out.println("Кінець: " + plan.getEndDate());
        System.out.println("Тренування:");

        for (int i = 0; i < plan.getWorkouts().size(); i++) {
            Workout workout = plan.getWorkouts().get(i);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String workoutDate = sdf.format(workout.getDate());
            System.out.println(
                (i + 1) + ". " + workout.getName() + " | Опис: " + workout.getDescription()
                    + " | Дата: " + workoutDate + " | Тип: " + workout.getType());
        }

        System.out.println("\nДоступні дії:");
        System.out.println("+ - Додати тренування");
        System.out.println("<номер> - Переглянути тренування");
        System.out.println("-<номер> - Видалити тренування");
        System.out.println("?<номер> - Редагувати тренування");
        System.out.println("0 - Вихід");
    }

    public static void displayWorkoutMenu(Workout workout) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        System.out.println("\n=== Робота з тренуванням: " + workout.getName() + " ===");
        System.out.println("Опис: " + workout.getDescription());
        System.out.println("Дата тренування: " + sdf.format(workout.getDate()));
        System.out.println("Тип тренування: " + workout.getType());
        System.out.println("\nДоступні дії:");
        System.out.println("1 - Додати вправу");
        System.out.println("2 - Видалити вправу");
        System.out.println("3 - Редагувати вправу");
        System.out.println("4 - Переглянути вправи");
        System.out.println("0 - Вихід");
    }
}
