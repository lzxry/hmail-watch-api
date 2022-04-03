package com.lzxry.hmail.component;

import com.lzxry.hmail.service.ListenerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.concurrent.TimeUnit;
 
/**
 * Created by wxl on 2018/12/13.
 */
@Slf4j
@Component
public class FileListenerFactory {

    // 设置监听路径
    @Value("${monitor.dir:D:\\install\\hMailServer\\Data}")
    private String monitorDir ;
    // 设置轮询间隔
    private final long interval = TimeUnit.SECONDS.toMillis(1);
 
    // 自动注入业务服务
    @Autowired
    private ListenerService listenerService;
 
    public FileAlterationMonitor getMonitor() {
        // 创建过滤器
        log.info("===in FileAlterationMonitor=== ");
        IOFileFilter directories = FileFilterUtils.and(
                FileFilterUtils.directoryFileFilter(),
                HiddenFileFilter.VISIBLE);
        IOFileFilter files = FileFilterUtils.and(
                FileFilterUtils.fileFileFilter(),
                FileFilterUtils.suffixFileFilter(".eml"));
        IOFileFilter filter = FileFilterUtils.or(directories, files);
 
        // 装配过滤器
//         FileAlterationObserver observer = new FileAlterationObserver(new File(monitorDir));
        FileAlterationObserver observer = new FileAlterationObserver(new File(monitorDir), filter);
        log.info("observer = "+observer);
        // 向监听者添加监听器，并注入业务服务
        observer.addListener(new FileListener(listenerService,monitorDir));

        // 返回监听者
        return new FileAlterationMonitor(interval, observer);
    }
}