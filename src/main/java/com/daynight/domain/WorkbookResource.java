package com.daynight.domain;

import com.daynight.constants.StatisticsTypeNum;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class WorkbookResource {

    private static final String XLSX = "xlsx";

    private static final String XLS = "xls";

    private static final String BLANK = "";

    private String orderSheetName;

    private FormulaEvaluator formulaEvaluator;

    private Workbook workbook;

    private File sourceFile;

    private Logger logger = LoggerFactory.getLogger(WorkbookResource.class);

    public WorkbookResource(File sourceFile) {
        this.workbook = getWorkbook(sourceFile);
        this.sourceFile = sourceFile;
        formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public void setOrderSheetName(String orderSheetName) {
        this.orderSheetName = orderSheetName;
    }

    public Sheet getOrderSheet() {
        return this.workbook.getSheet(orderSheetName);
    }

    private String getCellStringVal(Cell cell) {
        if (cell == null) {
            return BLANK;
        }
        CellType cellType = cell.getCellType();

        switch (cellType) {
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case STRING:
                return cell.getStringCellValue();
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return String.valueOf(formulaEvaluator.evaluate(cell).getNumberValue());
            case BLANK:
                return BLANK;
            case ERROR:
                return String.valueOf(cell.getErrorCellValue());
            default:
                return BLANK;
        }
    }

    public String getValue(Row row, int index) {
        String temp = getCellStringVal(row.getCell(index)).trim();
        if (temp.endsWith(".0")) {
            return temp.substring(0, temp.length() - 2);
        }
        return temp;
    }


    public int getInt(Row row, int index) {
        String value = getValue(row, index);
        if (BLANK.equals(value)) {
            return 0;
        }
        return Double.valueOf(value).intValue();
    }

    public double getDouble(Row row, int index) {
        String value = getValue(row, index);
        if (BLANK.equals(value)) {
            return 0;
        }
        return Double.valueOf(value);
    }

    public List<Order> readOrders() {
        Sheet sheet = getOrderSheet();
        List<Order> orders = Lists.newArrayList();

        //第0行是表名，忽略，从第二行开始读取
        for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
            Row row = sheet.getRow(rowNum);
            Cell cell = row.getCell(0);
            if (cell == null) {
                break;
            }
            Order order = new Order();
            order.setCustomer(getValue(row, 0));
            order.setBrand(getValue(row, 1));
            order.setStyle(getValue(row, 2));
            order.setColor(getValue(row, 3));
            order.setSizeLNum(getInt(row, 4));
            order.setSizeXLNum(getInt(row, 5));
            order.setSize2XLNum(getInt(row, 6));
            order.setSize3XLNum(getInt(row, 7));
            order.setPrice(getDouble(row, 9));
            order.setShareBillPerson(getValue(row, 11));

            orders.add(order);
        }
        return orders;
    }

    public List<StatisticsRow> readOrdersFromStatics() {
        Sheet sheet = workbook.getSheetAt(0);
        List<StatisticsRow> statisticsRows = Lists.newArrayList();

        String from = "";
        String fileName = sourceFile.getName();
        if (fileName.contains(StatisticsTypeNum.SOURCE.type)) {
            from = StatisticsTypeNum.SOURCE.type;
        } else if (fileName.contains(StatisticsTypeNum.RATE.type)) {
            from = StatisticsTypeNum.RATE.type;
        } else if (fileName.contains(StatisticsTypeNum.STOCK_UP.type)) {
            from = StatisticsTypeNum.STOCK_UP.type;
        }
        //第0行是表名，忽略，从第二行开始读取
        for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
            Row row = sheet.getRow(rowNum);
            Cell cell = row.getCell(0);
            if (cell == null) {
                break;
            }
            StatisticsRow statisticsRow = new StatisticsRow();
            statisticsRow.setFrom(from);
            statisticsRow.setBrand(getValue(row, 0));
            statisticsRow.setStyle(getValue(row, 1));
            statisticsRow.setColor(getValue(row, 2));
            statisticsRow.setSizeLNum(getInt(row, 3));
            statisticsRow.setSizeXLNum(getInt(row, 4));
            statisticsRow.setSize2XLNum(getInt(row, 5));
            statisticsRow.setSize3XLNum(getInt(row, 6));
            statisticsRow.setPrice(getDouble(row, 8));

            statisticsRows.add(statisticsRow);
        }
        return statisticsRows;
    }


    public Workbook getWorkbook(File srcFile) {

        FileInputStream is = null;

        try {

            String fileName = srcFile.getName();
            is = new FileInputStream(srcFile);
            if (fileName.toLowerCase().endsWith(XLSX)) {
                return new XSSFWorkbook(is);
            } else if (fileName.toLowerCase().endsWith(XLS)) {
                return new HSSFWorkbook(is);
            } else {
                throw new RuntimeException("excel格式文件错误");
            }

        } catch (IOException e) {
            logger.error("failed to read workbook, exception:{}", e.getMessage());
            //  抛出自定义的业务异常
        } finally {
            IOUtils.closeQuietly(is);
        }
        return null;
    }
}
