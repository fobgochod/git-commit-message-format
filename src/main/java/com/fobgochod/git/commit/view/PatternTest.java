package com.fobgochod.git.commit.view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class PatternTest {

    /**
     * 匹配如下：
     * 1. feat(compile): hello world
     * 2. feat(compile):
     * 3. feat(): hello world
     * 4. feat:
     */
    private static final Pattern HEADER_PATTERN = Pattern.compile("^([a-z]+)(\\((.+)?\\))?: (.+)?");


    public static void main(String[] args) {
        String message = "feat(compile): hello world";

        Matcher matcher = HEADER_PATTERN.matcher(message);

        if (matcher.find()) {
            IntStream.rangeClosed(0, matcher.groupCount())
                    .mapToObj(i -> i + ". " + matcher.group(i)).forEach(System.out::println);
        }
    }
}
