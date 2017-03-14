package org.tpri.sc.core;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>使用HashMap存储排序后的对象类<BR>
 * <B>概要说明：</B><BR>
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年6月19日
 */
public class SortedHashMap implements Serializable, Cloneable{

	private static final long serialVersionUID = 6563373821922248910L;
	HashMap<String, ObjectBase> map = new HashMap<String, ObjectBase>();

    public void add(ObjectBase o){
        map.put(o.getId(), o);
    }

    public List<ObjectBase> sort(){
    	ArrayList<ObjectBase> list = new ArrayList<ObjectBase>();
    	Collection<ObjectBase> objectBases = map.values();
    	list.addAll(objectBases);
        Collections.sort(list, new ObjectNameComparator());
        return list;
    }

    public void addAndSort(ObjectBase o){
        add(o);
        sort();
    }

    public ObjectBase get(String id){
        return map.get(id);
    }

    public ArrayList<ObjectBase> getList(){
    	ArrayList<ObjectBase> list = new ArrayList<ObjectBase>();
    	Collection<ObjectBase> objectBases = map.values();
    	list.addAll(objectBases);
        return list;
    }

    public HashMap<String, ObjectBase> getMap(){
        return map;
    }

    public void remove(ObjectBase o){
        map.remove(o.getId());
    }

    public int size(){
        return map.size();
    }
    
    public void clear() {
    	map.clear();
    }
    
    @Override
	protected Object clone() throws CloneNotSupportedException {
    	SortedHashMap map = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try{
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            oos.close();
 
            ByteArrayInputStream bais = new ByteArrayInputStream(
                    baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            map = (SortedHashMap) ois.readObject();
            ois.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return map;
	}
}
