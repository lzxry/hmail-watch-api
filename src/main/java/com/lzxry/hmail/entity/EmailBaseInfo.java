package com.lzxry.hmail.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Clob;
import java.util.Date;

/**
 * @author liyin
 * @description
 * @date
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailBaseInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String subject;
    private String fromAddress;
    private String toAddress;
    private Clob content;
    private Integer readNum;
    private String contentType;
    private Date sentDate;
    private Date receivedDate;
    private Date createDate;


}
