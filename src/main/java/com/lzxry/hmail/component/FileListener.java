package com.lzxry.hmail.component;
 
import com.lzxry.hmail.service.ListenerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;
 
/**
 * Created by wxl on 2018/12/13.
 *
 * 文件变化监听器
 * 在Apache的Commons-IO中有关于文件的监控功能的代码. 文件监控的原理如下：
 * 由文件监控类FileAlterationMonitor中的线程不停的扫描文件观察器FileAlterationObserver，
 * 如果有文件的变化，则根据相关的文件比较器，判断文件时新增，还是删除，还是更改。（默认为1000毫秒执行一次扫描）
 */
@Slf4j
public class FileListener extends FileAlterationListenerAdaptor {

    // 声明业务服务
    private ListenerService listenerService;

    private String ignorPath;
 
    // 采用构造函数注入服务
    public FileListener(ListenerService listenerService,String ignorPath) {
        this.listenerService = listenerService;
        this.ignorPath = ignorPath;
    }
 
    // 文件创建执行
    @Override
    public void onFileCreate(File file) {
        if(file.getParent().equals(ignorPath)){
            return;
        }
        log.info("[新建]:" + file.getAbsolutePath());
        listenerService.doMain(file);
    }
 
    // 文件创建修改
    @Override
    public void onFileChange(File file) {
        if(file.getParent().equals(ignorPath)){
            return;
        }
        log.info("[修改]:" + file.getAbsolutePath());
        // 触发业务
//        listenerService.doSomething();
    }
 
    // 文件创建删除
    @Override
    public void onFileDelete(File file) {
        if(file.getParent().equals(ignorPath)){
            return;
        }
        log.info("[删除]:" + file.getAbsolutePath());
    }
 
    // 目录创建
    @Override
    public void onDirectoryCreate(File directory) {
        //log.info("[目录创建]:" + directory.getAbsolutePath());
    }
 
    // 目录修改
    @Override
    public void onDirectoryChange(File directory) {
        //log.info("[目录修改]:" + directory.getAbsolutePath());
    }
 
    // 目录删除
    @Override
    public void onDirectoryDelete(File directory) {
        //log.info("[目录删除]:" + directory.getAbsolutePath());
    }
 
 
    // 轮询开始
    @Override
    public void onStart(FileAlterationObserver observer) {
    }
 
    // 轮询结束
    @Override
    public void onStop(FileAlterationObserver observer) {
    }
 
}