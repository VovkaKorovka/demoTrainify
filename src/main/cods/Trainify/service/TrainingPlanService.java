package main.cods.Trainify.service;

import static main.cods.Trainify.service.ClearConsoleService.clearConsole;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import main.cods.Trainify.Main;
import main.cods.Trainify.model.TrainingPlan;
import main.cods.Trainify.model.TrainingPlans;
import main.cods.Trainify.model.Workout;

public class TrainingPlanService {

    public static void createTrainingPlan(String userId) {
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class,
                new LocalDateAdapterService())
            .setPrettyPrinting()
            .create();
        Scanner scanner = new Scanner(System.in);
        LocalDate currentDate = LocalDate.now();

        clearConsole();
        System.out.println("=== Створення нового плану тренувань ===");

        int year = -1, month = -1, day = -1;

        System.out.print("Введіть рік початку плану: ");
        while (year <= 0) {
            if (scanner.hasNextInt()) {
                year = scanner.nextInt();
                if (year <= 0) {
                    System.out.print("Будь ласка, введіть коректний рік (позитивне число): ");
                }
            } else {
                System.out.print("Введіть коректний рік (позитивне число): ");
                scanner.next();
            }
        }

        System.out.print("Введіть місяць початку плану (1-12): ");
        while (month < 1 || month > 12) {
            if (scanner.hasNextInt()) {
                month = scanner.nextInt();
                if (month < 1 || month > 12) {
                    System.out.print("Місяць має бути між 1 та 12. Введіть місяць: ");
                }
            } else {
                System.out.print("Введіть коректний місяць (1-12): ");
                scanner.next();
            }
        }

        System.out.print("Введіть день початку плану (1-31): ");
        while (day < 1 || day > 31) {
            if (scanner.hasNextInt()) {
                day = scanner.nextInt();
                if (day < 1 || day > 31) {
                    System.out.print("День має бути між 1 та 31. Введіть день: ");
                }
            } else {
                System.out.print("Введіть коректний день (1-31): ");
                scanner.next();
            }
        }

        scanner.nextLine();
        LocalDate startDate = null;
        try {
            startDate = LocalDate.of(year, month, day);
        } catch (DateTimeException e) {
            System.out.println("Помилка: Некоректна дата.");
            return;
        }

        if (startDate.isBefore(currentDate)) {
            clearConsole();
            System.out.println("Помилка: Дата початку не може бути в минулому.");
            return;
        }

        System.out.print("Введіть опис нового плану тренувань: ");
        String description = scanner.nextLine();

        int endYear = -1, endMonth = -1, endDay = -1;
        System.out.print("Введіть рік кінцевої дати: ");
        while (endYear <= 0) {
            if (scanner.hasNextInt()) {
                endYear = scanner.nextInt();
                if (endYear <= 0) {
                    System.out.print("Будь ласка, введіть коректний рік (позитивне число): ");
                }
            } else {
                System.out.print("Введіть коректний рік (позитивне число): ");
                scanner.next();
            }
        }

        System.out.print("Введіть місяць кінцевої дати (1-12): ");
        while (endMonth < 1 || endMonth > 12) {
            if (scanner.hasNextInt()) {
                endMonth = scanner.nextInt();
                if (endMonth < 1 || endMonth > 12) {
                    System.out.print("Місяць має бути між 1 та 12. Введіть місяць: ");
                }
            } else {
                System.out.print("Введіть коректний місяць (1-12): ");
                scanner.next();
            }
        }

        System.out.print("Введіть день кінцевої дати (1-31): ");
        while (endDay < 1 || endDay > 31) {
            if (scanner.hasNextInt()) {
                endDay = scanner.nextInt();
                if (endDay < 1 || endDay > 31) {
                    System.out.print("День має бути між 1 та 31. Введіть день: ");
                }
            } else {
                System.out.print("Введіть коректний день (1-31): ");
                scanner.next();
            }
        }

        scanner.nextLine();
        LocalDate endDate = null;
        try {
            endDate = LocalDate.of(endYear, endMonth, endDay);
        } catch (DateTimeException e) {
            System.out.println("Помилка: Некоректна кінцева дата.");
            return;
        }

        if (endDate.isBefore(startDate)) {
            System.out.println("Помилка: Дата кінця не може бути ранішою за дату початку.");
            return;
        }

        List<Workout> workouts = new ArrayList<>();

        String planId = getNextPlanId(userId);

        String userDirPath = "C:\\Users\\payda\\Desktop\\demoTrainify\\Users_Plans\\User(" + userId
            + ")_TrainingPlans";
        File userDir = new File(userDirPath);

        if (!userDir.exists()) {
            boolean dirCreated = userDir.mkdirs();
            if (!dirCreated) {
                System.out.println("Не вдалося створити папку для користувача.");
                return;
            }
        }

        File jsonFile = new File(userDir, "trainingPlans.json");
        TrainingPlan newPlan = new TrainingPlan(planId, startDate, endDate, userId, description,
            workouts);

        TrainingPlans trainingPlans = new TrainingPlans();

        if (jsonFile.exists()) {
            try (Scanner fileScanner = new Scanner(jsonFile)) {
                String jsonContent = fileScanner.useDelimiter("\\A").next();
                trainingPlans = gson.fromJson(jsonContent, TrainingPlans.class);
            } catch (IOException e) {
                System.out.println("Помилка при зчитуванні існуючих планів: " + e.getMessage());
                return;
            }
        } else {
            trainingPlans = new TrainingPlans();
        }

        trainingPlans.getPlans().add(newPlan);

        try (FileWriter writer = new FileWriter(jsonFile)) {
            gson.toJson(trainingPlans, writer);

            try {
                System.out.println("Новий план тренувань створено");
                Thread.sleep(2000);
                clearConsole();
            } catch (InterruptedException e) {
                System.out.println("Помилка при затримці: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        } catch (IOException e) {
            System.out.println("Помилка при записі в JSON-файл: " + e.getMessage());
        }
    }

    public static void manageTrainingPlan(String userId) {
        String userDirPath = "C:\\Users\\payda\\Desktop\\demoTrainify\\Users_Plans\\User(" + userId
            + ")_TrainingPlans";
        File userDir = new File(userDirPath);
        File jsonFile = new File(userDir, "trainingPlans.json");

        if (!jsonFile.exists()) {
            System.out.println("У вас немає збережених планів тренувань.");
            return;
        }

        Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapterService())
            .setPrettyPrinting()
            .create();

        try (Scanner scanner = new Scanner(jsonFile)) {
            String jsonContent = scanner.useDelimiter("\\A").next();
            TrainingPlans trainingPlans = gson.fromJson(jsonContent, TrainingPlans.class);
            clearConsole();
            System.out.println("Кількість планів: " + trainingPlans.getPlans().size());
            if (trainingPlans.getPlans() == null || trainingPlans.getPlans().isEmpty()) {
                System.out.println("У вас немає збережених планів тренувань.");
                return;
            }

            System.out.println("Ваші плани тренувань:");
            for (int i = 0; i < trainingPlans.getPlans().size(); i++) {
                TrainingPlan plan = trainingPlans.getPlans().get(i);
                System.out.println((i + 1) + " | " + plan.getDescription() +
                    " | " + plan.getStartDate() + " - " + plan.getEndDate());
            }

            Scanner input = new Scanner(System.in);
            System.out.println(
                "Виберіть план за номером, з яким хочете працювати (введіть - для видалення або 0 для повернення в меню):");

            String userInput = input.nextLine().trim();

            if ("0".equals(userInput)) {
                clearConsole();
                System.out.println("Повернення в меню...");
            } else if ("-".equals(userInput)) {
                System.out.println("Виберіть план для видалення:");
                int deletePlanIndex = input.nextInt() - 1;
                if (deletePlanIndex < 0 || deletePlanIndex >= trainingPlans.getPlans().size()) {
                    System.out.println("Невірний номер плану.");
                    return;
                }

                trainingPlans.getPlans().remove(deletePlanIndex);

                try (FileWriter writer = new FileWriter(jsonFile)) {
                    gson.toJson(trainingPlans, writer);
                    System.out.println("План тренувань успішно видалено.");
                } catch (IOException e) {
                    System.out.println("Помилка при записі в JSON-файл: " + e.getMessage());
                }
            } else {
                try {
                    int planIndex = Integer.parseInt(userInput)
                        - 1;
                    if (planIndex < 0 || planIndex >= trainingPlans.getPlans().size()) {
                        System.out.println("Невірний номер плану.");
                        return;
                    }

                    TrainingPlan selectedPlan = trainingPlans.getPlans().get(planIndex);
                    WorkoutService.manageWorkouts(selectedPlan, jsonFile, gson, trainingPlans);
                } catch (NumberFormatException e) {
                    System.out.println("Невірний ввід. Спробуйте ще раз.");
                }
            }
        } catch (IOException e) {
            System.out.println("Помилка при зчитуванні JSON-файлу: " + e.getMessage());
        }
    }

    private static String getNextPlanId(String userId) {
        String userDirPath = "C:\\Users\\payda\\Desktop\\demoTrainify\\Users_Plans\\User(" + userId
            + ")_TrainingPlans";
        File userDir = new File(userDirPath);
        File jsonFile = new File(userDir, "trainingPlans.json");

        if (!jsonFile.exists()) {
            return "1";
        }

        Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapterService())
            .setPrettyPrinting()
            .create();

        TrainingPlans trainingPlans = new TrainingPlans();

        try (Scanner fileScanner = new Scanner(jsonFile)) {
            String jsonContent = fileScanner.useDelimiter("\\A").next();
            trainingPlans = gson.fromJson(jsonContent, TrainingPlans.class);
        } catch (IOException e) {
            System.out.println("Помилка при зчитуванні існуючих планів: " + e.getMessage());
            return "1";
        }

        int maxId = 0;

        for (TrainingPlan plan : trainingPlans.getPlans()) {
            try {
                int currentId = Integer.parseInt(plan.getPlanId());
                if (currentId > maxId) {
                    maxId = currentId;
                }
            } catch (NumberFormatException e) {
            }
        }

        return String.valueOf(maxId + 1);
    }

    public static void logOut(String userId) {
        String configFilePath =
            "C:\\Users\\payda\\Desktop\\demoTrainify\\src\\main\\resources\\config.json";
        File configFile = new File(configFilePath);

        if (configFile.exists()) {
            try (FileWriter writer = new FileWriter(configFile)) {
                writer.write("");
                System.out.println("Конфігурація користувача очищена.");
            } catch (IOException e) {
                System.out.println("Помилка при очищенні конфігурації: " + e.getMessage());
            }
        } else {
            System.out.println("Файл конфігурації не знайдений, нічого не очищено.");
        }

        try {
            System.out.println("До побачення!");
            clearConsole();
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Помилка при затримці: " + e.getMessage());
            Thread.currentThread().interrupt();
        }

        Main.showMenu();
    }
}