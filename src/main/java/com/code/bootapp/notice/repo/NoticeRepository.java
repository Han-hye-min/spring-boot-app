package com.code.bootapp.notice.repo;


import com.code.bootapp.notice.entity.NoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NoticeRepository extends JpaRepository<NoticeEntity, Integer> {

}
