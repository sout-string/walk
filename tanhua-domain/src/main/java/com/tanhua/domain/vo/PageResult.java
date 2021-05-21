package com.tanhua.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author : TuGen
 * @date : 2021/5/8 23:07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> implements Serializable {
    private Long counts;
    private Long pagesize;
    private Long pages;
    private Long page;
    private List<T> items = Collections.emptyList();
}
