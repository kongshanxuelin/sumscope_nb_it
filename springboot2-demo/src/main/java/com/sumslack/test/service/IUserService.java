package com.sumslack.test.service;

import com.sumslack.test.entity.User;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author you name
 * @since 2018-07-27
 */
public interface IUserService extends IService<User> {
	public User getUserById(String uid);
}
