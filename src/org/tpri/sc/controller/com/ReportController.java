package org.tpri.sc.controller.com;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.tpri.sc.controller.BaseController;
import org.tpri.sc.entity.sys.EnvironmentId;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.service.com.ReportService;
import org.tpri.sc.util.FileUtil;

/***
 * 
 * <B>系统名称：</B>上报控制器<BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B><BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年5月12日
 */
@Controller
@RequestMapping("/zbsc")
public class ReportController extends BaseController {

    @Resource(name = "ReportService")
    private ReportService reportService;

    /**
     * 导出上报数据
     */
    @RequestMapping("reportExport")
    @ResponseBody
    public Map<String, Object> reportExport(HttpServletRequest request, HttpServletResponse response) {
        logger.debug(this.getClass() + " reportExport begin");

        Map<String, Object> param = new HashMap<String, Object>();
        String ccpartyId = getString(request, "ccpartyId");
        param.put("ccpartyId", getString(request, "ccpartyId"));
        param.put("categoryIds", getString(request, "categoryIds"));
        param.put("beginTime", getString(request, "beginTime"));
        param.put("endTime", getString(request, "endTime"));

        String exportPathPre = FileUtil.getContextPath(request);
        String exportPath = exportPathPre + "/tmp/report/export/" + ccpartyId + "/";
        String filesRootPath = (String) getEnvironmentValueById(EnvironmentId.FILES_ROOT_PATH);
        UserMc user = loadUserMc(request);
        String zipDownloadPath = reportService.reportExport(user, param, exportPath, filesRootPath);
        Map<String, Object> ret = new HashMap<String, Object>();
        if (zipDownloadPath == null) {
            ret.put("success", false);
            ret.put("msg", "导出失败");
        }
        ret.put("success", true);
        ret.put("msg", "导出成功");
        ret.put("zipPath", zipDownloadPath.substring(exportPathPre.length() + 1));
        logger.debug(this.getClass() + " reportExport end");
        return ret;
    }

    /**
     * 导入上报数据包
     * 
     * @throws IOException
     */
    @RequestMapping("reportImport")
    @ResponseBody
    public Map<String, Object> reportImport(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.debug(this.getClass() + " reportImport begin");
        String ccpartyId = getString(request, "ccpartyId");

        MultipartFile mf = getUploadFile(request);
        Map<String, Object> ret = new HashMap<String, Object>();
        if (mf != null) {
            String uploadPath = FileUtil.getContextPath(request) + "/tmp/report/import/";

            boolean result = reportService.reportUpload(uploadPath, mf);
            if (result) {
                String filePath = uploadPath + mf.getOriginalFilename();
                String filesRootPath = (String) getEnvironmentValueById(EnvironmentId.FILES_ROOT_PATH);
                result = reportService.importReport(uploadPath, filePath, filesRootPath);
            }
            ret.put("success", result);
            if (result) {
                ret.put("msg", "导入成功");
            } else {
                ret.put("msg", "导入失败");
            }
        }else{
            ret.put("success", false);
            ret.put("msg", "上传的文件未找到，导入失败");
        }
        logger.debug(this.getClass() + " reportImport end");
        return ret;
    }
}
