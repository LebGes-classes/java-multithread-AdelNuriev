package work;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import work.process.WorkerSystem;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class DataWriter {
    private static final String filename = "C:\\Users\\User\\Desktop\\WorkingProcess.xlsx";

    public static void writeTasks() {
        XSSFWorkbook workbook;
        try {
            workbook = new XSSFWorkbook(new FileInputStream(filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        XSSFSheet sheet = workbook.getSheetAt(0);
        Row timeRow = sheet.getRow(0);

        Cell dayCell = timeRow.getCell(0);
        Cell monthCell = timeRow.getCell(1);
        Cell yearCell = timeRow.getCell(2);

        dayCell.setCellValue(WorkerSystem.timeSystem.getCalendar().getD());
        monthCell.setCellValue(WorkerSystem.timeSystem.getCalendar().getM());
        yearCell.setCellValue(WorkerSystem.timeSystem.getCalendar().getY());

        int rownum = 1;
        for (Task task : WorkerSystem.taskQueue) {
            Row row = sheet.createRow(++rownum);
            Cell IDCell = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            Cell employeeIDCell = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            Cell remainingTimeCell = row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            Cell descriptionCell = row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);


            //в соответствующие ячейки записываем наши значения
            IDCell.setCellValue(task.getID());
            employeeIDCell.setCellValue(task.getEmployee() != null ? task.getEmployee() : "пусто");
            remainingTimeCell.setCellValue((int)((float)task.getRemainingTime() / 12));
            descriptionCell.setCellValue(task.getDescription());
        }

        try {
            FileOutputStream out = new FileOutputStream(filename);
            workbook.write(out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeEmployees() {
        XSSFWorkbook workbook;
        try {
            workbook = new XSSFWorkbook(new FileInputStream(filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        XSSFSheet sheet = workbook.getSheetAt(1);
        int rownum = 0;
        for (Employee employee : WorkerSystem.getEmployees()) {
            Row row = sheet.createRow(++rownum);
            Cell IDCell = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            Cell taskIDCell = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            Cell kpdCell = row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            Cell moodCell = row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);


            //в соответствующие ячейки записываем наши значения
            IDCell.setCellValue(employee.getName());
            taskIDCell.setCellValue(employee.getCurrentTask() != null ? employee.getCurrentTask().getID() : "пусто");
            kpdCell.setCellValue(employee.getKpd());
            moodCell.setCellValue(employee.getMood());
        }

        try {
            FileOutputStream out = new FileOutputStream(filename);
            workbook.write(out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
