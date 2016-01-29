package com.tenggyut.common.utils;

import com.google.common.collect.ImmutableMap;
import org.junit.Assert;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.*;

public class MathUtilsTest {

    @Test
    public void testVerifyBrackets() throws Exception {
        Map<String, Boolean> data = ImmutableMap.of(
                "(2)+3", true,
                "[(3+2])-4+(2/3)", false,
                "()[]", true
        );
        for (Map.Entry<String, Boolean> entry : data.entrySet()) {
            Assert.assertEquals(entry.getValue(), MathUtils.verifyBrackets(entry.getKey()));
        }
    }
}