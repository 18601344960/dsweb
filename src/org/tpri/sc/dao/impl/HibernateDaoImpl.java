package org.tpri.sc.dao.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Repository;
import org.tpri.sc.dao.BaseDao;
import org.tpri.sc.dao.condition.Condition;
import org.tpri.sc.dao.condition.DaoPara;
import org.tpri.sc.dao.condition.Order;
import org.tpri.sc.dao.exception.DaoException;
import org.tpri.sc.util.BaseConstants;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>使用Hibernate实现数据库基本操作<BR>
 * <B>概要说明：</B><BR>
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年4月2日
 */
@Repository("HibernateDaoImpl")
@SuppressWarnings("all")
public class HibernateDaoImpl<T> implements BaseDao<T> {

	Logger logger = Logger.getLogger(HibernateDaoImpl.class);

	private SessionFactory sessionFactory;
	private DaoPara daoPara;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Session getCurrentSession() {
		Session session = sessionFactory.getCurrentSession();
		return session;
	}

	/**
	 * 新增
	 * 
	 * @param objectBase
	 * @return
	 */
	public Serializable save(T objectBase) {
		Serializable serializable = null;
		String logInfo = "save " + getFullClassName(objectBase.getClass());
		try {
			this.getCurrentSession().clear();
			serializable = this.getCurrentSession().save(objectBase);
			this.getCurrentSession().flush();
			logger.info(logInfo);
		} catch (Exception e) {
			logger.info(logInfo);
			throw new DaoException(logInfo, e);
		}
		return serializable;
	}

	/**
	 * 新增或更新
	 * 
	 * @param objectBase
	 * @return
	 */
	public Serializable saveOrUpdate(T objectBase) {
		Serializable serializable = null;
		String logInfo = "saveOrUpdate " + getFullClassName(objectBase.getClass());
		try {
			this.getCurrentSession().clear();
			this.getCurrentSession().saveOrUpdate(objectBase);
			this.getCurrentSession().flush();
			logger.info(logInfo);
		} catch (Exception e) {
			logger.info(logInfo);
			throw new DaoException(logInfo, e);
		}
		return serializable;
	}

	/**
	 * 删除
	 * 
	 * @param objectBase
	 * @return
	 */
	public void delete(T objectBase) {

		String logInfo = "delete " + getFullClassName(objectBase.getClass());
		try {
			this.getCurrentSession().delete(objectBase);
			logger.info(logInfo);
		} catch (Exception e) {
			logger.info(logInfo);
			throw new DaoException(logInfo, e);
		}
	}

	/**
	 * 条件删除
	 * 
	 * @param daoPara
	 */
	public void delete(DaoPara daoPara) {
		String logInfo = "";
		try {
			this.daoPara = daoPara;
			final StringBuffer hql = new StringBuffer();
			final List<Object> paramValues = new ArrayList<Object>();
			hql.append("delete " + getFullClassName(daoPara.getClazz()) + " where 1=1 ");
			String where = buildQueryCondition(paramValues);

			hql.append(where);
			Session session = this.getCurrentSession();
			Query query = session.createQuery(hql.toString());
			if (paramValues != null && paramValues.size() > 0) {
				setParameters(query, paramValues.toArray());
			}
			logInfo = "delete : hql--" + hql + "[" + paramLog(paramValues) + "]";
			query.executeUpdate();

			logger.info(logInfo);
		} catch (Exception e) {
			logger.info(logInfo);
			throw new DaoException(logInfo, e);
		}
	}

	/**
	 * 批量删除
	 * 
	 * @param objectBaseList
	 */
	public void deleteBatch(List<T> objectBaseList) {
		logger.info("deleteBatch start!");
		if (objectBaseList != null && objectBaseList.size() > 0) {
			for (int i = 0; i < objectBaseList.size(); i++) {
				T objectBase = objectBaseList.get(i);
				delete(objectBase);
			}
		}
		logger.info("deleteBatch end!");
	}

	/**
     * 
     * <B>方法名称：</B>hql语句删除<BR>
     * <B>概要说明：</B><BR>
     * @author 易文俊
     * @since 2015年9月22日    
     * @param hql
     * @param params
     * @return
     */
    
    public Boolean delete(String hql, Object[] params) {
        String logInfo = "";
        try {
            logInfo = "update : hql--" + hql + "[" + paramLog(params) + "]";
            Session session = this.getCurrentSession();
            Query query = session.createQuery(hql.toString());
            if (params != null && params.length > 0) {
                setParameters(query, params);
            }
            query.executeUpdate();
            logger.info(logInfo);
        } catch (Exception e) {
            logger.info(logInfo);
            throw new DaoException(logInfo, e);
        }

        return true;
    }

	/**
	 * 保存或更新
	 * 
	 * @param objectBase
	 */
	public void update(T objectBase) {
		String logInfo = "";
		try {
			Session session = this.getCurrentSession();
			logInfo = "update " + getFullClassName(objectBase.getClass());
			session.saveOrUpdate(objectBase);
			logger.info(logInfo);
		} catch (Exception e) {
			logger.info(logInfo);
			throw new DaoException(logInfo, e);
		}
	}

	/**
	 * 条件更新
	 * 
	 * @param daoPara
	 */
	public Boolean update(DaoPara daoPara) {
		String logInfo = "";
		try {
			this.daoPara = daoPara;
			final StringBuffer hql = new StringBuffer();
			final List<Object> paramValues = new ArrayList<Object>();
			String set = buildSetCondition(paramValues);
			if (set.isEmpty()) {
				return false;
			}
			hql.append("update " + getFullClassName(daoPara.getClazz()) + " set " + set + " where 1=1 ");
			String where = buildQueryCondition(paramValues);
			hql.append(where);
			logInfo = "update : hql--" + hql + "[" + paramLog(paramValues) + "]";
			Session session = this.getCurrentSession();
			Query query = session.createQuery(hql.toString());
			if (paramValues != null && paramValues.size() > 0) {
				setParameters(query, paramValues.toArray());
			}
			query.executeUpdate();

			logger.info(logInfo);
		} catch (Exception e) {
			logger.info(logInfo);
			throw new DaoException(logInfo, e);
		}

		return true;
	}
	

	/**
	 * 批量更新
	 * 
	 * @param objectBaseList
	 */
	public void updateBatch(List<T> objectBaseList) {
		logger.info("updateBatch start!");
		if (objectBaseList != null && objectBaseList.size() > 0) {
			for (int i = 0; i < objectBaseList.size(); i++) {
				update(objectBaseList.get(i));
			}
		}
		logger.info("updateBatch end!");
	}
	/**
	 * 
	 * <B>方法名称：</B>hql语句更新<BR>
	 * <B>概要说明：</B><BR>
	 * @author 易文俊
	 * @since 2015年9月22日 	
	 * @param hql
	 * @param params
	 * @return
	 */
	
    public Boolean update(String hql, Object[] params) {
        String logInfo = "";
        try {
            logInfo = "update : hql--" + hql + "[" + paramLog(params) + "]";
            Session session = this.getCurrentSession();
            Query query = session.createQuery(hql.toString());
            if (params != null && params.length > 0) {
                setParameters(query, params);
            }
            query.executeUpdate();
            logger.info(logInfo);
        } catch (Exception e) {
            logger.info(logInfo);
            throw new DaoException(logInfo, e);
        }

        return true;
    }
    /**
     * <B>方法名称：</B>hql获取整型值<BR>
     * <B>概要说明：</B><BR>
     * @author 易文俊
     * @since 2015年10月18日    
     * @param hql
     * @param parameters
     * @return
     */
    public int queryForInt(String hql, Object[] params) {
        String logInfo = "";
        int value = 0;
        try {
            Session session = this.getCurrentSession();
            Integer result = new Integer(0);
            Query query = session.createQuery(hql);
            if (params != null && params.length > 0) {
                setParameters(query, params);
            }
            if (query.uniqueResult() == null) {
                return 0;
            }
            logInfo = "queryForInt : sql--" + hql + "[" + paramLog(params) + "]";
            result = ((Number) query.uniqueResult()).intValue();
            value = result.intValue();
            logger.info(logInfo);
            logger.info("Int Value : " + value);
        } catch (Exception e) {
            logger.info(logInfo);
            throw new DaoException(logInfo, e);
        }
        return value;
    }

	/**
	 * 获取一个对象
	 * 
	 * @param daoCondition
	 * @return
	 */
	public T loadOne(DaoPara daoCondition) {
		logger.info("loadOne start!");

		List<T> objectList = loadList(daoCondition);
		if (objectList == null) {
			objectList = new ArrayList<T>();
		}
		if (objectList.size() == 0) {
			return null;
		}
		T obj = objectList.get(0);
		logger.info("loadOne end!");
		return obj;
	}

	/**
	 * 返回符合条件的记录总数
	 * 
	 * @param daoCondition
	 * @return
	 */
	public Integer getTotalCount(DaoPara daoCondition) {

		String logInfo = "";
		int total = 0;
		try {
			StringBuffer hql = new StringBuffer();
			this.daoPara = daoCondition;
			List<Object> paramValues = new ArrayList<Object>();

			hql.append("select count(*) from " + getFullClassName(daoCondition.getClazz()) + " where 1=1 ");
			hql.append(buildQueryCondition(paramValues));

			logInfo = "getTotalCount : hql--" + hql + "[" + paramLog(paramValues) + "]";
			Session session = this.getCurrentSession();
			Query query = session.createQuery(hql.toString());
			if (paramValues != null && paramValues.size() > 0) {
				setParameters(query, paramValues.toArray());
			}
			Object result = query.uniqueResult();
			if (result == null) {
				return new Integer(0);
			}
			total = ((Long) result).intValue();
			logger.info(logInfo);
			logger.info("totalCount : " + total);
		} catch (Exception e) {
			logger.info(logInfo);
			throw new DaoException(logInfo, e);
		}
		return total;
	}

	/**
	 * 返回符合条件的记录
	 * 
	 * @param daoCondition
	 * @return
	 */
	public List<T> loadList(DaoPara daoCondition) {

		List list = null;
		String logInfo = "";
		try {
			this.daoPara = daoCondition;

			List<Object> paramValues = new ArrayList<Object>();
			StringBuffer hql = new StringBuffer();

			hql.append("from " + getFullClassName(daoCondition.getClazz()) + " where 1=1 ");
			hql.append(buildQueryCondition(paramValues));
			hql.append(buildOrderCondition());

			logInfo = "LoadList : hql--" + hql + "[" + paramLog(paramValues) + "]";

			Integer start = daoCondition.getStart();
			Integer limit = daoCondition.getLimit();

			Boolean isForPage = (limit != null);
			Session session = this.getCurrentSession();
			Query query = session.createQuery(hql.toString());
			logger.info("----loadList Hql:" + hql.toString());
			if (paramValues != null && paramValues.size() > 0) {
				setParameters(query, paramValues.toArray());
			}
			if (isForPage) {
				query.setFirstResult(start);
				query.setMaxResults(limit);
				logger.info("start: " + start + ", limit:" + limit);
			}
			list = query.list();
			logger.info(logInfo);
		} catch (Exception e) {
			logger.info(logInfo);
			throw new DaoException(logInfo, e);
		}
		if (list == null) {
			return new ArrayList<T>();
		}
		return list;
	}

	/**
	 * 返回符合条件的记录
	 * 
	 * @param hql
	 * @param start
	 * @param limit
	 * @return
	 */
	public List<T> loadList(String hql, Integer start, Integer limit) {

		List list = null;
		String logInfo = "";
		try {
			logInfo = "LoadList : hql--" + hql;
			Boolean isForPage = (limit != null);
			Session session = this.getCurrentSession();
			Query query = session.createQuery(hql.toString());

			if (isForPage) {
				query.setFirstResult(start);
				query.setMaxResults(limit);
				logger.info("start: " + start + ", limit:" + limit);
			}
			list = query.list();
			logger.info(logInfo);
		} catch (Exception e) {
			logger.info(logInfo);
			throw new DaoException(logInfo, e);
		}
		if (list == null) {
			return new ArrayList<T>();
		}
		return list;
	}

	/**
	 * 返回符合条件的记录
	 * 
	 * @param hql
	 * @param start
	 * @param limit
	 * @return
	 */
	public List<T> loadList(String hql, Integer start, Integer limit, Object[] params) {

		List list = null;
		String logInfo = "";
		try {
			logInfo = "LoadList : hql--" + hql;
			Boolean isForPage = (limit != null);
			Session session = this.getCurrentSession();
			Query query = session.createQuery(hql.toString());

			setParameters(query, params);

			if (isForPage) {
				query.setFirstResult(start);
				query.setMaxResults(limit);
				logger.info("start: " + start + ", limit:" + limit);
			}
			list = query.list();
			logger.info(logInfo);
		} catch (Exception e) {
			logger.info(logInfo);
			throw new DaoException(logInfo, e);
		}
		if (list == null) {
			return new ArrayList<T>();
		}
		return list;
	}

	/**
	 * 返回符合条件的记录总数
	 * 
	 * @param hql
	 * @param start
	 * @param limit
	 * @return
	 */
	public Integer getTotalCount(String hql) {
		return getTotalCount(hql, null);
	}

	/**
	 * 返回符合条件的记录总数
	 * 
	 * @param hql
	 * @param start
	 * @param limit
	 * @return
	 */
	public Integer getTotalCount(String hql, Object[] params) {

		List list = null;
		String logInfo = "";
		int total = 0;
		try {
			logInfo = "getTotalCount : hql--" + hql;
			Session session = this.getCurrentSession();
			Query query = session.createQuery(hql.toString());
			setParameters(query, params);
			Object result = query.uniqueResult();
			if (result == null) {
				return new Integer(0);
			}
			total = ((Long) result).intValue();
			logger.info(logInfo);
			logger.info("totalCount : " + total);
		} catch (Exception e) {
			logger.info(logInfo);
			throw new DaoException(logInfo, e);
		}
		return total;
	}

	/**
	 * 原生sql 新增
	 * 
	 * @param sql
	 * @param params
	 */
	public void saveNative(String sql, Object[] params) {

		String logInfo = "";
		try {
			Session session = this.getCurrentSession();
			Query query = session.createSQLQuery(sql);
			if (params != null && params.length > 0) {
				setParameters(query, params);
			}
			logInfo = "saveNative : sql--" + sql + "[" + paramLog(params) + "]";
			query.executeUpdate();

			logger.info(logInfo);
		} catch (Exception e) {
			logger.info(logInfo);
			throw new DaoException(logInfo, e);
		}
	}

	/**
	 * 原生sql 修改
	 * 
	 * @param sql
	 * @param params
	 */
	public void updateNative(String sql, Object[] params) {

		String logInfo = "";
		try {
			Session session = this.getCurrentSession();
			Query query = session.createSQLQuery(sql);
			if (params != null && params.length > 0) {
				setParameters(query, params);
			}
			logInfo = "updateNative : sql--" + sql + "[" + paramLog(params) + "]";
			query.executeUpdate();

			logger.info(logInfo);
		} catch (Exception e) {
			logger.info(logInfo);
			throw new DaoException(logInfo, e);
		}

	}

	/**
	 * 原生sql 删除
	 * 
	 * @param sql
	 * @param params
	 */
	public void deleteNative(String sql, Object[] params) {

		String logInfo = "";
		try {
			Session session = this.getCurrentSession();
			Query query = session.createSQLQuery(sql);
			if (params != null && params.length > 0) {
				setParameters(query, params);
			}
			logInfo = "deleteNative : sql--" + sql + "[" + paramLog(params) + "]";
			query.executeUpdate();
			logger.info(logInfo);
		} catch (Exception e) {
			logger.info(logInfo);
			throw new DaoException(logInfo, e);
		}

	}
	

	/**
	 * 原生sql获取记录总数
	 * 
	 * @param sql
	 * @param parameters
	 * @return
	 */
	public int getNativeTotalCount(String sql, Object[] parameters) {
		String logInfo = "";
		int totalCount = 0;
		try {
			Session session = this.getCurrentSession();
			Integer result = new Integer(0);
			Query query = session.createSQLQuery(sql);
			setParameters(query, parameters);
			if (query.uniqueResult() == null) {
				return 0;
			}
			logInfo = "getNativeTotalCount : sql--" + sql + "[" + paramLog(parameters) + "]";
			result = ((Number) query.uniqueResult()).intValue();
			totalCount = result.intValue();
			logger.info(logInfo);
			logger.info("totalCount : " + totalCount);
		} catch (Exception e) {
			logger.info(logInfo);
			throw new DaoException(logInfo, e);
		}
		return totalCount;
	}

	/**
	 * 原生sql获取整型值
	 * 
	 * @param sql
	 * @param parameters
	 * @return
	 */
	public int queryNativeForInt(String sql, Object[] parameters) {
		String logInfo = "";
		int value = 0;
		try {
			Session session = this.getCurrentSession();
			Integer result = new Integer(0);
			Query query = session.createSQLQuery(sql);
			setParameters(query, parameters);
			if (query.uniqueResult() == null) {
				return 0;
			}
			logInfo = "queryNativeForInt : sql--" + sql + "[" + paramLog(parameters) + "]";
			result = ((Number) query.uniqueResult()).intValue();
			value = result.intValue();
			logger.info(logInfo);
			logger.info("Int Value : " + value);
		} catch (Exception e) {
			logger.info(logInfo);
			throw new DaoException(logInfo, e);
		}
		return value;
	}

	/**
	 * 原声sql分页查询指定排序规则
	 * 
	 * @param sql
	 * @param parameters
	 * @param start
	 * @param limit
	 * @param orderBy
	 * @param clazz
	 * @return
	 */
	public List<Object> loadNative(String sql, Object[] parameters, Integer start, Integer limit, String orderBy, Class clazz, boolean elementAsMap) {

		String logInfo = "";
		List list = null;
		try {
			if (orderBy != null) {
				sql = sql + orderBy;
			}
			Session session = this.getCurrentSession();
			Query query = null;
			if (clazz == null) {
				query = session.createSQLQuery(sql);
				if (elementAsMap) {
					query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				}
			} else {
				query = session.createSQLQuery(sql).addEntity(clazz);
			}

			logInfo = "loadNative : sql--" + sql + "[" + paramLog(parameters) + "]";
			if (limit != null && limit.intValue() != 0) {
				query.setFirstResult(start);
				query.setMaxResults(limit);
				logger.info("start: " + start + ", limit:" + limit);
			}
			setParameters(query, parameters);
			list = query.list();
			logger.info(logInfo);
		} catch (Exception e) {
			logger.info(logInfo);
			throw new DaoException(logInfo, e);
		}
		return list;
	}

	/**
	 * 原声sql分页查询指定排序规则
	 * 
	 * @param sql
	 * @param parameters
	 * @param start
	 * @param limit
	 * @param orderBy
	 * @param clazz
	 * @return
	 */
	public List<Object> loadNative(String sql, Object[] parameters, Integer start, Integer limit, String orderBy, Class clazz) {
		return loadNative(sql, parameters, start, limit, orderBy, clazz, false);
	}

	/**
	 * 原生sql分页查询不指定排序规则
	 * 
	 * @param sql
	 * @param params
	 * @param start
	 * @param limit
	 * @param clazz
	 * @return
	 */
	public List<Object> loadNative(String sql, Object[] params, int start, int limit, Class clazz) {
		return loadNative(sql, params, start, limit, null, clazz);
	}

	/**
	 * 原生sql参数带参数查询
	 * 
	 * @param sql
	 * @param params
	 * @param clazz
	 * @return
	 */
	public List<Object> loadNative(String sql, Object[] params, Class clazz) {
		return loadNative(sql, params, null, null, null, clazz);
	}
	
	/**
	 * 原声sql查询
	 * 
	 * @param sql
	 * @param clazz
	 * @return
	 */
	public List<Object> loadNative(String sql, Class clazz) {
		return loadNative(sql, null, null, null, null, clazz);
	}

	/**
	 * 构建更新Set条件
	 * 
	 * @param paramNames
	 * @param paramValues
	 * @return
	 */
	private String buildSetCondition(List<Object> paramValues) {
		if (daoPara == null) {
			return "";
		}
		Map<String, Object> values = daoPara.getValues();

		if (values == null) {
			return "";
		}
		if (values.size() == 0) {
			return "";
		}
		StringBuffer set = new StringBuffer();

		String questionMark = " ? ";
		for (Entry<String, Object> entry : values.entrySet()) {

			String key = entry.getKey();
			Object value = entry.getValue();
			paramValues.add(value);
			set.append(" ").append(key).append(" = ").append(questionMark).append(" ,");

		}
		if (set.length() > 0)
			set.delete(set.length() - 1, set.length());

		return set.toString();
	}

	/**
	 * 根据clazz获取全类名
	 * 
	 * @param clazz
	 * @return
	 */
	private String getFullClassName(Class clazz) {
		return clazz.getName();
	}

	/**
	 * 设置参数
	 * 
	 * @param query
	 * @param parameters
	 */
	private void setParameters(Query query, Object[] parameters) {
		if (parameters == null || query == null)
			return;
		for (int i = 0; i < parameters.length; i++) {
			if (parameters[i] == null) {
				query.setString(i, null);
				continue;
			}
			if (parameters[i] instanceof String) {
				query.setString(i, (String) parameters[i]);
			} else if (parameters[i] instanceof Integer) {
				query.setInteger(i, (Integer) parameters[i]);
			} else if (parameters[i] instanceof Timestamp) {
				query.setTimestamp(i, (Timestamp) parameters[i]);
			} else if (parameters[i] instanceof Float) {
				query.setFloat(i, (Float) parameters[i]);
			} else if (parameters[i] instanceof java.util.Date) {
				query.setTimestamp(i, new Timestamp(((java.util.Date) parameters[i]).getTime()));
			}
		}
	}

	/**
	 * 构建查询条件
	 * 
	 * @param paramNames
	 * @return
	 */
	private String buildQueryCondition(List<Object> paramValues) {
		if (daoPara == null) {
			return "";
		}
		List<Condition> conditions = daoPara.getConditions();
		if (conditions.size() == 0) {
			return "";
		}
		StringBuffer where = new StringBuffer();
		for (Condition condition : conditions) {
			where.append(condition.buildLogicWhere());
			where.append(condition.buildCondition(paramValues));
		}
		return where.toString();
	}

	/**
	 * 构建order条件
	 * 
	 * @return
	 */
	private String buildOrderCondition() {
		if (daoPara == null) {
			return " ";
		}
		List<Order> orders = daoPara.getOrders();
		if (orders.size() == 0) {
			return " ";
		}

		StringBuffer orderBuf = new StringBuffer();
		for (int i = 0; i < orders.size(); i++) {
			Order order = orders.get(i);
			String fieldName = order.getFieldName();
			String orderRule = order.getOrderRule();
			if (i == 0) {
				orderBuf.append(" " + fieldName + " " + orderRule);
			} else {
				orderBuf.append("," + fieldName + " " + orderRule);
			}
		}
		return " order by" + orderBuf.toString();
	}

	/**
	 * 构建打印参数串
	 * 
	 * @param paramValues
	 * @return
	 */
	private String paramLog(List<Object> paramValues) {
		String paramStr = "";
		if (paramValues != null)
			for (int i = 0; i < paramValues.size(); i++) {
				Object value = paramValues.get(i);
				if (i == 0) {
					paramStr = paramStr + value;
				} else {
					paramStr = paramStr + "," + value;
				}
			}
		return paramStr;

	}

	/**
	 * 构建打印参数串
	 * 
	 * @param paramValues
	 * @return
	 */
	private String paramLog(Object[] paramValues) {
		String paramStr = "";
		if (paramValues != null)
			for (int i = 0; i < paramValues.length; i++) {
				Object value = paramValues[i];
				if (i == 0) {
					paramStr = paramStr + value;
				} else {
					paramStr = paramStr + "," + value;
				}
			}
		return paramStr;

	}

	/**
	 * 获取数据库类型，对应枚举在 BaseConstants.DATABASE_TYPE_XXX 定义
	 * 
	 * @return
	 */
	public int getDbType() {
		int dbType = BaseConstants.DATABASE_TYPE_UNKNOWN;
		try {
			Connection c = getConnection();
			String company = c.getMetaData().getDatabaseProductName();
			c.close();

			if (company != null) {
				company = company.toLowerCase();

				if (company.startsWith("microsoft")) {
					dbType = BaseConstants.DATABASE_TYPE_SQLSERVER;
				}
				if (company.startsWith("oracle")) {
					dbType = BaseConstants.DATABASE_TYPE_ORACLE;
				}
				if (company.startsWith("db2")) {
					dbType = BaseConstants.DATABASE_TYPE_DB2;
				}
				if (company.startsWith("mysql")) {
					dbType = BaseConstants.DATABASE_TYPE_MYSQL;
				}
			}

		} catch (SQLException e) {
			logger.error("error: ", e);
		}
		return dbType;
	}

	@Override
	public Connection getConnection() {
		Connection c = null;
		try {
			c = SessionFactoryUtils.getDataSource(sessionFactory).getConnection();
		} catch (SQLException e) {
			logger.error("connection get error: ", e);
		}
		return c;
	}
	
}
