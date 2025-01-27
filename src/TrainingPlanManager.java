import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

        System.out.print("Введіть рік початку плану: ");
        int year = scanner.nextInt();
        System.out.print("Введіть місяць початку плану (1-12): ");
        int month = scanner.nextInt();
        System.out.print("Введіть день початку плану (1-31): ");
        int day = scanner.nextInt();
        scanner.nextLine();

        LocalDate startDate = LocalDate.of(year, month, day);

        if (startDate.isBefore(currentDate)) {
            Main.clearConsole();
            System.out.println("Помилка: Дата початку не може бути в минулому.");
            return;
        }

        System.out.print("Введіть опис нового плану тренувань: ");
        String description = scanner.nextLine();

        System.out.print("Введіть рік кінцевої дати: ");
        int endYear = scanner.nextInt();
        System.out.print("Введіть місяць кінцевої дати (1-12): ");
        int endMonth = scanner.nextInt();
        System.out.print("Введіть день кінцевої дати (1-31): ");
        int endDay = scanner.nextInt();
        scanner.nextLine();

        LocalDate endDate = LocalDate.of(endYear, endMonth, endDay);

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
            System.out.println("Зчитаний JSON: " + jsonContent);  // Для відлагодження
            TrainingPlans trainingPlans = gson.fromJson(jsonContent, TrainingPlans.class);

            // Додайте виведення розміру списку та вмісту
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
            System.out.println("Виберіть план за номером, з яким хочете працювати:");
            int planIndex = input.nextInt() - 1;

            if (planIndex < 0 || planIndex >= trainingPlans.getPlans().size()) {
                System.out.println("Невірний номер плану.");
                return;
            }

            TrainingPlan selectedPlan = trainingPlans.getPlans().get(planIndex);
            // Передаємо вибраний план у WorkoutManage
            WorkoutManage.manageWorkouts(selectedPlan, jsonFile, gson, trainingPlans);
        } catch (IOException e) {
            System.out.println("Помилка при зчитуванні JSON-файлу: " + e.getMessage());
        }
    }

    private static String getNextPlanId(String userId) {
        String userDirPath = "C:\\Users\\payda\\Desktop\\demoTrainify\\Users_Plans\\User(" + userId
            + ")_TrainingPlans";
        File userDir = new File(userDirPath);

        if (!userDir.exists()) {
            userDir.mkdirs();
        }

        File[] files = userDir.listFiles(
            (dir, name) -> name.endsWith(".json"));
        int nextId = 1;

        if (files != null && files.length > 0) {
            int maxId = 0;
            for (File file : files) {
                String fileName = file.getName();
                String idPart = fileName.replace(".json", "");
                try {
                    int id = Integer.parseInt(idPart);
                    if (id > maxId) {
                        maxId = id;
                    }
                } catch (NumberFormatException e) {
                    // Якщо не вдається перетворити, пропускаємо цей файл
                }
            }
            nextId = maxId + 1;
        }

        return String.valueOf(nextId);  // Повертаємо унікальний ID для нового плану
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
