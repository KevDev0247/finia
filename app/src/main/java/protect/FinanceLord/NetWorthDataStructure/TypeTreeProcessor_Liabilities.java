package protect.FinanceLord.NetWorthDataStructure;

import java.util.ArrayList;
import java.util.List;

public class TypeTreeProcessor_Liabilities {

    private List<TypeTreeLeaf_Liabilities> dataList;

    public TypeTreeProcessor_Liabilities(List<TypeTreeLeaf_Liabilities> dataList){
        this.dataList = dataList;
    }

    public List<NodeContainer_Liabilities> getSubGroup(String parentGroupLabel, int level) {
        List<NodeContainer_Liabilities> subGroupLiabilities = new ArrayList<>();

        if (level == 0) {
            for (TypeTreeLeaf_Liabilities liabilitiesTypeTreeLeaf : dataList) {
                if (liabilitiesTypeTreeLeaf.liabilitiesFirstLevelName != null) {

                    NodeContainer_Liabilities dataCarrier = new NodeContainer_Liabilities(liabilitiesTypeTreeLeaf.liabilitiesFirstLevelName, liabilitiesTypeTreeLeaf.liabilitiesFirstLevelId, 0);
                    addTypeToSubGroup(dataCarrier, subGroupLiabilities);
                }
            }
        } else if (level == 1) {
            for (TypeTreeLeaf_Liabilities liabilitiesTypeTreeLeaf : dataList) {
                if (liabilitiesTypeTreeLeaf.liabilitiesFirstLevelName != null
                        && liabilitiesTypeTreeLeaf.liabilitiesFirstLevelName.equals(parentGroupLabel)
                        && liabilitiesTypeTreeLeaf.liabilitiesSecondLevelName != null) {

                    NodeContainer_Liabilities dataCarrier = new NodeContainer_Liabilities(liabilitiesTypeTreeLeaf.liabilitiesSecondLevelName, liabilitiesTypeTreeLeaf.liabilitiesSecondLevelId, 1);
                    addTypeToSubGroup(dataCarrier, subGroupLiabilities);
                }
            }
        } else if (level == 2) {
            for (TypeTreeLeaf_Liabilities liabilitiesTypeTreeLeaf : dataList) {
                if (liabilitiesTypeTreeLeaf.liabilitiesSecondLevelName != null
                        && liabilitiesTypeTreeLeaf.liabilitiesSecondLevelName.equals(parentGroupLabel)
                        && liabilitiesTypeTreeLeaf.liabilitiesThirdLevelName != null) {

                    NodeContainer_Liabilities dataCarrier = new NodeContainer_Liabilities(liabilitiesTypeTreeLeaf.liabilitiesThirdLevelName, liabilitiesTypeTreeLeaf.liabilitiesThirdLevelId, 2);
                    addTypeToSubGroup(dataCarrier, subGroupLiabilities);
                }
            }
        }

        return subGroupLiabilities;
    }

    void addTypeToSubGroup(NodeContainer_Liabilities liabilitiesFragmentDataCarrier, List<NodeContainer_Liabilities> subGroupLiabilities) {
        for (NodeContainer_Liabilities dataCarrier: subGroupLiabilities) {
            if (dataCarrier.liabilitiesId == liabilitiesFragmentDataCarrier.liabilitiesId && dataCarrier.liabilitiesId != 0) {
                return;
            }
        }
        subGroupLiabilities.add(liabilitiesFragmentDataCarrier);
    }
}
