package org.tpri.sc.view.com;


/**
 * 统计分析查询结果集
 * 
 * @author zhaozijing
 *
 */
public class QueryResultConference {

    protected String ccpartyId = ""; //组织ID
    protected String ccpartyName = ""; //组织名称
    protected String categoryName = ""; //模块名称
    protected int num = 0; //文章数

    protected int hits = 0; //浏览数

    protected int reply = 0; //回复数

    protected int files = 0; //附件数
    protected String year = ""; //文章的年份

    public String getCategoryName() {
        return categoryName;
    }

    public String getCcpartyId() {
        return ccpartyId;
    }

    public String getCcpartyName() {
        return ccpartyName;
    }

    public int getFiles() {
        return files;
    }

    public int getHits() {
        return hits;
    }

    public int getNum() {
        return num;
    }

    public int getReply() {
        return reply;
    }

    public String getYear() {
        return year;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setCcpartyId(String ccpartyId) {
        this.ccpartyId = ccpartyId;
    }

    public void setCcpartyName(String ccpartyName) {
        this.ccpartyName = ccpartyName;
    }

    public void setFiles(int files) {
        this.files = files;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setReply(int reply) {
        this.reply = reply;
    }

    public void setYear(String year) {
        this.year = year;
    }

}
