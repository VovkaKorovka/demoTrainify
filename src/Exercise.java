public class Exercise {

    private String id;
    private String name;
    private String description;
    private int reps;
    private int sets;
    private String workoutId;

    public Exercise(String id, String name, String description, int reps, int sets,
        String workoutId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.reps = reps;
        this.sets = sets;
        this.workoutId = workoutId;
    }

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

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public String getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(String workoutId) {
        this.workoutId = workoutId;
    }
}
