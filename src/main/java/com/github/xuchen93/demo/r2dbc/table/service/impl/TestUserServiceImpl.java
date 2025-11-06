package com.github.xuchen93.demo.r2dbc.table.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.xuchen93.demo.r2dbc.table.dao.TestUserMapper;
import com.github.xuchen93.demo.r2dbc.table.entity.TestUser;
import com.github.xuchen93.demo.r2dbc.table.service.TestUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author xuchen.wang
 * @since 2025-10-10
 */
@Service
public class TestUserServiceImpl extends ServiceImpl<TestUserMapper, TestUser> implements TestUserService {

}
