package org.tpri.sc.service.com;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tpri.sc.entity.com.ComFile;
import org.tpri.sc.entity.com.TableIndex;
import org.tpri.sc.entity.sys.EnvironmentId;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.manager.com.ComFileManager;
import org.tpri.sc.manager.sys.EnvironmentManager;
import org.tpri.sc.service.sys.EnvironmentService;
import org.tpri.sc.util.DateUtil;
import org.tpri.sc.util.FileUtil;
import org.tpri.sc.util.UUIDUtil;

/**
 * @description 文件服务类
 * @author 易文俊
 * @since 2015-07-10
 */

@Service("ComFileService")
public class ComFileService {

    @Autowired
    ComFileManager comFileManager;

    @Autowired
    EnvironmentManager environmentManager;

    @Autowired
    private EnvironmentService environmentService;

    /**
     * 删除文件
     */
    public boolean deleteFile(UserMc user, String fileId) {
        ComFile file = comFileManager.getFileById(fileId);
        if (file != null) {
            comFileManager.deleteFile(fileId);
            String filesRootPath = (String) environmentService.getEnvironmentValueById(EnvironmentId.FILES_ROOT_PATH);
            FileUtil.deleteFile(filesRootPath + File.separator + file.getFilePath());
        }
        return true;
    }

    /**
     * 保存文件记录信息 (用于保存上传的文件记录)
     */
    public List<String> saveFilesFromJSONArray(String parentId, TableIndex tableIndex, JSONArray files) {
        if (files == null || files.size() == 0) {
            return null;
        }
        List<String> fileIds = new ArrayList<String>();
        for (int i = 0; i < files.size(); i++) {
            JSONObject file = JSONObject.fromObject(files.getString(i));
            ComFile comFile = new ComFile();
            comFile.setTableIndex(tableIndex.getType());
            comFile.setTableName(tableIndex.getDesc());
            String id = UUIDUtil.id();
            comFile.setId(id);
            comFile.setObjectId(parentId);
            comFile.setFilePath(file.getString("filePath"));
            comFile.setFileSize(file.getLong("fileSize"));
            String fileName = file.getString("fileName");
            comFile.setName(fileName);
            comFile.setPostfix(FileUtil.getPostfix(fileName));
            comFile.setFileType(FileUtil.getFileType(fileName));
            comFile.setCreateTime(new Timestamp(System.currentTimeMillis()));
            comFileManager.add(comFile);
            fileIds.add(id);
        }
        return fileIds;
    }

    /**
     * 保存文件记录信息 (用于保存通过webservice上报的文件记录)
     */
    public void saveFilesFromElementList(String parentId, TableIndex tableIndex, List<Element> fileList) {
        if (fileList == null || fileList.size() == 0) {
            return;
        }
        String filesRootPath = (String) environmentService.getEnvironmentValueById(EnvironmentId.FILES_ROOT_PATH);
        for (Element file : fileList) {
            ComFile comFile = new ComFile();
            comFile.setTableIndex(tableIndex.getType());
            comFile.setTableName(tableIndex.getDesc());
            comFile.setId(UUIDUtil.id());
            comFile.setObjectId(parentId);
            comFile.setFileSize(Integer.valueOf(file.elementText("FileSize")));
            String fileName = file.elementText("Name");
            comFile.setName(fileName);
            comFile.setPostfix(file.elementText("Postfix"));
            comFile.setFileType(Integer.valueOf(file.elementText("FileType")));
            comFile.setCreateTime(new Timestamp(System.currentTimeMillis()));

            String filePath = copyFile(fileName, file.elementText("FullPath"), filesRootPath);
            comFile.setFilePath(filePath);
            comFileManager.add(comFile);
        }
    }

    /**
     * 获取文件记录xml对象 (用于通过webservice传输的文件记录)
     */
    public void addFilesElement(Element files, List<ComFile> comFiles) {
        if (comFiles == null || comFiles.size() == 0) {
            return;
        }
        String filesRootPath = (String) environmentService.getEnvironmentValueById(EnvironmentId.FILES_ROOT_PATH);
        for (ComFile comFile : comFiles) {
            Element file = files.addElement("File");
            file.addElement("Id").addText(comFile.getId());
            file.addElement("Name").addText(comFile.getName());
            file.addElement("Postfix").addText(comFile.getPostfix());
            file.addElement("Exist").addText(String.valueOf(comFile.getExist()));
            file.addElement("FileSize").addText(String.valueOf(comFile.getFileSize()));
            file.addElement("FileType").addText(String.valueOf(comFile.getFileType()));
            file.addElement("FullPath").addText(filesRootPath + File.separator + comFile.getFilePath());
        }
    }

    private String copyFile(String fileName, String fullPath, String filesRootPath) {
        if (fullPath == null || fullPath.equals("")) {
            return null;
        }
        String date = DateUtil.getNowDate();
        String uuid = UUIDUtil.id();
        String filePath = FileUtil.DIRECTORY_UPLOAD + File.separator + date + File.separator + uuid + "_" + fileName;
        FileUtil.copyFile(fullPath, filesRootPath + File.separator + filePath);
        return filePath;
    }

    /**
     * 在本系统内容复制文件记录
     */
    public void copyComFile(String parentId, TableIndex tableIndex, ComFile source) {
        if (source == null) {
            return;
        }
        String filesRootPath = (String) environmentService.getEnvironmentValueById(EnvironmentId.FILES_ROOT_PATH);
        ComFile comFile = new ComFile();
        comFile.setTableIndex(tableIndex.getType());
        comFile.setTableName(tableIndex.getDesc());
        comFile.setId(UUIDUtil.id());
        comFile.setObjectId(parentId);
        comFile.setFileSize(source.getFileSize());
        comFile.setName(source.getName());
        comFile.setPostfix(source.getPostfix());
        comFile.setFileType(source.getFileType());
        comFile.setCreateTime(new Timestamp(System.currentTimeMillis()));

        String filePath = copyFile(source.getName(), filesRootPath + File.separator + source.getFilePath(), filesRootPath);
        comFile.setFilePath(filePath);
        comFileManager.add(comFile);
    }

    public List<ComFile> getFileList(int tableIndex, String objectId) {
        return comFileManager.getFileList(tableIndex, objectId);
    }

    /**
     * <B>方法名称：</B><BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月2日
     * @param fileId
     * @return
     */
    public boolean upFile(String fileId) {
        ComFile file = comFileManager.getFileById(fileId);
        if (file != null && file.getOrderNo() != 0) {
            file.setOrderNo(file.getOrderNo() - 1);
            comFileManager.saveOrUpdate(file);
        } else {
            return false;
        }
        return true;
    }

    /**
     * <B>方法名称：</B><BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月2日
     * @param fileId
     * @return
     */
    public boolean downFile(String fileId) {
        ComFile file = comFileManager.getFileById(fileId);
        if (file != null) {
            file.setOrderNo(file.getOrderNo() + 1);
            comFileManager.saveOrUpdate(file);
        } else {
            return false;
        }
        return true;
    }
}
