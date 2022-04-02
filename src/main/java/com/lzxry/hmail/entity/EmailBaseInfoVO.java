package com.lzxry.hmail.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Date;

/**
 * @author liyin
 * @description
 * @date
 */
@Data
@NoArgsConstructor
public class EmailBaseInfoVO {

    private Long id;
    private String subject;
    private String fromAddress;
    private String toAddress;
    private String content;
    private String contentType;
    private Date sentDate;
    private Date receivedDate;
    private Date createDate;

    public EmailBaseInfoVO(EmailBaseInfo emailBaseInfo) throws SQLException, IOException {
        this.id = emailBaseInfo.getId();
        this.subject = emailBaseInfo.getSubject();
        this.fromAddress = emailBaseInfo.getFromAddress();
        this.toAddress = emailBaseInfo.getToAddress();
        this.content = emailBaseInfo.getContent()==null?"":this.ClobToString(emailBaseInfo.getContent());
        this.contentType = emailBaseInfo.getContentType();
        this.sentDate = emailBaseInfo.getSentDate();
        this.receivedDate = emailBaseInfo.getReceivedDate();
        this.createDate = emailBaseInfo.getCreateDate();
    }


    // Clob类型 转String
    public String ClobToString(Clob clob) throws SQLException, IOException {
        String reString = "";
        Reader is = clob.getCharacterStream();// 得到流
        BufferedReader br = new BufferedReader(is);
        String s = br.readLine();
        StringBuffer sb = new StringBuffer();
        while (s != null) {// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
            sb.append(s);
            s = br.readLine();
        }
        reString = sb.toString();
        if(br!=null){
            br.close();
        }
        if(is!=null){
            is.close();
        }
        return reString;
    }
}
