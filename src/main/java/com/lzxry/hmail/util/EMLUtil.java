package com.lzxry.hmail.util;

import com.lzxry.hmail.entity.EmailBaseInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;


@Slf4j
public class EMLUtil {


	//http://blog.csdn.net/aassdd_zz/article/details/8204344
	public static EmailBaseInfo parserFile(String emlPath) throws Exception {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		InputStream inMsg;
		inMsg = new FileInputStream(emlPath);
		Message msg = new MimeMessage(session, inMsg);
		return parseEml(msg);
	}
 
	private static EmailBaseInfo parseEml(Message msg) throws Exception {
        EmailBaseInfo emailBaseInfo = new EmailBaseInfo();
		emailBaseInfo.setReadNum(0);
        emailBaseInfo.setCreateDate(new Date());
        // 发件人信息
		Address[] froms = msg.getFrom();
		if (froms != null) {
			InternetAddress addr = (InternetAddress) froms[0];
            emailBaseInfo.setFromAddress(addr.getAddress());
		}
        emailBaseInfo.setSentDate(msg.getSentDate());
        Address[] allRecipients = msg.getAllRecipients();
        if (allRecipients != null) {
            InternetAddress addr = (InternetAddress) allRecipients[0];
            emailBaseInfo.setToAddress(addr.getAddress());
        }
        emailBaseInfo.setReceivedDate(msg.getReceivedDate());
        emailBaseInfo.setSubject(msg.getSubject());
		// getContent() 是获取包裹内容, Part相当于外包装
		Object o = msg.getContent();
		if (o instanceof Multipart) {
			Multipart multipart = (Multipart) o;
			reMultipart(multipart,emailBaseInfo);
		} else if (o instanceof Part) {
			Part part = (Part) o;
			rePart(part,emailBaseInfo);
		} else {
            emailBaseInfo.setContentType(msg.getContentType());

            emailBaseInfo.setContent(new javax.sql.rowset.serial.SerialClob(msg.getContent().toString().toCharArray()));
		}
		return emailBaseInfo;
	}
	
 
	/**
	 * @param part
	 *            解析内容
	 * @param emailBaseInfo
     * @throws Exception
	 */
	private static void rePart(Part part, EmailBaseInfo emailBaseInfo) throws Exception {
 
		if (part.getDisposition() != null) {
 
			String strFileNmae = part.getFileName();
			if(!StringUtils.isEmpty(strFileNmae))
			{	// MimeUtility.decodeText解决附件名乱码问题
				strFileNmae=MimeUtility.decodeText(strFileNmae);
				
				InputStream in = part.getInputStream();// 打开附件的输入流
				// 读取附件字节并存储到文件中
				java.io.FileOutputStream out = new FileOutputStream(strFileNmae);
				int data;
				while ((data = in.read()) != -1) {
					out.write(data);
				}
				in.close();
				out.close();
				
			}
            emailBaseInfo.setContentType(MimeUtility.decodeText(part.getContentType()));
            emailBaseInfo.setContent(new javax.sql.rowset.serial.SerialClob(part.getContent().toString().toCharArray()));
			
		} else {
			if (part.getContentType().startsWith("text/plain")) {
                emailBaseInfo.setContent(new javax.sql.rowset.serial.SerialClob(part.getContent().toString().toCharArray()));
			} else {
                emailBaseInfo.setContent(new javax.sql.rowset.serial.SerialClob(part.getContent().toString().toCharArray()));
				// log.info("HTML内容：" + part.getContent());
			}
		}
	}
 
	/**
	 * @param multipart
	 *            // 接卸包裹（含所有邮件内容(包裹+正文+附件)）
	 * @param emailBaseInfo
     * @throws Exception
	 */
	private static void reMultipart(Multipart multipart, EmailBaseInfo emailBaseInfo) throws Exception {
		// log.info("邮件共有" + multipart.getCount() + "部分组成");
		// 依次处理各个部分
		for (int j = 0, n = multipart.getCount(); j < n; j++) {
			// log.info("处理第" + j + "部分");
			Part part = multipart.getBodyPart(j);// 解包, 取出 MultiPart的各个部分,
													// 每部分可能是邮件内容,
			// 也可能是另一个小包裹(MultipPart)
			// 判断此包裹内容是不是一个小包裹, 一般这一部分是 正文 Content-Type: multipart/alternative
			if (part.getContent() instanceof Multipart) {
				Multipart p = (Multipart) part.getContent();// 转成小包裹
				// 递归迭代
				reMultipart(p, emailBaseInfo);
			} else {
				rePart(part, emailBaseInfo);
			}
		}
	}
	
	public static void test(String emlPath) {
		try {
 
			log.info(emlPath);
			Properties props = new Properties();
			Session session = Session.getDefaultInstance(props, null);
			InputStream inMsg;
			inMsg = new FileInputStream(emlPath);
			Message msg = new MimeMessage(session, inMsg);
 
			String[] date = msg.getHeader("Date");
			Address[] from = msg.getFrom();
			for (Address address : from) {
				InternetAddress internetAddress = (InternetAddress) address;
				log.info(internetAddress.getAddress());
				log.info(internetAddress.getPersonal());
			}
			log.info(msg.getSubject());
 
			Address[] to = msg.getReplyTo();
 
			Object o = msg.getContent();
 
			if (msg.isMimeType("multipart/*") || msg.isMimeType("MULTIPART/*")) {
				log.info("multipart");
				Multipart mp = (Multipart) o;
 
				int totalAttachments = mp.getCount();
				if (totalAttachments > 0) {
					for (int i = 0; i < totalAttachments; i++) {
						Part part = mp.getBodyPart(i);
						String s = getMailContent(part);
						String attachFileName = part.getFileName();
						String disposition = part.getDisposition();
						String contentType = part.getContentType();
						if ((attachFileName != null && attachFileName
								.endsWith(".ics"))
								|| contentType.indexOf("text/calendar") >= 0) {
							String[] dateHeader = msg.getHeader("date");
						}
 
						log.info(s);
						log.info(attachFileName);
						log.info(disposition);
						log.info(contentType);
						log.info("==============");
					}
					inMsg.close();
				}
			} else if (o instanceof Part) {
				Part part = (Part) o;
                EmailBaseInfo emailBaseInfo = new EmailBaseInfo();
                rePart(part, emailBaseInfo);
			} else {
				log.info("类型" + msg.getContentType());
				log.info("内容" + msg.getContent());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
 
	}


	public static String getMailContent(Part part) throws Exception {
		String contenttype = part.getContentType();
		int nameindex = contenttype.indexOf("name");
		boolean conname = false;
		if (nameindex != -1) {
			conname = true;
		}
		StringBuilder bodytext = new StringBuilder();
		if (part.isMimeType("text/plain") && !conname) {
			bodytext.append((String) part.getContent());
		} else if (part.isMimeType("text/html") && !conname) {
			bodytext.append((String) part.getContent());
		} else if (part.isMimeType("multipart/*")) {
			Multipart multipart = (Multipart) part.getContent();
			int counts = multipart.getCount();
			for (int i = 0; i < counts; i++) {
				getMailContent(multipart.getBodyPart(i));
			}
		} else if (part.isMimeType("message/rfc822")) {
			getMailContent((Part) part.getContent());
		} else {
		}
		return bodytext.toString();
	}
 
 
}