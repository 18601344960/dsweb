package org.tpri.sc.entity.com;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.tpri.sc.core.ObjectBase;
import org.tpri.sc.core.ObjectType;

/**
 * @description 文件列表bean
 * @author 易文俊
 * @since 2015-07-10
 */
@Entity
@Table(name = "COM_FILE")
public class ComFile extends ObjectBase {

	/** 文件类型-附件 */
	public static int FILETYPE_ATTACHMENT = 0;
	/** 文件类型-图片 */
	public static int FILETYPE_IMAGE = 1;
	/** 文件类型-视频 */
	public static int FILETYPE_VIDEO = 2;
	/** 文件类型-文档 */
	public static int FILETYPE_DOCUMENT = 3;

	private static final long serialVersionUID = 1L;
	protected int tableIndex;
	protected String tableName;
	protected String objectId;
	protected int fileType;
	protected long fileSize;
	protected String postfix;
	protected String filePath;
	protected int status;
	protected int exist;
	protected Timestamp createTime;
	protected int orderNo;
	
	public ComFile(){
		super();
		objectType = ObjectType.COM_FILE;
	}

	@Id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "TABLE_INDEX")
	public int getTableIndex() {
		return tableIndex;
	}

	public void setTableIndex(int tableIndex) {
		this.tableIndex = tableIndex;
	}
	
	@Column(name = "TABLE_NAME")
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	@Column(name = "OBJECT_ID")
	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "FILE_TYPE")
	public int getFileType() {
		return fileType;
	}

	public void setFileType(int fileType) {
		this.fileType = fileType;
	}

	@Column(name = "FILE_SIZE")
	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	@Column(name = "POSTFIX")
	public String getPostfix() {
		return postfix;
	}

	public void setPostfix(String postfix) {
		this.postfix = postfix;
	}

	@Column(name = "FILE_PATH")
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Column(name = "STATUS")
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Column(name = "CREATE_TIME")
	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@Column(name = "EXIST")
	public int getExist() {
		return exist;
	}

	public void setExist(int exist) {
		this.exist = exist;
	}
	@Column(name = "ORDER_NO")
    public int getOrderNo() {
        return orderNo;
    }
    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }
	
}
