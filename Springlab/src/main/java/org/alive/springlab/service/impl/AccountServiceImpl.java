package org.alive.springlab.service.impl;

import org.alive.springlab.entity.Account;
import org.alive.springlab.mapper.AccountMapper;
import org.alive.springlab.service.IAccountService;
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
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements IAccountService {

}
