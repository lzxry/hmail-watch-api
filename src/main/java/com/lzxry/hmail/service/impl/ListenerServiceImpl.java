package com.lzxry.hmail.service.impl;

import com.lzxry.hmail.entity.EmailBaseInfo;
import com.lzxry.hmail.entity.EmailBaseInfoVO;
import com.lzxry.hmail.mapper.MailRepository;
import com.lzxry.hmail.service.ListenerService;
import com.lzxry.hmail.util.EMLUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

/**
 * @author liyin
 * @description
 * @date
 */
@Slf4j
@Service
public class ListenerServiceImpl implements ListenerService {

    @Autowired
    private MailRepository mailRepository;

    @SneakyThrows
    @Override
    public void doMain(File file) {
        Files.walkFileTree(Paths.get(file.getAbsolutePath()),
                new SimpleFileVisitor<Path>() {

                    @Override
                    public FileVisitResult visitFile(Path file,
                                                     BasicFileAttributes attrs) throws IOException {
                        try {
                            if (file.toFile().getAbsolutePath().endsWith(".eml")) {
                                EmailBaseInfo emailBaseInfo = EMLUtil.parserFile(file.toFile().getAbsolutePath());
                                if(emailBaseInfo.getToAddress()!=null){
                                    //入库
                                    mailRepository.saveAndFlush(emailBaseInfo);
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return super.visitFile(file, attrs);
                    }
                });
        //删除文件
        new File(file.getAbsolutePath()).delete();
    }

    @SneakyThrows
    @Override
    public EmailBaseInfoVO find(String toAddress){
        if(StringUtils.isEmpty(toAddress)){
            return null;
        }
        List<EmailBaseInfo> list = mailRepository.findByToAddress(toAddress);
        if(!CollectionUtils.isEmpty(list)){

            return new EmailBaseInfoVO(list.get(0));
        }
        return null;
    }
}
