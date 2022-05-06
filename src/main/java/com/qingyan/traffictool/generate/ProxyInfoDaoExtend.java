package com.qingyan.traffictool.generate;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * ProxyInfoDaoExtend
 *
 * @author xuzhou
 * @version v1.0.0
 * @date 2022/5/5 15:41
 */
@Mapper
public interface ProxyInfoDaoExtend extends ProxyInfoDao {

    /**
     * 批量新增
     *
     * @param proxyInfoList 插入列表
     */
    void batchInsert(@Param("list") List<ProxyInfo> proxyInfoList);

    /**
     * 查询全部
     *
     * @return 全部代理
     */
    List<ProxyInfo> selectAll();

    /**
     * 批量更新
     *
     * @param proxyInfoList 更新列表
     */
    void batchUpdate(@Param("list") List<ProxyInfo> proxyInfoList);

    /**
     * 获取有效代理
     *
     * @return 有效代理列表
     */
    List<ProxyInfo> selectUseFullProxy();
}
