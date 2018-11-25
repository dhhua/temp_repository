package com.daynight.service;

import com.daynight.constants.StatisticsTypeNum;
import com.daynight.domain.Order;
import com.daynight.domain.StatisticsRow;
import com.daynight.domain.WorkbookResource;
import org.apache.commons.compress.utils.Lists;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExcelService {


    private Logger logger = LoggerFactory.getLogger(ExcelService.class);



    public void handleSource(File sourceFile, String sheetName) {

        WorkbookResource workbookResource = new WorkbookResource(sourceFile);
        workbookResource.setOrderSheetName(sheetName);

        List<Order> orders = workbookResource.readOrders();

        Map<String, List<Order>> classifiedMap = classify(orders);

        SortedMap<String, List<Order>> sortedMap = new TreeMap<>();
        sortedMap.putAll(classifiedMap);
        List<Order> mergedOrders = mergeOrder(sortedMap);

        try {
            export(mergedOrders, "/Users/honghua.dong/Documents/svnworkspace/prolongs-life/src/main/resources/终端.xls");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(mergedOrders);
    }

    private void export(List<Order> mergedOrders, String fileName) throws IOException {

        HSSFWorkbook wb = new HSSFWorkbook();

        HSSFSheet sheet =  wb.createSheet("汇总");

        HSSFRow row = sheet.createRow(0);

        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);

        //声明列对象
        HSSFCell cell;
        List<String> titles = Arrays.asList("品牌", "款号", "颜色", "L", "XL", "2XL", "3XL", "合计", "零售价", "总价");
        //创建标题
        for (int i = 0; i < titles.size(); i++) {
            cell = row.createCell(i);
            cell.setCellValue(titles.get(i));
            cell.setCellStyle(style);
        }

        //创建内容
        for (int i = 0; i < mergedOrders.size(); i++) {
            Order order = mergedOrders.get(i);
            row = sheet.createRow(i + 1);

            row.createCell(0).setCellValue(order.getBrand());
            row.createCell(1).setCellValue(order.getStyle());
            row.createCell(2).setCellValue(order.getColor());
            row.createCell(3).setCellValue(order.getSizeLNum());
            row.createCell(4).setCellValue(order.getSizeXLNum());
            row.createCell(5).setCellValue(order.getSize2XLNum());
            row.createCell(6).setCellValue(order.getSize3XLNum());
            int total = order.getSizeLNum() + order.getSizeXLNum() + order.getSize2XLNum() + order.getSize3XLNum();
            row.createCell(7).setCellValue(total);
            row.createCell(8).setCellValue(order.getPrice());
            row.createCell(9).setCellValue(order.getPrice() * total);
        }

        wb.write(new File(fileName));

    }

    private List<Order> mergeOrder(Map<String, List<Order>> classifiedMap) {

        List<Order> result = Lists.newArrayList();

        classifiedMap.forEach((s, orders) -> {
            Order temp = new Order();
            temp.setSize3XLNum(0);
            temp.setSize2XLNum(0);
            temp.setSizeXLNum(0);
            temp.setSizeLNum(0);
            for (Order order : orders) {
                temp.setColor(order.getColor());
                temp.setShareBillPerson(order.getShareBillPerson());
                temp.setStyle(order.getStyle());
                temp.setBrand(order.getBrand());
                temp.setPrice(order.getPrice());
                temp.setCustomer(order.getCustomer());

                temp.setSizeLNum(temp.getSizeLNum() + order.getSizeLNum());
                temp.setSizeXLNum(temp.getSizeXLNum() + order.getSizeXLNum());
                temp.setSize2XLNum(temp.getSize2XLNum() + order.getSize2XLNum());
                temp.setSize3XLNum(temp.getSize3XLNum() + order.getSize3XLNum());
            }

            result.add(temp);
        });
        return result;
    }

    /**
     *
     * 按照 品牌-款号-颜色分类
     * @param orders
     * @return
     */
    private Map<String, List<Order>> classify(List<Order> orders) {
        return orders.stream().collect(Collectors.groupingBy(Order::group));
    }

    /**
     *
     * 按照 品牌-款号-颜色分类 From
     * @param statisticsRows
     * @return
     */
    private Map<String, Map<String, List<StatisticsRow>>> classifyStatistics(List<StatisticsRow> statisticsRows) {
        return statisticsRows.stream().collect(Collectors.groupingBy(StatisticsRow::group, Collectors.groupingBy(StatisticsRow::getFrom)));
    }

    public void handleStatics(List<File> staticsFile) {

        List<StatisticsRow> totalRows = Lists.newArrayList();
        for (File file : staticsFile) {
            String fileName = file.getName();
            String from = "";
            if (fileName.contains(StatisticsTypeNum.SOURCE.type)) {
                from = StatisticsTypeNum.SOURCE.type;
            } else if (fileName.contains(StatisticsTypeNum.RATE.type)) {
                from = StatisticsTypeNum.RATE.type;
            } else if (fileName.contains(StatisticsTypeNum.STOCK_UP.type)) {
                from = StatisticsTypeNum.STOCK_UP.type;
            }
            if ("".equals(from)) {
                continue;
            }
            WorkbookResource workbookResource = new WorkbookResource(file);
            List<StatisticsRow> statisticsRows = workbookResource.readOrdersFromStatics();
            totalRows.addAll(statisticsRows);
        }

        Map<String, Map<String, List<StatisticsRow>>> classifiedMap = classifyStatistics(totalRows);

        SortedMap<String, Map<String, List<StatisticsRow>>> sortedMap = new TreeMap<>();
        sortedMap.putAll(classifiedMap);
        List<Map<String, StatisticsRow>> mergedOrders = mergeStatistics(sortedMap);

        System.out.println(mergedOrders);
    }

    private List<Map<String, StatisticsRow>> mergeStatistics(SortedMap<String, Map<String, List<StatisticsRow>>> classifiedMap) {
        List<Map<String, StatisticsRow>> result = Lists.newArrayList();

        classifiedMap.forEach((s, rowsMap) -> {

            Map<String, StatisticsRow> tempMap = new HashMap<>();
            rowsMap.forEach((from, rows) -> {
                StatisticsRow temp = new StatisticsRow();
                temp.setSize3XLNum(0);
                temp.setSize2XLNum(0);
                temp.setSizeXLNum(0);
                temp.setSizeLNum(0);
                temp.setFrom(from);
                for (StatisticsRow row : rows) {
                    temp.setColor(row.getColor());
                    temp.setStyle(row.getStyle());
                    temp.setBrand(row.getBrand());
                    temp.setPrice(row.getPrice());

                    temp.setSizeLNum(temp.getSizeLNum() + row.getSizeLNum());
                    temp.setSizeXLNum(temp.getSizeXLNum() + row.getSizeXLNum());
                    temp.setSize2XLNum(temp.getSize2XLNum() + row.getSize2XLNum());
                    temp.setSize3XLNum(temp.getSize3XLNum() + row.getSize3XLNum());
                }
                tempMap.put(from, temp);
            });
            result.add(tempMap);
        });
        return result;
    }


    private void exportStatistics(List<Map<String, StatisticsRow>> mergedStatistics, String fileName) throws IOException {

        HSSFWorkbook wb = new HSSFWorkbook();

        HSSFSheet sheet =  wb.createSheet("汇总");

        HSSFRow row = sheet.createRow(0);

        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);

        //声明列对象
        HSSFCell cell;
        List<String> titles = Arrays.asList("品牌", "款号", "颜色", "L", "XL", "2XL", "3XL", "合计", "零售价", "总价");
        //创建标题
        for (int i = 0; i < titles.size(); i++) {
            cell = row.createCell(i);
            cell.setCellValue(titles.get(i));
            cell.setCellStyle(style);
        }

        //创建内容
        for (int i = 0; i < mergedStatistics.size(); i++) {
            Map<String, StatisticsRow> statisticsRowMap = mergedStatistics.get(i);

            row = sheet.createRow(i + 1);

//            row.createCell(0).setCellValue(order.getBrand());
//            row.createCell(1).setCellValue(order.getStyle());
//            row.createCell(2).setCellValue(order.getColor());
//            row.createCell(3).setCellValue(order.getSizeLNum());
//            row.createCell(4).setCellValue(order.getSizeXLNum());
//            row.createCell(5).setCellValue(order.getSize2XLNum());
//            row.createCell(6).setCellValue(order.getSize3XLNum());
//            int total = order.getSizeLNum() + order.getSizeXLNum() + order.getSize2XLNum() + order.getSize3XLNum();
//            row.createCell(7).setCellValue(total);
//            row.createCell(8).setCellValue(order.getPrice());
//            row.createCell(9).setCellValue(order.getPrice() * total);
        }

        wb.write(new File(fileName));

    }
}
