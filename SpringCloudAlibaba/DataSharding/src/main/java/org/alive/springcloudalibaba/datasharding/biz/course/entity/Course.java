package org.alive.springcloudalibaba.datasharding.biz.course.entity;

import lombok.Data;

@Data
public class Course {
    private Long cid;

    private String cname;

    private Long userId;

    private String status;
}
