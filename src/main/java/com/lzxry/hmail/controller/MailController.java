package com.lzxry.hmail.controller;

import com.lzxry.hmail.entity.EmailBaseInfo;
import com.lzxry.hmail.entity.EmailBaseInfoVO;
import com.lzxry.hmail.service.ListenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liyin
 * @description
 * @date
 */
@RestController
public class MailController {
    @Autowired
    private ListenerService listenerServiceImpl;

    @RequestMapping("/find")
    public EmailBaseInfoVO find(String toAddress){
        return listenerServiceImpl.find(toAddress);
    }
}
