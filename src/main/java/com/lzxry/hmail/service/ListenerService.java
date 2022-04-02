package com.lzxry.hmail.service;

import com.lzxry.hmail.entity.EmailBaseInfo;
import com.lzxry.hmail.entity.EmailBaseInfoVO;

import java.io.File;

public interface ListenerService {

    void doMain(File file);

    EmailBaseInfoVO find(String toAddress);
}
