package protect.FinanceLord.NetWorthDataStructure;

import protect.FinanceLord.DAOs.AssetsTypeDao;

/**
 * Leaf node of the AssetsTypeTree.
 * Each individual leaf is the fundamental element of the AssetsTypeTree expressed as a list.
 * Each leaf node contains the name of the leaf node, and the names of all parents that the leaf node belongs to.
 * The idea behind a list representation of the tree is to use each leaf node to store the address of the leaf node in the tree
 * by documenting the name of its parent and its parent's parent.
 * By representing the type tree as a list, the tree structure will be more flexible in an java environment in terms of read, edit, group, and delete.
 * A list representation of the tree is also the best way to store the results of deserialization.
 * The tree structure can be obtained through a deserialization of the database entity AssetsType.
 * The deserialization method is defined in AssetsTypeDao which is a series of sub queries in SQL.
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 * @see AssetsTypeDao#queryAssetsTypeTreeAsList()
 */
public class TypeTreeLeaf_Assets {

    public int assetsFirstLevelId;
    public String assetsFirstLevelName;
    public int assetsSecondLevelId;
    public String assetsSecondLevelName;
    public int assetsThirdLevelId;
    public String assetsThirdLevelName;
    public int assetsFourthLevelId;
    public String assetsFourthLevelName;

    public String toString() {
        return  "assetsFirstLevelId:" + assetsFirstLevelId + "\n" + "assetsFirstLevelName: " + assetsFirstLevelName + "\n"
                + "assetsSecondLevelId:" + assetsSecondLevelId + "\n" + "assetsSecondLevelName: " + assetsSecondLevelName + "\n"
                + "assetsThirdLevelId:" + assetsThirdLevelId + "\n"  + "assetsThirdLevelType: " + assetsThirdLevelName + "\n"
                + "assetsFourthLevelId:" + assetsFourthLevelId + "\n" + "assetsFourthLevelName: " + assetsFourthLevelName;
    }
}
