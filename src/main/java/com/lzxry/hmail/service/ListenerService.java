package com.lzxry.hmail.service;

import com.lzxry.hmail.entity.EmailBaseInfoVO;

import java.io.File;
import java.util.List;

public interface ListenerService {

    void doMain(File file);

    List<EmailBaseInfoVO> find(String toAddress, Integer size);
}
