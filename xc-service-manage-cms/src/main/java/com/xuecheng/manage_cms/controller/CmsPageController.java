package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-09-12 17:24
 * @blame Android Team
 */
@RestController
@RequestMapping("/cms/page")
public class CmsPageController implements CmsPageControllerApi {

    @Autowired
    PageService pageService;

    @Override
    @GetMapping("/{page}/{size}")
    public QueryResponseResult findPageList(@PathVariable("page") int page, @PathVariable("size") int size, QueryPageRequest queryPageRequest) {
        //调用service
        return pageService.findPageList(page, size, queryPageRequest);
    }

    @PreAuthorize("hasAuthority('findList')")
    @Override
    @GetMapping("/")
    public QueryResponseResult findList() {
        //调用service
        return pageService.findList();
    }

    @Override
    @PostMapping("/")
    public CmsPageResult add(@RequestBody CmsPage cmsPage) {
        //调用service
        return pageService.add(cmsPage);
    }

    @Override
    @GetMapping("/{id}")
    public CmsPage findByPageId(@PathVariable("id") String id) {
        return pageService.findByPageId(id);
    }

    @Override
    @PutMapping("/{id}")
    public CmsPageResult edit(@PathVariable("id") String id, @RequestBody CmsPage cmsPage) {
        return pageService.edit(id, cmsPage);
    }

    @Override
    @DeleteMapping("/{id}")
    public CmsPageResult delete(@PathVariable("id") String id) {
        return pageService.delete(id);
    }

    @Override
    @PostMapping("/{pageId}")
    public ResponseResult post(@PathVariable("pageId") String pageId) {
        return pageService.postPage(pageId);
    }
}
