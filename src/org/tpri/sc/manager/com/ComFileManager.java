package org.tpri.sc.manager.com;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.tpri.sc.core.ManagerBase;
import org.tpri.sc.core.ObjectRegister;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.dao.condition.Condition;
import org.tpri.sc.dao.condition.DaoPara;
import org.tpri.sc.dao.condition.Order;
import org.tpri.sc.entity.com.ComFile;

/**
 * @description 文件列表管理类
 * @author 易文俊
 * @since 2015-07-10
 */

@Repository("ComFileManager")
public class ComFileManager extends ManagerBase {
	static {
        ObjectRegister.registerClass(ObjectType.COM_FILE, ComFile.class);
    }
    /**
     * 获取某记录文件列表
     */
    public List<ComFile> getFileList(int tableIndex,String objectId)  {
    	DaoPara daoPara = new DaoPara();
		daoPara.setClazz(ComFile.class);
		daoPara.addCondition(Condition.EQUAL("tableIndex", tableIndex));
		daoPara.addCondition(Condition.EQUAL("objectId", objectId));
		daoPara.addOrder(Order.asc("orderNo"));
		daoPara.addOrder(Order.asc("createTime"));
        List list = dao.loadList(daoPara);
        return list;
    }
    /**
     * 获取某种类型文件列表
     * @return
     */
    public List<ComFile> getFileList(int tableIndex,String objectId,Integer fileType)  {
    	DaoPara daoPara = new DaoPara();
		daoPara.setClazz(ComFile.class);
		daoPara.addCondition(Condition.EQUAL("tableIndex", tableIndex));
		daoPara.addCondition(Condition.EQUAL("objectId", objectId));
		if(fileType!=null){
			daoPara.addCondition(Condition.EQUAL("fileType", fileType));
		}
		daoPara.addOrder(Order.asc("orderNo"));
		daoPara.addOrder(Order.asc("createTime"));
        List list = dao.loadList(daoPara);
        return list;
    }
    /**
     * 获取文件记录
     * @return
     */
    public ComFile getFileById(String id)  {
    	ComFile file=(ComFile)super.load(id, ObjectType.COM_FILE);
    	return file;
    }
    /**
     * 更新文件记录
     * @return
     */
    public boolean editFile(String id, Map<String, Object> fieldValues)  {
    	return super.update(id, ObjectType.COM_FILE, fieldValues);
    }
    /**
     * 删除文件记录
     * @return
     */
    public boolean deleteFile(String id)  {
    	return super.delete(id, ObjectType.COM_FILE);
    }
    /**
     * 删除某文章下的所有文件记录
     * @return
     */
    public boolean deleteFileByObjectId(int tableIndex,String objectId)  {
    	DaoPara daoPara = new DaoPara();
    	daoPara.setClazz(ComFile.class);
    	daoPara.addCondition(Condition.EQUAL("tableIndex", tableIndex));
		daoPara.addCondition(Condition.EQUAL("objectId", objectId));
    	dao.delete(daoPara);
    	return true;
    }
}
