package com.tanhua.server.controller;

import com.tanhua.server.service.LocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author : TuGen
 * @date : 2021/5/19 15:19
 */
@RestController
@RequestMapping("/baidu")
@Slf4j
public class LocationController {
    @Autowired
    private LocationService locationService;

    /**
     * 上报地理位置
     *
     * @return
     */
    @PostMapping("/location")
    public ResponseEntity reportLocation(@RequestBody Map<String, Object> paramMap) {
        log.info("LocationController-上报地理位置");
        locationService.reportLocation(paramMap);
        return ResponseEntity.ok(null);
    }
}
