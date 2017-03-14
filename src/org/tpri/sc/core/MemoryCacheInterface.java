package org.tpri.sc.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>缓存接口类<BR>
 * <B>概要说明：</B><BR>
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年6月19日
 */
public interface MemoryCacheInterface {
	
	 public void addObject(ObjectBase object);
	 
	 public void addObjects(ArrayList<ObjectBase> list, int objectType);
	 
	 public void addObjects(List list, int objectType);
	 
	 public void addObjects(HashMap<String, ObjectBase> map, int objectType);
	 
	 public void addObject(String key, Object object);
	 
	 public List<ObjectBase> sortList(int objectType);
	 
	 public ObjectBase getObject(int objectType, String objectID);
	 
	 public Object getObject(String key);
	 
	 public ArrayList<ObjectBase> getObjectList(int objectType);
	 
	 public SortedHashMap getObjectMap(int objectType);
	 
	 public void removeObject(ObjectBase object);
	 
	 public void clear();
	 
	 public void clearObject(int objectType);
	 /**
     * 单条记录更新操作
     */
	 public void update(ObjectBase objectBase);
	 /**
     * 批量更新操作
     */
	 public void update(HashMap<String, ObjectBase> map);
	 /**
     * 批量更新操作
     */
     public void update(List<ObjectBase> list);
     
     /**
      * 根据key从缓存里获得对应数据
      */
     public Object get(String key);

     /**
      * 向缓存更新数据
      */
     public void update(String key, int exp, Object object);

     /**
      * 根据Key删除缓存数据
      */
     public Boolean delete(String key);
     
}
