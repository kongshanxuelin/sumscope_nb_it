package com.sumslack.test.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sumslack.test.entity.User;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author you name
 * @since 2018-07-27
 */
public interface UserMapper extends SuperMapper<User> {
	@Select("select * from user")
    List<User> selectListBySQL();
	
	List<User> selectListByWrapper(@Param("nick") Wrapper wrapper);
}
