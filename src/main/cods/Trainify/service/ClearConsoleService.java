package main.cods.Trainify.service;

public class ClearConsoleService {

    public static void clearConsole() {
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
}
