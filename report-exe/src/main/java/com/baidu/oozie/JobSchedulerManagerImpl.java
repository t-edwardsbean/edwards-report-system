package com.baidu.oozie;

import com.baidu.hadoop.Constants;
import com.baidu.hadoop.HdfsUtil;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.oozie.client.CoordinatorAction;
import org.apache.oozie.client.CoordinatorJob;
import org.apache.oozie.client.OozieClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by edwardsbean on 14-8-18.
 */
public class JobSchedulerManagerImpl implements JobSchedulerManager {
    public static final Logger LOG = LoggerFactory.getLogger(JobSchedulerManagerImpl.class);
    private OozieClient wc;
    private Properties conf;

    public JobSchedulerManagerImpl() {
        wc = new OozieClient(Constants.OOZIE_URL);
        //固定参数，从配置文件中读取，如jobTracker,nameNode
        conf = wc.createConfiguration();
        conf.setProperty("jobTracker", Constants.JOBTRACKER);
        conf.setProperty("nameNode", Constants.NAMENODE);
        conf.setProperty("oozie.use.system.libpath", "true");
        conf.setProperty("workflowAppUri", Constants.OOZIE_WORKFLOW_PATH);
        conf.setProperty("metastore", Constants.HIVE_METASTORE);
    }

    /**
     * 部署任务:
     * - 上传hql脚本到对应的hdfs路径
     * - 配置coordinator，workflow的参数
     * [x] 上传coordinator.xml到对应的hdfs路径
     *
     * @param job
     */
    private void deploy(Job job) throws RuntimeException {
        String localPath = Constants.TMP_PATH + System.currentTimeMillis();
        File file = new File(localPath);
        try {
            Files.write(job.getFormatHql(), file, Charsets.UTF_8);
            LOG.debug("在本地写入hql脚本" + localPath);
            LOG.debug("hql:" + job.getHql());
        } catch (IOException e) {
            LOG.error(ExceptionUtils.getFullStackTrace(e));
            throw new RuntimeException("本地磁盘无法写入，保存hive脚本出错");
        }
        LOG.debug("将本地的hql脚本上传到hdfs上:" + job.getHqlPath());
        HdfsUtil.upload(localPath, job.getHqlPath());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        long startTime;
        long endTime;
        //oozie服务器用的是UTC时间，而不是中国这边的时区
        if(Constants.OOZIE_UTC == "true"){
             startTime = job.getStartTime().getTime()-3600000 * 8;
             endTime = job.getEndTime().getTime()-3600000 * 8;
        }else {
            startTime = job.getStartTime().getTime();
            endTime = job.getEndTime().getTime();
        }

        conf.setProperty("start", sdf.format(startTime));
        conf.setProperty("end", sdf.format(endTime));
        conf.setProperty("reportId",job.getReportId()+"");
        conf.setProperty(wc.COORDINATOR_APP_PATH, Constants.OOZIE_COORDINATOR_PATH);
        //配置频率，目前是固定每天执行一次
//        conf.setProperty("frequency",job.getFrequency());
        conf.setProperty("hql", Constants.NAMENODE + job.getHqlPath());
    }

    @Override
    public String run(Job job) throws RuntimeException {

        deploy(job);
        String jobId = null;
        try {
            jobId = wc.run(conf);
        } catch (Exception e) {
            LOG.error(ExceptionUtils.getFullStackTrace(e));
            throw new RuntimeException("调度器异常，请联系管理员");
        }
        return jobId;
    }

    @Override
    public String reRun(String jobId) {
        return null;
    }

    @Override
    public String getJobs() {
        return null;
    }

    @Override
    public List<JobInfo> getCoordinators(String jobId,long reportId,int dayScope) {
        //过滤，获取job为SUCCESS的action
        //filter = OozieClient.FILTER_STATUS+"=" + Job.status.SUCCEED;
        String filter = "";
        CoordinatorJob coordinatorJob;
        try {
            coordinatorJob = wc.getCoordJobInfo(jobId, filter, 1, dayScope);
        } catch (Exception e) {
            LOG.error(ExceptionUtils.getFullStackTrace(e));
            throw new RuntimeException("获取调度任务列表失败，jobId:" + jobId);
        }
        List<CoordinatorAction> list = coordinatorJob.getActions();
        List<JobInfo> jobInfos = new ArrayList<JobInfo>();
        for (CoordinatorAction coordinatorAction : list) {
            JobInfo jobInfo = new JobInfo();
            jobInfo.setActionId(coordinatorAction.getActionNumber());
            jobInfo.setExecuteTime(coordinatorAction.getCreatedTime());
            if(coordinatorAction.getStatus().equals(org.apache.oozie.client.Job.Status.SUCCEEDED)){
                jobInfo.setStatus(JobInfo.Status.SUCCEEDED);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
            String date = sdf.format(coordinatorAction.getCreatedTime());
            LOG.debug("获取该报表任务" +date+ "的数据");
            String result = HdfsUtil.read(Constants.HIVE_BASE_PATH + reportId + "/" + date + "/000000_0");
            jobInfo.setExecuteResult(result);
            jobInfos.add(jobInfo);

        }
        return jobInfos;
    }

    @Override
    public void killCoordinator(String jobId) throws RuntimeException{
        try {
            wc.kill(jobId);
        } catch(Exception e) {
            LOG.error(ExceptionUtils.getFullStackTrace(e));
            throw new RuntimeException("停止任务失败");
        }
    }

    @Override
    public void killCoordinatorAction(String actionId) {

    }

    @Override
    public String getJobDefinition(String jobId) {
        return null;
    }

    @Override
    public String getJobLog(String jobId) {
        return null;
    }

    @Override
    public String getJobResult(String jobId, String date) {
        return null;
    }

    @Override
    public Map<String, String> getJobResult(String jobId, String start, String end) {
        return null;
    }
}
