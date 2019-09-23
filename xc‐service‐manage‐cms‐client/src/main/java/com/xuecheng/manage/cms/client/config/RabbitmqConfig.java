package com.xuecheng.manage.cms.client.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    //队列名称
    public static final String QUEUE_CMS_POSTPAGE = "queue_cms_postpage";
    //交换机名称
    public static final String EX_ROUTING_CMS_POSTPAGE = "ex_routing_cms_postpage";

    @Value("${xuecheng.mq.queue}")
    private String queue_cms_postpage_name;

    @Value("${xuecheng.mq.routingKey}")
    private String routingKey;

    /**
     * 设置交换机
     *
     * @return Exchange
     */
    @Bean(EX_ROUTING_CMS_POSTPAGE)
    public Exchange settingExchange() {
        return ExchangeBuilder.directExchange(EX_ROUTING_CMS_POSTPAGE).durable(true).build();
    }

    /**
     * 设置队列名称
     *
     * @return Queue
     */
    @Bean(QUEUE_CMS_POSTPAGE)
    public Queue settingQueue() {
        return new Queue(QUEUE_CMS_POSTPAGE);
    }

    /**
     * 绑定队列至交换机
     */
    @Bean
    public Binding bindingQueueToExchange(@Qualifier(QUEUE_CMS_POSTPAGE) Queue queue, @Qualifier(EX_ROUTING_CMS_POSTPAGE) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs();
    }
}
