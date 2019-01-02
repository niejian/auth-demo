package cn.com.authDemo.config;

import com.rabbitmq.client.AMQP;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.RabbitConnectionFactoryBean;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;


/**
 * 不步骤：
 * 1.声明一个队列queue
 * 2.声明交换机模式：topicor其他
 * 3.声明绑定：exchange和队列的绑定。并声明通佩服
 * @author: nj
 * @date: 2018/12/24:下午2:15
 */
@Configuration
@Slf4j
public class RabbitConfig {

//    @Autowired(required = false)
//    private RabbitTemplate rabbitTemplate;



    /**
     * 定制化amqp模版
     *
     * ConfirmCallback接口用于实现消息发送到RabbitMQ交换器后接收ack回调   即消息发送到exchange  ack
     * ReturnCallback接口用于实现消息发送到RabbitMQ 交换器，但无相应队列与交换器绑定时的回调  即消息发送不到任何一个队列中  ack
     */
//    @Bean
//    public RabbitTemplate rabbitTemplate() {
//        RabbitTemplate rabbitTemplate = new RabbitTemplate();
//        // 消息发送失败返回到队列中, yml需要配置 publisher-returns: true
//        rabbitTemplate.setMandatory(true);
//
//        // 消息返回, yml需要配置 publisher-returns: true
//        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
//            String correlationId = message.getMessageProperties().getCorrelationIdString();
//            log.debug("消息：{} 发送失败, 应答码：{} 原因：{} 交换机: {}  路由键: {}", correlationId, replyCode, replyText, exchange, routingKey);
//        });
//
//        // 消息确认, yml需要配置 publisher-confirms: true
//        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
//            if (ack) {
//                log.debug("消息发送到exchange成功,id: {}", correlationData.getId());
//            } else {
//                log.debug("消息发送到exchange失败,原因: {}", cause);
//            }
//        });
//
//        return rabbitTemplate;
//    }


    @Bean
    public Queue helloQueue() {
        return new Queue("helloQueue");
    }

    @Bean
    public Queue userQueue() {
        return new Queue("user");
    }


    @Bean
    public Queue topicQueue() {
        return new Queue("topicQueue");
    }

    @Bean
    public Queue topicQueue2() {
        return new Queue("topicQueue2");
    }

    //创建exchange
    @Bean
    public TopicExchange topicExChange() {
        return new TopicExchange("topicExchange");
    }

    @Bean
    public TopicExchange topicExChange2() {
        return new TopicExchange("topicExchange2");
    }

    /**
     * 绑定操作，routing_key为通配符
     *
     * @return
     */
    @Bean
    public Binding bindingTopic1() {

        return BindingBuilder.bind(topicQueue()).to(topicExChange()).with("cn.com.test");

    }

    @Bean
    Queue queue3() {
        return new Queue("cn.com.test");
    }


    /**
     * 绑定操作，routing_key为通配符
     *
     * @return
     */
    @Bean
    public Binding bindingTopic() {
        return BindingBuilder.bind(topicQueue()).to(topicExChange2()).with("cn.com.test.#");

    }

    @Bean
    public TopicExchange topicExChange3() {
        return new TopicExchange("topicExchange3");
    }

    /**
     * 绑定操作，routing_key为通配符
     *
     * @return
     */
    @Bean
    public Binding bindingTopic3() {
        return BindingBuilder.bind(queue3()).to(topicExChange3()).with("cn.com.test");

    }

    @Bean
    public Queue demoTestQueue() {
        return new Queue("demo-test");
    }

    @Bean
    public TopicExchange demoTestTopicExchange() {
        return new TopicExchange("demoTestTopic");
    }

    @Bean
    public Binding demoTestBinding() {
        return BindingBuilder.bind(demoTestQueue()).to(demoTestTopicExchange()).with("cn.com.test");

    }


    /******************************
     * 用户相关
     *******************************/
    @Bean
    public Queue addUserQueue() {
        return new Queue("demo-user-add");
    }

    @Bean
    public TopicExchange addUserTopicExchange() {
        return new TopicExchange("addUserTopic");
    }

    @Bean
    public Binding addUserBinding() {
        return BindingBuilder.bind(addUserQueue()).to(addUserTopicExchange()).with("cn.com.user.add");

    }

}
