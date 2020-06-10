package protect.FinanceLord.NetWorthDataStructure;

/**
 * A data carrier to carry assets type data in LiabilitiesAdapter and LiabilitiesTypeTreeProcessor.
 * Data carrier acts as a container to store data and the level of the node on the type tree data structure.
 * This container class will be used in the adapters to enable better performance than using the nodes themselves.
 * TypeTreeProcessor is a helper class designed to process the category names to get the children of a particular node.
 * TypeTreeProcessor greatly helped the LiabilitiesFragmentAdapter to deal with an unbalanced tree structure.
 * The conventional approach of Hash Map doesn't work on an unbalanced tree.
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
public class NodeContainer_Liabilities {

    public String liabilitiesTypeName;
    public int liabilitiesId;
    public int level;

    NodeContainer_Liabilities(String liabilitiesTypeName, int liabilitiesId, int level){
        this.liabilitiesTypeName = liabilitiesTypeName;
        this.liabilitiesId = liabilitiesId;
        this.level = level;
    }
}
