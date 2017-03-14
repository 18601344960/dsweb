package org.tpri.sc.service.com;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.document.AbstractExcelView;
import org.tpri.sc.service.obt.ConferenceService;
import org.tpri.sc.service.sys.EnvironmentService;

/**
 * 导出统计分析Excel文件
 * 
 * @author zhaozijing
 *
 */
@Service("ExportStatistics")
public class ExportStatistics extends AbstractExcelView {
    @Resource(name = "ConferenceService")
    private ConferenceService articleService;

    @Autowired
    private EnvironmentService environmentService;

    @Override
    public void buildExcelDocument(Map<String, Object> map, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //数据准备
        String beginTime = (String) map.get("beginTime");
        String endTime = (String) map.get("endTime");
        String ccpartyId = (String) map.get("ccpartyId"); //组织
//        List<QueryResultConference> lists = articleService.getConferencesViewList(ccpartyId, beginTime, endTime);
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY年MM月DD日 HH时mm分ss秒");
        String time = sdf.format(new Date());
        HSSFSheet sheet = workbook.createSheet("机关支部工作手册统计分析");
        sheet.setDefaultColumnWidth((short) 15);
        sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) 7));
        HSSFCell cell = getCell(sheet, 0, 0);
        HSSFCellStyle styleTitle = workbook.createCellStyle(); // 样式对象    标题
        styleTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直    
        styleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平    
        /** 字体begin */
        //生成一个字体 标题
        HSSFFont fontTitle = workbook.createFont();
        fontTitle.setColor(HSSFColor.BLACK.index);//HSSFColor.VIOLET.index //字体颜色
        fontTitle.setFontHeightInPoints((short) 10);
        fontTitle.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //字体增粗
        //把字体应用到当前的样式
        styleTitle.setFont(fontTitle);
        styleTitle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);

        //标题样式
        HSSFCellStyle styleHead = workbook.createCellStyle(); // 样式对象    标题
        styleHead.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直    
        styleHead.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平    
        /** 字体begin */
        //生成一个字体 标题
        HSSFFont fontHead = workbook.createFont();
        fontHead.setColor(HSSFColor.BLACK.index);//HSSFColor.VIOLET.index //字体颜色 
        fontHead.setFontHeightInPoints((short) 9);
        fontHead.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //字体增粗
        //把字体应用到当前的样式
        styleHead.setFont(fontHead);
        styleHead.setFillForegroundColor(HSSFColor.SKY_BLUE.index);

        //内容样式
        HSSFCellStyle styleContent = workbook.createCellStyle(); // 样式对象    内容
        styleContent.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直    
        styleContent.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平    
        /** 字体begin */
        //生成一个字体 标题
        HSSFFont fontContent = workbook.createFont();
        fontContent.setColor(HSSFColor.BLACK.index);//HSSFColor.VIOLET.index //字体颜色 
        fontContent.setFontHeightInPoints((short) 9);
        //把字体应用到当前的样式
        styleContent.setFont(fontContent);
        styleContent.setFillForegroundColor(HSSFColor.SKY_BLUE.index);

        cell.setCellStyle(styleTitle); // 样式，居中    
        setText(cell, "支部工作手册统计分析");
        HSSFCellStyle dateStyle = workbook.createCellStyle();
        //dateStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("mm/dd/yyyy"));    
        cell = getCell(sheet, 1, 0);
        cell.setCellValue("导出日期：" + time);
        //cell.setCellStyle(dateStyle);    
        getCell(sheet, 2, 0).setCellValue("支部名称");
        getCell(sheet, 2, 0).setCellStyle(styleHead);
        getCell(sheet, 2, 1).setCellValue("栏目名称");
        getCell(sheet, 2, 1).setCellStyle(styleHead);
        getCell(sheet, 2, 2).setCellValue("文章数");
        getCell(sheet, 2, 2).setCellStyle(styleHead);
        getCell(sheet, 2, 3).setCellValue("浏览数");
        getCell(sheet, 2, 3).setCellStyle(styleHead);
        getCell(sheet, 2, 4).setCellValue("回复数");
        getCell(sheet, 2, 4).setCellStyle(styleHead);
        getCell(sheet, 2, 5).setCellValue("附件数");
        getCell(sheet, 2, 5).setCellStyle(styleHead);
        //行数遍历
//        for (int i = 0; i < lists.size(); i++) {
//            HSSFRow sheetRow = sheet.createRow(i + 3);
//            HSSFCell cell0 = sheetRow.createCell(0);
//            HSSFCell cell1 = sheetRow.createCell(1);
//            HSSFCell cell2 = sheetRow.createCell(2);
//            HSSFCell cell3 = sheetRow.createCell(3);
//            HSSFCell cell4 = sheetRow.createCell(4);
//            HSSFCell cell5 = sheetRow.createCell(5);
//            cell0.setCellStyle(styleContent);
//            cell1.setCellStyle(styleContent);
//            cell2.setCellStyle(styleContent);
//            cell3.setCellStyle(styleContent);
//            cell4.setCellStyle(styleContent);
//            cell5.setCellStyle(styleContent);
//            cell0.setCellValue(lists.get(i).getCcpartyName());
//            cell1.setCellValue(lists.get(i).getCategoryName());
//            cell2.setCellValue(lists.get(i).getNum());
//            cell3.setCellValue(lists.get(i).getHits());
//            cell4.setCellValue(lists.get(i).getReply());
//            cell5.setCellValue(lists.get(i).getFiles());
//        }
//        String filename = "支部工作手册统计-" + time ;//设置下载时客户端Excel的名称     
//        response.setContentType("application/vnd.ms-excel");
//        response.setHeader("Content-disposition","attachment; filename="+new String((filename).getBytes("gbk"),"iso8859-1")+".xls");
//        OutputStream ouputStream = response.getOutputStream();
//        workbook.write(ouputStream);
//        ouputStream.flush();
//        ouputStream.close();
    }

}
