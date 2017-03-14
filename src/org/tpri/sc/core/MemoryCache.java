package org.tpri.sc.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>本地内存实现类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年6月19日
 */
public class MemoryCache implements MemoryCacheInterface {
    private HashMap<Integer, SortedHashMap> cacheObjects = new HashMap<Integer, SortedHashMap>();

    public void addObject(ObjectBase object) {
        if (object == null)
            return;
        SortedHashMap objectMap = cacheObjects.get(object.getObjectType());
        if (objectMap == null) {
            objectMap = new SortedHashMap();
            cacheObjects.put(object.getObjectType(), objectMap);
        }
        objectMap.add(object);
    }

    public void addObjects(ArrayList<ObjectBase> list, int objectType) {
        if (list == null)
            return;
        SortedHashMap objectMap = cacheObjects.get(objectType);
        if (objectMap == null) {
            objectMap = new SortedHashMap();
            cacheObjects.put(objectType, objectMap);
        }
        for (ObjectBase object : list) {
            objectMap.add(object);
        }
        objectMap.sort();
    }

    public void addObjects(List list, int objectType) {
        if (list == null)
            return;
        SortedHashMap objectMap = cacheObjects.get(objectType);
        if (objectMap == null) {
            objectMap = new SortedHashMap();
            cacheObjects.put(objectType, objectMap);
        }
        for (Object object : list) {
            objectMap.add((ObjectBase) object);
        }
        objectMap.sort();
    }

    public void addObjects(HashMap<String, ObjectBase> map, int objectType) {
        if (map == null)
            return;
        SortedHashMap objectMap = cacheObjects.get(objectType);
        if (objectMap == null) {
            objectMap = new SortedHashMap();
            cacheObjects.put(objectType, objectMap);
        }
        for (ObjectBase object : map.values()) {
            objectMap.add(object);
        }
        objectMap.sort();
    }

    public List<ObjectBase> sortList(int objectType) {
        SortedHashMap objectMap = cacheObjects.get(objectType);
        if (objectMap == null)
            return new ArrayList<ObjectBase>();
        return objectMap.sort();
    }

    public ObjectBase getObject(int objectType, String objectID) {
        SortedHashMap objectMap = cacheObjects.get(objectType);
        if (objectMap == null)
            return null;
        ObjectBase ob = objectMap.get(objectID);
        return ob;
    }

    public ArrayList<ObjectBase> getObjectList(int objectType) {
        SortedHashMap objectMap = getObjectMap(objectType);
        if (objectMap == null)
            return new ArrayList<ObjectBase>();

        return objectMap.getList();
    }

    public SortedHashMap getObjectMap(int objectType) {
        SortedHashMap objectMap = cacheObjects.get(objectType);
        if (objectMap == null) {
            return new SortedHashMap();
        }
        return objectMap;
    }

    public void removeObject(ObjectBase object) {
        SortedHashMap objectMap = cacheObjects.get(object.getObjectType());
        if (objectMap != null) {
            objectMap.remove(object);
        }
    }

    public void clear() {
        cacheObjects.clear();
    }

    public void clearObject(int objectType) {
        SortedHashMap objectMap = cacheObjects.get(objectType);
        if (objectMap != null) {
            objectMap.clear();
            cacheObjects.remove(objectMap);
        }
    }

    /**
     * 单条记录更新操作
     */
    public void update(ObjectBase objectBase) {
        addObject(objectBase);
    }

    /**
     * 批量更新操作
     */
    public void update(HashMap<String, ObjectBase> map) {
        for (ObjectBase ob : map.values()) {
            update(ob);
        }
    }

    /**
     * 批量更新操作
     */
    public void update(List<ObjectBase> list) {
        for (ObjectBase ob : list) {
            update(ob);
        }
    }

    @Override
    public void addObject(String key, Object object) {

    }

    @Override
    public Object getObject(String key) {
        return null;
    }

    @Override
    public Object get(String key) {
        return null;
    }

    @Override
    public Boolean delete(String key) {
        return null;
    }

    @Override
    public void update(String key, int exp, Object object) {
    }

}
