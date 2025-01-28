import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Workout {

    private String id;
    private String name;
    private String description;
    private int duration; // Тривалість у хвилинах
    private Date date;
    private String type; // Тип тренування (наприклад: "Кардіо", "Сила" тощо)
    private List<Exercise> exercises; // Список вправ

    public Workout(String id, String name, String description, int duration, Date date,
        String type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.date = date;
        this.type = type; // Ініціалізація типу тренування
        this.exercises = new ArrayList<>();
    }

    // Геттери та сеттери
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }
}
