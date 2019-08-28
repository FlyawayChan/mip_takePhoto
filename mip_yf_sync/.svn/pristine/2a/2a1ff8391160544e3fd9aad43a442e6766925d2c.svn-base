package com.mip.biz.oms.yf_sync.service;

import java.util.Date;
import java.util.List;

import com.mip.core.basebiz.dto.DataRecord;
import com.mip.core.basebiz.service.BizService;

public interface YfSyncService extends BizService<DataRecord> {
    public static String SQLMAP_PACKAGE = "com.mip.biz.oms.yf_sync.";
    /**
     * 获取同步数据
     */
    public List<DataRecord> getSyncData(Date beginTime, Date endTime);


    /**
     * 往巡视任务表保存数据
     */
    public void saveSyncData();
}
