import java.util.Scanner;

public class TrainifyMenu {

    private static final Scanner scanner = new Scanner(System.in);

    public static void showUserMenu(String userName) {
        boolean running = true;

        while (running) {
            clearConsole();
            System.out.println("=== Привіт, " + userName + "! ===");
            System.out.println("1. Переглянути профіль");
            System.out.println("2. Вийти з системи");
            System.out.print("Оберіть опцію: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    showProfile(userName);
                    break;
                case "2":
                    System.out.println("До побачення, " + userName + "!");
                    running = false;
                    break;
                default:
                    System.out.println("Невірний вибір. Спробуйте ще раз.");
            }
        }
    }

    private static void showProfile(String userName) {
        // Тут можна додати логіку для перегляду профілю користувача
        System.out.println("Профіль користувача: " + userName);
        // Задати додаткові дії для користувача
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
