package com.zsj.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.zsj.common.valid.AddGroup;
import com.zsj.common.valid.ListValue;
import com.zsj.common.valid.UpdateGroup;
import lombok.Data;
import org.checkerframework.common.value.qual.MinLenFieldInvariant;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;

/**
 * 品牌
 * 
 * @author zhangshijie
 * @email 642011598@gmail.com
 * @date 2022-05-27 07:12:15
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 品牌id
	 */
	@TableId
	@Null(message = "新增不用带品牌id,甘你娘！",groups ={AddGroup.class} )
	@NotNull(message = "修改必须指定品牌id,甘你娘！",groups ={UpdateGroup.class} )
	private Long brandId;
	/**
	 * 品牌名
	 */
	@NotBlank(message = "添加的时候品牌名必须不为空,操！",groups = {AddGroup.class})
	private String name;
	/**
	 * 品牌logo地址
	 */
	@NotEmpty(message = "添加的时候网址名不能为空,尼玛",groups = {AddGroup.class})
	@URL(message = "必须填写一个URL地址,我检测贼几把严格懂吗",groups = {AddGroup.class,UpdateGroup.class})
	private String logo;
	/**
	 * 介绍
	 */
	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
	@NotNull(message = "添加的时候显示状态不能为空，干你娘！",groups = {AddGroup.class})
	@ListValue(vals={0,1},message = "这尼玛只能填0和1啊，0不显示，1显示",groups = {AddGroup.class,UpdateGroup.class})
	private Integer showStatus;
	/**
	 * 检索首字母
	 */
	@NotEmpty(message = "添加的时候检索首字母不能为空",groups = {AddGroup.class})
	@Pattern(regexp = "^[a-zA-Z]$",message = "检索首字母必须是一个字母",groups = {AddGroup.class,UpdateGroup.class})
	private String firstLetter;
	/**
	 * 排序
	 */
	@NotNull(message = "添加的时候排序不能为空，干你娘！",groups = {AddGroup.class})
	@Min(value = 0,message = "你必须传入一个大于0的数字，因为这是排序",groups = {AddGroup.class,UpdateGroup.class})
	private Integer sort;

}
