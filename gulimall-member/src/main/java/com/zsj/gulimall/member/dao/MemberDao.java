package com.zsj.gulimall.member.dao;

import com.zsj.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author zhangshijie
 * @email 642011598@gmail.com
 * @date 2022-05-28 01:01:47
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
