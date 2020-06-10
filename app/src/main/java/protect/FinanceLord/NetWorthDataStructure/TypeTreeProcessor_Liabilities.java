package protect.FinanceLord.NetWorthDataStructure;

import java.util.ArrayList;
import java.util.List;

public class TypeTreeProcessor_Liabilities {

    private List<TypeTreeLeaf_Liabilities> typesTree;

    public TypeTreeProcessor_Liabilities(List<TypeTreeLeaf_Liabilities> typesTree) {
        this.typesTree = typesTree;
    }

    public List<NodeContainer_Liabilities> getSubGroup(String parentGroupLabel, int level) {
        List<NodeContainer_Liabilities> containersSubGroup = new ArrayList<>();

        if (level == 0) {
            for (TypeTreeLeaf_Liabilities liabilitiesTypeTreeLeaf : typesTree) {
                if (liabilitiesTypeTreeLeaf.liabilitiesFirstLevelName != null) {

                    NodeContainer_Liabilities nodeContainer = new NodeContainer_Liabilities(liabilitiesTypeTreeLeaf.liabilitiesFirstLevelName, liabilitiesTypeTreeLeaf.liabilitiesFirstLevelId, 0);
                    addTypeToSubGroup(nodeContainer, containersSubGroup);
                }
            }
        } else if (level == 1) {
            for (TypeTreeLeaf_Liabilities liabilitiesTypeTreeLeaf : typesTree) {
                if (liabilitiesTypeTreeLeaf.liabilitiesFirstLevelName != null
                        && liabilitiesTypeTreeLeaf.liabilitiesFirstLevelName.equals(parentGroupLabel)
                        && liabilitiesTypeTreeLeaf.liabilitiesSecondLevelName != null) {

                    NodeContainer_Liabilities nodeContainer = new NodeContainer_Liabilities(liabilitiesTypeTreeLeaf.liabilitiesSecondLevelName, liabilitiesTypeTreeLeaf.liabilitiesSecondLevelId, 1);
                    addTypeToSubGroup(nodeContainer, containersSubGroup);
                }
            }
        } else if (level == 2) {
            for (TypeTreeLeaf_Liabilities liabilitiesTypeTreeLeaf : typesTree) {
                if (liabilitiesTypeTreeLeaf.liabilitiesSecondLevelName != null
                        && liabilitiesTypeTreeLeaf.liabilitiesSecondLevelName.equals(parentGroupLabel)
                        && liabilitiesTypeTreeLeaf.liabilitiesThirdLevelName != null) {

                    NodeContainer_Liabilities nodeContainer = new NodeContainer_Liabilities(liabilitiesTypeTreeLeaf.liabilitiesThirdLevelName, liabilitiesTypeTreeLeaf.liabilitiesThirdLevelId, 2);
                    addTypeToSubGroup(nodeContainer, containersSubGroup);
                }
            }
        }

        return containersSubGroup;
    }

    private void addTypeToSubGroup(NodeContainer_Liabilities nodeContainer, List<NodeContainer_Liabilities> containersSubGroup) {
        for (NodeContainer_Liabilities dataCarrier: containersSubGroup) {
            if (dataCarrier.liabilitiesId == nodeContainer.liabilitiesId && dataCarrier.liabilitiesId != 0) {
                return;
            }
        }
        containersSubGroup.add(nodeContainer);
    }
}
