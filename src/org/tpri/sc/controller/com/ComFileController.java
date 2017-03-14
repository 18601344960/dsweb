package org.tpri.sc.controller.com;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tpri.sc.controller.BaseController;
import org.tpri.sc.entity.com.ComFile;
import org.tpri.sc.service.com.ComFileService;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>文件控制器<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年5月5日
 */
@Controller
@RequestMapping("/com")
public class ComFileController extends BaseController {

    @Autowired
    ComFileService comFileService;

    /**
     * <B>方法名称：</B>获取某表的某条记录的附件列表<BR>
     * <B>概要说明：</B><BR>
     * @author 易文俊
     * @since 2016年7月2日 	
     * @param request
     * @return
     */
    @RequestMapping("getFileList")
    @ResponseBody
    public Map<String, Object> getFileList(HttpServletRequest request) {
        logger.debug(this.getClass() + " getFileList begin");
        int tableIndex = getInt(request, "tableIndex", 10000);
        String objectId = getString(request, "objectId");
        List<ComFile> comFiles = comFileService.getFileList(tableIndex, objectId);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("rows", comFiles);
        logger.debug(this.getClass() + " getFileList begin");
        return ret;
    }

    /**
     * <B>方法名称：</B>刪除文件<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月5日
     * @param request
     * @return
     */
    @RequestMapping("deleteFile")
    @ResponseBody
    public Map<String, Object> deleteFile(HttpServletRequest request) {
        logger.debug(this.getClass() + " deleteFile begin");
        String fileId = getString(request, "fileId");
        comFileService.deleteFile(loadUserMc(request), fileId);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", true);
        ret.put("msg", "删除成功");
        logger.debug(this.getClass() + " deleteFile begin");
        return ret;
    }

    /**
     * <B>方法名称：</B>上移文件顺序<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月2日
     * @param request
     * @return
     */
    @RequestMapping("upFile")
    @ResponseBody
    public Map<String, Object> upFile(HttpServletRequest request) {
        logger.debug(this.getClass() + " upFile begin");
        String fileId = getString(request, "fileId");
        boolean result = comFileService.upFile(fileId);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", result);
        logger.debug(this.getClass() + " upFile begin");
        return ret;
    }

    /**
     * <B>方法名称：</B>下移文件顺序<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月2日
     * @param request
     * @return
     */
    @RequestMapping("downFile")
    @ResponseBody
    public Map<String, Object> downFile(HttpServletRequest request) {
        logger.debug(this.getClass() + " downFile begin");
        String fileId = getString(request, "fileId");
        boolean result = comFileService.downFile(fileId);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", result);
        logger.debug(this.getClass() + " downFile begin");
        return ret;
    }
}
