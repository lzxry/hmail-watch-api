package com.lzxry.hmail.component;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
 
/**
 * Created by wxl on 2018/12/13.
 */
@Slf4j
@Component
public class FileListenerRunner implements CommandLineRunner {


    @Autowired
    private FileListenerFactory fileListenerFactory;
 
    @Override
    public void run(String... strings) throws Exception {
        // 创建监听者

        log.info("fileListenerFactory = "+fileListenerFactory);
        FileAlterationMonitor fileAlterationMonitor = fileListenerFactory.getMonitor();
        try {
            // do not stop this thread
            fileAlterationMonitor.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}