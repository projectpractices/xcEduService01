package com.xuecheng.manage_cms.dao;


import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CmsPageRepository extends MongoRepository<CmsPage, String> {
    /**
     * 根据页面名称查询
     *
     * @param pageName 页面名称
     * @return CmsPage
     */
    CmsPage findByPageName(String pageName);

    /**
     * 查询是否唯一
     *
     * @param pageWebPath webpath
     * @param siteId      站点id
     * @param pagename    页面名称
     * @return CmsPage
     */
    CmsPage findByPageWebPathAndSiteIdAndPageName(String pageWebPath, String siteId, String pagename);
}
