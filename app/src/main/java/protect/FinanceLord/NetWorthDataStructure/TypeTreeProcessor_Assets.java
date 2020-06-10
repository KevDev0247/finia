package protect.FinanceLord.NetWorthDataStructure;

import java.util.ArrayList;
import java.util.List;

public class TypeTreeProcessor_Assets {

    private List<TypeTreeLeaf_Assets> dataList;

    public TypeTreeProcessor_Assets(List<TypeTreeLeaf_Assets> dataList){
        this.dataList = dataList;
    }

    public List<NodeContainer_Assets> getSubGroup(String parentGroupLabel, int level) {
        List<NodeContainer_Assets> subGroupAssets = new ArrayList<>();

        if (level == 0) {
            for(TypeTreeLeaf_Assets assetsTypeTreeLeaf : dataList) {
                if (assetsTypeTreeLeaf.assetsFirstLevelName != null) {

                    NodeContainer_Assets dataCarrier = new NodeContainer_Assets(assetsTypeTreeLeaf.assetsFirstLevelName, assetsTypeTreeLeaf.assetsFirstLevelId, 0);
                    addTypeToSubGroup(dataCarrier, subGroupAssets);
                }
            }
        } else if(level == 1) {
            for(TypeTreeLeaf_Assets assetsTypeTreeLeaf : dataList) {
                if (assetsTypeTreeLeaf.assetsFirstLevelName != null
                        && assetsTypeTreeLeaf.assetsFirstLevelName.equals(parentGroupLabel)
                        && assetsTypeTreeLeaf.assetsSecondLevelName != null) {

                    NodeContainer_Assets dataCarrier = new NodeContainer_Assets(assetsTypeTreeLeaf.assetsSecondLevelName, assetsTypeTreeLeaf.assetsSecondLevelId, 1);
                    addTypeToSubGroup(dataCarrier, subGroupAssets);
                }
            }
        } else if(level == 2) {
            for(TypeTreeLeaf_Assets assetsTypeTreeLeaf : dataList) {
                if (assetsTypeTreeLeaf.assetsSecondLevelName != null
                        && assetsTypeTreeLeaf.assetsSecondLevelName.equals(parentGroupLabel)
                        && assetsTypeTreeLeaf.assetsThirdLevelName != null) {

                    NodeContainer_Assets dataCarrier = new NodeContainer_Assets(assetsTypeTreeLeaf.assetsThirdLevelName, assetsTypeTreeLeaf.assetsThirdLevelId, 2);
                    addTypeToSubGroup(dataCarrier, subGroupAssets);
                }
            }
        } else if(level == 3) {
            for(TypeTreeLeaf_Assets assetsTypeTreeLeaf : dataList) {
                if(assetsTypeTreeLeaf.assetsThirdLevelName != null
                        && assetsTypeTreeLeaf.assetsThirdLevelName.equals(parentGroupLabel)
                        && assetsTypeTreeLeaf.assetsFourthLevelName != null) {

                    NodeContainer_Assets dataCarrier = new NodeContainer_Assets(assetsTypeTreeLeaf.assetsFourthLevelName, assetsTypeTreeLeaf.assetsFourthLevelId, 3);
                    addTypeToSubGroup(dataCarrier, subGroupAssets);
                }
            }
        }

        return subGroupAssets;
    }

    void addTypeToSubGroup(NodeContainer_Assets assetsFragmentDataCarrier, List<NodeContainer_Assets> subGroupAssets) {
        for(NodeContainer_Assets dataCarrier : subGroupAssets) {
            if(dataCarrier.assetsTypeId == assetsFragmentDataCarrier.assetsTypeId && dataCarrier.assetsTypeId != 0) {
                return;
            }
        }
        subGroupAssets.add(assetsFragmentDataCarrier);
    }
}
