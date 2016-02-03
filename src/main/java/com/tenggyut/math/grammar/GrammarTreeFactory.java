package com.tenggyut.math.grammar;

import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.common.collect.TreeTraverser;
import com.tenggyut.common.logging.LogFactory;
import com.tenggyut.common.utils.ListUtils;
import com.tenggyut.math.utils.MathUtils;
import com.tenggyut.common.utils.StringsUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * grammar tree factory
 * <p/>
 * Created by tenggyt on 2015/7/16.
 */
public class GrammarTreeFactory {
    private static final Logger LOG = LogFactory.getLogger(GrammarTreeFactory.class);

    private static final GrammarTreeFactory factory = new GrammarTreeFactory();
    private static final TreeTraverser<GrammarNode> NODE_TREE_TRAVERSER = new TreeTraverser<GrammarNode>() {
        @Override
        public Iterable<GrammarNode> children(GrammarNode node) {
            if (!node.getTerms().isEmpty()) {
                return Collections.unmodifiableList(node.getTerms());
            } else {
                return Collections.emptyList();
            }
        }
    };
    private LoadingCache<String, GrammarNode> grammarNodeCache;

    GrammarTreeFactory() {
        grammarNodeCache = CacheBuilder.newBuilder().maximumSize(1000).build(new CacheLoader<String, GrammarNode>() {
            @Override
            public GrammarNode load(String key) throws Exception {
                return new GrammarNode(key);
            }
        });
    }

    public static GrammarNode parse(String expr) {
        try {
            return factory.grammarNodeCache.get(expr);
        } catch (Exception e) {
            LOG.error("failed to parse {}", expr, e.getCause());
            return emptyNode();
        }
    }

    public static boolean verify(String expr) {
        return !parse(expr).isEmptyNode();
    }

    public static GrammarNode emptyNode() {
        return new GrammarNode("");
    }

    public static TreeTraverser<GrammarNode> getNodeTreeTraverser() {
        return NODE_TREE_TRAVERSER;
    }

    public static List<String> op(String expr) throws IllegalArgumentException {
        return opByStringAnalysis(expr);
    }

    public static List<String> opByStringAnalysis(String expr) throws IllegalArgumentException {
        String exprWithoutOutsideBrackets = stripRedundantOutsideBracketsIfAny(unifyBrackets(expr));

        if (StringUtils.isBlank(exprWithoutOutsideBrackets)) {
            return Lists.newArrayListWithCapacity(0);
        }

        List<String> terms = Lists.newLinkedList();
        Function opt;
        String exprWithoutBracketsAndFunc = stripBrackets(stripFuncs(exprWithoutOutsideBrackets));
        if (exprWithoutBracketsAndFunc.contains(Function.EQUAL.toString())) {
            opt = Function.EQUAL;
        } else if (exprWithoutBracketsAndFunc.contains(Function.NOTEQUAL.toString())) {
            opt = Function.NOTEQUAL;
        } else if (exprWithoutBracketsAndFunc.contains(Function.INEQUAL.toString())) {
            opt = Function.INEQUAL;
        } else if (exprWithoutBracketsAndFunc.contains(Function.INEQUAL_BIGGER.toString())) {
            opt = Function.INEQUAL_BIGGER;
        } else if (exprWithoutBracketsAndFunc.contains(Function.EQUAL_OR_INEQUAL.toString())) {
            opt = Function.EQUAL_OR_INEQUAL;
        } else if (exprWithoutBracketsAndFunc.contains(Function.EQUAL_OR_INEQUAL_BIGGER.toString())) {
            opt = Function.EQUAL_OR_INEQUAL_BIGGER;
        } else if (exprWithoutBracketsAndFunc.contains(Function.PLUS.toString())) {
            opt = Function.PLUS;
        } else if (exprWithoutBracketsAndFunc.contains(Function.MINUS.toString())) {
            opt = Function.MINUS;
        } else if (exprWithoutBracketsAndFunc.contains(Function.MULTIPLY.toString())) {
            opt = Function.MULTIPLY;
        } else if (exprWithoutBracketsAndFunc.contains(Function.DIVIDE.toString())) {
            opt = Function.DIVIDE;
        } else if (exprWithoutOutsideBrackets.startsWith(Function.ABS.toString())) {
            opt = Function.ABS;
        } else if (exprWithoutOutsideBrackets.startsWith(Function.SIN.toString())) {
            opt = Function.SIN;
        } else if (exprWithoutOutsideBrackets.startsWith(Function.COS.toString())) {
            opt = Function.COS;
        } else if (exprWithoutOutsideBrackets.startsWith(Function.TAN.toString())) {
            opt = Function.TAN;
        } else if (exprWithoutOutsideBrackets.contains(Function.POWER.toString())) {
            opt = Function.POWER;
        } else if (NumberUtils.isNumber(exprWithoutBracketsAndFunc)
                || exprWithoutBracketsAndFunc.equalsIgnoreCase("Pi")) {
            opt = Function.NUMBER;
        } else {
            opt = Function.NAME;
        }
        terms.addAll(ListUtils.map(getTerms(exprWithoutOutsideBrackets, opt)
                , new com.google.common.base.Function<String, String>() {
            @Nullable
            @Override
            public String apply(String input) {
                return stripRedundantOutsideBracketsIfAny(input);
            }
        }));
        return terms;
    }

    private static String stripFuncs(String expr) {
        List<String> funcs = Function.getFuncs();
        String result = expr;
        for (String func : funcs) {
            result = stripSingleFunc(result, func);
        }
        return result;
    }

    private static String unifyBrackets(String expr) {
        return expr.replaceAll("[\\[\\{]", "(").replaceAll("[\\]\\}]", ")");
    }

    private static String stripSingleFunc(String expr, String func) {
        if (!expr.contains(func)) return expr;
        String exprWithoutPower = expr;
        while (exprWithoutPower.contains(func)) {
            int powerTermStart = exprWithoutPower.indexOf(func) + func.length();
            if (powerTermStart < 0 || powerTermStart >= exprWithoutPower.length()) {
                throw new IllegalArgumentException("invalid expression " + expr);
            }
            String powerTerm;
            char powerTermStartChar = exprWithoutPower.charAt(powerTermStart);
            int powerTermEnd;
            if (MathUtils.isLeftBracket(powerTermStartChar)) {
                powerTermEnd = MathUtils.findTheOtherBracket(exprWithoutPower, powerTermStart + 1, powerTermStartChar);
                if (powerTermEnd < 0) {
                    throw new IllegalArgumentException("invalid expression " + expr);
                }
                powerTerm = exprWithoutPower.substring(powerTermStart, powerTermEnd + 1);
            } else {
                char[] chars = exprWithoutPower.toCharArray();
                powerTermEnd = exprWithoutPower.length();
                for (int i = powerTermStart + 1; i < chars.length; i++) {
                    if (MathUtils.isArithmeticOpt(chars[i]) || MathUtils.isRightBracket(chars[i])) {
                        powerTermEnd = i;
                        break;
                    }
                }
                powerTerm = exprWithoutPower.substring(powerTermStart, powerTermEnd);
            }
            exprWithoutPower = exprWithoutPower.replace(func + powerTerm, "");
        }
        return exprWithoutPower;
    }

    private static String stripBrackets(String expr) {
        String exprWithoutBrackets = expr;
        while (containLeftBrackets(exprWithoutBrackets)) {
            int start = exprWithoutBrackets.indexOf('(');
            int end = MathUtils.findTheOtherBracket(exprWithoutBrackets, start + 1, '(');
            if (start < end && end > 0) {
                exprWithoutBrackets = exprWithoutBrackets.replace(exprWithoutBrackets.substring(start, end + 1), "");
            } else {
                throw new IllegalArgumentException("invalid expression " + expr);
            }
        }
        return exprWithoutBrackets;
    }

    private static boolean containLeftBrackets(String expr) {
        return expr.contains("(");
    }

    private static String stripRedundantOutsideBracketsIfAny(String expr) {
        String exprWithoutOutsideBrackets = expr;

        while (exprWithoutOutsideBrackets.length() > 2 && MathUtils.isLeftBracket(exprWithoutOutsideBrackets.charAt(0))
                && MathUtils.findTheOtherBracket(exprWithoutOutsideBrackets, 1, exprWithoutOutsideBrackets.charAt(0))
                == exprWithoutOutsideBrackets.length() - 1) {
            exprWithoutOutsideBrackets = exprWithoutOutsideBrackets.substring(1, exprWithoutOutsideBrackets.length() - 1);
        }

        return exprWithoutOutsideBrackets;
    }

    private static List<String> getTerms(String expr, Function opt) throws IllegalArgumentException {
        List<String> terms = Lists.newLinkedList();
        terms.add(opt.toString());
        switch (opt) {
            case PLUS:
            case MINUS:
            case PLUSMINUS:
            case MULTIPLY:
            case DIVIDE:
            case EQUAL:
            case NOTEQUAL:
            case INEQUAL:
            case INEQUAL_BIGGER:
            case EQUAL_OR_INEQUAL:
            case EQUAL_OR_INEQUAL_BIGGER:
                if (expr.startsWith("-")) {
                    expr = "0" + expr;
                }
                terms.addAll(getArithmeticTerms(expr, opt.toString()));
                break;
            case POWER:
                String base = "";
                String power = "";

                int powerIndex = expr.indexOf('^');
                while (powerIndex < expr.length() && powerIndex > 0) {
                    base = expr.substring(0, powerIndex);
                    power = expr.substring(powerIndex + 1, expr.length());
                    if (verifyTerm(base)) {
                        break;
                    } else {
                        powerIndex = findNextOptIndex(expr, powerIndex + 1, "^");
                    }
                }

                terms.add(base);
                terms.add(power);
                break;
            case ABS:
            case SIN:
            case COS:
            case TAN:
                int termStartIndex = opt.toString().length();
                int termEndIndex = MathUtils.findTheOtherBracket(expr, termStartIndex + 1, '(');
                Preconditions.checkArgument(termEndIndex > termStartIndex && termEndIndex <= expr.length()
                        , "invalid " + opt + " expression " + expr);
                terms.add(expr.substring(termStartIndex, termEndIndex + 1));
                break;
            case NAME:
            case NUMBER:
                Preconditions.checkArgument(StringUtils.isNotBlank(expr), "invalid" + opt + " expression" + expr);
                terms.add(expr);
                break;
        }

        for (int i = 1; i < terms.size(); i++) {
            Preconditions.checkArgument(verifyTerm(terms.get(i))
                    , "invalid term " + terms.get(i) + " in expression " + expr);
        }
        return terms;
    }

    private static List<String> getArithmeticTerms(String expr, String arithmeticOpt) throws IllegalArgumentException {
        List<String> terms = Lists.newLinkedList();
        int termStart = 0;
        int termEnd = findNextOptIndex(expr, termStart, arithmeticOpt);
        do {
            String term = expr.substring(termStart, termEnd < 0 ? expr.length() : termEnd);

            if (verifyTerm(term)) {
                terms.add(term);
                termStart = (termEnd < 0 ? expr.length() : termEnd) + 1;
                termEnd = findNextOptIndex(expr, termStart, arithmeticOpt);
            } else if (termEnd < 0 || StringUtils.isBlank(term)) {
                throw new IllegalArgumentException("invalid arithmetic expression " + expr);
            } else {
                termEnd = findNextOptIndex(expr, termEnd + 1, arithmeticOpt);
            }

        } while (termStart < expr.length());

        Preconditions.checkArgument(StringsUtils.join(terms, arithmeticOpt).equals(expr)
                , "invalid expression " + expr);
        Preconditions.checkArgument(terms.size() > 1, "invalid expression " + expr);

        return terms;
    }

    private static int findNextOptIndex(String expr, int from, String opt) {
        return expr.indexOf(opt, from);
    }

    private static boolean verifyTerm(String term) {
        return MathUtils.verifyBrackets(term) && StringUtils.isNotBlank(term
                .replaceAll("[\\(\\[{\\)\\]}\\+\\-\\*/]", ""));
    }

}
