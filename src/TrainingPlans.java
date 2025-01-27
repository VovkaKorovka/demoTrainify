import java.util.ArrayList;
import java.util.List;

public class TrainingPlans {

    private List<TrainingPlan> plans = new ArrayList<>();  // ініціалізація списку

    public List<TrainingPlan> getPlans() {
        return plans;
    }

    public void setPlans(List<TrainingPlan> plans) {
        this.plans = plans;
    }
}
