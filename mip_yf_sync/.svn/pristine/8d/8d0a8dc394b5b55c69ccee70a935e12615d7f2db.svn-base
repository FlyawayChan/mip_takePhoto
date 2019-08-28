package com.mip.biz.oms.yf_sync.service.impl;

import com.mip.biz.oms.yf_sync.service.YfSyncService;
import com.mip.core.util.ServiceLocator;

public class YfSyncDayDataJob {
    public void execute() throws Exception {
        YfSyncService ysService = (YfSyncService) ServiceLocator
                .getBean(YfSyncService.class.getName());
        ysService.saveSyncData();
    }
}
