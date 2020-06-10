package protect.FinanceLord.NetWorthDataStructure;

import java.util.ArrayList;
import java.util.List;

public class TypeTreeProcessor_Assets {

    private List<TypeTreeLeaf_Assets> typesTree;

    public TypeTreeProcessor_Assets(List<TypeTreeLeaf_Assets> typesTree) {
        this.typesTree = typesTree;
    }

    public List<NodeContainer_Assets> getSubGroup(String parentGroupLabel, int level) {
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
                        && assetsTypeTreeLeaf.assetsFirstLevelName.equals(parentGroupLabel)
                        && assetsTypeTreeLeaf.assetsSecondLevelName != null) {

                    NodeContainer_Assets nodeContainer = new NodeContainer_Assets(assetsTypeTreeLeaf.assetsSecondLevelName, assetsTypeTreeLeaf.assetsSecondLevelId, 1);
                    addTypeToSubGroup(nodeContainer, containersSubGroup);
                }
            }
        } else if(level == 2) {
            for(TypeTreeLeaf_Assets assetsTypeTreeLeaf : typesTree) {
                if (assetsTypeTreeLeaf.assetsSecondLevelName != null
                        && assetsTypeTreeLeaf.assetsSecondLevelName.equals(parentGroupLabel)
                        && assetsTypeTreeLeaf.assetsThirdLevelName != null) {

                    NodeContainer_Assets nodeContainer = new NodeContainer_Assets(assetsTypeTreeLeaf.assetsThirdLevelName, assetsTypeTreeLeaf.assetsThirdLevelId, 2);
                    addTypeToSubGroup(nodeContainer, containersSubGroup);
                }
            }
        } else if(level == 3) {
            for(TypeTreeLeaf_Assets assetsTypeTreeLeaf : typesTree) {
                if(assetsTypeTreeLeaf.assetsThirdLevelName != null
                        && assetsTypeTreeLeaf.assetsThirdLevelName.equals(parentGroupLabel)
                        && assetsTypeTreeLeaf.assetsFourthLevelName != null) {

                    NodeContainer_Assets nodeContainer = new NodeContainer_Assets(assetsTypeTreeLeaf.assetsFourthLevelName, assetsTypeTreeLeaf.assetsFourthLevelId, 3);
                    addTypeToSubGroup(nodeContainer, containersSubGroup);
                }
            }
        }

        return containersSubGroup;
    }

    private void addTypeToSubGroup(NodeContainer_Assets nodeContainer, List<NodeContainer_Assets> containersSubGroup) {
        for(NodeContainer_Assets dataCarrier : containersSubGroup) {
            if(dataCarrier.assetsTypeId == nodeContainer.assetsTypeId && dataCarrier.assetsTypeId != 0) {
                return;
            }
        }
        containersSubGroup.add(nodeContainer);
    }
}
