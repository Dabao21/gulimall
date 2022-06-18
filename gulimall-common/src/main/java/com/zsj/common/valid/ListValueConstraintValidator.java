package com.zsj.common.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

//老子把这个取名为 判断提交的信息是否在注解上写的信息的几把校验器
public class ListValueConstraintValidator implements ConstraintValidator<ListValue,Integer> {

    private Set<Integer> set=new HashSet<>();  //搞个set用来存注解上的信息

    //初始化校验方法
    @Override
    public void initialize(ListValue constraintAnnotation) {
        //ConstraintValidator.super.initialize(constraintAnnotation);
        int[] vals = constraintAnnotation.vals();    //拿到注解上的信息然后他妈的put到set里面去
        for (int val : vals) {
            set.add(val);
        }
    }
    //这里他妈是判断是否校验成功
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return set.contains(value);   //判断提交的信息是否符合注解上要求的信息
    }
}
