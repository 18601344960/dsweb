package org.tpri.sc.view.obt;

public class HomePagePartyFeeView {
    private String id;
    private String name;
    private int month;
    private boolean status=true;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getMonth() {
        return month;
    }
    public void setMonth(int month) {
        this.month = month;
    }
    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
    @Override
    public String toString() {
        return "HomePagePartyFeeView [id=" + id + ", name=" + name + ", month=" + month + ", status=" + status + "]";
    }
   
    
    
}
