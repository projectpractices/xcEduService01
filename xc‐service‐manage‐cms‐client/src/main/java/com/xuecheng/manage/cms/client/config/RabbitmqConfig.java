package com.xuecheng.manage.cms.client.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    //队列名称
    public static final String QUEUE_CMS_POSTPAGE = "queue_cms_postpage";
    //交换机名称
    public static final String EX_ROUTING_CMS_POSTPAGE = "ex_routing_cms_postpage";
}
