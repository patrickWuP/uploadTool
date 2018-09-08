package com.wup.bean;

/**
 * @Description : 操作结果对象
 * @Project : imoocsecurity
 * @Program Name  : com.wup.bean.OperateResult
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
public class OperateResult {

    public static final int ERROR_STATUS = 999;
    public static final int SUCCESS_STATUS = 1000;
    //执行状态,1000代表成，999代表执行失败
    private int status;

    //执行后消息
    private String msg;

    private OperateResult(){
    }
    private OperateResult(int status,String msg){
        this.status = status;
        this.msg = msg;
    }
    //创建执行失败结果
    public static OperateResult ErrorResult(String msg){
        OperateResult result = new OperateResult(ERROR_STATUS,msg);
        return result;
    }
    //创建执行成功结果
    public static OperateResult SuccessResult(){
        OperateResult result = new OperateResult(SUCCESS_STATUS,"");
        return result;
    }
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
