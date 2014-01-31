/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author leokassio
 */
@Entity
public class ReplyTwitter implements Serializable {
    
    @Id
    @GeneratedValue
    private Long idReply;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateReply;
    private String tweet;
    @ManyToOne
    private QueryTwitter queryTwitter;
    private Boolean published;

    public ReplyTwitter() {
        published = false;
    }

    public ReplyTwitter(Date dateReply, String tweet, QueryTwitter queryTwitter, Boolean published) {
        this.dateReply = dateReply;
        this.tweet = tweet;
        this.queryTwitter = queryTwitter;
        this.published = published;
    }

    public Date getDateReply() {
        return dateReply;
    }

    public void setDateReply(Date dateReply) {
        this.dateReply = dateReply;
    }

    public Long getIdReply() {
        return idReply;
    }

    public void setIdReply(Long idReply) {
        this.idReply = idReply;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public QueryTwitter getQueryTwitter() {
        return queryTwitter;
    }

    public void setQueryTwitter(QueryTwitter queryTwitter) {
        this.queryTwitter = queryTwitter;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }    
    
}
