package org.tpri.sc.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.tpri.sc.dao.BaseDao;
import org.tpri.sc.dao.condition.Condition;
import org.tpri.sc.dao.condition.DaoPara;
import org.tpri.sc.dao.condition.Order;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>管理类基类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年4月2日
 */
@Repository("ManagerBase")
public class ManagerBase {

    protected Logger logger = Logger.getLogger(ManagerBase.class);

    /** 空对象 */
    protected static final String EMPTY_OBJECT = "-";

    @Resource(name = "HibernateDaoImpl")
    public BaseDao<ObjectBase> dao;

    public boolean add(ObjectBase objectBase) {
        dao.save(objectBase);
        return true;
    }

    public boolean update(ObjectBase objectBase) {
        dao.update(objectBase);
        return true;
    }

    public boolean delete(ObjectBase objectBase) {
        dao.delete(objectBase);
        return true;
    }

    public boolean saveOrUpdate(ObjectBase objectBase) {
        dao.saveOrUpdate(objectBase);
        return true;
    }

    public boolean delete(String id, int objectType) {
        DaoPara daoPara = new DaoPara();
        Class clazz = ObjectRegister.getClassByClassType(objectType);
        daoPara.setClazz(clazz);
        daoPara.addCondition(Condition.EQUAL("id", id));
        dao.delete(daoPara);
        return true;
    }

    public boolean deleteBatch(List ids, Class clazz) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(clazz);
        daoPara.addCondition(Condition.IN("id", ids));
        dao.delete(daoPara);
        return true;
    }

    public boolean update(String id, int objectType, Map<String, Object> fieldValues) {
        DaoPara daoPara = new DaoPara();
        Class clazz = ObjectRegister.getClassByClassType(objectType);
        daoPara.setClazz(clazz);
        daoPara.addCondition(Condition.EQUAL("id", id));
        daoPara.setValues(fieldValues);
        return dao.update(daoPara);
    }

    public ObjectBase load(String id, int objectType) {
        return (ObjectBase) loadOne(objectType, new String[] { "id" }, new Object[] { id });
    }

    /**
     * 底层HQL实现查询
     * 
     * @param objectType
     * @param paramNames
     * @param paramValues
     * @param orderByArr : 值的组成 例如：new String[] {"created:desc"}
     * @param start
     * @param limit
     * @return
     */
    public List<ObjectBase> loadList(int objectType, String[] paramNames, Object[] paramValues, String[] orderByArr, Integer start, Integer limit) {
        DaoPara daoPara = new DaoPara();
        buildDaoParaParam(objectType, paramNames, paramValues, daoPara);
        buildDaoParaOrderBy(orderByArr, daoPara);
        builddaoParaForPage(start, limit, daoPara);
        return loadList(daoPara);
    }

    /**
     * 根据对象主键批量获取对象
     * 
     * @param ids
     * @param clazz
     * @return
     */
    public List<ObjectBase> loadBatch(String[] ids, Class clazz) {
        List<Object> values = arrayToList(ids);
        return loadBatch(values, clazz);
    }

    /**
     * 根据对象主键批量获取对象
     * 
     * @param ids
     * @param clazz
     * @return
     */
    public List<ObjectBase> loadBatch(List ids, Class clazz) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(clazz);
        daoPara.addCondition(Condition.IN("id", ids));
        return dao.loadList(daoPara);
    }

    /**
     * 支持任何条件查询 使用时需要daoPara。setClazz(clazz)，是必填的。其他条件可以根据实际情况添加自行设置.
     * 如:daoPara.addCondition(condition),daoPara.addOrder(order)等等;
     * 
     * @param daoPara
     * @return
     */
    public Object loadOne(DaoPara daoPara) {

        return dao.loadOne(daoPara);
    }

    /**
     * 支持任何条件查询 使用时需要daoPara。setClazz(clazz)，是必填的。其他条件可以根据实际情况添加自行设置.
     * 如:daoPara.addCondition(condition),daoPara.addOrder(order)等等;
     * 
     * @param daoPara
     * @return
     */
    public List<ObjectBase> loadList(DaoPara daoPara) {

        return dao.loadList(daoPara);
    }

    /**
     * 底层HQL实现查询
     * 
     * @param clazz
     * @param paramNames
     * @param paramValues
     * @param orderByArr : 值的组成 例如：new String[] {"created:desc"}
     * @param start
     * @param limit
     * @return
     */
    public List<ObjectBase> loadList(Class clazz, String[] paramNames, Object[] paramValues, String[] orderByArr, Integer start, Integer limit) {
        DaoPara daoPara = new DaoPara();
        builddaoParaParam(clazz, paramNames, paramValues, daoPara);
        buildDaoParaOrderBy(orderByArr, daoPara);
        builddaoParaForPage(start, limit, daoPara);
        return loadList(daoPara);
    }

    /**
     * 底层HQL删除
     * 
     * @param objectType
     * @param paramNames
     * @param paramValues
     */
    public void delete(int objectType, String[] paramNames, Object[] paramValues) {
        DaoPara daoPara = new DaoPara();
        buildDaoParaParam(objectType, paramNames, paramValues, daoPara);
        dao.delete(daoPara);
    }

    /**
     * 构建daoPara对象的参数
     * 
     * @param objectType
     * @param paramNames
     * @param paramValues
     * @param daoPara ：is not null
     * @return
     */
    private DaoPara buildDaoParaParam(int objectType, String[] paramNames, Object[] paramValues, DaoPara daoPara) {
        Class clazz = ObjectRegister.getClassByClassType(objectType);
        daoPara.setClazz(clazz);
        if (paramNames != null && paramValues != null) {
            for (int i = 0; i < paramNames.length; i++) {
                String paramName = paramNames[i];
                Object paramValue = paramValues[i];
                daoPara.addCondition(Condition.EQUAL(paramName, paramValue));
            }
        }
        return daoPara;
    }

    /**
     * 构建daoPara对象的参数
     * 
     * @param clazz
     * @param paramNames
     * @param paramValues
     * @param daoPara ：is not null
     * @return
     */
    private DaoPara builddaoParaParam(Class clazz, String[] paramNames, Object[] paramValues, DaoPara daoPara) {
        daoPara.setClazz(clazz);
        if (paramNames != null && paramValues != null) {
            for (int i = 0; i < paramNames.length; i++) {
                String paramName = paramNames[i];
                Object paramValue = paramValues[i];
                daoPara.addCondition(Condition.EQUAL(paramName, paramValue));
            }
        }
        return daoPara;
    }

    /**
     * 构建daoPara对象的分页数据
     * 
     * @param start
     * @param limit
     * @param daoPara ：is not null
     */
    private void builddaoParaForPage(Integer start, Integer limit, DaoPara daoPara) {
        if (limit != null && start != null && limit > 0) {
            daoPara.setLimit(limit);
            daoPara.setStart(start);
        }
    }

    /**
     * 将数组类型的数据转换成List类型的数据
     * 
     * @param array
     * @return
     */
    public List<Object> arrayToList(Object[] array) {
        List<Object> list = new ArrayList<Object>();
        if (array != null) {
            for (Object temp : array) {
                list.add(temp);
            }
        }
        return list;
    }

    /**
     * 构建daoPara对象的排序规则
     * 
     * @param orderByArr : 值的组成 例如：new String[] {"created:desc"}
     * @param daoPara ：is not null
     */
    private void buildDaoParaOrderBy(String[] orderByArr, DaoPara daoPara) {
        if (orderByArr != null) {
            List<Order> orderList = new ArrayList<Order>();
            for (String orderBy : orderByArr) {
                String[] order = orderBy.split("\\:");
                String fieldName = order[0];
                String orderRule = order[1];
                orderList.add(new Order(fieldName, orderRule));
            }
            daoPara.setOrders(orderList);
        }
    }

    /**
     * 底层HQL实现查询
     * 
     * @param objectType
     * @param paramNames
     * @param paramValues
     * @return
     */
    public List<ObjectBase> loadList(int objectType, String[] paramNames, Object[] paramValues) {

        return loadList(objectType, paramNames, paramValues, null, null, null);
    }

    /**
     * 底层HQL实现查询
     * 
     * @param objectType
     * @param orderByArr : 值的组成 例如：new String[] {"created:desc"}
     * @return
     */
    public List<ObjectBase> loadList(int objectType, String[] orderByArr) {

        return loadList(objectType, null, null, orderByArr, null, null);
    }

    /**
     * 底层HQL实现查询
     * 
     * @param objectType
     * @param paramNames
     * @param paramValues
     * @param orderByArr : 值的组成 例如：new String[] {"created:desc"}
     * @return
     */
    public List<ObjectBase> loadList(int objectType, String[] paramNames, Object[] paramValues, String[] orderByArr) {

        return loadList(objectType, paramNames, paramValues, orderByArr, null, null);
    }

    /**
     * 底层HQL实现查询
     * 
     * @param objectType
     * @return
     */
    public List<ObjectBase> loadList(int objectType) {

        return loadList(objectType, null, null, null, null, null);
    }

    /**
     * 底层HQL实现查询
     * 
     * @param objectType
     * @param paramNames
     * @param paramValues
     * @param start
     * @param limit
     * @return
     */
    public List<ObjectBase> loadList(int objectType, String[] paramNames, Object[] paramValues, Integer start, Integer limit) {

        return loadList(objectType, paramNames, paramValues, null, start, limit);
    }

    /**
     * 底层HQL实现查询
     * 
     * @param objectType
     * @param orderByArr : 值的组成 例如：new String[] {"created:desc"}
     * @param start
     * @param limit
     * @return
     */
    public List<ObjectBase> loadList(int objectType, String[] orderByArr, Integer start, Integer limit) {

        return loadList(objectType, null, null, orderByArr, start, limit);
    }

    /**
     * 底层HQL实现查询一个对象
     * 
     * @param objectType
     * @param paramNames
     * @param paramValues
     * @return
     */
    public Object loadOne(int objectType, String[] paramNames, Object[] paramValues) {
        DaoPara daoPara = new DaoPara();
        buildDaoParaParam(objectType, paramNames, paramValues, daoPara);
        return dao.loadOne(daoPara);
    }

    /**
     * 
     * @param objectType
     * @param paramNames
     * @param paramValues
     * @return
     */
    public int getTotalCount(int objectType, String[] paramNames, Object[] paramValues) {
        DaoPara daoPara = new DaoPara();
        buildDaoParaParam(objectType, paramNames, paramValues, daoPara);
        return dao.getTotalCount(daoPara);
    }

    /**
     * 
     * @param daoPara
     * @return
     */
    public int getTotalCount(DaoPara daoPara) {
        return dao.getTotalCount(daoPara);
    }

    /**
     * 生成随机GUID
     * 
     * @return
     */
    public static String generateID() {
        return java.util.UUID.randomUUID().toString().toUpperCase();
    }

    /**
     * 获取数据库类型
     * 
     * @return
     */
    public int getDbType() {
        return dao.getDbType();
    }

    /** memcached缓存 */
    protected static MemoryCacheInterface mc = MemoryCacheFactory.getInstance(MemoryCacheFactory.CACHE_MEMECACHE);

    /**
     * 初始化缓存对象
     * 
     * @param objectType
     * @return
     */
    public HashMap<String, ObjectBase> initializeObjects(int objectType) {
        List list = loadList(objectType, null, null, null, null);
        mc.clearObject(objectType);
        mc.addObjects(list, objectType);
        return mc.getObjectMap(objectType).getMap();
    }

    /**
     * 初始化缓存对象
     * 
     * @param objectType
     * @return
     */
    public HashMap<String, ObjectBase> initializeObjects(int objectType, List list) {
        mc.clearObject(objectType);
        mc.addObjects(list, objectType);
        return mc.getObjectMap(objectType).getMap();
    }

    /**
     * 更新mc缓存对象
     * 
     * @param object
     */
    protected void updateCache(ObjectBase object) {
        object.clear();
        mc.update(object);
    }
    
    /**
     * 更新mc缓存对象
     * 
     * @param object
     */
    protected void updateCache(String key, Object object) {
        mc.update(key, Integer.MAX_VALUE, object);
    }
    
    
    /**
     * 从mc缓存移除对象
     * 
     * @param object
     */
    protected void removeCache(ObjectBase object) {
        mc.removeObject(object);
    }
    
    /**
     * 从mc缓存移除对象
     * 
     */
    protected void removeCache(String key) {
        mc.delete(key);
    }

    /**
     * 向mc缓存添加对象
     * 
     * @param object
     */
    protected void addCache(ObjectBase object) {
        mc.addObject(object);
    }
    
    /**
     * 向mc缓存添加对象
     * 
     */
    protected void addCache(String key, Object object) {
        mc.addObject(key, object);
    }
    
    /**
     * 根据ID获得memcached缓存中的数据
     */
    protected Object getCache(String key) {
        return mc.get(key);
    }
    /**
     * 根据objectType获得memcached缓存中的数据
     * 
     * @param objectType
     */
    protected static SortedHashMap loadMcMap(int objectType) {
        return mc.getObjectMap(objectType);
    }

    /**
     * 根据objectType获得memcached缓存中的数据列表
     * 
     * @param objectType
     */
    protected List<ObjectBase> loadMcList(int objectType) {
        return mc.getObjectMap(objectType).getList();
    }

    /**
     * 根据objectType和对象ID获得memcached缓存中的数据对象
     * 
     * @param objectType
     */
    protected static ObjectBase loadMcCacheObject(int objectType, String id) {
        SortedHashMap map = mc.getObjectMap(objectType);
        if (map == null)
            return null;
        return map.get(id);
    }
}
