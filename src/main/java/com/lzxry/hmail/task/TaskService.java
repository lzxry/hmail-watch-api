package com.lzxry.hmail.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author felord.cn
 * @since 11:02
 **/
@Slf4j
@Component
public class TaskService {

    @Value("${monitor.dir:D:\\install\\hMailServer\\Data}")
    private String path;

    @Scheduled(fixedDelay = 60000)
    public void task() {
        log.info("定时清理：start");
        func(new File(path));
        log.info("定时清理：end");
    }



    private static Date getCreateTime(String fullFileName){
        Path path= Paths.get(fullFileName);
        BasicFileAttributeView basicview= Files.getFileAttributeView(path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS );
        BasicFileAttributes attr;
        try {
            attr = basicview.readAttributes();
            Date createDate = new Date(attr.creationTime().toMillis());
            return createDate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.set(1970, 0, 1, 0, 0, 0);
        return cal.getTime();
    }




        private static void func(File file){
            File[] fs = file.listFiles();
            if(fs==null){
                return;
            }
            for(File f:fs){
                if(f.isDirectory()){
                    func(f);
                }
                String absolutePath = f.getAbsolutePath();
                File file1 = new File(absolutePath);
                if(file1.isFile() && file1.exists() && absolutePath.endsWith(".eml"))		//若是文件，直接打印
                {
                    Date createTime = getCreateTime(absolutePath);
                    if((System.currentTimeMillis()-createTime.getTime()) >300000L){
                        boolean delete = file1.delete();
                       // log.info("删除状态：{}，路径：{}",delete,absolutePath);
                    }
                }

            }
        }

}
