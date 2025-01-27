import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
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

public class TrainingPlanManager {

    public static void createTrainingPlan(String userId) {
        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        System.out.print("Введіть дату початку (yyyy-MM-dd): ");
        String startDateInput = scanner.nextLine();

        System.out.print("Введіть дату завершення (yyyy-MM-dd): ");
        String endDateInput = scanner.nextLine();

        System.out.print("Введіть опис нового плану тренувань: ");
        String description = scanner.nextLine();

        Date startDate, endDate;
        try {
            startDate = dateFormat.parse(startDateInput);
            endDate = dateFormat.parse(endDateInput);
        } catch (ParseException e) {
            System.out.println("Помилка: Невірний формат дати. Використовуйте формат yyyy-MM-dd.");
            return;
        }

        List<Workout> workouts = new ArrayList<>();
        System.out.println("Додайте тренування до плану (введіть 'stop' для завершення):");
        while (true) {
            System.out.print("Введіть тип тренування (або 'stop'): ");
            String workoutType = scanner.nextLine();
            if ("stop".equalsIgnoreCase(workoutType)) {
                break;
            }

            System.out.print("Введіть опис тренування: ");
            String workoutDescription = scanner.nextLine();

            int duration = -1;
            while (duration <= 0) {
                System.out.print("Введіть тривалість тренування (у хвилинах): ");
                if (scanner.hasNextInt()) {
                    duration = scanner.nextInt();
                    if (duration <= 0) {
                        System.out.println("Тривалість повинна бути додатнім числом.");
                    }
                } else {
                    System.out.println("Будь ласка, введіть число.");
                    scanner.next();
                }
            }
            scanner.nextLine();

            System.out.print("Введіть дату тренування (yyyy-MM-dd): ");
            String workoutDateInput = scanner.nextLine();
            Date workoutDate;
            try {
                workoutDate = dateFormat.parse(workoutDateInput);
            } catch (ParseException e) {
                System.out.println("Помилка: Невірний формат дати тренування.");
                return;
            }

            workouts.add(
                new Workout(UUID.randomUUID().toString(), workoutType, workoutDescription, duration,
                    workoutDate));
        }

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

        int nextPlanId = getNextPlanId(userDir);

        String planId = "Training_Plan_" + nextPlanId;
        TrainingPlan trainingPlan = new TrainingPlan(planId, startDate, endDate, userId,
            description, workouts);

        File jsonFile = new File(userDir, planId + ".json");
        try (FileWriter writer = new FileWriter(jsonFile)) {
            gson.toJson(trainingPlan, writer);
            System.out.println("Новий план тренувань створено: " + jsonFile.getName());
        } catch (IOException e) {
            System.out.println("Помилка при створенні JSON-файлу: " + e.getMessage());
        }
    }

    public static void viewTrainingPlans(String userId) {
        String userDirPath = "C:\\Users\\payda\\Desktop\\demoTrainify\\Users_Plans\\User(" + userId
            + ")_TrainingPlans";
        File userDir = new File(userDirPath);

        if (!userDir.exists() || !userDir.isDirectory()) {
            System.out.println("Папка користувача не знайдена або не є директорією.");
            return;
        }

        File[] plans = userDir.listFiles((dir, name) -> name.endsWith(".json"));
        if (plans == null || plans.length == 0) {
            System.out.println("У вас немає збережених планів тренувань.");
        } else {
            System.out.println("Ваші плани тренувань:");
            Arrays.sort(plans, (f1, f2) -> Long.compare(f2.lastModified(),
                f1.lastModified()));
            for (int i = 0; i < plans.length; i++) {
                System.out.println((i + 1) + ". " + plans[i].getName());
            }
            System.out.println(
                "Введіть ID плану, з яким хочете працювати (або '0' для повернення в меню):");

            Scanner scanner = new Scanner(System.in);
            int selectedPlan = scanner.nextInt();

            if (selectedPlan == 0) {
                System.out.println("Повернення в меню...");
            } else if (selectedPlan > 0 && selectedPlan <= plans.length) {
                File selectedFile = plans[selectedPlan - 1];
                System.out.println("Ви обрали план: " + selectedFile.getName());
                viewExistingPlan(userId, selectedFile.getName().replace(".json", ""));
            } else {
                System.out.println("Невірний ID. Спробуйте ще раз.");
            }
        }
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

    public static void viewExistingPlan(String userId, String planId) {
        String userDirPath = "C:\\Users\\payda\\Desktop\\demoTrainify\\Users_Plans\\User(" + userId
            + ")_TrainingPlans";
        File userDir = new File(userDirPath);

        File planFile = new File(userDir, planId + ".json");
        if (!planFile.exists()) {
            System.out.println("План тренувань не знайдено.");
            return;
        }
        try (Scanner scanner = new Scanner(planFile)) {
            String jsonContent = scanner.useDelimiter("\\A").next();
            TrainingPlan trainingPlan = new Gson().fromJson(jsonContent, TrainingPlan.class);
            System.out.println("Інформація про план тренувань: ");
            System.out.println("ID: " + trainingPlan.getId());
            System.out.println("Опис: " + trainingPlan.getDescription());
            System.out.println("Дата початку: " + trainingPlan.getStartDate());
            System.out.println("Дата завершення: " + trainingPlan.getEndDate());
            for (Workout workout : trainingPlan.getWorkouts()) {
                System.out.println(workout);
            }
        } catch (IOException e) {
            System.out.println("Помилка при зчитуванні плану: " + e.getMessage());
        }
    }

    private static int getNextPlanId(File userDir) {
        File[] files = userDir.listFiles(
            (dir, name) -> name.startsWith("Training_Plan_") && name.endsWith(".json"));
        if (files == null || files.length == 0) {
            return 1;
        }

        int maxId = 0;
        for (File file : files) {
            String fileName = file.getName();
            String idPart = fileName.replace("Training_Plan_", "").replace(".json", "");
            try {
                int id = Integer.parseInt(idPart);
                if (id > maxId) {
                    maxId = id;
                }
            } catch (NumberFormatException e) {
            }
        }
        return maxId + 1;
    }
}
