package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.CustomException;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-09-12 18:32
 **/
@Service
public class PageService {

    @Autowired
    CmsPageRepository cmsPageRepository;


    /**
     * 页面查询方法
     *
     * @param page             页码，从1开始记数
     * @param size             每页记录数
     * @param queryPageRequest 查询条件
     * @return QueryResponseResult
     */
    public QueryResponseResult findPageList(int page, int size, QueryPageRequest queryPageRequest) {
        if (queryPageRequest == null) {
            queryPageRequest = new QueryPageRequest();
        }
        //分页参数
        if (page <= 0) {
            page = 1;
        }
        page = page - 1;
        if (size <= 0) {
            size = 10;
        }
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        CmsPage cmsPage = new CmsPage();
        if (StringUtils.isNotEmpty(queryPageRequest.getSiteId())) {
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        if (StringUtils.isNotEmpty(queryPageRequest.getTemplateId())) {
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        if (StringUtils.isNotEmpty(queryPageRequest.getPageAliase())) {
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);
        Pageable pageable = PageRequest.of(page, size);
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);
        QueryResult<CmsPage> queryResult = new QueryResult<>();
        //数据列表
        queryResult.setList(all.getContent());
        //数据总记录数
        queryResult.setTotal(all.getTotalElements());
        return new QueryResponseResult(CommonCode.SUCCESS, queryResult);
    }

    /**
     * 不带查询方法
     *
     * @return QueryResponseResult
     */
    public QueryResponseResult findList() {
        List<CmsPage> all = cmsPageRepository.findAll();
        QueryResult<CmsPage> queryResult = new QueryResult<>();
        //数据列表
        queryResult.setList(all);
        //数据总记录数
        queryResult.setTotal(all.size());
        return new QueryResponseResult(CommonCode.SUCCESS, queryResult);
    }

    /**
     * @param cmsPage 新增对象
     * @return CmsPageResult
     */
    public CmsPageResult add(CmsPage cmsPage) {
        CmsPage cms = cmsPageRepository.findByPageWebPathAndSiteIdAndPageName(cmsPage.getPageWebPath(), cmsPage.getSiteId(), cmsPage.getPageName());
        if (cms != null){
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
        //添加页面主键由spring data 自动生成
        cmsPage.setPageId(null);
        cmsPageRepository.save(cmsPage);
        //返回结果
        return new CmsPageResult(CommonCode.SUCCESS, cmsPage);
    }

    /**
     * 根据id查询
     *
     * @param pageId 数据id
     * @return CmsPage
     */
    public CmsPage findByPageId(String pageId) {
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        return optional.orElse(null);
    }

    /**
     * 更新
     *
     * @param cmsPage 更新对象
     * @return CmsPageResult
     */
    public CmsPageResult edit(String pageId, CmsPage cmsPage) {
        CmsPage one = this.findByPageId(pageId);
        if (one != null) {
            one = cmsPageRepository.findByPageWebPathAndSiteIdAndPageName(cmsPage.getPageWebPath(), cmsPage.getSiteId(), cmsPage.getPageName());
            if (one == null) {
                cmsPage.setPageId(cmsPage.getPageId());
                cmsPageRepository.save(cmsPage);
                return new CmsPageResult(CommonCode.SUCCESS, cmsPage);
            } else {
                return new CmsPageResult(CommonCode.FAIL, null);
            }
        }
        return new CmsPageResult(CommonCode.FAIL, null);
    }

    /**
     * 删除对象
     *
     * @param pageId  数据id
     * @return CmsPageResult
     */
    public CmsPageResult delete(String pageId) {
        CmsPage one = this.findByPageId(pageId);
        if (one != null) {
            cmsPageRepository.delete(one);
            return new CmsPageResult(CommonCode.SUCCESS, null);
        }
        return new CmsPageResult(CommonCode.FAIL, null);
    }
}
