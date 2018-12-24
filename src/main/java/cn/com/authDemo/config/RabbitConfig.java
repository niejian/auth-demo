package cn.com.authDemo.config;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 不步骤：
 * 1.声明一个队列queue
 * 2.声明交换机模式：topicor其他
 * 3.声明绑定：exchange和队列的绑定。并声明通佩服
 * @author: nj
 * @date: 2018/12/24:下午2:15
 */
@Configuration
public class RabbitConfig {

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

}
