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

public class TrainingPlanManager {

    public static void createTrainingPlan(String userId) {
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())  // Реєстрація адаптера
            .setPrettyPrinting()  // Зробить серіалізацію "гарною" (з відступами)
            .create();
        Scanner scanner = new Scanner(System.in);
        LocalDate currentDate = LocalDate.now();

        Main.clearConsole();
        System.out.println("=== Створення нового плану тренувань ===");

        // Введення року, місяця та дня початку плану
        int year = -1, month = -1, day = -1;

        // Введення року
        System.out.print("Введіть рік початку плану: ");
        while (year <= 0) {
            if (scanner.hasNextInt()) {
                year = scanner.nextInt();
                if (year <= 0) {
                    System.out.print("Будь ласка, введіть коректний рік (позитивне число): ");
                }
            } else {
                System.out.print("Введіть коректний рік (позитивне число): ");
                scanner.next(); // Очищення вводу
            }
        }

        // Введення місяця
        System.out.print("Введіть місяць початку плану (1-12): ");
        while (month < 1 || month > 12) {
            if (scanner.hasNextInt()) {
                month = scanner.nextInt();
                if (month < 1 || month > 12) {
                    System.out.print("Місяць має бути між 1 та 12. Введіть місяць: ");
                }
            } else {
                System.out.print("Введіть коректний місяць (1-12): ");
                scanner.next(); // Очищення вводу
            }
        }

        // Введення дня
        System.out.print("Введіть день початку плану (1-31): ");
        while (day < 1 || day > 31) {
            if (scanner.hasNextInt()) {
                day = scanner.nextInt();
                if (day < 1 || day > 31) {
                    System.out.print("День має бути між 1 та 31. Введіть день: ");
                }
            } else {
                System.out.print("Введіть коректний день (1-31): ");
                scanner.next(); // Очищення вводу
            }
        }

        scanner.nextLine(); // Очищення буфера після числових введень
        LocalDate startDate = null;
        try {
            startDate = LocalDate.of(year, month, day);
        } catch (DateTimeException e) {
            System.out.println("Помилка: Некоректна дата.");
            return;
        }

        if (startDate.isBefore(currentDate)) {
            Main.clearConsole();
            System.out.println("Помилка: Дата початку не може бути в минулому.");
            return;
        }

        System.out.print("Введіть опис нового плану тренувань: ");
        String description = scanner.nextLine();

        // Введення кінцевої дати
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
                scanner.next(); // Очищення вводу
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
                scanner.next(); // Очищення вводу
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
                scanner.next(); // Очищення вводу
            }
        }

        scanner.nextLine(); // Очищення буфера
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

        // Шлях до загального файлу з планами
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

        // Перевірка на наявність файлу
        if (jsonFile.exists()) {
            try (Scanner fileScanner = new Scanner(jsonFile)) {
                String jsonContent = fileScanner.useDelimiter("\\A").next();
                trainingPlans = gson.fromJson(jsonContent, TrainingPlans.class);
            } catch (IOException e) {
                System.out.println("Помилка при зчитуванні існуючих планів: " + e.getMessage());
                return;
            }
        } else {
            // Якщо файл не існує, створюємо порожній об'єкт
            trainingPlans = new TrainingPlans();
        }

        trainingPlans.getPlans().add(newPlan);

        try (FileWriter writer = new FileWriter(jsonFile)) {
            gson.toJson(trainingPlans, writer);

            try {
                System.out.println("Новий план тренувань створено");
                Thread.sleep(2000);
                Main.clearConsole();
            } catch (InterruptedException e) {
                System.out.println("Помилка при затримці: " + e.getMessage());
                Thread.currentThread().interrupt();  // Відновлюємо статус переривання
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
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .setPrettyPrinting()
            .create();

        try (Scanner scanner = new Scanner(jsonFile)) {
            String jsonContent = scanner.useDelimiter("\\A").next();
            TrainingPlans trainingPlans = gson.fromJson(jsonContent, TrainingPlans.class);
            Main.clearConsole();
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
                Main.clearConsole();
                System.out.println("Повернення в меню...");
            } else if ("-".equals(userInput)) {
                System.out.println("Виберіть план для видалення:");
                int deletePlanIndex = input.nextInt() - 1;
                if (deletePlanIndex < 0 || deletePlanIndex >= trainingPlans.getPlans().size()) {
                    System.out.println("Невірний номер плану.");
                    return;
                }

                // Видалення плану
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
                        - 1; // Вибір плану за індексом (мінус 1 для коректності індексації)
                    if (planIndex < 0 || planIndex >= trainingPlans.getPlans().size()) {
                        System.out.println("Невірний номер плану.");
                        return;
                    }

                    // Якщо вибрано існуючий план, передаємо його для подальшої обробки
                    TrainingPlan selectedPlan = trainingPlans.getPlans().get(planIndex);
                    // Передаємо вибраний план у WorkoutManage
                    WorkoutManage.manageWorkouts(selectedPlan, jsonFile, gson, trainingPlans);
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
            // Якщо файлу ще не існує, починаємо з 1
            return "1";
        }

        Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .setPrettyPrinting()
            .create();

        TrainingPlans trainingPlans = new TrainingPlans();

        try (Scanner fileScanner = new Scanner(jsonFile)) {
            String jsonContent = fileScanner.useDelimiter("\\A").next();
            trainingPlans = gson.fromJson(jsonContent, TrainingPlans.class);
        } catch (IOException e) {
            System.out.println("Помилка при зчитуванні існуючих планів: " + e.getMessage());
            return "1"; // Якщо сталася помилка при зчитуванні, почнемо з 1
        }

        int maxId = 0;

        // Проходимо по всіх планах і знаходимо максимальний ID
        for (TrainingPlan plan : trainingPlans.getPlans()) {
            try {
                int currentId = Integer.parseInt(plan.getPlanId());
                if (currentId > maxId) {
                    maxId = currentId;
                }
            } catch (NumberFormatException e) {
                // Якщо ID не є числом (що малоймовірно), просто ігноруємо цей план
            }
        }

        // Повертаємо наступний ID
        return String.valueOf(maxId + 1);
    }

    public static void logOut(String userId) {
        String configFilePath =
            "C:\\Users\\payda\\Desktop\\demoTrainify\\config.json";
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
            Main.clearConsole();
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Помилка при затримці: " + e.getMessage());
            Thread.currentThread().interrupt();
        }

        Main.showMenu();
    }
}