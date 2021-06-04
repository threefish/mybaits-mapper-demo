package com.example.mybaitsmapper.demo;

import io.mybatis.service.AbstractService;
import org.springframework.stereotype.Service;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@Service
public class UserServiceImpl extends AbstractService<User, Long, UserMapper> implements UserService {
}
