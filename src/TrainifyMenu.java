import java.util.Scanner;

public class TrainifyMenu {

    private static final Scanner scanner = new Scanner(System.in);

    public static void showUserMenu(String userName, String userId) {
        boolean running = true;

        while (running) {
            clearConsole();
            System.out.println("=== Привіт, " + userName + "! ===");
            System.out.println("1. Створити новий план тренувань");
            System.out.println("2. Переглянути мої плани тренувань");
            System.out.println("3. Вийти з аккаунту");
            System.out.print("Оберіть опцію: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    TrainingPlanManager.createTrainingPlan(userId);
                    break;
                case "2":
                    TrainingPlanManager.viewTrainingPlans(userId);
                    break;
                case "3":
                    TrainingPlanManager.logOut(userId);
                    running = false;
                    break;
                default:
                    clearConsole();
                    System.out.println("Невірний вибір. Спробуйте ще раз.");
            }
        }
    }

    private static void clearConsole() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.println("Не вдалося очистити консоль.");
        }
    }
}
