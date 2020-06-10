package protect.FinanceLord.NetWorthDataTerminal;

/**
 * A data carrier to carry assets data in AssetsAdapter and AssetsTypeProcessor.
 * Data carrier acts as a cache to store data and the level of the node on the tree data structure.
 * TypeProcessor is a class designed to process the category names to get the children of a particular node.
 * TypeProcessor greatly helped the AssetsFragmentAdapter to deal with an unbalanced tree structure.
 * The conventional approach of Hash Map doesn't work on an unbalanced tree.
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
public class DataCarrier_Assets {

    public String assetsTypeName;
    public int assetsTypeId;
    public int level;

    DataCarrier_Assets(String assetsTypeName, int assetsTypeId, int level) {
        this.assetsTypeName = assetsTypeName;
        this.assetsTypeId = assetsTypeId;
        this.level = level;
    }
}
