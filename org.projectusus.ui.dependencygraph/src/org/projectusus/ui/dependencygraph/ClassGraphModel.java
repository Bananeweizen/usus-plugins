package org.projectusus.ui.dependencygraph;

import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

import org.projectusus.core.internal.UsusCorePlugin;
import org.projectusus.core.internal.proportions.rawdata.GraphNode;
import org.projectusus.ui.dependencygraph.common.DependencyGraphModel;

public class ClassGraphModel extends DependencyGraphModel {

    public ClassGraphModel() {
        super();
    }

    @Override
    protected Set<? extends GraphNode> getRefreshedNodes() {
        return UsusCorePlugin.getUsusModel().getAllClassRepresenters();
    }

    @Override
    public int getMaxFilterValue() {
        if( getGraphNodes().isEmpty() ) {
            return -1;
        }
        return Collections.max( getGraphNodes(), new Comparator<GraphNode>() {

            public int compare( GraphNode node1, GraphNode node2 ) {
                return node1.getFilterValue() - node2.getFilterValue();
            }

        } ).getFilterValue();
    }
}