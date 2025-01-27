import java.time.LocalDate;
import java.util.List;

public class TrainingPlan {

    private final String planId; // поле final, тому не потрібно setId
    private LocalDate startDate;
    private LocalDate endDate;
    private String userId;
    private String description;
    private List<Workout> workouts; // Список тренувань має бути типу Workout, а не String

    // Конструктор
    public TrainingPlan(String planId, LocalDate startDate, LocalDate endDate, String userId,
        String description, List<Workout> workouts) {
        this.planId = planId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userId = userId;
        this.description = description;
        this.workouts = workouts;
    }

    public String getPlanId() {
        return planId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Workout> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(List<Workout> workouts) {
        this.workouts = workouts;
    }
}
