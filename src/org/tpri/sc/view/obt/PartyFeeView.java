package org.tpri.sc.view.obt;

import java.util.List;

import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import org.tpri.sc.entity.obt.PartyFee;
import org.tpri.sc.entity.uam.User;
import org.tpri.sc.entity.uam.UserMc;

public class PartyFeeView {

    protected String id;
    protected String name;
    protected String ccpartyId;
    protected List<UserMc> receiverList; //签收人列表用于党费管理表格用户选择签收人
    protected double total; //党费实缴总和
    protected double partyFeetotalShould; //党费应缴总和

    public double getPartyFeetotalShould() {
        return partyFeetotalShould;
    }

    public void setPartyFeetotalShould(double partyFeetotalShould) {
        this.partyFeetotalShould = partyFeetotalShould;
    }

    protected double partyFeeSpecial;

    public double getPartyFeeSpecial() {
        return partyFeeSpecial;
    }

    public void setPartyFeeSpecial(double partyFeeSpecial) {
        this.partyFeeSpecial = partyFeeSpecial;
    }

    protected PartyFee partyFee;

    @OneToOne(mappedBy = "partyFeeView", fetch = FetchType.EAGER)
    public PartyFee getPartyFee() {
        return partyFee;
    }

    public void setPartyFee(PartyFee partyFee) {
        this.partyFee = partyFee;
    }

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

    public String getCcpartyId() {
        return ccpartyId;
    }

    public void setCcpartyId(String ccpartyId) {
        this.ccpartyId = ccpartyId;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<UserMc> getReceiverList() {
        return receiverList;
    }

    public void setReceiverList(List<UserMc> receiverList) {
        this.receiverList = receiverList;
    }

    @Override
    public String toString() {
        return "PartyFeeView [id=" + id + ", name=" + name + ", ccpartyId=" + ccpartyId + ",partyFeeSpecial=" + partyFeeSpecial + ",total=" + total + ",partyFeetotalShould=" + partyFeetotalShould
                + ", receiverList=" + receiverList + ", partyFee=" + partyFee + "]";
    }

}
