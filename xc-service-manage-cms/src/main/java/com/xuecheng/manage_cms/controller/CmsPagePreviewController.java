package com.xuecheng.manage_cms.controller;

import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_cms.service.PageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/cms/preview")
public class CmsPagePreviewController extends BaseController {

    @Autowired
    PageService pageService;

    @GetMapping("/{pageId}")
    public void preview(@PathVariable("pageId") String pageId) throws IOException {
        String pageHtml = pageService.getPageHtml(pageId);
        if (StringUtils.isNotEmpty(pageHtml)){
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(pageHtml.getBytes(StandardCharsets.UTF_8));
        }
    }
}
