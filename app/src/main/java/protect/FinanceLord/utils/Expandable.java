package protect.FinanceLord.utils;

import java.util.List;

public interface Expandable {
    public String getNodeName();
    public List<Expandable> getChildren();
    public int getChildrenCount();
    public Expandable getNode(int position);
    public boolean isExpanded();
    public void setExpanded(boolean expanded);
}
