package protect.FinanceLord.NetWorthDataStructureProcessors;

import java.util.ArrayList;
import java.util.List;

import protect.FinanceLord.Database.AssetsTypeTreeLeaf;

public class TypeTreeProcessor_Assets {

    private List<AssetsTypeTreeLeaf> dataList;

    public TypeTreeProcessor_Assets(List<AssetsTypeTreeLeaf> dataList){
        this.dataList = dataList;
    }

    public List<DataCarrier_Assets> getSubGroup(String parentGroupLabel, int level) {
        List<DataCarrier_Assets> subGroupAssets = new ArrayList<>();

        if (level == 0) {
            for(AssetsTypeTreeLeaf assetsTypeTreeLeaf : dataList) {
                if (assetsTypeTreeLeaf.assetsFirstLevelName != null) {

                    DataCarrier_Assets dataCarrier = new DataCarrier_Assets(assetsTypeTreeLeaf.assetsFirstLevelName, assetsTypeTreeLeaf.assetsFirstLevelId, 0);
                    addTypeToSubGroup(dataCarrier, subGroupAssets);
                }
            }
        } else if(level == 1) {
            for(AssetsTypeTreeLeaf assetsTypeTreeLeaf : dataList) {
                if (assetsTypeTreeLeaf.assetsFirstLevelName != null
                        && assetsTypeTreeLeaf.assetsFirstLevelName.equals(parentGroupLabel)
                        && assetsTypeTreeLeaf.assetsSecondLevelName != null) {

                    DataCarrier_Assets dataCarrier = new DataCarrier_Assets(assetsTypeTreeLeaf.assetsSecondLevelName, assetsTypeTreeLeaf.assetsSecondLevelId, 1);
                    addTypeToSubGroup(dataCarrier, subGroupAssets);
                }
            }
        } else if(level == 2) {
            for(AssetsTypeTreeLeaf assetsTypeTreeLeaf : dataList) {
                if (assetsTypeTreeLeaf.assetsSecondLevelName != null
                        && assetsTypeTreeLeaf.assetsSecondLevelName.equals(parentGroupLabel)
                        && assetsTypeTreeLeaf.assetsThirdLevelName != null) {

                    DataCarrier_Assets dataCarrier = new DataCarrier_Assets(assetsTypeTreeLeaf.assetsThirdLevelName, assetsTypeTreeLeaf.assetsThirdLevelId, 2);
                    addTypeToSubGroup(dataCarrier, subGroupAssets);
                }
            }
        } else if(level == 3) {
            for(AssetsTypeTreeLeaf assetsTypeTreeLeaf : dataList) {
                if(assetsTypeTreeLeaf.assetsThirdLevelName != null
                        && assetsTypeTreeLeaf.assetsThirdLevelName.equals(parentGroupLabel)
                        && assetsTypeTreeLeaf.assetsFourthLevelName != null) {

                    DataCarrier_Assets dataCarrier = new DataCarrier_Assets(assetsTypeTreeLeaf.assetsFourthLevelName, assetsTypeTreeLeaf.assetsFourthLevelId, 3);
                    addTypeToSubGroup(dataCarrier, subGroupAssets);
                }
            }
        }

        return subGroupAssets;
    }

    void addTypeToSubGroup(DataCarrier_Assets assetsFragmentDataCarrier, List<DataCarrier_Assets> subGroupAssets) {
        for(DataCarrier_Assets dataCarrier : subGroupAssets) {
            if(dataCarrier.assetsTypeId == assetsFragmentDataCarrier.assetsTypeId && dataCarrier.assetsTypeId != 0) {
                return;
            }
        }
        subGroupAssets.add(assetsFragmentDataCarrier);
    }
}
