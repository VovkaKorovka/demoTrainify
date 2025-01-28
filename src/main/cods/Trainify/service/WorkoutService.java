package main.cods.Trainify.service;

import static main.cods.Trainify.service.ClearConsoleService.clearConsole;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import main.cods.Trainify.menu.WorkoutMenu;
import main.cods.Trainify.model.Exercise;
import main.cods.Trainify.model.TrainingPlan;
import main.cods.Trainify.model.TrainingPlans;
import main.cods.Trainify.model.Workout;

public class WorkoutService {

    private static final List<String> WORKOUT_TYPES = Arrays.asList("Силове", "Кардіо", "Йога",
        "Плавання", "Біг");
    private static final String EXERCISES_FILE = "Exercises\\exercises.json";

    public static void manageWorkouts(TrainingPlan plan, File jsonFile, Gson gson,
        TrainingPlans trainingPlans) {
        boolean exit = false;
        Scanner input = new Scanner(System.in);

        while (!exit) {
            WorkoutMenu.displayTrainingPlanMenu(plan);

            System.out.print("Оберіть дію: ");
            String action = input.next();

            switch (action) {
                case "0":
                    exit = true;
                    break;
                case "+":
                    addWorkout(plan, gson);
                    break;
                default:
                    handleAction(action, plan, gson);
                    break;
            }

            savePlansToFile(trainingPlans, jsonFile, gson);
        }
    }

    public static void handleAction(String action, TrainingPlan plan, Gson gson) {
        if (action.startsWith("-")) {
            int index = parseIndex(action.substring(1), plan);
            if (index != -1) {
                deleteWorkout(index, plan);
            }
        } else if (action.startsWith("?")) {
            int index = parseIndex(action.substring(1), plan);
            if (index != -1) {
                editWorkout(plan.getWorkouts().get(index));
            }
        } else {
            try {
                int index = Integer.parseInt(action) - 1;
                if (isValidIndex(index, plan)) {
                    manageExercises(plan.getWorkouts().get(index), gson);
                }
            } catch (NumberFormatException e) {
                System.out.println("Помилка: введено некоректний формат.");
            }
        }
    }

    private static void deleteWorkout(int index, TrainingPlan plan) {
        Scanner input = new Scanner(System.in);
        System.out.print(
            "Ви впевнені, що хочете видалити тренування \"" + plan.getWorkouts().get(index)
                .getName() + "\"? (y/n): ");
        String confirmation = input.nextLine();
        if (confirmation.equalsIgnoreCase("y")) {
            plan.getWorkouts().remove(index);
            System.out.println("Тренування успішно видалено.");
        } else {
            System.out.println("Видалення скасовано.");
        }
    }

    private static void editWorkout(Workout workout) {
        Scanner input = new Scanner(System.in);

        System.out.println("\n=== Редагування тренування: " + workout.getName() + " ===");
        System.out.print("Нова назва (поточна: " + workout.getName()
            + ", або залиште порожнім для збереження): ");
        String newName = input.nextLine();
        if (!newName.isEmpty()) {
            workout.setName(newName);
        }

        System.out.print("Новий опис (поточний: " + workout.getDescription()
            + ", або залиште порожнім для збереження): ");
        String newDescription = input.nextLine();
        if (!newDescription.isEmpty()) {
            workout.setDescription(newDescription);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        System.out.print("Нова дата (поточна: " + sdf.format(workout.getDate())
            + ", або залиште порожнім для збереження): ");
        String newDate = input.nextLine();
        if (!newDate.isEmpty()) {
            try {
                Date parsedDate = sdf.parse(newDate);
                workout.setDate(parsedDate);
            } catch (ParseException e) {
                System.out.println("Помилка: некоректний формат дати.");
            }
        }

        System.out.println("Тренування успішно оновлено!");
    }

    private static int parseIndex(String actionPart, TrainingPlan plan) {
        try {
            int index = Integer.parseInt(actionPart) - 1;
            if (isValidIndex(index, plan)) {
                return index;
            }
        } catch (NumberFormatException e) {
            System.out.println("Помилка: введено некоректний формат.");
        }
        return -1;
    }

    private static void manageExercises(Workout workout, Gson gson) {
        boolean exit = false;
        Scanner input = new Scanner(System.in);

        while (!exit) {
            WorkoutMenu.displayWorkoutMenu(workout);

            System.out.print("Оберіть дію: ");
            String action = input.next();

            switch (action) {
                case "0":
                    exit = true;
                    break;
                case "1":
                    addExercise(workout, gson);
                    break;
                case "2":
                    deleteExercise(workout);
                    break;
                case "3":
                    editExercise(workout);
                    break;
                case "4":
                    viewExercises(workout);
                    break;
                default:
                    System.out.println("Невірна дія. Спробуйте ще раз.");
            }
        }
    }

    public static void addWorkout(TrainingPlan plan, Gson gson) {
        Scanner input = new Scanner(System.in);
        clearConsole();
        System.out.println("\n=== Додавання тренування ===");
        System.out.println("Доступні типи тренувань:");
        for (int i = 0; i < WORKOUT_TYPES.size(); i++) {
            System.out.println((i + 1) + ". " + WORKOUT_TYPES.get(i));
        }
        System.out.println("0. Відміна");

        int typeIndex = -1;
        while (typeIndex < 0 || typeIndex > WORKOUT_TYPES.size()) {
            System.out.print("Оберіть тип тренування (0-" + WORKOUT_TYPES.size() + "): ");
            typeIndex = input.nextInt() - 1;
            if (typeIndex == -1) {
                System.out.println("Додавання тренування відмінено.");
                return;
            }
        }

        String type = WORKOUT_TYPES.get(typeIndex);

        input.nextLine();
        System.out.print("Опис тренування (або введіть 0 для відміни): ");
        String description = input.nextLine();
        if (description.equals("0")) {
            System.out.println("Додавання тренування відмінено.");
            return;
        }

        String name = "";
        while (name.trim().isEmpty()) {
            System.out.print("Введіть назву тренування: ");
            name = input.nextLine();
            if (name.trim().isEmpty()) {
                System.out.println(
                    "Назва тренування не може бути порожньою або складатися тільки з пробілів. Спробуйте ще раз.");
            }
        }

        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        while (date == null) {
            System.out.print("Введіть рік дати тренування: ");
            int year = input.nextInt();

            System.out.print("Введіть місяць дати тренування (1-12): ");
            int month = input.nextInt();

            System.out.print("Введіть день дати тренування (1-31): ");
            int day = input.nextInt();

            try {
                String dateString = String.format("%02d-%02d-%d", day, month, year);
                date = sdf.parse(dateString);
            } catch (ParseException e) {
                System.out.println("Некоректна дата. Спробуйте ще раз.");
            }
        }

        String id = String.valueOf(plan.getWorkouts().size() + 1);
        Workout newWorkout = new Workout(id, name, description, 0, date,
            type);
        plan.getWorkouts().add(newWorkout);
        System.out.println("Тренування успішно додано!");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void addExercise(Workout workout, Gson gson) {
        Scanner input = new Scanner(System.in);

        List<Exercise> exercises = loadExercisesFromFile(gson);

        if (exercises == null) {
            System.out.println("Помилка при зчитуванні вправ.");
            return;
        }

        System.out.println("\n=== Додавання вправи ===");
        System.out.println("Оберіть тип тренування:");
        for (int i = 0; i < WorkoutService.WORKOUT_TYPES.size(); i++) {
            System.out.println((i + 1) + ". " + WorkoutService.WORKOUT_TYPES.get(i));
        }
        System.out.println("0. Відміна");

        int typeIndex = -1;
        while (true) {
            System.out.print("Оберіть тип тренування (1-5, або 0 для відміни): ");
            typeIndex = input.nextInt() - 1;

            if (typeIndex == -1) {
                System.out.println("Додавання вправи відмінено.");
                return;
            }

            if (typeIndex >= 0 && typeIndex < WORKOUT_TYPES.size()) {
                break;
            } else {
                System.out.println("Некоректний вибір. Спробуйте ще раз.");
            }
        }

        String selectedType = WORKOUT_TYPES.get(typeIndex);

        List<Exercise> filteredExercises = new ArrayList<>();
        for (Exercise exercise : exercises) {
            if (exercise.getType().equalsIgnoreCase(selectedType)) {
                filteredExercises.add(exercise);
            }
        }

        System.out.println("Доступні вправи для типу: " + selectedType);
        for (int i = 0; i < filteredExercises.size(); i++) {
            Exercise exercise = filteredExercises.get(i);
            System.out.println(
                (i + 1) + ". " + exercise.getName() + " - " + exercise.getDescription());
        }

        System.out.print(
            "Оберіть вправу (1-" + filteredExercises.size() + ", або 0 для відміни): ");
        int exerciseIndex = input.nextInt() - 1;

        if (exerciseIndex == -1) {
            System.out.println("Додавання вправи відмінено.");
            return;
        }

        if (exerciseIndex >= 0 && exerciseIndex < filteredExercises.size()) {
            Exercise selectedExercise = filteredExercises.get(exerciseIndex);

            System.out.print("Кількість повторів: ");
            int reps = input.nextInt();
            System.out.print("Кількість підходів: ");
            int sets = input.nextInt();

            String id = UUID.randomUUID().toString();
            Exercise newExercise = new Exercise(id, selectedExercise.getName(),
                selectedExercise.getDescription(), selectedExercise.getType(), reps, sets,
                workout.getId());
            workout.getExercises().add(newExercise);

            System.out.println("Вправа успішно додана!");
        } else {
            System.out.println("Некоректний вибір вправи.");
        }
    }

    private static List<Exercise> loadExercisesFromFile(Gson gson) {
        try (FileReader reader = new FileReader(EXERCISES_FILE)) {
            ExercisesWrapperService exercisesWrapper = gson.fromJson(reader,
                ExercisesWrapperService.class);
            return exercisesWrapper.getExercises();
        } catch (IOException e) {
            System.out.println("Помилка при зчитуванні файлу вправ: " + e.getMessage());
        }
        return null;
    }

    public static void deleteExercise(Workout workout) {
        Scanner input = new Scanner(System.in);

        if (workout.getExercises().isEmpty()) {
            System.out.println("У тренуванні немає вправ для видалення.");
            return;
        }

        System.out.println("\n=== Список вправ для тренування: " + workout.getType() + " ===");
        for (int i = 0; i < workout.getExercises().size(); i++) {
            Exercise exercise = workout.getExercises().get(i);
            System.out.println(
                (i + 1) + ". " + exercise.getName() + " - " + exercise.getDescription() +
                    ", " + exercise.getReps() + " повторень, " + exercise.getSets() + " підходів");
        }

        System.out.print("Оберіть номер вправи для видалення (0 для відміни): ");
        int exerciseIndex = input.nextInt();

        if (exerciseIndex == 0) {
            System.out.println("Видалення скасовано.");
            return;
        }

        if (exerciseIndex > 0 && exerciseIndex <= workout.getExercises().size()) {
            workout.getExercises().remove(exerciseIndex - 1);
            System.out.println("Вправа успішно видалена!");
        } else {
            System.out.println("Некоректний вибір вправи.");
        }

        System.out.println("Видалення завершено.");
    }

    private static void editExercise(Workout workout) {
        Scanner input = new Scanner(System.in);
        viewExercises(workout);
        System.out.print("Оберіть номер вправи для редагування (0 для скасування): ");
        int index = input.nextInt() - 1;

        if (index == -1) {
            System.out.println("Редагування скасовано.");
            return;
        }

        if (index >= 0 && index < workout.getExercises().size()) {
            Exercise exercise = workout.getExercises().get(index);

            System.out.print("Кількість повторень (поточна: " + exercise.getReps() + "): ");
            int newReps = input.nextInt();
            exercise.setReps(newReps);

            System.out.print("Кількість підходів (поточна: " + exercise.getSets() + "): ");
            int newSets = input.nextInt();
            exercise.setSets(newSets);

            System.out.println("Вправа успішно оновлена!");
        } else {
            System.out.println("Некоректний номер вправи.");
        }
    }

    public static void viewExercises(Workout workout) {
        Scanner input = new Scanner(System.in);

        if (workout.getExercises().isEmpty()) {
            System.out.println("Немає вправ у цьому тренуванні.");
        } else {
            System.out.println("\n=== Список вправ для тренування: " + workout.getName() + " ===");
            for (int i = 0; i < workout.getExercises().size(); i++) {
                Exercise exercise = workout.getExercises().get(i);
                System.out.println(
                    (i + 1) + ". " + exercise.getName() + " - " + exercise.getDescription() +
                        ", " + exercise.getReps() + " повторень, " + exercise.getSets()
                        + " підходів");
            }
        }

        System.out.println("\nНатисніть Enter, щоб повернутись в меню.");
        input.nextLine();
    }

    private static boolean isValidIndex(int index, TrainingPlan plan) {
        return index >= 0 && index < plan.getWorkouts().size();
    }

    private static void savePlansToFile(TrainingPlans trainingPlans, File jsonFile, Gson gson) {
        try (FileWriter writer = new FileWriter(jsonFile)) {
            gson.toJson(trainingPlans, writer);
        } catch (IOException e) {
            System.out.println("Помилка при збереженні планів тренувань: " + e.getMessage());
        }
    }
}
