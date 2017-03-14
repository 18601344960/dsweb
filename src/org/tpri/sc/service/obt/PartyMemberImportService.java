package org.tpri.sc.service.obt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Synchronization;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.entity.obt.DevelopmentProcedure;
import org.tpri.sc.entity.obt.PartyMember;
import org.tpri.sc.entity.sys.Code;
import org.tpri.sc.entity.sys.EnvironmentId;
import org.tpri.sc.entity.uam.Role;
import org.tpri.sc.entity.uam.User;
import org.tpri.sc.manager.obt.DevelopmentProcedureManager;
import org.tpri.sc.manager.obt.PartyMemberManager;
import org.tpri.sc.manager.org.OrganizationManager;
import org.tpri.sc.manager.sys.CodeManager;
import org.tpri.sc.manager.uam.RoleManager;
import org.tpri.sc.manager.uam.UserManager;
import org.tpri.sc.service.com.ComFileService;
import org.tpri.sc.service.sys.EnvironmentService;
import org.tpri.sc.util.BaseConfig;
import org.tpri.sc.util.DateUtil;
import org.tpri.sc.util.ExcelUtil;
import org.tpri.sc.util.FileUtil;
import org.tpri.sc.util.MD5Util;
import org.tpri.sc.util.PinYinUtil;
import org.tpri.sc.util.StringUtil;
import org.tpri.sc.util.UUIDUtil;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>用户数据导入服务类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2016年1月26日
 */
@Service("PartyMemberImportService")
public class PartyMemberImportService {

    @Autowired
    private ComFileService comFileService;
    @Autowired
    private EnvironmentService environmentService;
    @Autowired
    private UserManager userManager;
    @Autowired
    private PartyMemberManager partyMemberManager;
    @Autowired
    private OrganizationManager organizationManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private CodeManager codeManager;
    @Autowired
    private DevelopmentProcedureManager developmentProcedureManager;

    /**
     * 
     * <B>方法名称：</B>Excel文件导入<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年2月22日
     * @param objs
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public Map<String, Object> beginExcelImportPartymemberInfo(JSONObject objs, String ccpartyId) throws IOException, ParseException {
        Map<String, Object> ret = new HashMap<String, Object>();
        int sumRows = 0; //导入行数
        int successTotal = 0;//成功导入数
        JSONArray files = JSONArray.fromObject(objs.getString("files"));
        String filesRootPath = (String) environmentService.getEnvironmentValueById(EnvironmentId.FILES_ROOT_PATH);//附件完整路径
        if (files != null && files.size() > 0) {
            //遍历文件
            for (int i = 0; i < files.size(); i++) {
                JSONObject file = JSONObject.fromObject(files.getString(i));
                String fileName = filesRootPath + "\\" + file.getString("filePath"); //文件完整路径和文件名
                //判断是否为excel文件
                String suffix = file.getString("fileName").substring(file.getString("fileName").lastIndexOf(".") + 1);
                List<String> partymemberInfos = new ArrayList<String>();
                if ("xls".equals(suffix)) {
                    //excel 2003格式
                    partymemberInfos = ExcelUtil.importExcel2003(new FileInputStream(new File(fileName)));
                } else if ("xlsx".equals(suffix)) {
                    //excel 2007格式
                    partymemberInfos = ExcelUtil.importExcel2007(new FileInputStream(new File(fileName)));
                } else {
                    FileUtil.deleteFile(fileName);
                    ret.put("success", false);
                    ret.put("msg", "请选择标准的excel格式进行导入。");
                    return ret;
                }
                FileUtil.deleteFile(fileName);
                if (partymemberInfos != null && partymemberInfos.size() > 0) {
                    sumRows = sumRows + partymemberInfos.size() - 1;
                    //遍历excel行 从第一行开始
                    for (int r = 1; r < partymemberInfos.size(); r++) {
                        String[] infos = partymemberInfos.get(r).split(",");
                        //遍历excel列
                        String name = infos[0].replace(" ", "").trim();
                        User user = parseExcelToCreateUser(name,ccpartyId);
                        boolean isOverFor = false;
                        for (int c = 0; c < infos.length; c++) {
                            if (!isOverFor) {
                                switch (c) {
                                case 0:
                                    //姓名
                                    user.setNamePhoneticize(PinYinUtil.getEname(user.getName()));
                                    user.setNameFirstCharacter(PinYinUtil.getFirstEname(user.getName()));
                                    break;
                                case 1:
                                    //性别
                                    if ("男".equals(infos[1])) {
                                        user.setGender(1);
                                    } else if ("女".equals(infos[1])) {
                                        user.setGender(2);
                                    }
                                    break;
                                case 2:
                                    break;
                                case 3:
                                    //民族
                                    if (!"null".equals(infos[3])) {
                                        //根据名称从code表查询该民族是否存在
                                        List<Code> codes = codeManager.getCodeByName(infos[3], "A0121");
                                        if (codes != null && codes.size() > 0) {
                                            user.setNation(codes.get(0).getCode());
                                        }
                                    }
                                    break;
                                case 4:
                                    //籍贯
                                    if (!"null".equals(infos[4])) {
                                        //根据名称从code表查询该籍贯是否存在
                                        List<Code> codes = codeManager.getCodeByName(infos[4], "A0114");
                                        if (codes != null && codes.size() > 0) {
                                            user.setBirthPlace(codes.get(0).getCode());
                                        }
                                    }
                                    break;
                                case 5:
                                    //婚姻状况
                                    break;
                                case 6:
                                    //学历
                                    if (!"null".equals(infos[6])) {
                                        //根据名称从code表查询该学历是否存在
                                        List<Code> codes = codeManager.getCodeByName(infos[6], "A0405");
                                        if (codes != null && codes.size() > 0) {
                                            user.setEducation(codes.get(0).getCode());
                                        }
                                    }
                                    break;
                                case 7:
                                    //学位
                                    if (!"null".equals(infos[7])) {
                                        //根据名称从code表查询该学位是否存在
                                        List<Code> codes = codeManager.getCodeByName(infos[7], "A0440");
                                        if (codes != null && codes.size() > 0) {
                                            user.setDegree(codes.get(0).getCode());
                                        }
                                    }
                                    break;
                                case 8:
                                    //毕业院校
                                    break;
                                case 9:
                                    //入党日期
                                    if (!"null".equals(infos[9])) {
                                        if (infos[9].length() == 6) {
                                            infos[9] += "01";
                                        }
                                        user.getPartyMember().setJoinTime(DateUtil.str2Date(infos[9], DateUtil.YYYYMMDD_FORMAT));
                                    }
                                    break;
                                case 10:
                                    //入党时所在党支部
                                    break;
                                case 11:
//                                    //所在党支部代码
//                                    if (!"null".equals(infos[11])) {
//                                        user.getPartyMember().setCcpartyId(infos[11]);
//                                    }
                                    break;
                                case 12:
                                    //进入当前支部日期
                                    if (!"null".equals(infos[12])) {
                                        if (infos[12].length() == 6) {
                                            infos[12] += "01";
                                        }
                                        user.getPartyMember().setJoinCurrentTime(DateUtil.str2Date(infos[12], DateUtil.YYYYMMDD_FORMAT));
                                    }
                                    break;
                                case 13:
                                    //参加工作日期
                                    break;
                                case 14:
                                    //党内职务
                                    break;
                                case 15:
                                    //任职机构
                                    break;
                                case 18:
                                    //专业技术职务
                                    break;
                                case 19:
                                    //身份证号
//                                    String idNumber = StringUtil.updateIdNumberLastCharX(infos[19]);
//                                    if (!StringUtil.isEmpty(idNumber)) {
//                                        user.setIdNumber(idNumber);
//                                        //提取出生日期
//                                        user.setBirthDay(DateUtil.str2Date(idNumber.substring(6, 14), DateUtil.YYYYMMDD_FORMAT));
//                                    }
                                    break;
                                case 20:
                                    //联系电话
                                    if (!"null".equals(infos[20])) {
                                        user.setMobile(infos[20]);
                                    }
                                    break;
                                default:
                                    break;
                                }
                            }
                        }
                        if (!isOverFor) {
                            userManager.updateUser(user);
                            partyMemberManager.saveOrUpdate(user.getPartyMember());
                            ++successTotal;
                        }
                    }
                }
            }
            if (sumRows == successTotal) {
                ret.put("success", true);
                ret.put("msg", "已成功导入所有信息。需导入总数：" + sumRows + ",已成功导入数：" + successTotal);
            } else {
                ret.put("success", true);
                ret.put("msg", "已成功导入数据， 但有部分导入失败。需导入总数：" + sumRows + ",已成功导入数：" + successTotal);
            }
        } else {
            ret.put("success", false);
            ret.put("msg", "没有可用导入文件，请选择文件后开始导入。");
        }
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>excel导入方式判断用户是否存在并创建<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年2月23日
     * @param userId
     * @return
     */
    public User parseExcelToCreateUser(String name,String ccpartyId) {
        String ename = PinYinUtil.getEname(name).toLowerCase();
        String loginNo = ename;
        User user = userManager.getUserByLoginNo(ename);
        while(true){
            int flag = 1;
            if(user!=null){
                //如果登录账号存在则持续加1
                loginNo = ename+flag;
                user = userManager.getUserByLoginNo(ename+flag);
                flag ++;
            }else{
                break;//跳出循环
            }
        }
        user = new User();
        user.setId(UUIDUtil.id());
        user.setName(name);
        user.setLoginNo(loginNo);
        user.setPassword(MD5Util.md5(User.PASSWORD_DEFAULT));
        user.setSystemNo(BaseConfig.SYSTEM_NO);
        user.setCreateTime(new Timestamp(System.currentTimeMillis()));
        user.setType(User.TYPE_01);
        Set<Role> roles = new HashSet<Role>();
        roles.add(roleManager.getRoleById(Role.ROLE_PARTYMEMBER_DEFAULT));
        user.setRoles(roles);
        PartyMember member = new PartyMember();
        member.setId(user.getId());
        member.setCcpartyId(ccpartyId);
        member.setType(PartyMember.TYPE_4);//正式党员
        member.setDevelopmentId(DevelopmentProcedure.OFFICIAL_PARTYMEMBER);
        member.setCreateTime(new Timestamp(System.currentTimeMillis()));
        user.setPartyMember(member);
        partyMemberManager.add(member);
        userManager.addUser(user);
        return user;
    }

    /**
     * 
     * <B>方法名称：</B>构建Document对象<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年5月11日
     * @param ccpartyId
     * @param name
     * @param gender
     * @param birthday
     * @param idNumber
     * @return
     */
    private Element buildUserXML(String ccpartyId, String name, String gender, String birthday, String idNumber) {
        Document doc = DocumentHelper.createDocument();
        Element baseInfo = doc.addElement("baseInfo");
        baseInfo.addElement("ccpartyId").addText(ccpartyId);
        baseInfo.addElement("name").addText(name);
        baseInfo.addElement("gender").addText(gender);
        baseInfo.addElement("birthday").addText(birthday.toString());
        baseInfo.addElement("idNumber").addText(idNumber);
        return baseInfo;
    }
}
