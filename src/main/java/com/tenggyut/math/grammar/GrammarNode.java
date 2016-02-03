package com.tenggyut.math.grammar;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.tenggyut.common.logging.LogFactory;
import com.tenggyut.math.utils.MathUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * grammar node in grammar tree
 * <p/>
 * Created by tenggyt on 2015/7/16.
 */
public class GrammarNode {
    private static final Logger LOG = LogFactory.getLogger(GrammarNode.class);

    private List<GrammarNode> terms;
    private Function function;
    private String symbol;
    private GrammarNodeVisitor visitor;

    public GrammarNode(String symbol) {
        this.terms = Lists.newArrayList();
        initTerms(symbol);
        this.visitor = new GrammarNodeVisitor(this);
    }

    private void initTerms(String symbol) {
        this.symbol = symbol;
        if (StringUtils.isNotBlank(symbol)) {
            List<String> terms = GrammarTreeFactory.op(this.symbol);
            Optional<Function> function = Optional.fromNullable(Function.parse(terms.get(0)));
            if (!function.isPresent()) {
                return;
            }
            this.function = function.get();
            if (!function.get().equals(Function.NAME) && !function.get().equals(Function.NUMBER)) {
                if (terms.size() >= 2) {
                    for (int i = 1; i < terms.size(); i++) {
                        GrammarNode term = new GrammarNode(terms.get(i));
                        this.terms.add(term);
                    }
                }
            }
        }
    }

    public List<GrammarNode> getTerms() {
        return Lists.newArrayList(terms);
    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean isPower() {
        return isFunction(Function.POWER);
    }

    public boolean isPlus() {
        return isFunction(Function.PLUS);
    }

    public boolean isTimes() {
        return isFunction(Function.MULTIPLY);
    }

    public boolean isSymbol() {
        return isFunction(Function.NAME);
    }

    public boolean isNumber() {
        return isFunction(Function.NUMBER);
    }

    public boolean isAtom() {
        return isFunction(Function.NUMBER) || isFunction(Function.NAME);
    }

    public boolean isAbs() {
        return isFunction(Function.ABS);
    }

    private boolean isFunction(Function func) {
        return function != null && function.equals(func);
    }

    public boolean isOneDepth() {
        for (GrammarNode node : terms) {
            if (!node.isAtom())
                return false;
        }
        return true;
    }

    public boolean isEmptyNode() {
        return this.symbol.isEmpty();
    }

    public GrammarNode copy() {
        return new GrammarNode(symbol);
    }

    public BigDecimal evalWithPrimeNumbers(Map<String, Integer> varAndPrimeNumberMapping) throws IllegalArgumentException {
        Preconditions.checkArgument(!isEmptyNode(), "cant eval an empty expression");
        GrammarNode rootCopy = new GrammarNode(MathUtils.replaceConstantSymbolWithValue(this.toString()));
        Function function = rootCopy.getFunction();
        List<GrammarNode> terms = rootCopy.getTerms();
        BigDecimal result = BigDecimal.ZERO;
        switch (function) {
            case PLUS:
                for (GrammarNode node : terms) {
                    result = result.add(node.evalWithPrimeNumbers(varAndPrimeNumberMapping));
                }
                break;
            case MINUS:
                result = terms.get(0).evalWithPrimeNumbers(varAndPrimeNumberMapping);
                for (GrammarNode node : terms.subList(1, terms.size())) {
                    result = result.subtract(node.evalWithPrimeNumbers(varAndPrimeNumberMapping));
                }
                break;
            case PLUSMINUS:
                break;
            case MULTIPLY:
                result = BigDecimal.ONE;
                for (GrammarNode node : terms) {
                    result = result.multiply(node.evalWithPrimeNumbers(varAndPrimeNumberMapping));
                }
                break;
            case DIVIDE:
                result = terms.get(0).evalWithPrimeNumbers(varAndPrimeNumberMapping);
                for (GrammarNode node : terms.subList(1, terms.size())) {
                    result = result.divide(node.evalWithPrimeNumbers(varAndPrimeNumberMapping)
                            , 20, BigDecimal.ROUND_HALF_UP);
                }
                break;
            case POWER:
                result = BigDecimal.valueOf(Math.pow(terms.get(0).evalWithPrimeNumbers(varAndPrimeNumberMapping).doubleValue()
                        , terms.get(1).evalWithPrimeNumbers(varAndPrimeNumberMapping).doubleValue()));
                break;
            case ABS:
                result = terms.get(0).evalWithPrimeNumbers(varAndPrimeNumberMapping).abs();
                break;
            case SIN:
                result = BigDecimal
                        .valueOf(Math.sin(terms.get(0).evalWithPrimeNumbers(varAndPrimeNumberMapping).doubleValue()));
                break;
            case COS:
                result = BigDecimal
                        .valueOf(Math.cos(terms.get(0).evalWithPrimeNumbers(varAndPrimeNumberMapping).doubleValue()));
                break;
            case TAN:
                result = BigDecimal
                        .valueOf(Math.tan(terms.get(0).evalWithPrimeNumbers(varAndPrimeNumberMapping).doubleValue()));
                break;
            case EQUAL:
                break;
            case INEQUAL:
                break;
            case NOTEQUAL:
                break;
            case EQUAL_OR_INEQUAL:
                break;
            case NAME:
                result = BigDecimal.valueOf(varAndPrimeNumberMapping.get(rootCopy.toString()));
                break;
            case NUMBER:
                result = new BigDecimal(rootCopy.toString());
                break;
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GrammarNode)) return false;

        GrammarNode that = (GrammarNode) o;

        return !(symbol != null ? !symbol.equals(that.symbol) : that.symbol != null);

    }

    @Override
    public int hashCode() {
        return symbol != null ? symbol.hashCode() : 0;
    }

    @Override
    public String toString() {
        return symbol;
    }

    public GrammarNodeVisitor getVisitor() {
        return visitor;
    }

}
