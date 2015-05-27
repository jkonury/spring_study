package springbook.learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
    public Integer calcSum(String filepath) throws IOException {
        LineCallback<Integer> sumCallback = (line, value) -> value + Integer.parseInt(line);

        return lineReadTemplate(filepath, sumCallback, 0);
    }

    public Integer calcMultiply(String filepath) throws IOException {
        LineCallback<Integer> multiplyCallback = (line, value) -> value * Integer.parseInt(line);
        return lineReadTemplate(filepath, multiplyCallback, 1);
    }

    public String concatenate(String filepath) throws IOException {
        LineCallback<String> concatenateCallback = (line, value) -> value + line;

        return lineReadTemplate(filepath, concatenateCallback, "");
    }

    private <T> T lineReadTemplate(String filepath, LineCallback<T> callback, T initValue) throws IOException {
        BufferedReader in = null;

        try {
            in = new BufferedReader(new FileReader(filepath));
            T res = initValue;
            String line;

            while ((line = in.readLine()) != null) {
                res = callback.doSomethingWithLine(line, res);
            }

            return res;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
