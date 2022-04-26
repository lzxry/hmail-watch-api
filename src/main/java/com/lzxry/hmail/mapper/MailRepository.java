package com.lzxry.hmail.mapper;

import com.lzxry.hmail.entity.EmailBaseInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MailRepository extends JpaRepository<EmailBaseInfo,Long> {

    @Query(value = "select * from email_base_info where to_address = ?1 order by create_date desc limit ?2",nativeQuery = true)
    List<EmailBaseInfo> findByToAddress(String toAddress, Integer size);

    @Query(value = "update email_base_info set  read_num = ?2 where id= ?1",nativeQuery = true)
    @Modifying
    @Transactional
    void updateReadNum(Long id, Integer readNum);
}
