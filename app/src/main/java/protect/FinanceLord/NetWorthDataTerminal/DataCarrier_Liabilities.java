package protect.FinanceLord.NetWorthDataTerminal;

/**
 * A data carrier to carry liabilities data in LiabilitiesAdapter and LiabilitiesTypeProcessor.
 * Data carrier acts as a cache to store data and the level of the node on the tree data structure.
 * TypeProcessor is a class designed to process the category names to get the children of a particular node.
 * TypeProcessor greatly helped the LiabilitiesFragmentAdapter to deal with an unbalanced tree structure.
 * The conventional approach of Hash Map doesn't work on an unbalanced tree.
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
public class DataCarrier_Liabilities {

    public String liabilitiesTypeName;
    public int liabilitiesId;
    public int level;

    DataCarrier_Liabilities(String liabilitiesTypeName, int liabilitiesId, int level){
        this.liabilitiesTypeName = liabilitiesTypeName;
        this.liabilitiesId = liabilitiesId;
        this.level = level;
    }
}
