package org.tpri.sc.service.com;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.tpri.sc.entity.com.ComFile;
import org.tpri.sc.entity.com.TableIndex;
import org.tpri.sc.entity.obt.Conference;
import org.tpri.sc.entity.obt.ConferenceLabel;
import org.tpri.sc.entity.org.CCParty;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.manager.com.ComFileManager;
import org.tpri.sc.manager.obt.ConferenceCategoryManager;
import org.tpri.sc.manager.obt.ConferenceLabelManager;
import org.tpri.sc.manager.obt.ConferenceManager;
import org.tpri.sc.manager.org.CCPartyManager;
import org.tpri.sc.manager.sys.EnvironmentManager;
import org.tpri.sc.util.DateUtil;
import org.tpri.sc.util.FileUtil;
import org.tpri.sc.util.XmlSupport;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>上报服务类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年5月10日
 */

@Service("ReportService")
public class ReportService {

    public Logger logger = Logger.getLogger(ReportService.class);

    @Autowired
    ConferenceManager conferenceManager;
    @Autowired
    ConferenceLabelManager conferenceCategoryManager;
    @Autowired
    ConferenceLabelManager conferencelabelManager;
    @Autowired
    EnvironmentManager environmentManager;
    @Autowired
    CCPartyManager cCPartyManager;
    @Autowired
    ConferenceCategoryManager categoryManager;
    @Autowired
    ComFileManager comFileManager;

    /**
     * 导出上报数据
     */
    public String reportExport(UserMc user, Map<String, Object> param, String exportPath, String filesRootPath) {
        // 导出之前清空导出目录
        FileUtil.deleteAllFile(exportPath);

        String beginTimeStr = (String) param.get("beginTime");
        String endTimeStr = (String) param.get("endTime");
        String ccpartyId = (String) param.get("ccpartyId");
        CCParty ccpartyMc = cCPartyManager.getCCPartyFromMc(ccpartyId);

        Timestamp beginTime = DateUtil.str2Timestamp(beginTimeStr + " 00:00:00", DateUtil.YYYYMMDDHHMMSS_FORMAT);
        Timestamp endTime = DateUtil.str2Timestamp(endTimeStr + " 23:59:59", DateUtil.YYYYMMDDHHMMSS_FORMAT);

        String categoryIds = (String) param.get("categoryIds");

        String categoryIdsParam = "";
        if (categoryIds != null) {
            String[] categoryIdArray = categoryIds.trim().split(",");
            for (String categoryId : categoryIdArray) {
                if (categoryId != null && !categoryId.equals("")) {
                    categoryIdsParam += "'" + categoryId + "',";
                }
            }
            if (categoryIdsParam.length() > 0) {
                categoryIdsParam = categoryIdsParam.substring(0, categoryIdsParam.length() - 1);
            }
        }
        List<Conference> conferences = conferenceManager.getConferenceList(categoryIdsParam, ccpartyId, beginTime, endTime);

        String now = DateUtil.getNowDate();
        String nowPath = exportPath + now;
        String exportTmpPath = nowPath + "/" + ccpartyId;
        FileUtil.pathExists(exportTmpPath, true);
        exportCcparty(ccpartyId, exportTmpPath);
        exportConference(conferences, exportTmpPath, filesRootPath);

        String zipName = ccpartyMc.getName() + "(" + beginTimeStr + "到" + endTimeStr + ")上报数据.zip";
        String zipPath = exportPath + zipName;
        FileUtil.createZip(exportTmpPath, zipPath);
        FileUtil.deleteFolder(nowPath);
        return zipPath;
    }

    /** 导出本级和下级党组织信息 */
    private boolean exportCcparty(String ccpartyId, String exportTmpPath) {
        Document document = XmlSupport.buildDocumentByRootName("ccparties");
        Element rootElement = document.getRootElement();
        exportCcpartyInfo(ccpartyId, rootElement);
        XmlSupport.saveXmlFile(document, exportTmpPath + File.separator + "ccparty.xml", "UTF-8");
        return true;
    }

    /** 导出级党组织信息 */
    private boolean exportCcpartyInfo(String ccpartyId, Element rootElement) {
        CCParty ccparty = cCPartyManager.getCCPartyById(ccpartyId);
        if (ccparty != null) {
            Element element = rootElement.addElement("ccparty");
            element.addElement("id").setText(ccparty.getId());
            element.addElement("name").setText(ccparty.getName());
            element.addElement("parentId").setText(ccparty.getParentId());
            element.addElement("type").setText(String.valueOf(ccparty.getType()));
        }
        List<CCParty> ccparties = cCPartyManager.getCCPartyListByParentId(ccpartyId, CCParty.STATUS_0);
        if (ccparties != null && ccparties.size() > 0) {
            for (CCParty ccpartyChild : ccparties) {
                exportCcpartyInfo(ccpartyChild.getId(), rootElement);
            }
        }
        return true;
    }

    /** 导出文章信息 */
    private boolean exportConference(List<Conference> conferences, String exportTmpPath, String filesRootPath) {
        if (conferences != null) {
            for (Conference conference : conferences) {
                String acticlePath = exportTmpPath + File.separator + "acticles" + File.separator + conference.getId();
                FileUtil.pathExists(acticlePath, true);
                Document document = XmlSupport.buildDocumentByRootName("conference");
                Element rootElement = document.getRootElement();
                Element metaElement = rootElement.addElement("metadata");
                metaElement.addElement("id").setText(conference.getId());
                metaElement.addElement("name").setText(conference.getName());
                metaElement.addElement("ccparty").setText(conference.getCcpartyId());
                metaElement.addElement("content").setText(conference.getContent());
                metaElement.addElement("hits").setText(String.valueOf(conference.getHits()));
                metaElement.addElement("sourceType").setText(String.valueOf(conference.getSourceType()));
                metaElement.addElement("sourceId").setText(conference.getSourceId());
                metaElement.addElement("sourceName").setText(conference.getSourceName());

                List<ConferenceLabel> conferenceCategories = conferenceCategoryManager.getConferenceLabelsByConferenceId(conference.getId(), null);
                if (conferenceCategories != null && conferenceCategories.size() > 0) {
                    Element conferenceCategoriesElement = rootElement.addElement("conferenceCategories");
                    for (ConferenceLabel conferenceCategory : conferenceCategories) {
                        Element element = conferenceCategoriesElement.addElement("conferenceCategory");
                        element.addElement("id").setText(conferenceCategory.getId());
                        element.addElement("conferenceId").setText(conferenceCategory.getConferenceId());
                        element.addElement("categoryId").setText(conferenceCategory.getCategoryId());
                    }
                }

                List<ComFile> comFiles = comFileManager.getFileList(TableIndex.TABLE_OBT_CONFERENCE.getType(), conference.getId());
                if (comFiles != null && comFiles.size() > 0) {
                    String filesPath = acticlePath + File.separator + "files";
                    FileUtil.pathExists(filesPath, true);
                    Element filesElement = rootElement.addElement("files");
                    for (ComFile comFile : comFiles) {
                        Element fileElement = filesElement.addElement("file");
                        String filePath = comFile.getFilePath();
                        fileElement.addElement("id").setText(comFile.getId());
                        fileElement.addElement("conferenceId").setText(comFile.getId());
                        fileElement.addElement("name").setText(comFile.getName());
                        fileElement.addElement("fileType").setText(String.valueOf(comFile.getFileType()));
                        fileElement.addElement("fileSize").setText(String.valueOf(comFile.getFileSize()));
                        fileElement.addElement("postFix").setText(comFile.getPostfix());
                        if (filePath != null) {
                            fileElement.addElement("filePath").setText(filePath);
                        }
                        if (filePath != null) {
                            String sourcePath = filesRootPath + File.separator + filePath;
                            String targetPath = filesPath + File.separator + comFile.getName();
                            if (FileUtil.pathExists(sourcePath, false)) {
                                FileUtil.copyFile(sourcePath, targetPath);
                            }
                        }
                    }
                }
                XmlSupport.saveXmlFile(document, acticlePath + File.separator + "conference.xml", "UTF-8");
            }
        }
        return true;
    }

    /**
     * 上传上报数据包
     * 
     * @throws IOException
     */
    public boolean reportUpload(String uploadPath, MultipartFile mf) throws IOException {
        // 上传之前清空上传目录
        FileUtil.deleteAllFile(uploadPath);
        String fileName = mf.getOriginalFilename();
        boolean result = FileUtil.writeFile(uploadPath, fileName, mf.getBytes(), false);
        return result;
    }

    /**
     * 导入上报数据
     * 
     * @throws IOException
     */
    public boolean importReport(String basePath, String filePath, String filesRootPath) throws IOException {
        FileUtil.unZipFiles(filePath, basePath);
        importData(basePath, filesRootPath);
        FileUtil.deleteFile(filePath);
        return true;
    }

    private boolean importData(String basePath, String filesRootPath) {
        File baseDirectory = new File(basePath);
        if (baseDirectory.exists() && baseDirectory.isDirectory()) {
            if (baseDirectory.listFiles() != null && baseDirectory.listFiles().length > 0) {
                File rootDirectory = baseDirectory.listFiles()[0];
                if (rootDirectory.exists() && rootDirectory.isDirectory()) {
                    importCcparty(rootDirectory);
                    importConferences(rootDirectory, filesRootPath);
                }
            }
        }
        return true;
    }

    /** 导入党组织 */
    private boolean importCcparty(File rootDirectory) {
        File[] files = rootDirectory.listFiles(new SingleFileFilter("ccparty.xml"));
        if (files != null && files.length > 0) {
            File ccpartyFile = files[0];
            String partyXML = FileUtil.readFile(ccpartyFile);
            Element ccpartiesElement = XmlSupport.getRootElement(partyXML);
            List<Element> ccpartyList = ccpartiesElement.elements("ccparty");
            for (Element element : ccpartyList) {
                String id = element.elementText("id");
                CCParty ccpartyMc = cCPartyManager.getCCPartyFromMc(id);
                if (ccpartyMc == null) {
                    CCParty party = new CCParty();
                    party.setId(id);
                    party.setName(element.elementText("name"));
                    party.setParentId(element.elementText("parentId"));
                    party.setType(element.elementText("type"));
                    cCPartyManager.add(party);
                    logger.debug("导入党组织ID：[" + party.getId() + "]名称：[" + party.getName() + "]");
                } else {
                    logger.debug("党组织ID：[" + ccpartyMc.getId() + "]名称：[" + ccpartyMc.getName() + "]已经存在，无需导入");
                }
            }
        }
        return true;
    }

    /** 导入根目录下的所有文章 */
    private boolean importConferences(File rootDirectory, String filesRootPath) {
        File[] files = rootDirectory.listFiles(new SingleFileFilter("acticles"));
        if (files != null && files.length > 0) {
            File acticlesPath = files[0];
            if (acticlesPath.exists() && acticlesPath.isDirectory()) {
                File[] acticleDirectories = acticlesPath.listFiles();
                for (File acticleDirectory : acticleDirectories) {
                    importConference(acticleDirectory, filesRootPath);
                }
            }
        }
        return true;
    }

    /** 导入某篇文章 */
    private boolean importConference(File acticleDirectory, String filesRootPath) {
        if (acticleDirectory.exists() && acticleDirectory.isDirectory()) {
            String conferenceId = acticleDirectory.getName();
            Conference conference = conferenceManager.getConferenceById(conferenceId);
            String conferencePath = acticleDirectory.getAbsolutePath();
            if (conference == null) {
                File[] xmlFiles = acticleDirectory.listFiles(new SingleFileFilter("conference.xml"));
                if (xmlFiles != null && xmlFiles.length > 0) {
                    File conferenceXMLFile = xmlFiles[0];
                    String conferenceXML = FileUtil.readFile(conferenceXMLFile);
                    Element conferenceElement = XmlSupport.getRootElement(conferenceXML);
                    importConferenceMetaData(conferenceElement);
                    importConferenceAttachments(conferenceElement, conferencePath, filesRootPath);
                    importConferenceCategory(conferenceElement);
                }
            } else {
                logger.debug("文章ID：[" + conference.getId() + "]名称：[" + conference.getName() + "]已经存在，无需导入");
            }
        }
        return true;
    }

    /** 导入文章元数据 */
    private void importConferenceMetaData(Element conferenceElement) {
        Element metadataElement = conferenceElement.element("metadata");
        String conferenceId = metadataElement.elementText("id");
        String name = metadataElement.elementText("name");
        logger.debug("导入文章ID：[" + conferenceId + "]名称：[" + name + "]");
        Conference conference = new Conference();
        conference.setId(conferenceId);
        conference.setName(name);
        conference.setHits(Integer.valueOf(metadataElement.elementText("hits")));
        conference.setContent(metadataElement.elementText("content"));
        conference.setSourceType(Integer.valueOf(metadataElement.elementText("sourceType")));
        conference.setSourceId(metadataElement.elementText("sourceId"));
        conference.setSourceName(metadataElement.elementText("sourceName"));
        conference.setCreateUserId(metadataElement.elementText("createId"));
        conference.setStatus(Integer.valueOf(metadataElement.elementText("status")));
        conference.setCreateTime(DateUtil.str2Timestamp(metadataElement.elementText("createTime")));
        conferenceManager.add(conference);
    }

    /** 导入文章附件 */
    private void importConferenceAttachments(Element conferenceElement, String conferencePath, String filesRootPath) {
        Element filesElement = conferenceElement.element("files");
        List<Element> fileList = filesElement.elements("file");
        for (Element element : fileList) {
            String fileId = element.elementText("id");
            String fileName = element.elementText("name");
            logger.debug("导入文章附件ID：[" + fileId + "]名称：[" + fileName + "]");
            ComFile comFile = comFileManager.getFileById(fileId);
            if (comFile == null) {
                comFile = new ComFile();
                String filePath = element.elementText("filePath");
                comFile.setId(fileId);
                comFile.setObjectId(element.elementText("conferenceId"));
                comFile.setName(fileName);
                comFile.setPostfix(element.elementText("postfix"));
                comFile.setStatus(Integer.valueOf(element.elementText("status")));
                comFile.setFileType(Integer.valueOf(element.elementText("fileType")));
                comFile.setFileSize(Integer.valueOf(element.elementText("fileSize")));
                comFile.setFilePath(filePath);
                comFileManager.add(comFile);
                String sourceFilePath = conferencePath + File.separator + "files" + File.separator + fileName;
                FileUtil.copyFile(sourceFilePath, filesRootPath + File.separator + filePath);
            }
        }
    }

    /** 导入文章栏目关联 */
    private boolean importConferenceCategory(Element conferenceElement) {
        Element conferenceCategoriesElement = conferenceElement.element("conferenceCategories");
        List<Element> conferenceCategoryList = conferenceCategoriesElement.elements("conferenceCategory");
        for (Element element : conferenceCategoryList) {
            String id = element.elementText("id");
            String conferenceId = element.elementText("conferenceId");
            String categoryId = element.elementText("categoryId");
            ConferenceLabel conferenceLabel = conferencelabelManager.getConferenceLabelById(id);
            if (conferenceLabel == null) {
                conferenceLabel = new ConferenceLabel();
                conferenceLabel.setId(id);
                conferenceLabel.setConferenceId(conferenceId);
                conferenceLabel.setCategoryId(categoryId);
                conferenceCategoryManager.add(conferenceLabel);
                logger.debug("导入文章栏目关联ID：[" + conferenceLabel.getId() + "]文章ID：[" + conferenceLabel.getConferenceId() + "]标签ID：[" + conferenceLabel.getCategoryId() + "]，导入成功");
            } else {
                logger.debug("文章栏目关联ID：[" + conferenceLabel.getId() + "]文章ID：[" + conferenceLabel.getConferenceId() + "]标签ID：[" + conferenceLabel.getCategoryId() + "]已经存在，无需导入");
            }
        }
        return true;
    }

    /** 单个文件过滤器 */
    class SingleFileFilter implements FilenameFilter {
        private String fileName = null;

        public SingleFileFilter(String fileName) {
            this.fileName = fileName;
        }

        @Override
        public boolean accept(File dir, String name) {
            if (name.equals(fileName)) {
                return true;
            }
            return false;
        }
    }
}
