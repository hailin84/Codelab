package org.alive.springlab.service.impl;

import org.alive.springlab.entity.Student;
import org.alive.springlab.mapper.StudentMapper;
import org.alive.springlab.service.IStudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 学生信息 服务实现类
 * </p>
 *
 * @author hailin84
 * @since 2022-12-26
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {

}
