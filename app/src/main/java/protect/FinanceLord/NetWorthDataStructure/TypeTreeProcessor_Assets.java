package protect.FinanceLord.NetWorthDataStructure;

import java.util.ArrayList;
import java.util.List;

/**
 * The data processor to retrieve or insert any data or data collection from the AssetsValueTree Structure.
 * The AssetsTypeTree is designed to be a read-only structure as the business logic of Finance
 * defines a fixed number of categories for net worth items.
 * The type processor is mostly used by the adapter of the Expandable List to retrieve the sub group of net worth items
 * in order to format the data stored in AssetsTypeTree so that they are compatible with the Expandable List widgets.
 * The node container is used so that the information is stored and the current level can be tracked.
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 * @see protect.FinanceLord.Database.LiabilitiesType
 */
public class TypeTreeProcessor_Assets {

    private List<TypeTreeLeaf_Assets> typesTree;

    public TypeTreeProcessor_Assets(List<TypeTreeLeaf_Assets> typesTree) {
        this.typesTree = typesTree;
    }

    /**
     * Retrieve the sub group items of a parent node.
     *
     * @param parentNodeName the name of the parent node for the sub group to be searched
     * @param level current level of the node
     * @return the sub group of containers
     */
    public List<NodeContainer_Assets> getSubGroup(String parentNodeName, int level) {
        List<NodeContainer_Assets> containersSubGroup = new ArrayList<>();

        if (level == 0) {
            for(TypeTreeLeaf_Assets assetsTypeTreeLeaf : typesTree) {
                if (assetsTypeTreeLeaf.assetsFirstLevelName != null) {

                    NodeContainer_Assets nodeContainer = new NodeContainer_Assets(assetsTypeTreeLeaf.assetsFirstLevelName, assetsTypeTreeLeaf.assetsFirstLevelId, 0);
                    addTypeToSubGroup(nodeContainer, containersSubGroup);
                }
            }
        } else if(level == 1) {
            for(TypeTreeLeaf_Assets assetsTypeTreeLeaf : typesTree) {
                if (assetsTypeTreeLeaf.assetsFirstLevelName != null
                        && assetsTypeTreeLeaf.assetsFirstLevelName.equals(parentNodeName)
                        && assetsTypeTreeLeaf.assetsSecondLevelName != null) {

                    NodeContainer_Assets nodeContainer = new NodeContainer_Assets(assetsTypeTreeLeaf.assetsSecondLevelName, assetsTypeTreeLeaf.assetsSecondLevelId, 1);
                    addTypeToSubGroup(nodeContainer, containersSubGroup);
                }
            }
        } else if(level == 2) {
            for(TypeTreeLeaf_Assets assetsTypeTreeLeaf : typesTree) {
                if (assetsTypeTreeLeaf.assetsSecondLevelName != null
                        && assetsTypeTreeLeaf.assetsSecondLevelName.equals(parentNodeName)
                        && assetsTypeTreeLeaf.assetsThirdLevelName != null) {

                    NodeContainer_Assets nodeContainer = new NodeContainer_Assets(assetsTypeTreeLeaf.assetsThirdLevelName, assetsTypeTreeLeaf.assetsThirdLevelId, 2);
                    addTypeToSubGroup(nodeContainer, containersSubGroup);
                }
            }
        } else if(level == 3) {
            for(TypeTreeLeaf_Assets assetsTypeTreeLeaf : typesTree) {
                if(assetsTypeTreeLeaf.assetsThirdLevelName != null
                        && assetsTypeTreeLeaf.assetsThirdLevelName.equals(parentNodeName)
                        && assetsTypeTreeLeaf.assetsFourthLevelName != null) {

                    NodeContainer_Assets nodeContainer = new NodeContainer_Assets(assetsTypeTreeLeaf.assetsFourthLevelName, assetsTypeTreeLeaf.assetsFourthLevelId, 3);
                    addTypeToSubGroup(nodeContainer, containersSubGroup);
                }
            }
        }

        return containersSubGroup;
    }

    /**
     * Add the node container to the sub group.
     * The method first check if the current container already exists and the id is valid.
     * Then, the node container is added to the sub group
     *
     * @param nodeContainer the node container to be added.
     * @param containersSubGroup the container sub group of a particular node.
     */
    private void addTypeToSubGroup(NodeContainer_Assets nodeContainer, List<NodeContainer_Assets> containersSubGroup) {
        for(NodeContainer_Assets currentNodeContainer : containersSubGroup) {
            if(currentNodeContainer.assetsTypeId == nodeContainer.assetsTypeId && currentNodeContainer.assetsTypeId != 0) {
                return;
            }
        }
        containersSubGroup.add(nodeContainer);
    }
}
