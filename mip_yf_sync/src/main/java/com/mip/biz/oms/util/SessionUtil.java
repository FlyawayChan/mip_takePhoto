package com.mip.biz.oms.util;

import com.mip.biz.oms.yf_sync.service.TestMapper;
import com.mip.biz.oms.yf_sync.service.impl.YfSyncServiceImpl;
import com.mip.core.basebiz.dto.DataRecord;
import org.apache.ibatis.io.ResolverUtil;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class SessionUtil {
    String resource = "config/mybatis-config.xml";
    InputStream is = Resources.getResourceAsStream(resource);

    SqlSessionFactory sqlSessionFactory = null;
    // 从sqlSessionFactory中获取sqlSession
    // SqlSession的作用域最好是请求或方法域，且在使用完毕之后及时释放资源，而且一定要确保资源得到释放
    //SqlSession sqlSession = sqlSessionFactory.openSession();
    // 从映射器接口中查询
    //ResolverUtil.Test test = sqlSession.getMapper(TestMapper.class).selectOneTest("03532208011907-5551");

    public SessionUtil() throws IOException {
    }

    public SqlSessionFactory getSqlSessionFactory(InputStream is){
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
        return sqlSessionFactory;
    }

    public static void main(String[] args) throws Exception{
        Logger logger = LoggerFactory.getLogger(YfSyncServiceImpl.class);
        String resource = "config/mybatis-config.xml";
        InputStream is = Resources.getResourceAsStream(resource);

        SessionUtil sessionUtil = new SessionUtil();
        SqlSession sqlSession = sessionUtil.getSqlSessionFactory(is).openSession();

        //DataRecord dr = sqlSession.getMapper(TestMapper.class).getOneByPlanNo("03532208011907-5551");

        // 从xml映射配置中查询
        DataRecord test = sqlSession.selectOne("com.mip.biz.oms.yf_sync.sqlmap.SyncRecord", "03532208011907-5551");
        sqlSession.close();
    }
}
