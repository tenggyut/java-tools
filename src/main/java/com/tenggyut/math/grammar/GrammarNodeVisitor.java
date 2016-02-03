package com.tenggyut.math.grammar;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.tenggyut.common.logging.LogFactory;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Set;

/**
 * grammar node visitor
 * <p/>
 * Created by tenggyt on 2015/7/16.
 */
public class GrammarNodeVisitor {
    private static final Logger LOG = LogFactory.getLogger(GrammarNodeVisitor.class);

    private GrammarNode root;

    public GrammarNodeVisitor(GrammarNode node) {
        this.root = node;
    }

    private FluentIterable<GrammarNode> iterable() {
        return GrammarTreeFactory.getNodeTreeTraverser().breadthFirstTraversal(root);
    }

    public List<String> getVariables() {
        Set<String> vars = Sets.newHashSet();
        for (GrammarNode node : iterable()) {
            if (node.isSymbol()) {
                vars.add(node.getSymbol());
            }
        }
        return Lists.newArrayList(vars);
    }

}
