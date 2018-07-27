package com.sumslack.test.service.impl;

import com.sumslack.test.entity.User;
import com.sumslack.test.mapper.UserMapper;
import com.sumslack.test.service.IUserService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author you name
 * @since 2018-07-27
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

	@Override
	@Cacheable(value="user",key="#uid",unless = "#result==null")
	public User getUserById(String uid) {
		return selectOne(new EntityWrapper<User>().eq("uid", uid));
	}

}
