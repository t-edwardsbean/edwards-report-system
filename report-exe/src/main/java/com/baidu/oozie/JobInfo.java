package com.baidu.oozie;

import java.util.Date;

/**
 * Created by edwardsbean on 14-8-21.
 */
public class JobInfo {

    //某个定时任务某天的作业id
    private int actionId;
    //执行时间
    private Date executeTime;
    //作业执行结果
    private String executeResult;
    //执行状态
    private Status status;
    //任务类型
    private Types types;

    public static enum Types {
        ACTION, COORDINATOR, WORKFLOW
    }

    public static enum Status {
        PREMATER, PREP, RUNNING, SUSPENDED, SUCCEEDED, KILLED, FAILED, PAUSED, PREPPAUSED, PREPSUSPENDED, RUNNINGWITHERROR, SUSPENDEDWITHERROR, PAUSEDWITHERROR, DONEWITHERROR
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getExecuteResult() {
        return executeResult;
    }

    public void setExecuteResult(String executeResult) {
        this.executeResult = executeResult;
    }

    public Date getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Date executeTime) {
        this.executeTime = executeTime;
    }

    public int getActionId() {
        return actionId;
    }

    public void setActionId(int actionId) {
        this.actionId = actionId;
    }

}
