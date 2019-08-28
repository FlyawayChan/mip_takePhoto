package com.mip.biz.oms.yf_sync.service.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.mip.biz.oms.yf_sync.db.DBUtils;
import com.mip.biz.oms.yf_sync.service.YfSyncService;
import com.mip.core.basebiz.dto.DataRecord;
import com.mip.core.basebiz.service.ServiceException;
import com.mip.core.basebiz.service.ServiceRuntimeException;
import com.mip.core.basebiz.service.impl.BizServiceImpl;
import com.mip.core.util.ServiceLocator;

public class YfSyncServiceImpl extends BizServiceImpl implements YfSyncService {
    private static Logger logger = LoggerFactory.getLogger(YfSyncServiceImpl.class);
    private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void saveSyncData() {
        try {
            //获取最新的数据同步时间
            DataRecord record = this.getNewSyncTimeRecord("yyzh_zygk_zyjh");

            Calendar lastTime = Calendar.getInstance();
            if(record != null){
                String syncTime = record.getString("SYNC_TIME");
                lastTime.setTime(sdf2.parse(syncTime));
                lastTime.add(Calendar.DAY_OF_YEAR,1);
            }else{
            	lastTime.set(Calendar.MONTH,0);
            	lastTime.set(Calendar.DAY_OF_MONTH,1);
            }
            lastTime.set(Calendar.HOUR_OF_DAY,0);
            lastTime.set(Calendar.MINUTE,0);
            lastTime.set(Calendar.SECOND,0);

            Calendar now = Calendar.getInstance();
            while(lastTime.before(now)){
                Calendar beginCalendar = Calendar.getInstance();
                beginCalendar.setTime(lastTime.getTime());
                beginCalendar.set(Calendar.HOUR_OF_DAY,0);
                beginCalendar.set(Calendar.MINUTE,0);
                beginCalendar.set(Calendar.SECOND,0);

                Calendar endCalendar = Calendar.getInstance();
                endCalendar.setTime(lastTime.getTime());
                endCalendar.set(Calendar.HOUR_OF_DAY,23);
                endCalendar.set(Calendar.MINUTE,59);
                endCalendar.set(Calendar.SECOND,59);

                handleData(beginCalendar.getTime(),endCalendar.getTime());
                lastTime.add(Calendar.DAY_OF_YEAR,1);
            }
            DataRecord dd = new DataRecord();
            dd.put("ID",this.generateId(null));
            dd.put("SYNC_TIME", now.getTime());
            dd.put("TABLE_NAME", "yyzh_zygk_zyjh");
            dd.put("TO_TABLE_NAME", "YD_EPCBM.MIP_PATROL_TASK");
            this.getDao().insert("YD_YF_SYNCH.YF_SYNC_RECORD",dd);
        } catch (Exception e) {
            logger.error("YfSyncServiceImpl.saveSyncData()",e);
        }
    }


    private void handleData(Date beginTime,Date endTime) throws ServiceException {
        DataSourceTransactionManager transactionManager = (DataSourceTransactionManager) ServiceLocator.getBean("transactionManager");
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            handleDataImpl(beginTime,endTime);
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
    }

    private void handleDataImpl(Date beginTime,Date endTime) throws ServiceException{
        logger.info("handleDataImpl:beginTime:{},endTime:{}",beginTime,endTime);
        Map<String, Object> param = new HashMap<String, Object>();

        param.clear();
        param.put("ORI_TABLE_NAME", "YD_EPCBM.MIP_PATROL_TASK");

        List<DataRecord> sDatas  = this.getSyncData(beginTime, endTime);
        for(DataRecord dd : sDatas){
            Long taskId = this.getTaskId(dd.getString("PLAN_NO"));
            logger.info("taskId:{}",taskId);
            if(taskId != null){
                param.clear();
                param.put("ID",taskId);
                this.getDao().update("YD_EPCBM.MIP_PATROL_TASK",dd,param);
            }else{
                dd.put("CREATE_TIME",new Date());
                dd.put("ID",this.generateId(null));
                this.getDao().insert("YD_EPCBM.MIP_PATROL_TASK",dd);
            }
        }
    }

    @Override
    public List<DataRecord> getSyncData(Date beginTime, Date endTime) {
        List<DataRecord> list = new ArrayList<>();
        Connection conn = null;
        String sql = "SELECT * FROM  yyzh_zygk_zyjh";
        try{
            conn = DBUtils.getConnection();
            Statement st = conn.createStatement();
            if (beginTime != null && endTime != null){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String begin = sdf.format(beginTime);
                String end = sdf.format(endTime);
                sql += " where fbsj >='" + begin + "' and fbsj <= '" + end + "'";
            }
            sql += " order by fbsj desc";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()){
                String planNo = rs.getString("jhbh");
                String task_title = rs.getString("jhlx");
                String task_summary = rs.getString("sgnr");
                Date begin_time = rs.getDate("jhkssj");
                Date end_time = rs.getDate("jhjssj");
                String exe_user_name = rs.getString("gzfzr");
                Date create_time = rs.getDate("fbsj");
                Date entity_modifytime = rs.getDate("fbsj");
                String sgdw = rs.getString("sgdw");
                String gzfzrdh = rs.getString("gzfzrdh");
                String gzdd = rs.getString("gzdd");
                String zyrs = rs.getString("zyrs");
                String zywxd = rs.getString("zywxd");

                DataRecord s = new DataRecord();
                s.put("PLAN_NO", planNo);
                s.put("TASK_TITLE", task_title);
                s.put("TASK_SUMMARY", task_summary);
                s.put("BEGIN_TIME", begin_time);
                s.put("END_TIME", end_time);
                s.put("EXE_USER_NAME", exe_user_name);
                s.put("CREATE_TIME", create_time);
                s.put("ENTITY_MODIFYTIME", entity_modifytime);
                s.put("SGDW", sgdw);
                s.put("GZFZRDH", gzfzrdh);
                s.put("GZDD", gzdd);
                s.put("ZYRS", zyrs);
                s.put("ZYWXD", zywxd);
                DataRecord userInfo = getCreateUserInfo(exe_user_name);
                s.put("CREATE_USER_ID", userInfo.getString("ID"));
                s.put("CREATE_USER_NAME", userInfo.getString("NAME"));
                s.put("CLIENT_ID", "100236775143486");
                list.add(s);
            }
            return list;
        }catch (Exception e){
            throw new ServiceRuntimeException("获取数据失败", e);
        } finally {
            DBUtils.close(conn);
        }
    }



    private Long getTaskId(String planNo){
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("PLAN_NO", planNo);
        List<DataRecord> result = this.getDao().queryForDataSet(SQLMAP_PACKAGE+"getTaskId", param,false).getResults();
        if(result!=null && result.size()>0){
            return result.get(0).getLong("ID");
        }
        return null;
    }

    public DataRecord getCreateUserInfo(String userName) throws ServiceRuntimeException{
    	logger.info("getCreateUserInfo:{}",userName);
    	if(StringUtils.isEmpty(userName)) {
    		userName = "yfadmin";
    	}
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("NAME", userName);
        List<DataRecord> result = this.getDao().queryForDataSet(SQLMAP_PACKAGE+"getCreateUserInfo", param,false).getResults();
        if(result!=null && result.size()>0){
            return result.get(0);
        }
        param.put("NAME", "yfadmin");
        result = this.getDao().queryForDataSet(SQLMAP_PACKAGE+"getCreateUserInfo", param,false).getResults();
        if(result!=null && result.size()>0){
            return result.get(0);
        }
        return null;
    }

    public DataRecord getNewSyncTimeRecord(String tableName) throws ServiceRuntimeException{
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("tableName", tableName);
        List<DataRecord> result = this.getDao().queryForDataSet(SQLMAP_PACKAGE+"getNewSyncTimeRecord", param,false).getResults();
        if(result!=null && result.size()>0){
            return result.get(0);
        }
        return null;
    }

}
