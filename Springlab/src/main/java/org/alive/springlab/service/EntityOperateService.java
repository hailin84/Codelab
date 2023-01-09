package org.alive.springlab.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.alive.springlab.mapper.UserMapper;
import org.alive.springlab.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
@Service
public class EntityOperateService {

    @Resource
    private UserMapper userMapper;


    public List<User> getUsers() {
        Page<User> page = new Page<>(1, 6);
        IPage<User> result = userMapper.selectPage(page, null);
        return result.getRecords();
    }


    @Transactional(rollbackFor = Exception.class)
    public Long addUser(User user) {
        userMapper.insert(user);
        throw new RuntimeException("异常"); // 异常，事务回滚
        // return user.getId();
    }
}
