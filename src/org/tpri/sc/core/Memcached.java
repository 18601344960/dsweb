package org.tpri.sc.core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Future;

import net.spy.memcached.MemcachedClient;

import org.apache.log4j.Logger;
/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>memcached缓存实现类<BR>
 * <B>概要说明：</B><BR>
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年6月19日
 */
public class Memcached implements MemoryCacheInterface  {

	private static final Logger logger = Logger.getLogger(Memcached.class);
	
	private final MemcachedClient client;
	
	public Memcached(final String[] hostnames) {
		final List<InetSocketAddress> addresses = new ArrayList<InetSocketAddress>();
		
		for (final String hostname : hostnames) {
			String[] hostPort = hostname.split(":");
			addresses.add(new InetSocketAddress(hostPort[0], Integer.parseInt(hostPort[1])));
		}
		
		try {
			this.client = new MemcachedClient(addresses);
		} catch (final IOException e) {
			throw new IllegalStateException(e);
		}
	}
	
	@Override
	public void addObject(ObjectBase object) {
		if(object == null)
            return;
        SortedHashMap objectMap = getObjectMap(object.getObjectType());
        if(objectMap == null){
            objectMap = new SortedHashMap();
            objectMap.add(object);
            add(string(object.getObjectType()), Integer.MAX_VALUE, objectMap);
        } else {
        	objectMap.add(object);
        	update(string(object.getObjectType()), Integer.MAX_VALUE, objectMap);
        }
	}

	@Override
	public void addObjects(ArrayList<ObjectBase> list, int objectType) {
		if(list == null)
            return;
		Boolean isUpdated = true;
        SortedHashMap objectMap = getObjectMap(objectType);
        if(objectMap == null){
            objectMap = new SortedHashMap();
            isUpdated = false;
        }
        for(ObjectBase object : list){
            objectMap.add(object);
        }
        objectMap.sort();
        if(!isUpdated) {
        	add(string(objectType), Integer.MAX_VALUE, objectMap);
        } else {
        	update(string(objectType), Integer.MAX_VALUE, objectMap);
        }
	}

	@Override
	public void addObjects(HashMap<String, ObjectBase> map, int objectType) {
		if(map == null)
            return;
		Boolean isUpdated = true;
        SortedHashMap objectMap = getObjectMap(objectType);
        if(objectMap == null){
            objectMap = new SortedHashMap();
            isUpdated = false;
        }
        for(ObjectBase object : map.values()){
            objectMap.add(object);
        }
        objectMap.sort();
        if(!isUpdated) {
        	add(string(objectType), Integer.MAX_VALUE, objectMap);
        } else {
        	update(string(objectType), Integer.MAX_VALUE, objectMap);
        }
	}

	@Override
	public void addObjects(List list, int objectType) {
		if(list == null)
            return;
		Boolean isUpdated = true;
        SortedHashMap objectMap = getObjectMap(objectType);
        if(objectMap == null){
            objectMap = new SortedHashMap();
            isUpdated = false;
        }
        for(Object object : list){
            objectMap.add((ObjectBase)object);
        }
        objectMap.sort();
        if(!isUpdated) {
        	add(string(objectType), Integer.MAX_VALUE, objectMap);
        } else {
        	update(string(objectType), Integer.MAX_VALUE, objectMap);
        }
	}
	
	@Override
	public void addObject(String key, Object object) {
		set(key, Integer.MAX_VALUE, object);
	}

	@Override
	public void clear() {
		this.client.flush();
	}

	@Override
	public void clearObject(int objectType){
		SortedHashMap objectMap = getObjectMap(objectType);
        if(objectMap!=null) {
        	objectMap.clear();
            delete(string(objectType));
        }
	}
	
	@Override
	public Object getObject(String key) {
		return get(key);
	}

	@Override
	public ObjectBase getObject(int objectType, String objectID) {
		long start = System.currentTimeMillis();
		SortedHashMap objectMap = getObjectMap(objectType);
		long end  = System.currentTimeMillis();
		System.out.println(Memcached.class.toString()+" objectType["+objectType+"] method getObject cost time:"+(end-start)+"ms");
        if(objectMap == null)
            return null;
        return objectMap.get(objectID);
	}

	@Override
	public ArrayList<ObjectBase> getObjectList(int objectType) {
		SortedHashMap objectMap = getObjectMap(objectType);
        if(objectMap == null)
            return new ArrayList<ObjectBase>();
        return objectMap.getList();
	}

	@Override
	public SortedHashMap getObjectMap(int objectType) {
		long start = System.currentTimeMillis();
		Object object = this.client.get(string(objectType));
		long end  = System.currentTimeMillis();
		System.out.println(Memcached.class.toString()+" objectType["+objectType+"] method getObjectMap cost time:"+(end-start)+"ms");
		if(object == null)
			return null;
		return (SortedHashMap)object;
	}

	@Override
	public void removeObject(ObjectBase object) {
		if(object == null) {
			return;
		}
		int objectType = object.getObjectType();
		SortedHashMap objectMap = getObjectMap(objectType);
        if(objectMap != null){
            objectMap.remove(object);
            update(string(objectType), Integer.MAX_VALUE, objectMap);
        }
	}

	@Override
	public List<ObjectBase> sortList(int objectType) {
		SortedHashMap objectMap = getObjectMap(objectType);
        if(objectMap == null)
            return new ArrayList<ObjectBase>();
        return objectMap.sort();
	}
	
	@Override
	public void update(List<ObjectBase> list) {
		if(list == null || list.size() == 0) {
			return;
		}
		addObjects(list, list.get(0).getObjectType());
	}

	@Override
	public void update(HashMap<String, ObjectBase> map) {
		if(map == null || map.size() == 0) {
			return;
		}
		Collection<ObjectBase> values = map.values();
		Iterator<ObjectBase> iterator = values.iterator();
		addObjects(map, iterator.next().getObjectType());
	}

	@Override
	public void update(ObjectBase objectBase) {
		addObject(objectBase);
	}
	
	private void set(String key, int exp, Object object) {
		this.client.set(key, Integer.MAX_VALUE, object);
	}
	
	/**
	 * 向缓存增加数据
	 * @param key
	 * @param exp
	 * @param object
	 */
	private void add(String key, int exp, Object object) {
		long t1 = System.currentTimeMillis();
		this.client.add(key, Integer.MAX_VALUE, object);
		System.out.println("add object to memcached cost:"+(System.currentTimeMillis()-t1)+"ms");
	}
	
	/**
	 * 向缓存更新数据
	 * @param key
	 * @param exp
	 * @param object
	 */
	public void update(String key, int exp, Object object) {
		long t1 = System.currentTimeMillis();
		Object old=this.client.get(key);
        if(old==null){
            this.client.add(key, Integer.MAX_VALUE, object);
        }else{
            this.client.replace(key, exp, object);
        }
		System.out.println("update object to memcached cost:"+(System.currentTimeMillis()-t1)+"ms");
	}
	
	/**
	 * 根据key从缓存里获得对应数据
	 * @param key
	 * @return
	 */
	public Object get(String key) {
		return this.client.get(key);
	}
	
	/**
	 * 根据Key删除缓存数据
	 * @param key
	 * @return
	 */
	public Boolean delete(String key) {
		Future<Boolean> f= this.client.delete(key);
		try {
			return f.get().booleanValue();
		} catch (final Exception e) {
			logger.error(e.getMessage(),e);
			return false;
		}
	}
	
	/**
	 * @param object
	 * @return
	 */
	private String string(int objectType) {
		return String.valueOf(objectType);
	}
	
	public static void main(String[] args) {
		Memcached memcached = new Memcached(new String[] {"127.0.0.1:11211"});
		memcached.add("2", Integer.MAX_VALUE, "22222");
		System.out.println(memcached.get("2"));
	}

}
