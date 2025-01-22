import java.util.Date;

public class Workout {

    private String id;
    private String type;
    private String description;
    private int duration;
    private Date date;

    public Workout(String id, String type, String description, int duration, Date date) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.duration = duration;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
}
