import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final UserService userService = new UserService();

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            showMenu();

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handleRegistration();
                    break;
                case "2":
                    handleLogin();
                    break;
                case "3":
                    System.out.println("До побачення!");
                    running = false;
                    break;
                default:
                    clearConsole();
                    System.out.println("Невірний вибір. Спробуйте ще раз.");
            }
        }
    }

    private static void showMenu() {
        System.out.println("=== Меню ===");
        System.out.println("1. Реєстрація");
        System.out.println("2. Вхід до системи");
        System.out.println("3. Вийти з програми");
        System.out.print("Оберіть опцію: ");
    }

    private static void handleRegistration() {
        String name = userService.getValidatedInput("Введіть ім'я користувача (3-20 символів): ",
            "name");
        if (name == null) {
            return;
        }

        String password = userService.getValidatedInput(
            "Введіть пароль (6-20 символів і хоча б одну цифру): ", "password");
        if (password == null) {
            return;
        }

        String email = userService.getValidatedInput("Введіть email (для подальшої перевірки): ",
            "email");
        if (email == null) {
            return;
        }

        if (userService.register(name, password, email)) {
            System.out.println("Реєстрація успішна!");
            clearConsole();
            TrainifyMenu.showUserMenu(name);
        } else {
            System.out.println("Помилка при реєстрації.");
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
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    private static void handleLogin() {
        String name = userService.getValidatedInput("Введіть ім'я користувача: ", "name");
        if (name == null) {
            return;
        }

        String password = userService.getValidatedInput("Введіть пароль: ", "password");
        if (password == null) {
            return;
        }

        if (userService.login(name, password)) {
            System.out.println("Вхід виконано успішно! Вітаємо, " + name + "!");
            clearConsole();
            TrainifyMenu.showUserMenu(name);
        } else {
            clearConsole();
            System.out.println("Невірне ім'я користувача або пароль.");
        }
    }
}
