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
public class LoggerException implements Serializable {
    
    @Id
    @GeneratedValue
    private Long idLoggerException;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable=false)
    private Date dateException;
    @Column(nullable=false)
    private String message;
    @Column(nullable=false)
    private String stackTrace;
    @Column(nullable=false)
    private String sourceClass;
    @Column(nullable=false)
    private String sourceThread;
    

    public LoggerException() {
    }

    public LoggerException(Date dateException, String message, String stackTrace, String sourceClass, String sourceThread) {
        this.dateException = dateException;
        this.message = message;
        this.stackTrace = stackTrace;
        this.sourceClass = sourceClass;
        this.sourceThread = sourceThread;
    }

    public Date getDateException() {
        return dateException;
    }

    public void setDateException(Date dateException) {
        this.dateException = dateException;
    }

    public Long getIdLoggerException() {
        return idLoggerException;
    }

    public void setIdLoggerException(Long idLoggerException) {
        this.idLoggerException = idLoggerException;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public String getSourceClass() {
        return sourceClass;
    }

    public void setSourceClass(String sourceClass) {
        this.sourceClass = sourceClass;
    }

    public String getSourceThread() {
        return sourceThread;
    }

    public void setSourceThread(String sourceThread) {
        this.sourceThread = sourceThread;
    }
}
