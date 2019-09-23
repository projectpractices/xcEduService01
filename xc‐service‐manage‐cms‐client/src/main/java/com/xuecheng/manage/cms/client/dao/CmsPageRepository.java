package com.xuecheng.manage.cms.client.dao;


import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * CmsPageRepository
 *
 * @blame wenhao
 */
public interface CmsPageRepository extends MongoRepository<CmsPage, String> {
}
