package com.tenggyut.math.grammar;

import com.tenggyut.config.ResourceFinder;
import org.junit.Assert;
import org.junit.Test;

/**
 * grammar tree factory unit test
 * <p/>
 * Created by tenggyt on 2015/8/7.
 */
public class GrammarTreeFactoryTest {

    @Test
    public void testParseValid() throws Exception {
        for (String expr : ResourceFinder.readResource("grammar/expression_valid")) {
            Assert.assertTrue("failed to verify " + expr, GrammarTreeFactory.verify(expr));
        }
    }

    @Test
    public void testParseInvalid() throws Exception {
        for (String expr : ResourceFinder.readResource("grammar/expression_invalid")) {
            Assert.assertFalse("failed to verify " + expr, GrammarTreeFactory.verify(expr));
        }
    }
}