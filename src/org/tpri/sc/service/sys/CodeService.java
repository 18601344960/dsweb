package org.tpri.sc.service.sys;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tpri.sc.entity.sys.Code;
import org.tpri.sc.entity.sys.CodeComparator;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.manager.sys.CodeManager;
import org.tpri.sc.view.ZTreeView;

/**
 * @description 代码表服务类
 * @author 易文俊
 * @since 2015-06-30
 */

@Service("CodeService")
public class CodeService {
    @Autowired
    CodeManager codeManager;

    /**
     * 添加代码表
     */
    public boolean addCode(UserMc user, Map<String, Object> param) {
        Code code = new Code();
        String id = (String) param.get("id");
        code.setId(id);
        code.setName((String) param.get("name"));
        code.setParentId((String) param.get("parentCode"));
        code.setDescription((String) param.get("description"));
        codeManager.addCode(code);
        return true;
    }

    /**
     * 修改代码表
     */
    public boolean updateCode(UserMc user, Map<String, Object> param) {
        String id = (String) param.get("id");
        Code code = codeManager.getCode(id);
        code.setName((String) param.get("name"));
        code.setParentId((String) param.get("parentCode"));
        code.setDescription((String) param.get("description"));
        codeManager.saveCode(code);
        return true;
    }

    /**
     * 删除代码表
     */
    public boolean deleteCode(UserMc user, String id) {
        Code code = codeManager.getCode(id);
        codeManager.deleteCode(code);
        return true;
    }

    /**
     * 根据ID获取代码表
     */
    public Code getCodeById(String id) {
        Code code = codeManager.getCode(id);
        return code;
    }

    /**
     * 获取某个分类的代码表
     */
    public List<Code> getCodesByParentId(String parentId) {
        List<Code> list = new ArrayList<Code>();
        list = codeManager.getCodeListByParentId(parentId);
        return list;
    }

    /**
     * 获取某个分类的代码表
     */
    public List<ZTreeView> getCodeTreeByParentId(String parentId) {
        List<ZTreeView> trees = new ArrayList<ZTreeView>();
        List<Code> codes = new ArrayList<Code>();
        codes = codeManager.getCodeListByParentId(parentId);
        if (codes != null && codes.size() > 0) {
            Collections.sort(codes, new CodeComparator());
            for (int i = 0; i < codes.size(); i++) {
                ZTreeView tree = new ZTreeView();
                String id = codes.get(i).getId();
                tree.setId(id);
                tree.setName(codes.get(i).getName());
                tree.setpId(parentId);
                tree.setIsParent(this.hasChild(id));
                tree.setAttr1(codes.get(i).getCode());
                tree.setAttr2(codes.get(i).getName1());
                trees.add(tree);
            }
        }
        return trees;
    }

    private boolean hasChild(String parentId) {
        List<Code> codes = new ArrayList<Code>();
        codes = codeManager.getCodeListByParentId(parentId);
        if (codes != null && codes.size() > 0) {
            return true;
        }
        return false;
    }

    public List<Code> getCodeList() {
        List<Code> list = new ArrayList<Code>();
        list = codeManager.getCodeList();
        return list;
    }

    public List<Code> getCodeList(Integer start, Integer limit) {
        List<Code> list = new ArrayList<Code>();
        list = codeManager.getCodeList(start, limit);
        return list;
    }

    public int getCodeTotal() {
        return codeManager.getCodeTotal();
    }
}
