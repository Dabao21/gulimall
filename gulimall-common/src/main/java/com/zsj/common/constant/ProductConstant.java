package com.zsj.common.constant;

public class ProductConstant {
    public enum AttrEnum{
        ATTR_TYPE_BASE(1,"规格参数属性"),ATTR_TYPE_SALE(0,"销售属性");
        private int code;
        private String msg;

        AttrEnum(int code,String msg){
            this.code=code;
            this.msg=msg;
        }

        public String getMsg() {
            return msg;
        }

        public int getCode() {
            return code;
        }
    }

}
