package org.tpri.sc.core;

import java.io.Serializable;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>bean基础类<BR>
 * <B>概要说明：</B><BR>
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年4月2日
 */
public class ObjectBase implements Serializable {

	@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	public @interface SetMyAnnotation {
		boolean needView() default true;
	}

	protected String id;

	@SetMyAnnotation(needView = true)
	protected String name;
	protected int objectType;

	protected String objectClassID;

	public static String EMPTY_OBJECT = "-";

	public ObjectBase() {

	}

	public ObjectBase(String id, String name, int objectType) {
		this.id = id;
		this.name = name;
		this.objectType = objectType;
	}

	/**
	 * 生成唯一的GUID
	 */
	public static String generateID() {
		return java.util.UUID.randomUUID().toString().toUpperCase();
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getObjectType() {
		return objectType;
	}

	public void setObjectType(int objectType) {
		this.objectType = objectType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getObjectClassID() {
		return objectClassID;
	}

	public void setObjectClassID(String objectClassID) {
		this.objectClassID = objectClassID;
	}

	public static boolean isEmpty(String s) {
		if (s == null)
			return true;
		return s.equals(EMPTY_OBJECT);
	}

	public int overrideHashCode(String keyOne, String keyTwo) {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((keyOne == null) ? 0 : keyOne.hashCode());
		result = prime * result + ((keyTwo == null) ? 0 : keyTwo.hashCode());
		return result;
	}
	 /**
     * 清除操作，具体操作由子类实现
     */
    protected void clear() {
    	
    }

	/**
	 * 根据objectType构建serialVersionUID
	  *所有要放入缓存的对象都应调用本方法生成serialVersionUID，
	 * 否则容易出现序列化对象不一致的报错
	 * 
	 * @param objectType
	 * @return
	 */
	protected static long buildSerialVersionUID(int objectType) {
		return 7840819529857750150L + objectType;
	}

}
