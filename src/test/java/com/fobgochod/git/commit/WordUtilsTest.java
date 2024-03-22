package com.fobgochod.git.commit;

import com.fobgochod.git.commit.util.WordUtils;
import org.junit.Assert;
import org.junit.Test;

public class WordUtilsTest {

    @Test
    public void wrap() {
        String message = "Create a commit message with the following template.";

        String[] wrapText = WordUtils.wrap(message, 20).split(System.lineSeparator());
        Assert.assertEquals("Create a commit", wrapText[0]);
        Assert.assertEquals("message with the", wrapText[1]);
        Assert.assertEquals("following template.", wrapText[2]);
    }
}