package org.tpri.sc.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.tpri.sc.entity.sys.EnvironmentId;
import org.tpri.sc.service.FileService;
import org.tpri.sc.service.sys.EnvironmentService;
import org.tpri.sc.util.FileUtil;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>文件控制器<BR>
 * <B>概要说明：</B><BR>
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年5月3日
 */
@Controller
@RequestMapping("/file")
public class FileController extends BaseController {

	@Autowired
	private FileService fileService;

	@Autowired
	private EnvironmentService environmentService;

	/**
	 * 上传文件
	 * 
	 * @throws IOException
	 */
	@RequestMapping("uploadFile")
	@ResponseBody
	public String uploadFile(HttpServletRequest request) {
		logger.debug("FileController uploadFile begin");
		MultipartFile mf = getUploadFile(request);
		logger.debug("OriginalFilename:" + mf.getOriginalFilename());
		logger.debug("Name:" + mf.getName());
		logger.debug("Size:" + mf.getSize());
		String filesRootPath = (String) getEnvironmentValueById(EnvironmentId.FILES_ROOT_PATH);
		
		String filePath = null;
        try {
            filePath = fileService.addUploadFileData(mf, filesRootPath);
        } catch (IOException e) {
            logger.debug("上传文件出错"+e.getStackTrace());
            e.printStackTrace();
            return filePath;
        }

		logger.debug("FileController uploadFile end");
		return filePath;
	}

	/**
	 * 下载文件
	 * 
	 * @throws IOException
	 */
	@RequestMapping("getFileData")
	@ResponseBody
	public synchronized void getFileData(HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.debug("FileController getFileData begin");
		String filePath = getString(request, "filePath");
		String filesRootPath = (String) getEnvironmentValueById(EnvironmentId.FILES_ROOT_PATH);
		request.setCharacterEncoding("UTF-8");
		String filename = filePath.substring(filePath.lastIndexOf("\\"));
        String userAgent = request.getHeader("User-Agent");  
        byte[] bytes = userAgent.contains("MSIE") ? filename.getBytes() : filename.getBytes("UTF-8"); // name.getBytes("UTF-8")处理safari的乱码问题  
        filename = new String(bytes, "ISO-8859-1"); // 各浏览器基本都支持ISO编码 
		response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", filename));
		FileUtil.copyFile(filesRootPath + File.separator + filePath, response.getOutputStream());
		logger.debug("FileController getFileData end");
	}
}
