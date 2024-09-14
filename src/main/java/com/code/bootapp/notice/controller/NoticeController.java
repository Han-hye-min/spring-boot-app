package com.code.bootapp.notice.controller;


import com.code.bootapp.notice.dto.Notice;
import com.code.bootapp.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class NoticeController {

    private final NoticeService noticeService;


    @GetMapping("/notice/{nowPage}")
    public ResponseEntity<Notice.Response> getNoticeList(@PathVariable("nowPage") int nowPage) {
        Map<String, Object> param = new HashMap<>();
        param.put("nowPage", nowPage);
        //해더 선언
        HttpHeaders headers = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.OK;
        Notice.Response response = null;

        try{
            //"application/json;utf-8"
            headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
            response = noticeService.getNoticeList(param);

        }catch (Exception e) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR; // 500 에러
            e.printStackTrace();
        }
 
        return new ResponseEntity<>(response, headers, httpStatus);


    }



}
