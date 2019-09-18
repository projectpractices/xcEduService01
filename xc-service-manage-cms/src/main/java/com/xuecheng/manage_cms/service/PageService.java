package com.xuecheng.manage_cms.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsConfigRepository;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;
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

    @Autowired
    CmsConfigRepository cmsConfigRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CmsTemplateRepository cmsTemplateRepository;

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    GridFSBucket gridFSBucket;


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
        if (cms != null) {
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
     * @param pageId 数据id
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

    /**
     * 获取配置类对象
     *
     * @param id 数据id
     * @return CmsConfig
     */
    public CmsConfig getModel(String id) {
        Optional<CmsConfig> optional = cmsConfigRepository.findById(id);
        return optional.orElse(null);
    }

    /**
     * 通过页面id获取模型
     *
     * @param pageId 页面id
     * @return Map(封装模型数据)
     */
    public Map getModelByPageId(String pageId) {
        //根据pageId查询cmspage
        CmsPage cmsPage = findByPageId(pageId);
        //如果为空抛出异常
        if (cmsPage == null) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        //取出模板数据请求接口
        String dataUrl = cmsPage.getDataUrl();
        if (StringUtils.isEmpty(dataUrl)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }
        ResponseEntity<Map> entity = restTemplate.getForEntity(dataUrl, Map.class);
        return entity.getBody();
    }

    public String getTemplateByPageId(String pageId) {
        //根据pageId查询cmspage
        CmsPage cmsPage = findByPageId(pageId);
        //如果为空抛出异常
        if (cmsPage == null) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        //获取模板id
        String templateId = cmsPage.getTemplateId();
        if (StringUtils.isEmpty(templateId)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        //查询模板
        Optional<CmsTemplate> cmsTemplate = cmsTemplateRepository.findById(templateId);
        if (cmsTemplate.isPresent()) {
            CmsTemplate template = cmsTemplate.get();
            String templateFileId = template.getTemplateFileId();
            //grid中查询文件
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
            //打开stream
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
            try {
                String string = IOUtils.toString(gridFsResource.getInputStream(), "UTF-8");
                return string;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getPageHtml(String pageId) {
        Map map = getModelByPageId(pageId);
        if (map == null) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }
        String content = getTemplateByPageId(pageId);
        if (StringUtils.isEmpty(content)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        String html = generateHtml(content, map);
        if (StringUtils.isEmpty(html)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        return html;
    }

    /**
     * 生成html方法
     *
     * @param template 模板
     * @param model    数据
     * @return html
     */
    public String generateHtml(String template, Map model) {
        try {
            //生成配置类
            Configuration configuration = new Configuration(Configuration.getVersion());
            //获取模板加载器
            StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
            //放入模板
            stringTemplateLoader.putTemplate("template", template);
            //配置模板加载器
            configuration.setTemplateLoader(stringTemplateLoader);
            //获取模板
            Template template1 = configuration.getTemplate("template");
            //生成html文件
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template1, model);
            return html;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
