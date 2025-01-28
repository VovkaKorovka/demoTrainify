package main.cods.Trainify;

import static main.cods.Trainify.service.ClearConsoleService.clearConsole;

import java.util.Scanner;
import main.cods.Trainify.menu.LoginMenu;
import main.cods.Trainify.menu.SignUpMenu;
import main.cods.Trainify.menu.TrainifyMenu;
import main.cods.Trainify.service.UserService;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final UserService userService = new UserService();

    public static void main(String[] args) {
        boolean running = true;

        String currentUser = userService.loadCurrentUser();
        if (currentUser != null) {
            String userId = userService.getUserIdByName(currentUser);
            TrainifyMenu.showUserMenu(currentUser, userId);
        } else {
            while (running) {
                showMenu();
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1":
                        SignUpMenu.showSignUpMenu();
                        break;
                    case "2":
                        LoginMenu.showLoginMenu();
                        break;
                    case "3":
                        System.out.println("До побачення!");
                        System.exit(0);
                        break;
                    default:
                        clearConsole();
                        System.out.println("Невірний вибір. Спробуйте ще раз.");
                }
            }
        }
    }

    public static void showMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("=== Меню ===");
            System.out.println("1. Реєстрація");
            System.out.println("2. Вхід до системи");
            System.out.println("3. Вийти з програми");
            System.out.print("Оберіть опцію: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    SignUpMenu.showSignUpMenu();
                    break;
                case "2":
                    LoginMenu.showLoginMenu();
                    break;
                case "3":
                    System.out.println("До побачення!");
                    System.exit(0);
                    break;
                default:
                    clearConsole();
                    System.out.println("Невірний вибір. Спробуйте ще раз.");
            }
        }
    }
}
