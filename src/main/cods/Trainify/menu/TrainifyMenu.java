package main.cods.Trainify.menu;

import static main.cods.Trainify.service.ClearConsoleService.clearConsole;

import java.util.Scanner;
import main.cods.Trainify.service.TrainingPlanService;

public class TrainifyMenu {

    private static final Scanner scanner = new Scanner(System.in);

    public static void showUserMenu(String userName, String userId) {
        boolean running = true;

        while (running) {
            System.out.println("=== Привіт, " + userName + "! ===");
            System.out.println("1. Створити новий план тренувань");
            System.out.println("2. Переглянути мої плани тренувань");
            System.out.println("3. Вийти з аккаунту");
            System.out.println("4. Вийти з програми");
            System.out.print("Оберіть опцію: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    TrainingPlanService.createTrainingPlan(userId);
                    break;
                case "2":
                    TrainingPlanService.manageTrainingPlan(userId);
                    break;
                case "3":
                    TrainingPlanService.logOut(userId);
                    break;
                case "4":
                    System.out.println("До побачення!");
                    System.exit(0);
                default:
                    clearConsole();
                    System.out.println("Невірний вибір. Спробуйте ще раз.");
            }
        }
    }
}