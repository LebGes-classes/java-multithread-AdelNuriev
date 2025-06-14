package work;

import work.process.Calendar;
import work.process.WorkerSystem;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static work.process.WorkerSystem.calendar;
import static work.process.WorkerSystem.taskQueue;

public class Employee implements Runnable {
    private static final int MAX_MOOD = 10;
    private final String name;
    private int mood;
    private Task currentTask;
    private int ticksWorked = 0;
    private int kpd;

    public Employee(String name, Task currentTask, int kpd, int mood) {
        this.name = name;
        this.currentTask = currentTask;
        this.kpd = kpd;
        this.mood = mood;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                if (mood <= 0) {
                    printTime(name + ": На перерыве из-за плохого настроения");
                    Thread.sleep(1000); // Восстановление
                    mood = MAX_MOOD;
                    continue;
                }

                if (currentTask == null || currentTask.isCompleted()) {
                    if (currentTask != null) {
                        printTime(name + ": Завершил задачу " + currentTask.getID());
                        currentTask = null;
                    }

                    currentTask = WorkerSystem.postTaskQueue.poll(1, TimeUnit.SECONDS);
                    currentTask.setEmployee(name);
                    taskQueue.forEach(task -> { if (task.getID().equals(currentTask.getID())) task.setEmployee(currentTask.getEmployee());});
                    if (currentTask != null) {
                        printTime(name + ": Взял задачу " + currentTask.getID());
                        taskQueue.forEach(task -> { if (task.getID().equals(currentTask.getID())) task.setRemainingTime(currentTask.getRemainingTime());});
                        ticksWorked = 0;
                    }
                }

                if (currentTask != null && !currentTask.isCompleted() && mood > 0) {
                    currentTask.work();
                    taskQueue.forEach(task -> { if (task.getID().equals(currentTask.getID())) task.setRemainingTime(currentTask.getRemainingTime());});
                    kpd = (kpd + 9) / 2;
                    ticksWorked++;
                    Thread.sleep(1000);
                }

                if (ticksWorked > 0 && ThreadLocalRandom.current().nextInt(10) < 1) {
                    mood--;
                    kpd = kpd / 2;
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static void printTime(String message) {
        System.out.printf("%n%d %s %d года. Время %d:%d %n %s", WorkerSystem.calendar.getD(), Calendar.months[calendar.getM()],
                calendar.getY(), WorkerSystem.timeSystem.getCurrentHour(), WorkerSystem.timeSystem.getCurrentMinute(), message);
    }

    public String getName() { return name; }
    public int getMood() { return mood; }
    public void setMood(int mood) { this.mood = mood; }
    public Task getCurrentTask() { return currentTask; }
    public void setCurrentTask(Task currentTask) { this.currentTask = currentTask; }
    public int getTicksWorked() { return ticksWorked; }
    public void setTicksWorked(int ticksWorked) { this.ticksWorked = ticksWorked; }
    public int getKpd() { return kpd; }
    public void setKpd(int kpd) { this.kpd = kpd; }
}
