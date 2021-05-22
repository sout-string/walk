package com.tanhua.server.controller;

import com.tanhua.domain.db.Question;
import com.tanhua.domain.vo.NearUserVo;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.domain.vo.RecommendUserQueryParam;
import com.tanhua.domain.vo.RecommendUserVo;
import com.tanhua.dubbo.api.QuestionApi;
import com.tanhua.server.service.IMService;
import com.tanhua.server.service.LocationService;
import com.tanhua.server.service.RecommendUserService;
import com.tanhua.server.service.TodayBestService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author : TuGen
 * @date : 2021/5/10 20:15
 */
@RestController
@RequestMapping("/tanhua")
@Slf4j
public class RecommendUserController {
    @Autowired
    private RecommendUserService recommendUserService;
    @Autowired
    private IMService imService;
    @Autowired
    private LocationService locationService;

    /**
     * 查询首页推荐
     *
     * @param recommendUserQueryParam
     * @return
     */
    @GetMapping("/recommendation")
    public ResponseEntity recommendList(RecommendUserQueryParam recommendUserQueryParam) {
        log.info("RecommendUserController-查询首页推荐");
        PageResult<RecommendUserVo> pageResult = recommendUserService.recommendList(recommendUserQueryParam);
        return ResponseEntity.ok(pageResult);
    }

    /**
     * 获取佳人信息
     *
     * @param userId
     * @return
     */
    @GetMapping("/{id}/personalInfo")
    public ResponseEntity<RecommendUserVo> queryUserDetail(@PathVariable("id") Long userId) {
        log.info("RecommendUserController-获取佳人信息");
        RecommendUserVo userInfoVo = recommendUserService.getUserInfo(userId);
        return ResponseEntity.ok(userInfoVo);
    }

    /**
     * 查询用户设置的陌生人问题
     *
     * @param userId
     * @return
     */
    @GetMapping("/strangerQuestions")
    public ResponseEntity<String> strangerQuestions(Long userId) {
        log.info("RecommendUserController-查询用户陌生人问题");
        String question = recommendUserService.queryStrangerQuestions(userId);
        return ResponseEntity.ok(question);
    }

    /**
     * 回复陌生人问题
     *
     * @param paramMap
     * @return
     */
    @PostMapping("/strangerQuestions")
    public ResponseEntity replyStrangerQuestions(@RequestBody Map<String, Object> paramMap) {
        log.info("RecommendUserController-回复陌生人问题");
        imService.replyStrangerQuestions(paramMap);
        return ResponseEntity.ok(null);
    }

    /**
     * 搜索附近
     *
     * @return
     */
    @GetMapping("/search")
    public ResponseEntity searchNearBy(@RequestParam(required = false) String gender, @RequestParam(defaultValue = "200") String distance) {
        log.info("RecommendUserController-搜索附近");
        List<NearUserVo> list = locationService.searchNearBy(gender, distance);
        return ResponseEntity.ok(list);
    }
}
