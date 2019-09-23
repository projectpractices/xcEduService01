package com.xuecheng.manage.cms.client.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage.cms.client.dao.CmsPageRepository;
import com.xuecheng.manage.cms.client.service.PageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * 页面发布消费端
 */
@Component
public class ConsumerPostPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(PageService.class);

    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Autowired
    PageService pageService;

    /**
     * @RabbitListener(queues = {"${xuecheng.mq.queue}"}) 监听队列
     * @param msg 获得队列消息
     */
    @RabbitListener(queues = "${xuecheng.mq.queue}")
    public void postPage(String msg) {
        Map map = JSON.parseObject(msg, Map.class);
        LOGGER.info("receive cms post page:{}", msg.toString());
        String pageId = (String) map.get("pageId");
        //获取页面
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        if (!optional.isPresent()) {
            LOGGER.error("receive cms post page,cmsPage is null:{}", msg.toString());
            return;
        }
        //保存页面到服务器路径
        pageService.savePageToServerPath(pageId);
    }
}
