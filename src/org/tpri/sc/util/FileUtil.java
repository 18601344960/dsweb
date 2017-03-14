package org.tpri.sc.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hwpf.HWPFDocument;
import org.tpri.sc.entity.com.ComFile;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>文件工具类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年4月15日
 */
public class FileUtil {
    private static Logger logger = Logger.getLogger(FileUtil.class);
    /** 文档后缀 */
    private static String[] DOCUMENT_POSTFIX = new String[] { "doc", "pdf", "xsl", "docx", "txt" };
    /** 图片后缀 */
    private static String[] IMAGE_POSTFIX = new String[] { "png", "gif", "jpg", "bmp", "jpeg" };
    /** 视频后缀 */
    private static String[] VIDEO_POSTFIX = new String[] { "mp4", "rmvb", "avi", "mov", "rm", "asf", "swf", "wmv" };

    // 模板文件存放目录
    public static final String DIRECTORY_TEMPLET = "templets";
    // 上传和上报文件目录
    public static final String DIRECTORY_UPLOAD = "upload";

    public static final String UPLOADDIR = "report\\exportTmp\\activityMetting\\";

    public static final String CONTENT_TYPE_ALL = "application/octet-stream";
    public static final String CONTENT_TYPE_DOC = "application/msword";

    /** 获取文件类型 */
    public static int getFileType(String fileName) {
        String postfix = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (postfix != null) {
            for (String imagePostfix : IMAGE_POSTFIX) {
                if (postfix.equalsIgnoreCase(imagePostfix)) {
                    return ComFile.FILETYPE_IMAGE;
                }
            }
            for (String videoPostfix : VIDEO_POSTFIX) {
                if (postfix.equalsIgnoreCase(videoPostfix)) {
                    return ComFile.FILETYPE_VIDEO;
                }
            }
            for (String docPostfix : DOCUMENT_POSTFIX) {
                if (postfix.equalsIgnoreCase(docPostfix)) {
                    return ComFile.FILETYPE_DOCUMENT;
                }
            }
        }
        return ComFile.FILETYPE_ATTACHMENT;
    }

    /** 获取文件后缀 */
    public static String getPostfix(String fileName) {
        String postfix = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (postfix != null) {
            postfix = postfix.toLowerCase();
        }
        return postfix;
    }

    /**
     * 判断文件夹是否存在
     * 
     * @param createWhileNotExist 当文件夹不存在时是否创建该文件夹
     */
    public static boolean pathExists(String path, boolean createWhileNotExist) {
        boolean exists = true;

        File file = new File(path);
        if (file.exists()) {
            exists = true;
        } else {
            exists = false;

            if (createWhileNotExist) {
                file.mkdirs();
            }
        }
        return exists;
    }

    /**
     * 写入文件
     * 
     * @throws IOException
     */
    public static boolean writeFile(String filePath, String fileName, byte[] data, boolean append) throws IOException {
        pathExists(filePath, true);
        File file = new File(filePath + File.separator + fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream out = new FileOutputStream(file, append);
        out.write(data);
        out.close();
        return true;
    }

    /** 拷贝文件 */
    public static boolean copyFile(String sourceFileName, String targetFileName) {
        try {
            targetFileName = targetFileName.replace("\\", File.separator);
            targetFileName = targetFileName.replace("/", File.separator);
            String targetDir = targetFileName.substring(0, targetFileName.lastIndexOf(File.separator));
            pathExists(targetDir, true);

            File sourceFile = new File(sourceFileName);
            File targetFile = new File(targetFileName);
            if (sourceFile.exists()) {
                FileInputStream in = new FileInputStream(sourceFile);
                FileOutputStream out = new FileOutputStream(targetFile);

                byte[] bytes = new byte[10000];
                int c;
                while ((c = in.read(bytes)) != -1) {
                    out.write(bytes, 0, c);
                }
                in.close();
                out.close();
                return true;
            } else {
                logger.debug("文件不存在：" + sourceFileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /** 拷贝文件 */
    public static boolean copyFile(String sourceFileName, ServletOutputStream outputStream) {
        try {
            File sourceFile = new File(sourceFileName);
            if (sourceFile.exists()) {
                FileInputStream in = new FileInputStream(sourceFile);
                byte[] bytes = new byte[10000];
                int c;
                while ((c = in.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, c);
                }
                in.close();
                return true;
            } else {
                logger.debug("文件不存在：" + sourceFileName);
            }
        } catch (Exception e) {
            logger.debug(e);
            e.printStackTrace();
        }
        return false;
    }

    /** 拷贝文件 */
    public static boolean copyFile(InputStream sourceFile, File file) {
        try {
            FileOutputStream out = new FileOutputStream(file);
            byte[] bytes = new byte[10000];
            int c;
            while ((c = sourceFile.read(bytes)) != -1) {
                out.write(bytes, 0, c);
            }
            sourceFile.close();
            out.close();
            return true;
        } catch (Exception e) {
            logger.debug(e);
            e.printStackTrace();
        }
        return false;
    }

    /** 读取文件内容到字符串 */
    public static String readFile(File file) {
        InputStreamReader inputReader = null;
        BufferedReader bufferReader = null;
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            inputReader = new InputStreamReader(inputStream);
            bufferReader = new BufferedReader(inputReader);

            // 读取一行
            String line = null;
            StringBuffer strBuffer = new StringBuffer();

            while ((line = bufferReader.readLine()) != null) {
                strBuffer.append(line);
            }
            return strBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                bufferReader.close();
                inputReader.close();
            } catch (IOException e) {
                logger.debug(e);
                e.printStackTrace();
            }
        }
        return null;
    }

    /** 读取文件内容到二进制 */
    public static byte[] readFileToByte(String filePath) {
        File file = new File(filePath);
        return readFileToByte(file);
    }

    /** 读取文件内容到二进制 */
    public static byte[] readFileToByte(File file) {
        if (!file.exists()) {
            return null;
        }
        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) {
            logger.debug("文件太大..." + file.getAbsolutePath());
            return null;
        }
        FileInputStream fi;
        try {
            fi = new FileInputStream(file);
            byte[] buffer = new byte[(int) fileSize];
            int offset = 0;
            int numRead = 0;
            while (offset < buffer.length && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
                offset += numRead;
            }
            // 确保所有数据均被读取
            if (offset != buffer.length) {
            }
            fi.close();
            return buffer;
        } catch (FileNotFoundException e) {
            logger.debug(e);
            e.printStackTrace();
        } catch (IOException e) {
            logger.debug(e);
            e.printStackTrace();
        }
        return null;

    }

    /** 刪除文件 */
    public static boolean deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
            return true;
        }
        return false;
    }

    /**
     * 删除文件夹 param folderPath 文件夹完整绝对路径
     * */
    public static void deleteFolder(String folderPath) {
        try {
            deleteAllFile(folderPath); // 删除完里面所有内容
            File folder = new File(folderPath);
            folder.delete(); // 删除空文件夹
        } catch (Exception e) {
            logger.debug(e);
            e.printStackTrace();
        }
    }

    /**
     * 删除指定文件夹下所有文件 param folderPath 文件夹完整绝对路径
     * */
    public static boolean deleteAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                deleteAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
                deleteFolder(path + "/" + tempList[i]);// 再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    /** 将文件大小转换成可显示的b,Kb,Mb,Gb */
    public static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else
            return String.format("%d B", size);
    }

    /** 获取应用路径 */
    public static String getContextPath(HttpServletRequest request) {
        String contextPath = request.getSession().getServletContext().getRealPath("/");
        contextPath = contextPath.replace("\\", "/");
        if (contextPath.endsWith("/")) {
            contextPath = contextPath.substring(0, contextPath.length() - 1);
        }
        return contextPath;
    }

    /**
     * APDPlat中的重要打包机制 将jar文件中的某个文件夹里面的内容复制到某个文件夹
     * 
     * @param jar 包含静态资源的jar包
     * @param subDir jar中包含待复制静态资源的文件夹名称
     * @param loc 静态资源复制到的目标文件夹
     * @param force 目标静态资源存在的时候是否强制覆盖
     */
    public static void unZip(String jar, String subDir, String loc, boolean force) {
        try {
            File base = new File(loc);
            if (!base.exists()) {
                base.mkdirs();
            }

            ZipFile zip = new ZipFile(new File(jar));
            Enumeration<? extends ZipEntry> entrys = zip.entries();
            while (entrys.hasMoreElements()) {
                ZipEntry entry = entrys.nextElement();
                String name = entry.getName();
                if (!name.startsWith(subDir)) {
                    continue;
                }
                // 去掉subDir
                name = name.replace(subDir, "").trim();
                if (name.length() < 2) {
                    logger.debug(name + " 长度 < 2");
                    continue;
                }
                if (entry.isDirectory()) {
                    File dir = new File(base, name);
                    if (!dir.exists()) {
                        dir.mkdirs();
                        logger.debug("创建目录");
                    } else {
                        logger.debug("目录已经存在");
                    }
                    logger.debug(name + " 是目录");
                } else {
                    File file = new File(base, name);
                    if (file.exists() && force) {
                        file.delete();
                    }
                    if (!file.exists()) {
                        InputStream in = zip.getInputStream(entry);
                        copyFile(in, file);
                        logger.debug("创建文件");
                    } else {
                        logger.debug("文件已经存在");
                    }
                    logger.debug(name + " 不是目录");
                }
            }
        } catch (ZipException ex) {
            logger.error("文件解压失败", ex);
        } catch (IOException ex) {
            logger.error("文件操作失败", ex);
        }
    }

    /**
     * 创建ZIP文件
     * 
     * @param sourcePath 文件或文件夹路径
     * @param zipPath 生成的zip文件存在路径（包括文件名）
     */
    public static void createZip(String sourcePath, String zipPath) {
        sourcePath = sourcePath.replace("/", "\\");
        zipPath = zipPath.replace("/", "\\");
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            fos = new FileOutputStream(zipPath);
            zos = new ZipOutputStream(fos);
            writeZip(new File(sourcePath), "", zos);
        } catch (FileNotFoundException e) {
            logger.error("创建ZIP文件失败", e);
        } finally {
            try {
                if (zos != null) {
                    zos.close();
                }
            } catch (IOException e) {
                logger.error("创建ZIP文件失败", e);
            }

        }
    }

    private static void writeZip(File file, String parentPath, ZipOutputStream zos) {
        if (file.exists()) {
            if (file.isDirectory()) {// 处理文件夹
                parentPath += file.getName() + File.separator;
                File[] files = file.listFiles();
                for (File f : files) {
                    writeZip(f, parentPath, zos);
                }
            } else {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(file);
                    ZipEntry ze = new ZipEntry(parentPath + file.getName());
                    zos.putNextEntry(ze);
                    byte[] content = new byte[1024];
                    int len;
                    while ((len = fis.read(content)) != -1) {
                        zos.write(content, 0, len);
                        zos.flush();
                    }

                } catch (FileNotFoundException e) {
                    logger.error("创建ZIP文件失败", e);
                } catch (IOException e) {
                    logger.error("创建ZIP文件失败", e);
                } finally {
                    try {
                        if (fis != null) {
                            fis.close();
                        }
                    } catch (IOException e) {
                        logger.error("创建ZIP文件失败", e);
                    }
                }
            }
        }
    }

    /**
     * 解压到指定目录
     * 
     * @param zipPath
     * @param descDir
     */
    public static void unZipFiles(String zipPath, String descDir) throws IOException {
        unZipFiles(new File(zipPath), descDir);
    }

    /**
     * 解压文件到指定目录
     * 
     * @param zipFile
     * @param descDir
     */
    @SuppressWarnings("rawtypes")
    public static void unZipFiles(File zipFile, String descDir) throws IOException {
        File pathFile = new File(descDir);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        if (!descDir.endsWith("\\") && !descDir.endsWith("/")) {
            descDir = descDir + File.separator;
        }
        ZipFile zip = new ZipFile(zipFile);
        logger.debug("******************开始解压********************");
        for (Enumeration entries = zip.entries(); entries.hasMoreElements();) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            String zipEntryName = entry.getName();
            InputStream in = zip.getInputStream(entry);
            String outPath = (descDir + zipEntryName).replace("\\", "/");
            // 判断路径是否存在,不存在则创建文件路径
            File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
            if (!file.exists()) {
                file.mkdirs();
            }
            // 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
            if (new File(outPath).isDirectory()) {
                continue;
            }
            // 输出文件路径信息
            System.out.println(outPath);

            OutputStream out = new FileOutputStream(outPath);
            byte[] buf1 = new byte[1024];
            int len;
            while ((len = in.read(buf1)) > 0) {
                out.write(buf1, 0, len);
            }
            in.close();
            out.close();
        }
        logger.debug("******************解压完毕********************");
    }

    /**
     * 下载指定文件
     */
    public static void downloadFile(HttpServletRequest request, HttpServletResponse response, String contentType, String filePath, String fileName) throws Exception {

        String userAgent = request.getHeader("User-Agent");
        byte[] bytes = userAgent.contains("MSIE") ? fileName.getBytes() : fileName.getBytes("UTF-8"); // name.getBytes("UTF-8")处理safari的乱码问题
        fileName = new String(bytes, "ISO-8859-1"); // 各浏览器基本都支持ISO编码

        File file = new File(filePath);
        InputStream fis = new BufferedInputStream(new FileInputStream(filePath));
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        fis.close();

        response.reset();
        request.setCharacterEncoding("UTF-8");
        response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", fileName));
        response.setHeader("Content-Length", String.valueOf(file.length()));
        OutputStream fos = new BufferedOutputStream(response.getOutputStream());
        response.setContentType(contentType);
        fos.write(buffer);
        fos.flush();
        fos.close();
    }

    /**
     * 下载doc模板文件
     */
    public static void downloadFile(HttpServletRequest request, HttpServletResponse response, String contentType, HWPFDocument doc, String fileName) throws Exception {

        String userAgent = request.getHeader("User-Agent");
        byte[] bytes = userAgent.contains("MSIE") ? fileName.getBytes() : fileName.getBytes("UTF-8"); // name.getBytes("UTF-8")处理safari的乱码问题
        fileName = new String(bytes, "ISO-8859-1"); // 各浏览器基本都支持ISO编码

        response.reset();
        request.setCharacterEncoding("UTF-8");
        response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", fileName));
        OutputStream fos = new BufferedOutputStream(response.getOutputStream());
        response.setContentType(contentType);
        doc.write(fos);
        fos.flush();
        fos.close();
    }

    public static void main(String[] args) throws IOException {
        unZipFiles("D:\\AppData\\workspace\\eclipse\\tpri\\djxt\\zbsc\\WebContent\\report\\importTmp\\2015年度上报数据.zip",
                "D:\\AppData\\workspace\\eclipse\\tpri\\djxt\\zbsc\\WebContent\\report\\importTmp\\");
    }
}
