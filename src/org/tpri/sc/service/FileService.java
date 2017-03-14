package org.tpri.sc.service;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.tpri.sc.manager.com.ComFileManager;
import org.tpri.sc.service.sys.EnvironmentService;
import org.tpri.sc.util.DateUtil;
import org.tpri.sc.util.FileUtil;
import org.tpri.sc.util.UUIDUtil;

/**
 * @description 文件服务类
 * @author 易文俊
 * @since 2015-05-04
 */

@Service("FileService")
public class FileService {
    public Logger logger = Logger.getLogger(FileService.class);

    @Autowired
    private EnvironmentService environmentService;

    @Autowired
    ComFileManager comFileManager;

    public String addUploadFileData(MultipartFile mf, String filesRootPath) throws IOException {
        String fileName = mf.getOriginalFilename();
        String date = DateUtil.getNowDate();
        String uuid = UUIDUtil.id();
        String datePath=date.substring(0, 4)+ File.separator + date.substring(4, 6)+ File.separator + date.substring(6, 8);
        String filePath = FileUtil.DIRECTORY_UPLOAD + File.separator + datePath + File.separator + uuid + "_" + fileName;
        FileUtil.writeFile(filesRootPath + File.separator + FileUtil.DIRECTORY_UPLOAD + File.separator + datePath, uuid + "_" + fileName, mf.getBytes(), false);
        return filePath;
    }
}
