package com.lzxry.hmail.task;

import java.io.File;
import java.nio.file.*;
import java.util.LinkedList;

public class WatchFile
{
    public static void main(String[] args) 
            throws Exception{

        String filePath = ("G:\\data\\test");

        // 获取文件系统的WatchService对象
        WatchService watchService = FileSystems.getDefault().newWatchService();
        Paths.get(filePath).register(watchService 
                , StandardWatchEventKinds.ENTRY_CREATE);

        File file = new File(filePath);
        LinkedList<File> fList = new LinkedList<File>();
        fList.addLast(file);
        while (fList.size() > 0 ) {
            File f = fList.removeFirst();
            if(f.listFiles() == null)
                continue;
            for(File file2 : f.listFiles()){
                    if (file2.isDirectory()){//下一级目录
                    fList.addLast(file2);
                    //依次注册子目录
                    Paths.get(file2.getAbsolutePath()).register(watchService 
                            , StandardWatchEventKinds.ENTRY_CREATE);
                }
            }
        }

        while(true)
        {
            // 获取下一个文件改动事件
            WatchKey key = watchService.take();
            for (WatchEvent<?> event : key.pollEvents()) 
            {
                System.out.println(event.context() +" --> " + event.kind());
                if(event.context().toString().endsWith(".eml")){
                    //读取邮件入库
                    //删除
                }else{//文件夹
                }
            }
            // 重设WatchKey
            boolean valid = key.reset();
            // 如果重设失败，退出监听
            if (!valid && key.pollEvents()!=null && key.pollEvents().size()>0)
            {
                break;
            }
        }
    }
}