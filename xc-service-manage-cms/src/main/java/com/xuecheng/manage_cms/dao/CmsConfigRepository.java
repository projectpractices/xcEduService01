package com.xuecheng.manage_cms.dao;


import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 页面配置dao
 * CmsConfigRepository
 *
 * @blame wenhao
 */
public interface CmsConfigRepository extends MongoRepository<CmsConfig, String> {
}
