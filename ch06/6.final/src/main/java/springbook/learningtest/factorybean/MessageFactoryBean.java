package springbook.learningtest.factorybean;

import org.springframework.beans.factory.FactoryBean;

/**
 * @author JiHong Jang
 * @since 2014.05.06
 */
public class MessageFactoryBean implements FactoryBean<Message> {
    String text;

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public Message getObject() throws Exception {
        return Message.newMessage(this.text);
    }

    @Override
    public Class<?> getObjectType() {
        return Message.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}