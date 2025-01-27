import java.util.Date;

public class Workout {

    private String id;                // Унікальний ідентифікатор тренування
    private String name;              // Назва тренування
    private String description;       // Опис тренування
    private int duration;             // Тривалість тренування у хвилинах
    private Date date;           // Дата тренування

    public Workout(String id, String name, String description, int duration, Date date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.date = date;
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

    @Override
    public String toString() {
        return "Workout{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", duration=" + duration +
            ", date=" + date +
            '}';
    }
}
