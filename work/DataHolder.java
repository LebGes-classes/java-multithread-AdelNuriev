package work;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DataHolder {
    private final static String filename = "C:\\Users\\User\\Desktop\\WorkingProcess.xlsx";

    public static BlockingQueue<Task> setTasks() {
        BlockingQueue<Task> tasks = new LinkedBlockingQueue<>();
        FileInputStream file;
        try {
            file = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        XSSFWorkbook workbook;
        try {
            workbook = new XSSFWorkbook(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        XSSFSheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            if (row.getRowNum() == 0 || row.getRowNum() == 1) {
                continue;
            }
            String id = "";
            String employeeId = "";
            int timeProcess = 0;
            String description = null;
            for (Cell cell : row) {
                switch (cell.getColumnIndex()) {
                    case 0 -> id = cell.getStringCellValue().trim();
                    case 1 -> employeeId = cell.getStringCellValue().trim().equals("пусто") ?
                            cell.getStringCellValue().trim() :
                            null;
                    case 2 -> timeProcess = (int) cell.getNumericCellValue();
                    case 3 -> description = cell.getStringCellValue().trim();
                }
            }
            tasks.add(new Task(id, employeeId, timeProcess, description));
        }

        try {
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return tasks;
    }

    public static ArrayList<Employee> setEmployees() {
        ArrayList<Employee> employees = new ArrayList<>();
        FileInputStream file;
        try {
            file = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        XSSFWorkbook workbook;
        try {
            workbook = new XSSFWorkbook(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        XSSFSheet sheet = workbook.getSheetAt(1);
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }
            String id = "";
            String taskId = "";
            int kpd = 0;
            int moodPoints = 0;
            for (Cell cell : row) {
                switch (cell.getColumnIndex()) {
                    case 0 -> id = cell.getStringCellValue().trim();
                    case 1 -> taskId = cell.getStringCellValue().trim().equals("пусто") ?
                            cell.getStringCellValue().trim() :
                            null;
                    case 2 -> kpd = (int) cell.getNumericCellValue();
                    case 3 -> moodPoints = (int) cell.getNumericCellValue();
                }
            }
            Task curTask = null;
            for (Task task : setTasks()) {
                if (task.getID().equals(taskId)) {
                    curTask = task;
                    break;
                }
            }
            employees.add(new Employee(id, curTask, kpd, moodPoints));
        }

        try {
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return employees;
    }

    public static int[] setCalendar() {
        int[] dayMonthYear = new int[3];
        FileInputStream file;
        try {
            file = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        XSSFWorkbook workbook;
        try {
            workbook = new XSSFWorkbook(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        XSSFSheet sheet = workbook.getSheetAt(0);
        Row row = sheet.getRow(0);
        dayMonthYear[0] = (int) row.getCell(0).getNumericCellValue();
        dayMonthYear[1] = (int) row.getCell(1).getNumericCellValue();
        dayMonthYear[2] = (int) row.getCell(2).getNumericCellValue();

        try {
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return dayMonthYear;
    }
}
