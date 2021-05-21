package com.tanhua.domain.db;

import com.tanhua.domain.db.BasePojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : TuGen
 * @date : 2021/5/10 18:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Announcement extends BasePojo {
    private String id;
    private String title;
    private String description;
}
