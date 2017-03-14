package org.tpri.sc.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import jxl.write.WritableWorkbook;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {

    public static Logger logger = Logger.getLogger(ExcelUtil.class);

    public static List<String> importExcel2007(InputStream inputStream) throws IOException {
        List<String> list = new ArrayList<String>();
        //得到 workbook
        XSSFWorkbook workBook = new XSSFWorkbook(inputStream);
        // 这里只取得第一个sheet的值，默认从0开始
        int colsNum = 0;
        XSSFSheet sheet = workBook.getSheetAt(0);
        for (int i = sheet.getFirstRowNum(); i < sheet.getPhysicalNumberOfRows(); i++) {
            StringBuffer stringBuffer = new StringBuffer();
            XSSFRow row = sheet.getRow(i);
            if (row == null)
                break;
            if (i == 0) {
                colsNum = row.getPhysicalNumberOfCells();
            }
            for (short j = row.getFirstCellNum(); j < colsNum; j++) {
                String cellContent = "";
                if (row.getCell(j) != null) {
                    cellContent = row.getCell(j).toString();
                    if (row.getCell(j).getCellType() == 0) {
                        DecimalFormat df = new DecimalFormat("0");  
                        cellContent = df.format(row.getCell(j).getNumericCellValue());
                    }
                    if(StringUtils.isEmpty(cellContent)){
                        cellContent="null";
                    }
                }else{
                    cellContent = "null";
                }
                if (j > 0)
                    stringBuffer.append(",");
                stringBuffer.append(cellContent);
            }
            // 将每行的字符串用一个String类型的集合保存

            list.add(stringBuffer.toString());
        }
        return list;
    }

    public static List<String> importExcel2003(InputStream inputStream) throws IOException {
        List<String> list = new ArrayList<String>();
        //得到 workbook
        HSSFWorkbook workBook = new HSSFWorkbook(inputStream);
        // 这里只取得第一个sheet的值，默认从0开始
        int colsNum = 0;
        HSSFSheet sheet = workBook.getSheetAt(0);
        for (int i = sheet.getFirstRowNum(); i < sheet.getPhysicalNumberOfRows(); i++) {
            StringBuffer stringBuffer = new StringBuffer();
            HSSFRow row = sheet.getRow(i);
            if (row == null)
                break;
            if (i == 0) {
                colsNum = row.getPhysicalNumberOfCells();
            }
            for (short j = row.getFirstCellNum(); j < colsNum; j++) {
                String cellContent = "";
                if (row.getCell(j) != null) {
                    cellContent = row.getCell(j).toString();
                    if (row.getCell(j).getCellType() == 0) {
                        DecimalFormat df = new DecimalFormat("0");  
                        cellContent = df.format(row.getCell(j).getNumericCellValue());
                    }
                    if(StringUtils.isEmpty(cellContent)){
                        cellContent="null";
                    }
                }else{
                    cellContent = "null";
                }
                if (j > 0)
                    stringBuffer.append(",");
                stringBuffer.append(cellContent);
            }
            // 将每行的字符串用一个String类型的集合保存

            list.add(stringBuffer.toString());
        }
        return list;
    }

    public WritableWorkbook exportExcel(String[] cellTitles, String sheetTitle, WritableWorkbook wwb) {
        return wwb;
    }
}
