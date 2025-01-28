package main.cods.Trainify.menu;

import static main.cods.Trainify.service.ClearConsoleService.clearConsole;

import main.cods.Trainify.Main;
import main.cods.Trainify.service.UserService;

public class LoginMenu {

    private static final UserService userService = new UserService();

    public static void showLoginMenu() {
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
            String userId = userService.getUserIdByName(name);
            TrainifyMenu.showUserMenu(name, userId);
        } else {
            clearConsole();
            System.out.println("Невірне ім'я користувача або пароль.");
            Main.showMenu();
        }
    }
}
