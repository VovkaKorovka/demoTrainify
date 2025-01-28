package main.cods.Trainify.menu;

import static main.cods.Trainify.service.ClearConsoleService.clearConsole;

import main.cods.Trainify.service.UserService;

public class SignUpMenu {

    private static final UserService userService = new UserService();

    public static void showSignUpMenu() {
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
            String userId = userService.getUserIdByName(name);
            TrainifyMenu.showUserMenu(name, userId);
        } else {
            System.out.println("Помилка при реєстрації.");
        }
    }
}

