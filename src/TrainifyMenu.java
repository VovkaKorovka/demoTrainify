import java.util.Scanner;

public class TrainifyMenu {

    private static final Scanner scanner = new Scanner(System.in);

    public static void showUserMenu(String userName, String userId) {
        boolean running = true;

        while (running) {
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
                    TrainingPlanManager.manageTrainingPlan(userId);
                    break;
                case "3":
                    TrainingPlanManager.logOut(userId);
                    break;
                default:
                    Main.clearConsole();
                    System.out.println("Невірний вибір. Спробуйте ще раз.");
            }
        }
    }
}
