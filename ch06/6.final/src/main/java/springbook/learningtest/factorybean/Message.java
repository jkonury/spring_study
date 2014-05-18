package springbook.learningtest.factorybean;

/**
 * @author JiHong Jang
 * @since 2014.05.06
 */
public class Message {
    String text;

    private Message(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static Message newMessage(String text) {
        return new Message(text);
    }
}
