package org.tpri.report.view;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.j2ee.servlets.BaseHttpServlet;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.view.jasperreports.JasperReportsMultiFormatView;

/**
 * Jasper报表视图,负责处理Jasper报表视图请求转发类：html,csv,pdf,xls
 * 
 * @author 易文俊
 * @since 2015-07-29
 */
public class JasperReportsView extends JasperReportsMultiFormatView {

	/** 默认字符集编码 */
	public static final String DEFAULT_CHARSET_ENCODING = "UTF-8";

	/** 报表格式：html */
	public static final String FORMAT_HTML = "html";

	/** 报表格式：xls */
	public static final String FORMAT_XLS = "xls";

	/** 报表格式：PDF */
	public static final String FORMAT_PDF = "pdf";

	/** 键值：报表数据 */
	public static final String KEY_FORMAT = DEFAULT_FORMAT_KEY;

	/** 键值：主数据 */
	public static final String KEY_MAIN_DATA = "maindata";

	/** 键值：文件名 */
	public static final String KEY_FILE_NAME = "filename";

	/** 键值：内嵌显示 */
	public static final String KEY_FILE_INLINE = "inline";

	/** 键值：用户客户端 */
	public static final String KEY_USER_AGENT = "userAgent";

	/** 键值：会话信息 */
	public static final String KEY_PRINT_SESSION = "printSession";

	/** 参数：能否分页 */
	public static final String PARAM_IGNORE_PAGE = "IS_IGNORE_PAGINATION";

	/** 地址：报表图片 */
	public static final String URL_JASPER_IMAGE = "report.jrimg?image=";

	/** 默认文件名 */
	public static final String DEFAULT_FILE_NAME = "JD_REPORT";

	/** 报表日期格式 */
	public static final String REPORT_DATE_FORMAT = "yyyy年MM月dd日";
	
	/** 打印纸张大小 */
    public static final String REPORT_PAPER_SIZE = "paperSize";

    /** 打印纸张大小:A3 */
    public static final String REPORT_PAPER_SIZE_A3 = "A3";

    /** 打印纸张大小:A4 */
    public static final String REPORT_PAPER_SIZE_A4 = "A4";
	
	/** 打印方向 */
    public static final String REPORT_ORIENTATION_VALUE = "orientationValue";
	
	/** 打印方向:竖 */
    public static final String REPORT_ORIENTATION_VALUE_PORTRAIT = "PORTRAIT";

    /** 打印方向:横 */
    public static final String REPORT_ORIENTATION_VALUE_LANDSCAPE = "LANDSCAPE";

	/** 日志输出 */
	private static final Logger logger = Logger.getLogger(JasperReportsMultiFormatView.class);

	/**
	 * 设定输出内容
	 */
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {

		// 是否分页
		String format = (String) model.get(KEY_FORMAT);
		if (isBlank(format)) {
			format = FORMAT_HTML;
		}
		if (FORMAT_PDF.equals(format)) {
			model.put(PARAM_IGNORE_PAGE, false);
		} else {
			model.put(PARAM_IGNORE_PAGE, true);
		}

		// 是否输出报表到会话
		if (FORMAT_HTML.equals(format)) {
			Boolean printSession = (Boolean) model.get(KEY_PRINT_SESSION);
			if (printSession != null && printSession) {
				model.put(KEY_PRINT_SESSION, request.getSession(true));
			}
		}

		// 下面俩行代码必须有,否则导出word,excel将报错
		Properties headers = new Properties();
		super.setHeaders(headers);

		// 调用父类设定输出内容
		try {
			super.renderMergedOutputModel(model, request, response);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * 输出报表内容
	 */
	@SuppressWarnings("deprecation")
	@Override
	protected void renderReport(JasperPrint jasperPrint, Map<String, Object> model, HttpServletResponse response) throws JRException, IOException {
		String format = (String) model.get(KEY_FORMAT);
		if (isBlank(format)) {
			format = FORMAT_HTML;
		}

		response.setCharacterEncoding(DEFAULT_CHARSET_ENCODING);
		response.setHeader("Cache-Control", "no-cache, no-store, max-age=0");
		response.addHeader("Content-Language", "zh-CN");
		response.addDateHeader("Last-Modified", System.currentTimeMillis());

		if (!FORMAT_HTML.equals(format)) {
			String fileName = (String) model.get(KEY_FILE_NAME);
			if (isBlank(fileName)) {
				fileName = DEFAULT_FILE_NAME;
			}
			if (!fileName.endsWith("." + format)) {
				fileName = fileName + "." + format;
			}
			try {
				if (isFirefox((String) model.get(KEY_USER_AGENT))) {
					fileName = new String(fileName.getBytes(DEFAULT_CHARSET_ENCODING), "ISO-8859-1");
				} else {
					fileName = URLEncoder.encode(fileName, DEFAULT_CHARSET_ENCODING);
				}
			} catch (UnsupportedEncodingException e) {
				logger.debug(e);
			}

			Boolean inline = (Boolean) model.get(KEY_FILE_INLINE);
			String type = (inline != null && inline) ? "inline" : "attachment";
			response.addHeader("Content-Disposition", type + ";filename=" + fileName);
		}

		JRAbstractExporter exporter = null;
		if (FORMAT_HTML.equals(format)) {
			exporter = new JRHtmlExporter();
			exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, URL_JASPER_IMAGE);
			exporter.setParameter(JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, true);
			exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, true);
			exporter.setParameter(JRHtmlExporterParameter.SIZE_UNIT, JRHtmlExporterParameter.SIZE_UNIT_POINT);
			exporter.setParameter(JRHtmlExporterParameter.ZOOM_RATIO, 1f);
			exporter.setParameter(JRHtmlExporterParameter.FRAMES_AS_NESTED_TABLES, false);
			HttpSession session = (HttpSession) model.get(KEY_PRINT_SESSION);
			if (session != null) {
				session.removeAttribute(BaseHttpServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE);
				session.setAttribute(BaseHttpServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);
			}
		} else if (FORMAT_XLS.equals(format)) {
			response.setContentType("application/vnd.ms-excel");
			exporter = new JRXlsExporter();
			exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, true);
			exporter.setParameter(JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED, true);
			exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, true);
			exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, true);
		} else if (FORMAT_PDF.equals(format)) {
			response.setContentType("application/pdf");
			exporter = new JRPdfExporter();
		} else {
			logger.error("不支持该格式的报表输出：" + format);
		}

		exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, DEFAULT_CHARSET_ENCODING);
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
		exporter.exportReport();
	}

	public boolean isBlank(String str) {
		if (str == null || str.trim().length() < 1) {
			return true;
		}
		return false;
	}

	public boolean isIE(HttpServletRequest request) {
		String agent = request.getHeader("USER-AGENT").toUpperCase();
		return (!isBlank(agent) && agent.indexOf("MSIE") > 0);
	}
	/**
     * 判断客户端是否为Firefox
     */
    public boolean isFirefox(String userAgent) {
        if (isBlank(userAgent)) {
            return false;
        }
        String agent = userAgent.toUpperCase();
        return !isBlank(agent) && agent.indexOf("FIREFOX") > 0;
    }

    /**
     * 判断客户端是否为Firefox
     */
    public boolean isFirefox(HttpServletRequest request) {
        return isFirefox(request.getHeader("USER-AGENT"));
    }

}
