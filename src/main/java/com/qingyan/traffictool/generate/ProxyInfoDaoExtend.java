package com.qingyan.traffictool.generate;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.qingyan.traffictool.entity.ProxyIP;

/**
 * ProxyInfoDaoExtend
 *
 * @author xuzhou
 * @version v1.0.0
 * @date 2022/5/5 15:41
 */
@Mapper
public interface ProxyInfoDaoExtend extends ProxyInfoDao {


    void batchInsert(@Param("list") List<ProxyInfo> proxyInfoList);

    List<ProxyInfo> selectAll();

    void batchUpdate(@Param("list") List<ProxyInfo> proxyInfoList);

    List<ProxyInfo> selectUseFullProxy();
}
