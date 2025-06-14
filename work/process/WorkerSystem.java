package work.process;

import work.DataHolder;
import work.DataWriter;
import work.Employee;
import work.Task;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.stream.Collectors;

public class WorkerSystem {
    public static final Calendar calendar = new Calendar(DataHolder.setCalendar()[0],
            DataHolder.setCalendar()[1],
            DataHolder.setCalendar()[2]);
    public static final TimeSystem timeSystem = new TimeSystem(calendar);
    private static final int WORK_DAY_DURATION_MINUTES = 2; // каждый день 2 минуты
    private static final int WORK_DAY_DURATION_SECONDS = WORK_DAY_DURATION_MINUTES * 60 - 10; // баланс по времени
    private static final int WORKERS_COUNT = 5; // заранее известно количество сотрудников
    private static final AtomicInteger dayCounter = new AtomicInteger(1);
    public static final BlockingQueue<Task> taskQueue = DataHolder.setTasks();
    public static final BlockingQueue<Task> postTaskQueue = taskQueue.stream().filter(task -> task.getRemainingTime()!=0).collect(Collectors.toCollection(LinkedBlockingQueue::new));
    private static ArrayList<Employee> employees = DataHolder.setEmployees();
    private static final ExecutorService executor = Executors.newFixedThreadPool(WORKERS_COUNT);
    private static final ScheduledExecutorService timeScheduler = Executors.newSingleThreadScheduledExecutor();
    private static final ScheduledExecutorService dayScheduler = Executors.newSingleThreadScheduledExecutor();

    public static void main(String[] args) {
        Thread.currentThread().setPriority(10);
        // Запуск системы
        startSystem();

        // Обработка ввода задач с консоли
        handleConsoleInput();

        try {
            Thread.sleep(30 * 60 * 1000);
            shutdownSystem();
        } catch (InterruptedException e) {
            shutdownSystem();
        }
    }

    private static void startSystem() {
        // инициализация работников как потоков
        for (int i = 0; i < WORKERS_COUNT; i++) {
            executor.execute(employees.get(i));
        }

        // Запускаем рабочий день
        dayScheduler.scheduleAtFixedRate(() -> {
            System.out.println("\n=== Начало рабочего дня " + dayCounter.getAndIncrement() + " ===");
            DataWriter.writeTasks();
            DataWriter.writeEmployees();
        }, 0, WORK_DAY_DURATION_SECONDS, TimeUnit.SECONDS);

        // Запускаем таймер на день
        timeScheduler.scheduleAtFixedRate(WorkerSystem::printTime, 1, 10, TimeUnit.SECONDS);
    }

    private static void handleConsoleInput() {
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("\nВведите название задачи и длительность в часах: ");
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("exit")) {
                    shutdownSystem();
                    break;
                }

                try {
                    String[] parts = input.split(" ");
                    String name = parts[0];
                    int duration = Integer.parseInt(parts[1]);
                    if (duration < 1) {
                        System.out.println("Длительность должна быть от 1 часа!");
                        continue;
                    }
                    taskQueue.put(new Task(name, null, duration, "описания еще не добавлены"));
                    postTaskQueue.put(new Task(name, null, duration, "описания еще не добавлены"));
                    printTime("Добавлена задача: " + name + " (" + duration + " часов)");
                } catch (Exception e) {
                    System.out.println("Неправильный формат ввода!");
                }
            }
            scanner.close();
        }).start();
    }

    private static void shutdownSystem() {
        executor.shutdownNow();
        timeScheduler.shutdownNow();
        dayScheduler.shutdownNow();
        System.exit(0);
    }

    private static void printTime(String message) {
        System.out.printf("%n%d %s %d года. Время %d:%d %n %s", calendar.getD(), Calendar.months[calendar.getM()],
                calendar.getY(), timeSystem.getCurrentHour(), timeSystem.getCurrentMinute(), message);
    }

    private static void printTime() {
        System.out.printf("%n%d %s %d года. Время %d:%d", calendar.getD(), Calendar.months[calendar.getM()],
                calendar.getY(), timeSystem.getCurrentHour(), timeSystem.getCurrentMinute());
        timeSystem.updateTime();
    }

    public static ArrayList<Employee> getEmployees() {
        return employees;
    }

    public static void setEmployees(ArrayList<Employee> employees) {
        WorkerSystem.employees = employees;
    }
}