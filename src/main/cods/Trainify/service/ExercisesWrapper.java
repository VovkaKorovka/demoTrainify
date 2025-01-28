package main.cods.Trainify.service;

import java.util.List;
import main.cods.Trainify.model.Exercise;

public class ExercisesWrapper {

    private List<Exercise> exercises;

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }
}
