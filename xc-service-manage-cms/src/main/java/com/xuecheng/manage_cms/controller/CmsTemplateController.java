package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsTemplateControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsTemplateResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.manage_cms.service.PageService;
import com.xuecheng.manage_cms.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-09-12 17:24
 * @blame Android Team
 */
@RestController
@RequestMapping("/cms/template")
public class CmsTemplateController implements CmsTemplateControllerApi {

    @Autowired
    TemplateService templateService;

    @Override
    @GetMapping("/{page}/{size}")
    public QueryResponseResult findPageList(@PathVariable("page") int page, @PathVariable("size") int size, QueryPageRequest queryPageRequest) {
        return templateService.findPageList(page, size, queryPageRequest);
    }

    @Override
    public QueryResponseResult findList() {
        return null;
    }

    @Override
    public CmsTemplateResult add(CmsTemplate cmsTemplate) {
        return null;
    }

    @Override
    public CmsTemplate findByTemplateId(String id) {
        return null;
    }

    @Override
    public CmsTemplateResult edit(String id, CmsTemplate cmsTemplate) {
        return null;
    }

    @Override
    public CmsTemplateResult delete(String id) {
        return null;
    }
}
