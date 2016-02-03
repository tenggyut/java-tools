package com.tenggyut.math.grammar;

import com.tenggyut.math.utils.PrimeNumberUtils;
import org.testng.annotations.Test;

import java.util.Map;

public class GrammarNodeTest {

    @Test
    public void testEvalWithPrimeNumbers() throws Exception {
        String expr = "((x^2-2*x+1)^(1/(2)))+((x^2-6*x+9)^(1/(2)))";
        GrammarNode node = GrammarTreeFactory.parse(expr);
        Map<String, Integer> primeNumberMapping = PrimeNumberUtils
                .mappingVar2PrimeNumber(node.getVisitor().getVariables());
        System.out.println(node.evalWithPrimeNumbers(primeNumberMapping));
    }
}