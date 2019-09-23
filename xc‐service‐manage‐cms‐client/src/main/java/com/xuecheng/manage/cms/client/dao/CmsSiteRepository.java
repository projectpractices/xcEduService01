package com.xuecheng.manage.cms.client.dao;


import com.xuecheng.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * CmsPageRepository
 *
 * @blame wenhao
 */
public interface CmsSiteRepository extends MongoRepository<CmsSite, String> {

}
