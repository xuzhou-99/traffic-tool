package com.qingyan.traffictool.generate;

import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface ProxyInfoDao {
    int deleteByPrimaryKey(String bh);

    int insert(ProxyInfo record);

    int insertSelective(ProxyInfo record);

    ProxyInfo selectByPrimaryKey(String bh);

    int updateByPrimaryKeySelective(ProxyInfo record);

    int updateByPrimaryKey(ProxyInfo record);
}