package com.baidu.oozie;


import java.util.List;
import java.util.Map;

/**
 * Created by edwardsbean on 14-8-19.
 */
public interface JobSchedulerManager {
    /**
     * 提交hive作业
     *
     * @return job
     */
    public String run(Job job) throws RuntimeException;

    /**
     * 任务重跑
     *
     * @param jobId
     * @return
     */
    public String reRun(String jobId);

    /**
     * 返回oozie的jobs列表
     *
     * @return
     */
    public String getJobs();

    /**
     * 返回coordinators，基于时间调度的工作
     *
     * @return List<JobInfo>
     */
    public List<JobInfo> getCoordinators(String jobId, long reportId, int dayScope);

    /**
     * 返回Job的配置，定义
     * @param jobId
     * @return
     */
    public String getJobDefinition(String jobId);

    /**
     * 返回作业运行记录？
     *
     * @param jobId
     * @return
     */
    public String getJobLog(String jobId);

    /**
     * 返回报表的计算结果
     * @param jobId 哪一个作业
     * @param date 哪一天计算的结果
     * @return
     */
    public String getJobResult(String jobId, String date);

    /**
     * 按照日期范围，返回计算结果
     * @param jobId
     * @param start
     * @param end
     * @return Map<日期：计算结果>
     */
    public Map<String,String> getJobResult(String jobId, String start, String end);


    public void killCoordinator(String jobId);
    public void killCoordinatorAction(String actionId);
}
