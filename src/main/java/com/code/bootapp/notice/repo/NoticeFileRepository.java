package com.code.bootapp.notice.repo;

import com.code.bootapp.notice.entity.NoticeFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeFileRepository extends JpaRepository<NoticeFileEntity, Integer> {
}
