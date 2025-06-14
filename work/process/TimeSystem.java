package work.process;

public class TimeSystem {
    private float factor;
    private int currentHour;
    private int currentMinute;
    Calendar calendar;
    private final int startHour;
    private final int startMinute;
    private final int endHour;
    private final int endMinute;
    private final int duration;

    public TimeSystem(Calendar calendar) {
        startHour = 8;
        duration = 8;
        startMinute = 0;
        currentHour = startHour;
        currentMinute = startMinute;
        endHour = startHour + duration;
        endMinute = 0;
        this.factor = 1.0f;
        this.calendar = calendar;
    }

    public float getFactor() { return factor; }
    public void setFactor(float factor) { this.factor = factor; }
    public int getCurrentHour() { return currentHour; }
    public void setCurrentHour(int currentHour) { this.currentHour = currentHour; }
    public int getCurrentMinute() { return currentMinute; }
    public void setCurrentMinute(int currentMinute) { this.currentMinute = currentMinute; }
    public Calendar getCalendar() { return calendar; }
    public void setCalendar(Calendar calendar) { this.calendar = calendar; }
    public int getStartHour() { return startHour; }
    public int getStartMinute() { return startMinute; }
    public int getEndHour() { return endHour; }
    public int getEndMinute() { return endMinute; }
    public int getDuration() { return duration; }

    public void updateTime() {
        currentMinute += (int) (45 * factor);
        fixTime();
    }

    public void fixTime() {
        if (currentMinute >= 60) { currentMinute = currentMinute % 60; currentHour += 1; }
        if (currentHour == endHour && currentMinute >= endMinute || currentHour > endHour) {
            calendar.setD(calendar.getD() + 1);
            currentHour = startHour;
            currentMinute = startMinute;
        }
        if (calendar.getD() % 7 == 6) calendar.setD(calendar.getD() + 2);
    }
}
