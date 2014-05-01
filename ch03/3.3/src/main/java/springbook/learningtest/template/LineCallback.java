package springbook.learningtest.template;

/**
 * @author JiHong Jang
 * @since 2014.04.29
 */
public interface LineCallback<T> {
    T doSomethingWithLine(String line, T value);
}
