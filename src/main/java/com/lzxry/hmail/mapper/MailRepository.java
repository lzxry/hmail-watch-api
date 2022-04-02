package com.lzxry.hmail.mapper;

import com.lzxry.hmail.entity.EmailBaseInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MailRepository extends JpaRepository<EmailBaseInfo,Long> {

    List<EmailBaseInfo> findByToAddress(String toAddress);
}
