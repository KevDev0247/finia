package protect.FinanceLord.utils;

import java.util.List;

public class ExpandableNode implements Expandable {
    private List<Expandable> childrenNodes;
    private String nodeName;
    private int nodeId;
    private boolean isExpanded;

    @Override
    public String getNodeName() {
        return nodeName;
    }

    public int getNodeId() {
        return nodeId;
    }

    @Override
    public List<Expandable> getChildren() {
        return childrenNodes;
    }

    @Override
    public int getChildrenCount() {
        return childrenNodes.size();
    }

    @Override
    public Expandable getNode(int position) {
        return null;
    }

    @Override
    public boolean isExpanded() {
        return isExpanded;
    }

    @Override
    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public int getVisibleChildrenCount() {
        int totalCount = 0;
        for(Expandable child : childrenNodes) {
            if (child.isExpanded()) {
                totalCount = totalCount + child.getChildrenCount() + 1; //节点自身占用一个位置
            } else {
                totalCount += 1;
            }
        }
        return totalCount;
    }
}
