package org.tpri.sc.core;

import java.util.HashMap;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B> 对象注册类<BR>
 * <B>概要说明：</B><BR>
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年4月2日
 */
public class ObjectRegister extends ObjectBase{
	/** */
	private static final long serialVersionUID = 7063348702353386363L;

	private static HashMap<Integer, ObjectRegister> objectRegisters = new HashMap<Integer, ObjectRegister>(); 
	
	private Class clazz;
	private int objectClassType;

	
    public ObjectRegister(int objectClassType, Class clazz){
    	objectType = ObjectType.OBJECTREGISTER;
    	this.objectClassType = objectClassType;
    	this.clazz = clazz;
    }
    
    public static ObjectRegister getObjectRegister(int objectClassType){
        return objectRegisters.get(objectClassType);
    }
    
    public static void addRegister(ObjectRegister objectRegister){
		objectRegisters.put(objectRegister.getObjectClassType(), objectRegister);
    }
    
    public static void registerClass(int objectClassType, Class clazz){
    	addRegister(new ObjectRegister(objectClassType, clazz));
    }
   
    /**
     * 根据类型获取对应的java类
     * @param type
     * @return
     */
    public static Class getClassByClassType(int type) {
    	ObjectRegister register = objectRegisters.get(type);
    	if (register != null) {
			return register.getClazz();
		}
    	return null;
    }

    public int getObjectClassType() {
        return objectClassType;
    }

	public Class getClazz() {
		return clazz;
	}
}
