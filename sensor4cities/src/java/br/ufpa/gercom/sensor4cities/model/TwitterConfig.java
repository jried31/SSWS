/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author leokassio
 */
@Entity
public class TwitterConfig implements Serializable {
    
    @Id
    @GeneratedValue
    private Long idTwitterConfig;
    @Column(nullable=false)
    private String twitterUser;
    @Column(nullable=false)
    private String consumerSecret;
    @Column(nullable=false)
    private String consumerKey;
    @Column(nullable=false)
    private String token;
    @Column(nullable=false)
    private String tokenSecret;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTwitterConfig;
    @Column(nullable=false)
    private Boolean valid;

    public TwitterConfig() {
    }

    public TwitterConfig(String twitterUser, String consumerSecret, String consumerKey, String token, String tokenSecret, Date dateTwitterConfig, Boolean valid) {
        this.twitterUser = twitterUser;
        this.consumerSecret = consumerSecret;
        this.consumerKey = consumerKey;
        this.token = token;
        this.tokenSecret = tokenSecret;
        this.dateTwitterConfig = dateTwitterConfig;
        this.valid = valid;
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    public Date getDateTwitterConfig() {
        return dateTwitterConfig;
    }

    public void setDateTwitterConfig(Date dateTwitterConfig) {
        this.dateTwitterConfig = dateTwitterConfig;
    }

    public Long getIdTwitterConfig() {
        return idTwitterConfig;
    }

    public void setIdTwitterConfig(Long idTwitterConfig) {
        this.idTwitterConfig = idTwitterConfig;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    public void setTokenSecret(String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    public String getTwitterUser() {
        return twitterUser;
    }

    public void setTwitterUser(String twitterUser) {
        this.twitterUser = twitterUser;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }
    
}
