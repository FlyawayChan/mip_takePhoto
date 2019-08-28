package com.mip.biz.oms.yf_sync.service;

import com.mip.core.basebiz.dto.DataRecord;
import org.apache.ibatis.annotations.Select;

public interface TestMapper {
    // 通过MyBatis的注解在Java接口方法上编写SQL语句
    @Select("select * from yyzh_zygk_zyjh where jhbh = #{jhbh}")
    public DataRecord getOneByPlanNo(String jhbh);
}
