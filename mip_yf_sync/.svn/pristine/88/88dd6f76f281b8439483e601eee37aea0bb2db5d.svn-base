/**
 * Copyright (C) 2011 Guangzhou JHComn Technologies Ltd.
 *
 * 本代码版权归广州佳和立创科技发展有限公司所有，且受到相关的法律保护。
 * 没有经过版权所有者的书面同意，
 * 任何其他个人或组织均不得以任何形式将本文件或本文件的部分代码用于其他商业用途。
 *
 */
package _com.mip.biz.oms.yf_sync;
/**
 * @author chenxx
 * @Date: 2018-1-4
 */
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mip.biz.oms.yf_sync.service.YfSyncService;
import com.mip.core.util.ServiceLocator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath*:core-context.xml" })
public class YfSyncServiceTest implements ApplicationContextAware {

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		ServiceLocator.initBeanFactory(applicationContext);
	}
	private YfSyncService yfSyncService;
	@org.junit.Before
	public void init(){
		yfSyncService = (YfSyncService) ServiceLocator.getBean(YfSyncService.class.getName());
	}

	@Test
	public void testsaveSyncData() {
		yfSyncService.saveSyncData();
	}

}
