package main.cods.Trainify.model;

import java.util.ArrayList;
import java.util.List;

public class TrainingPlans {

    private List<TrainingPlan> plans = new ArrayList<>();

    public List<TrainingPlan> getPlans() {
        return plans;
    }

    public void setPlans(List<TrainingPlan> plans) {
        this.plans = plans;
    }
}
