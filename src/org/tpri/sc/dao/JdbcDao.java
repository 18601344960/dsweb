package org.tpri.sc.dao;

import java.math.BigDecimal;
import java.net.URL;
import java.rmi.Remote;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.support.JdbcUtils;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>JDBC的dao<BR>
 * <B>概要说明：</B>
 * 用于没有使用spring注解的类，如servlet等无法从spring获取数据库连接的场合使用
 * <BR>
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年6月19日
 */
public class JdbcDao {

	public static Logger logger = Logger.getLogger(JdbcDao.class);

	public static Connection getConnection(String jndiName) {
		try {
			ServiceLocator sl = ServiceLocator.getInstance();
			DataSource ds = sl.getDataSource(jndiName);
			return ds.getConnection();
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	public static Connection getConnection() {
		return getConnection("java:comp/env/zhaozijing.datasource.dsweb");
	}

	public static void update(String sql, Object[] params) {
		logger.debug("SQL: " + sql + "\t" + Arrays.toString(params));
		Connection c = getConnection();
		PreparedStatement ps = getPsForUpdate(c, sql);
		setParams(ps, params);

		try {
			ps.executeUpdate();
		} catch (SQLException e) {
			logger.error(e);
		}

		closeConnection(c);

	}

	public static List query(String sql, Object[] params) {
		logger.debug("SQL: " + sql + "\t" + Arrays.toString(params));
		Connection c = getConnection();
		PreparedStatement ps = getPsForQuery(c, sql);
		setParams(ps, params);

		List list = queryForList(ps);
		packageResult(list);

		closePS(ps);
		closeConnection(c);

		return list;
	}

	private static void packageResult(List list) {
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				Map map = (Map) list.get(i);
				Set set = map.entrySet();
				Iterator it = set.iterator();
				while (it.hasNext()) {
					Entry entry = (Entry) it.next();
					String key = (String) entry.getKey();
					Object value = entry.getValue();
					if (value instanceof BigDecimal) {
						BigDecimal bigDecimalValue = (BigDecimal) value;
						int intValue = bigDecimalValue.intValue();
						map.put(key, intValue);
					}
				}
			}
		}
	}

	public static Map queryOne(String sql, Object[] params) {
		List list = query(sql, params);
		if (list != null && !list.isEmpty()) {
			return (Map) list.get(0);
		}
		return null;
	}

	private static List queryForList(PreparedStatement ps) {
		List list = new ArrayList();
		try {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(mapRow(rs, rs.getRow()));
			}
			closeRS(rs);
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
	}

	private static void setParams(PreparedStatement ps, Object[] params) {
		for (int i = 0; i < params.length; i++) {
			Object para = params[i];
			try {
				ps.setObject(i + 1, para);
			} catch (SQLException e) {
				logger.error(e);
			}
		}
	}

	public static PreparedStatement getPsForQuery(Connection c, String sql) {
		if (c != null) {
			try {
				return c.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			} catch (SQLException e) {
				logger.error(e);
			}
		}

		return null;
	}

	public static PreparedStatement getPsForUpdate(Connection c, String sql) {
		if (c != null) {
			try {
				return c.prepareStatement(sql);
			} catch (SQLException e) {
				logger.error(e);
			}
		}

		return null;
	}

	/**
	 * 关闭指定的连接
	 * 
	 * @param c
	 */
	public static void closeConnection(Connection c) {
		try {
			if (c != null) {
				c.close();
			}
		} catch (SQLException e) {
			logger.error(e);
		}
	}

	public static void closePS(PreparedStatement ps) {
		try {
			if (ps != null) {
				ps.close();
			}
		} catch (SQLException e) {
			logger.error(e);
		}
	}

	public static void closeRS(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			logger.error(e);
		}
	}

	public static Object mapRow(ResultSet rs, int rowNumber) throws Exception {
		Map map = new HashMap();
		ResultSetMetaData rsd = rs.getMetaData();
		rsd.getColumnCount();
		for (int i = 0; i < rsd.getColumnCount(); i++) {
			map.put(rsd.getColumnName(i + 1).toUpperCase(), JdbcUtils.getResultSetValue(rs, i + 1)); // key统一为大写，避免Oracle和SQLSERVER不一致
		}
		return map;
	}
}

class ServiceLocator {
	private InitialContext ic;
	private Map cache;
	private static ServiceLocator me;

	private ServiceLocator() throws ServiceLocatorException {
		try {
			this.ic = new InitialContext();
			this.cache = Collections.synchronizedMap(new HashMap());
		} catch (NamingException ne) {
			throw new ServiceLocatorException(ne);
		} catch (Exception e) {
			throw new ServiceLocatorException(e);
		}
	}

	public static ServiceLocator getInstance() {
		return me;
	}

	public void setInitialContext(InitialContext ic) {
		this.ic = ic;
	}

	public Remote getRemote(String jndiName) throws ServiceLocatorException {
		Remote remote = null;
		try {
			if (this.cache.containsKey(jndiName)) {
				remote = (Remote) this.cache.get(jndiName);
			} else {
				remote = (Remote) this.ic.lookup(jndiName);
				this.cache.put(jndiName, remote);
			}
			return remote;
		} catch (NamingException ne) {
			throw new ServiceLocatorException(ne);
		} catch (Exception e) {
			throw new ServiceLocatorException(e);
		}
	}

	public QueueConnectionFactory getQueueConnectionFactory(String qConnFactoryName) throws ServiceLocatorException {
		QueueConnectionFactory factory = null;
		try {
			if (this.cache.containsKey(qConnFactoryName)) {
				factory = (QueueConnectionFactory) this.cache.get(qConnFactoryName);
			} else {
				factory = (QueueConnectionFactory) this.ic.lookup(qConnFactoryName);
				this.cache.put(qConnFactoryName, factory);
			}
		} catch (NamingException ne) {
			throw new ServiceLocatorException(ne);
		} catch (Exception e) {
			throw new ServiceLocatorException(e);
		}
		return factory;
	}

	public Queue getQueue(String queueName) throws ServiceLocatorException {
		Queue queue = null;
		try {
			if (this.cache.containsKey(queueName)) {
				queue = (Queue) this.cache.get(queueName);
			} else {
				queue = (Queue) this.ic.lookup(queueName);
				this.cache.put(queueName, queue);
			}
		} catch (NamingException ne) {
			throw new ServiceLocatorException(ne);
		} catch (Exception e) {
			throw new ServiceLocatorException(e);
		}

		return queue;
	}

	public TopicConnectionFactory getTopicConnectionFactory(String topicConnFactoryName) throws ServiceLocatorException {
		TopicConnectionFactory factory = null;
		try {
			if (this.cache.containsKey(topicConnFactoryName)) {
				factory = (TopicConnectionFactory) this.cache.get(topicConnFactoryName);
			} else {
				factory = (TopicConnectionFactory) this.ic.lookup(topicConnFactoryName);
				this.cache.put(topicConnFactoryName, factory);
			}
		} catch (NamingException ne) {
			throw new ServiceLocatorException(ne);
		} catch (Exception e) {
			throw new ServiceLocatorException(e);
		}
		return factory;
	}

	public Topic getTopic(String topicName) throws ServiceLocatorException {
		Topic topic = null;
		try {
			if (this.cache.containsKey(topicName)) {
				topic = (Topic) this.cache.get(topicName);
			} else {
				topic = (Topic) this.ic.lookup(topicName);
				this.cache.put(topicName, topic);
			}
		} catch (NamingException ne) {
			throw new ServiceLocatorException(ne);
		} catch (Exception e) {
			throw new ServiceLocatorException(e);
		}
		return topic;
	}

	public DataSource getDataSource(String dataSourceName) throws ServiceLocatorException {
		DataSource dataSource = null;
		try {
			if (this.cache.containsKey(dataSourceName)) {
				dataSource = (DataSource) this.cache.get(dataSourceName);
			} else {
				dataSource = (DataSource) this.ic.lookup(dataSourceName);
				this.cache.put(dataSourceName, dataSource);
			}
		} catch (NamingException ne) {
			throw new ServiceLocatorException(ne.getMessage(), ne);
		} catch (Exception e) {
			throw new ServiceLocatorException(e.getMessage(), e);
		}
		return dataSource;
	}

	public DataSource getDataSourceNoCache(String dataSourceName) throws ServiceLocatorException {
		DataSource dataSource = null;
		try {
			dataSource = (DataSource) this.ic.lookup(dataSourceName);
			this.cache.put(dataSourceName, dataSource);
		} catch (NamingException ne) {
			throw new ServiceLocatorException(ne.getMessage(), ne);
		} catch (Exception e) {
			throw new ServiceLocatorException(e.getMessage(), e);
		}
		return dataSource;
	}

	public URL getUrl(String envName) throws ServiceLocatorException {
		URL url = null;
		try {
			url = (URL) this.ic.lookup(envName);
		} catch (NamingException ne) {
			throw new ServiceLocatorException(ne);
		} catch (Exception e) {
			throw new ServiceLocatorException(e);
		}

		return url;
	}

	public boolean getBoolean(String envName) throws ServiceLocatorException {
		Boolean bool = null;
		try {
			bool = (Boolean) this.ic.lookup(envName);
		} catch (NamingException ne) {
			throw new ServiceLocatorException(ne);
		} catch (Exception e) {
			throw new ServiceLocatorException(e);
		}
		return bool.booleanValue();
	}

	public String getString(String envName) throws ServiceLocatorException {
		String envEntry = null;
		try {
			envEntry = (String) this.ic.lookup(envName);
		} catch (NamingException ne) {
			throw new ServiceLocatorException(ne);
		} catch (Exception e) {
			throw new ServiceLocatorException(e);
		}
		return envEntry;
	}

	static {
		try {
			me = new ServiceLocator();
		} catch (ServiceLocatorException se) {
			System.err.println(se);
			se.printStackTrace(System.err);
		}
	}
}

class ServiceLocatorException extends Exception {
	private Exception exception;

	public ServiceLocatorException(String message, Exception exception) {
		super(message);
		this.exception = exception;
	}

	public ServiceLocatorException(String message) {
		this(message, null);
	}

	public ServiceLocatorException(Exception exception) {
		this(null, exception);
	}

	public Exception getException() {
		return this.exception;
	}

	public Exception getRootCause() {
		if (this.exception instanceof ServiceLocatorException) {
			return ((ServiceLocatorException) this.exception).getRootCause();
		}
		return (this.exception == null) ? this : this.exception;
	}

	public String toString() {
		if (this.exception instanceof ServiceLocatorException) {
			return this.exception.toString();
		}
		return (this.exception == null) ? super.toString() : this.exception.toString();
	}
}