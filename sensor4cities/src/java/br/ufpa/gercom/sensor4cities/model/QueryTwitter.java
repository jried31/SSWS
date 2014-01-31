/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author leokassio
 */
@Entity
public class QueryTwitter implements Serializable {
    
    @Id
    @GeneratedValue
    private Long idQueryTwitter;
    private Long idTweet;
    private String tweet;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateQuery;
    private String userQuery;
    private Boolean attended;
    @OneToMany(mappedBy = "queryTwitter")
    private List<ReplyTwitter> replyTwitter;

    public QueryTwitter() {
        attended = false;
    }

    public QueryTwitter(Long idTweet, String tweet, Date dateQuery, String userQuery, Boolean attended) {
        super();
        this.idTweet = idTweet;
        this.tweet = tweet;
        this.dateQuery = dateQuery;
        this.userQuery = userQuery;
        this.attended = attended;
    }

    public Boolean getAttended() {
        return attended;
    }

    public void setAttended(Boolean attended) {
        this.attended = attended;
    }

    public Date getDateQuery() {
        return dateQuery;
    }

    public void setDateQuery(Date dateQuery) {
        this.dateQuery = dateQuery;
    }

    public Long getIdQueryTwitter() {
        return idQueryTwitter;
    }

    public void setIdQueryTwitter(Long idQueryTwitter) {
        this.idQueryTwitter = idQueryTwitter;
    }

    public Long getIdTweet() {
        return idTweet;
    }

    public void setIdTweet(Long idTweet) {
        this.idTweet = idTweet;
    }

    public List<ReplyTwitter> getReplyTwitter() {
        return replyTwitter;
    }

    public void setReplyTwitter(List<ReplyTwitter> replyTwitter) {
        this.replyTwitter = replyTwitter;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public String getUserQuery() {
        return userQuery;
    }

    public void setUserQuery(String userQuery) {
        this.userQuery = userQuery;
    }

}
