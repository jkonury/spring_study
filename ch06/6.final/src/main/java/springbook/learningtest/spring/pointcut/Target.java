package springbook.learningtest.spring.pointcut;

/**
 * @author JiHong Jang
 * @since 2014.05.06
 */
public class Target implements TargetInterface {
    @Override
    public void hello() {

    }

    @Override
    public void hello(String a) {

    }

    @Override
    public int plus(int a, int b) {
        return 0;
    }

    @Override
    public int minus(int a, int b) throws RuntimeException{
        return 0;
    }

    public void method() {}
}
