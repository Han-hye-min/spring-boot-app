package com.code.bootapp.notice.service;

import com.code.bootapp.notice.dto.Notice;
import com.code.bootapp.notice.entity.NoticeEntity;
import com.code.bootapp.notice.repo.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public Notice.Response getNoticeList(Map<String, Object> param) throws Exception {
        int nowPage = (int)param.get("nowPage");
        // 정렬 기준
        Sort sortBy = Sort.by("noId").descending();
        // Pageable 객체 생성(보여줄 페이지 번호, 한 페이지에 출력할 개수, sort)
        Pageable pageable = PageRequest.of(nowPage - 1, 10, sortBy);
        Page<NoticeEntity> entities = noticeRepository.findAll(pageable);

        List<Notice.NoticeDTO> noticeList =
                entities.getContent().stream()
                        .map(Notice.NoticeDTO::of).collect(Collectors.toList());

        return Notice.Response
                .builder()
                .noticeList(noticeList)
                .nowPage(nowPage)
                .totalRows(entities.getTotalElements())
                .build();
    }
}
