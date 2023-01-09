package org.alive.springlab.service.impl;

import org.alive.springlab.entity.User;
import org.alive.springlab.mapper.UserMapper;
import org.alive.springlab.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hailin84
 * @since 2022-12-26
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
