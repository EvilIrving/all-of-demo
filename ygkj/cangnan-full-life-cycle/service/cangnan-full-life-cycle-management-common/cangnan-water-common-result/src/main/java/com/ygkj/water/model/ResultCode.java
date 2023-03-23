package com.ygkj.water.model;

public interface ResultCode {

    /**
     * 操作是否成功,true为成功，false操作失败
     * @return: boolean
     * @Date: 2020/8/13 11:26
     * @params:[]
     */
    /*boolean success();*/

    /**
     * 操作码
     * @return: int
     * @Date: 2020/8/13 11:26
     * @params:[]
     */
    int code();

    /**
     * 提示信息
     * @return: java.lang.String
     * @Date: 2020/8/13 11:27
     * @params:[]
     */
    String message();
}
