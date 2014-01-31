/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author leokassio
 */
@Entity
public class QueryWeb implements Serializable {
    
    @Id
    @GeneratedValue
    private Long idQueryWeb;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable=false)
    private Date dateQueryWeb;
    private Boolean attentedSunspot = false;
    private Boolean attentedMemsic = false;
    private Boolean attentedXbee = false;
    private Boolean sunspot = false;
    private Boolean memsic = false;
    private Boolean xbee = false;
    @ManyToMany(mappedBy="queriesWeb", cascade= CascadeType.ALL)
    private List<ReplyWeb> repliesWeb;

    public QueryWeb() {
    }

    public Boolean getAttentedMemsic() {
        return attentedMemsic;
    }

    public void setAttentedMemsic(Boolean attentedMemsic) {
        this.attentedMemsic = attentedMemsic;
    }

    public Boolean getAttentedSunspot() {
        return attentedSunspot;
    }

    public void setAttentedSunspot(Boolean attentedSunspot) {
        this.attentedSunspot = attentedSunspot;
    }

    public Boolean getAttentedXbee() {
        return attentedXbee;
    }

    public void setAttentedXbee(Boolean attentedXbee) {
        this.attentedXbee = attentedXbee;
    }

    public List<ReplyWeb> getRepliesWeb() {
        return repliesWeb;
    }

    public void setRepliesWeb(List<ReplyWeb> repliesWeb) {
        this.repliesWeb = repliesWeb;
    }

    public Date getDateQueryWeb() {
        return dateQueryWeb;
    }

    public void setDateQueryWeb(Date dateQueryWeb) {
        this.dateQueryWeb = dateQueryWeb;
    }

    public Long getIdQueryWeb() {
        return idQueryWeb;
    }

    public void setIdQueryWeb(Long idQueryWeb) {
        this.idQueryWeb = idQueryWeb;
    }

    public Boolean getMemsic() {
        return memsic;
    }

    public void setMemsic(Boolean memsic) {
        this.memsic = memsic;
    }

    public Boolean getSunspot() {
        return sunspot;
    }

    public void setSunspot(Boolean sunspot) {
        this.sunspot = sunspot;
    }

    public Boolean getXbee() {
        return xbee;
    }

    public void setXbee(Boolean xbee) {
        this.xbee = xbee;
    }
 
}
