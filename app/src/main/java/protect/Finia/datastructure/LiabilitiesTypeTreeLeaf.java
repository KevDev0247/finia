package protect.Finia.datastructure;

import protect.Finia.dao.LiabilitiesTypeDao;

/**
 * Leaf node of the LiabilitiesTypeTree.
 * Each individual leaf is the fundamental element of the LiabilitiesTypeTree expressed as a list.
 * Each leaf node contains the name of the leaf node, and the names of all parents that the leaf node belongs to.
 * The idea behind a list representation of the tree is to use each leaf node to store the address of the leaf node in the tree
 * by documenting the name of its parent and its parent's parent.
 * By representing the type tree as a list, the tree structure will be more flexible in an java environment in terms of read, edit, group, and delete.
 * A list representation of the tree is also the best way to store the results of deserialization.
 * The tree structure can be obtained through a deserialization of the database entity AssetsType.
 * The deserialization method is defined in AssetsTypeDao which is a series of sub queries in SQL.
 *
 * @author Owner  Kevin Zhijun Wang
 * @see LiabilitiesTypeDao#queryLiabilitiesTypeTreeAsList()
 * created on 2020/04/16
 */
public class LiabilitiesTypeTreeLeaf {

    public int liabilitiesFirstLevelId;
    public String liabilitiesFirstLevelName;
    public int liabilitiesSecondLevelId;
    public String liabilitiesSecondLevelName;
    public int liabilitiesThirdLevelId;
    public String liabilitiesThirdLevelName;

    public String toString() {
        return "liabilitiesFirstLevelId" + liabilitiesFirstLevelId + "\n" + "liabilitiesFirstLevelName" + liabilitiesFirstLevelName + "\n"
                + "liabilitiesSecondLevelId" + liabilitiesSecondLevelId + "\n" + "liabilitiesSecondLevelName" + liabilitiesSecondLevelName + "\n"
                + "liabilitiesThirdLevelId" + liabilitiesThirdLevelId + "\n" + "liabilitiesThirdLevelName" + liabilitiesThirdLevelName;
    }
}
