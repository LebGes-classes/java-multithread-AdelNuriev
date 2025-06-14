package work;

public class Task {
    private final String ID;
    private String employee;
    private int remainingTime;
    private String description;

    public Task(String ID, int remainingTime) {
        this.ID = ID;
        this.remainingTime = remainingTime * 12;
    }

    public Task(String ID, String employee, int remainingTime, String description) {
        this.ID = ID;
        this.employee = employee;
        this.remainingTime = remainingTime * 12;
        this.description = description;
    }

    public String getID() { return ID; }
    public String getEmployee() { return employee; }
    public void setEmployee(String employee) { this.employee = employee; }
    public int getRemainingTime() { return remainingTime; }
    public void setRemainingTime(int remainingTime) { this.remainingTime = remainingTime; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public void work() {
        if (remainingTime > 0) {
            remainingTime--;
        }
    }

    public boolean isCompleted() {
        return remainingTime <= 0;
    }
}
