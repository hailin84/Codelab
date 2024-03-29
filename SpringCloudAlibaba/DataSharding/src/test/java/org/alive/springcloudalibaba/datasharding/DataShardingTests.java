package org.alive.springcloudalibaba.datasharding;

import org.alive.springcloudalibaba.datasharding.biz.course.entity.Course;
import org.alive.springcloudalibaba.datasharding.biz.course.mapper.CourseMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataShardingTests {

    @Autowired
    private CourseMapper courseMapper;

    //添加课程
    @Test
    public void addCourse() {
        Course course = new Course();
        //cid由我们设置的策略，雪花算法进行生成
        course.setCname("Java");
        course.setUserId(100L);
        course.setStatus("Normal");
        courseMapper.insert(course);
    }
}
