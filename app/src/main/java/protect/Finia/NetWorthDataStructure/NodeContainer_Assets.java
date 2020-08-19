package protect.Finia.NetWorthDataStructure;

/**
 * A data carrier to carry assets type data in AssetsAdapter and AssetsTypeTreeProcessor.
 * Data carrier acts as a container to store data and the level of the node on the type tree data structure.
 * This container class will be used in the adapters to enable better performance than using the nodes themselves.
 * TypeTreeProcessor is a helper class designed to process the category names to get the children of a particular node.
 * TypeTreeProcessor greatly helped the AssetsFragmentAdapter to deal with an unbalanced tree structure.
 * The conventional approach of Hash Map doesn't work on an unbalanced tree.
 *
 * @author Owner  Kevin Zhijun Wang
 * created on 2020/06/10
 */
public class NodeContainer_Assets {

    public String assetsTypeName;
    public int assetsTypeId;
    public int level;

    NodeContainer_Assets(String assetsTypeName, int assetsTypeId, int level) {
        this.assetsTypeName = assetsTypeName;
        this.assetsTypeId = assetsTypeId;
        this.level = level;
    }
}
