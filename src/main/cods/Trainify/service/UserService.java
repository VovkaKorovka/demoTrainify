package main.cods.Trainify.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

public class UserService {

    private static final String FILE_PATH = "C:\\Users\\payda\\Desktop\\demoTrainify\\Data\\users.json";
    private static final String CONFIG_FILE_PATH = "C:\\Users\\payda\\Desktop\\demoTrainify\\src\\main\\resources\\config.json";
    private static final Scanner scanner = new Scanner(System.in);
    private final List<User> users;
    private final Map<String, VerificationDetails> emailVerificationCodes = new HashMap<>();
    private final int nextId;
    private Gson gson = new Gson();

    public UserService() {
        this.users = loadUsersFromFile();
        this.nextId = calculateNextId();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
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

    public boolean register(String name, String password, String email) {
        if (name == null || password == null || email == null) {
            return false;
        }

        if (users.stream().anyMatch(user -> user.getName().equalsIgnoreCase(name))) {
            clearConsole();
            System.out.println("Користувач із таким ім'ям вже існує.");
            return false;
        }

        if (users.stream().anyMatch(user -> user.getEmail().equalsIgnoreCase(email))) {
            clearConsole();
            System.out.println("Користувач із такою email адресою вже існує.");
            return false;
        }

        try {
            String code = generateVerificationCode();
            VerificationDetails verificationDetails = new VerificationDetails(code,
                System.currentTimeMillis());
            emailVerificationCodes.put(email, verificationDetails);

            if (!sendVerificationEmail(email, code)) {
                clearConsole();
                System.out.println("Не вдалося надіслати код підтвердження на email.");
                return false;
            }

            System.out.println("Код підтвердження надіслано на " + email);

            boolean verified = waitForVerification(email);
            if (verified) {
                User newUser = new User(String.valueOf(nextId), name, hashPassword(password),
                    email);
                users.add(newUser);
                saveUsersToFile();

                saveCurrentUserToConfig(name, newUser.getId());
                return true;
            } else {
                clearConsole();
                System.out.println("Підтвердження не пройдено.");
            }
        } catch (Exception e) {
            clearConsole();
            System.out.println("Помилка реєстрації: " + e.getMessage());
        }
        return false;
    }

    public String getUserIdByName(String name) {
        for (User user : users) {
            if (user.getName().equals(name)) {
                return user.getId();
            }
        }
        return null;
    }

    public String loadCurrentUser() {
        try {
            File file = new File(CONFIG_FILE_PATH);
            if (!file.exists()) {
                return null;
            }

            try (FileReader reader = new FileReader(CONFIG_FILE_PATH)) {
                JsonObject config = gson.fromJson(reader, JsonObject.class);
                if (config != null && config.has("currentUser")) {
                    return config.get("currentUser").getAsString();
                }
            }
        } catch (IOException e) {
            System.out.println("Помилка при завантаженні конфігурації: " + e.getMessage());
        }
        return null;
    }

    public String getValidatedInput(String prompt, String fieldType) {
        String input;
        while (true) {
            System.out.print(prompt + " (Або введіть 0, щоб повернутись в меню): ");
            input = scanner.nextLine();

            if ("0".equals(input)) {
                clearConsole();
                return null;
            }

            switch (fieldType) {
                case "name":
                    if (isValidName(input)) {
                        return input;
                    } else {
                        System.out.println(
                            "Ім'я має бути від 3 до 20 символів, може містити тільки літери, цифри та підкреслення.");
                    }
                    break;
                case "password":
                    if (isValidPassword(input)) {
                        return input;
                    } else {
                        System.out.println(
                            "Пароль має містити від 6 до 20 символів і хоча б одну цифру.");
                    }
                    break;
                case "email":
                    if (isValidEmail(input)) {
                        return input;
                    } else {
                        System.out.println("Некоректна email адреса.");
                    }
                    break;
                default:
                    System.out.println("Невірний тип вводу.");
            }
        }
    }

    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 6 && password.length() <= 20
            && password.chars().anyMatch(Character::isDigit);
    }

    private boolean isValidName(String name) {
        return name != null && name.matches("^[\\w]{3,20}$");
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
    }

    private boolean waitForVerification(String email) {
        long startTime = System.currentTimeMillis();
        long endTime = startTime + 60000;

        while (System.currentTimeMillis() < endTime) {
            long remainingTime =
                (endTime - System.currentTimeMillis()) / 1000;
            System.out.print("\rЗалишилося часу для підтвердження: " + remainingTime
                + " секунд. Введіть код підтвердження: ");

            String inputCode = System.console().readLine();

            if (inputCode != null && verifyEmail(email, inputCode)) {
                System.out.println("\nРеєстрація успішна!");
                return true;
            } else if (inputCode != null) {
                System.out.println("\nНевірний код. Спробуйте ще раз.");
            }
        }

        System.out.println("\nЧас для підтвердження коду вичерпано.");
        return false;
    }

    public boolean verifyEmail(String email, String code) {
        VerificationDetails details = emailVerificationCodes.get(email);
        if (details != null && details.getCode().equals(code)) {
            emailVerificationCodes.remove(email);
            return true;
        }
        return false;
    }

    public boolean login(String name, String password) {
        String hashedPassword = hashPassword(password);
        boolean loggedIn = users.stream()
            .anyMatch(
                user -> user.getName().equals(name) && user.getPassword().equals(hashedPassword));

        if (loggedIn) {
            saveUsersToFile();
            String userId = getUserIdByName(name);
            saveCurrentUserToConfig(name, userId);
        }

        return loggedIn;
    }

    private void saveCurrentUserToConfig(String username, String userId) {
        JsonObject config = new JsonObject();
        config.addProperty("currentUser", username);
        config.addProperty("userId", userId);

        try (FileWriter writer = new FileWriter(CONFIG_FILE_PATH)) {
            gson.toJson(config, writer);
        } catch (IOException e) {
            System.out.println("Помилка збереження конфігурації: " + e.getMessage());
        }
    }

    private List<User> loadUsersFromFile() {
        File file = new File(FILE_PATH);
        File parentDir = file.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (FileReader reader = new FileReader(FILE_PATH)) {
            Type userListType = new TypeToken<ArrayList<User>>() {
            }.getType();
            return gson.fromJson(reader, userListType);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private void saveUsersToFile() {
        File file = new File(FILE_PATH);
        File parentDir = file.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            System.out.println("Помилка збереження користувачів у файл: " + e.getMessage());
        }
    }

    private int calculateNextId() {
        return users.stream()
            .mapToInt(user -> Integer.parseInt(user.getId()))
            .max()
            .orElse(0) + 1;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Помилка хешування паролю: " + e.getMessage());
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    private boolean sendVerificationEmail(String email, String code) {
        final String username = "paydavolodimirtop@gmail.com";
        final String password = "whejoutspuiugmvt";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(email));
            message.setSubject("Код підтвердження реєстрації");
            message.setText("Ваш код підтвердження: " + code);

            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            System.out.println("Помилка при надсиланні email: " + e.getMessage());
        }
        return false;
    }

    private static class VerificationDetails {

        private final String code;
        private final long timestamp;

        public VerificationDetails(String code, long timestamp) {
            this.code = code;
            this.timestamp = timestamp;
        }

        public String getCode() {
            return code;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }

    private static class User {

        private final String id;
        private final String name;
        private final String password;
        private final String email;

        public User(String id, String name, String password, String email) {
            this.id = id;
            this.name = name;
            this.password = password;
            this.email = email;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getPassword() {
            return password;
        }

        public String getEmail() {
            return email;
        }
    }
}
