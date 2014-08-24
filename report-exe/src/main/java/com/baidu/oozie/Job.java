package com.baidu.oozie;

import java.util.Date;

/**
 * Created by edwardsbean on 14-8-19.
 */
public class Job {
    //hive语句
    private String hql;
    //任务开始时间
    private Date startTime;
    //任务结束时间
    private Date endTime;
    //任务执行周期
    private String frequency;
    //用于生成hql脚本路径
    private long reportId;
    //hql在hdfs上的路径
    private String hqlPath;
    //hql语句，包含insert overwrite into到Hdfs目录的语句
    private String formatHql;

    public long getReportId() {
        return reportId;
    }

    public void setReportId(long reportId) {
        this.reportId = reportId;
    }

    public String getHqlPath() {
        if (hql != null && reportId != 0)
            hqlPath = "/user/oozie/91report/" + reportId + "/hive.sql";
        return hqlPath;
    }

    public String getHql() {
        return hql;
    }

    public void setHql(String hql) {
        this.hql = hql;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getFormatHql(){
        if(formatHql == null){
            int index = hql.indexOf(";");
            index ++;
            formatHql = hql.substring(0,index)
                    +"insert overwrite directory "
                    + "'/user/oozie/91report/" + reportId + "/${date}'"
                    + " "
                    + hql.substring(index);

        }
        return formatHql;
    }
}
