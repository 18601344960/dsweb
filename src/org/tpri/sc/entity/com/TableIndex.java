package org.tpri.sc.entity.com;

import java.util.ArrayList;
import java.util.List;

/**
 * @description 用于关联文件的表索引枚举
 * @author 易文俊
 * @since 2015-08-07
 */
public enum TableIndex {

    TABLE_OBT_CONFERENCE(0, "obt_conference"),
    TABLE_COM_ANNOUNCEMENT(1, "com_announcement"),
	TABLE_COM_INFORMATION(2, "com_information"),
	
	TEST(10000, "no_table");

	private int type;
	private String desc;

	TableIndex(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public int getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}

	public static TableIndex get(int type) {
		TableIndex[] types = TableIndex.values();
		for (int i = 0; i < types.length; i++) {
			if (types[i].getType() == type) {
				return types[i];
			}
		}
		return null;
	}

	public static List<TableIndex> getValues() {
		TableIndex[] types = TableIndex.values();
		List<TableIndex> list = new ArrayList<TableIndex>();
		for (int i = 0; i < types.length; i++) {
			list.add(types[i]);
		}
		return list;
	}
}
