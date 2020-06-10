package protect.FinanceLord.NetWorthDataTerminal;

import java.util.ArrayList;
import java.util.List;

import protect.FinanceLord.Database.AssetsTypeTree;

public class TypeProcessor_Assets {

    private List<AssetsTypeTree> dataList;

    public TypeProcessor_Assets(List<AssetsTypeTree> dataList){
        this.dataList = dataList;
    }

    public List<DataCarrier_Assets> getSubGroup(String parentGroupLabel, int level) {
        List<DataCarrier_Assets> subGroupAssets = new ArrayList<>();

        if (level == 0) {
            for(AssetsTypeTree assetsTypeTree : dataList) {
                if (assetsTypeTree.assetsFirstLevelName != null) {

                    DataCarrier_Assets dataCarrier = new DataCarrier_Assets(assetsTypeTree.assetsFirstLevelName, assetsTypeTree.assetsFirstLevelId, 0);
                    addTypeToSubGroup(dataCarrier, subGroupAssets);
                }
            }
        } else if(level == 1) {
            for(AssetsTypeTree assetsTypeTree : dataList) {
                if (assetsTypeTree.assetsFirstLevelName != null
                        && assetsTypeTree.assetsFirstLevelName.equals(parentGroupLabel)
                        && assetsTypeTree.assetsSecondLevelName != null) {

                    DataCarrier_Assets dataCarrier = new DataCarrier_Assets(assetsTypeTree.assetsSecondLevelName, assetsTypeTree.assetsSecondLevelId, 1);
                    addTypeToSubGroup(dataCarrier, subGroupAssets);
                }
            }
        } else if(level == 2) {
            for(AssetsTypeTree assetsTypeTree : dataList) {
                if (assetsTypeTree.assetsSecondLevelName != null
                        && assetsTypeTree.assetsSecondLevelName.equals(parentGroupLabel)
                        && assetsTypeTree.assetsThirdLevelName != null) {

                    DataCarrier_Assets dataCarrier = new DataCarrier_Assets(assetsTypeTree.assetsThirdLevelName, assetsTypeTree.assetsThirdLevelId, 2);
                    addTypeToSubGroup(dataCarrier, subGroupAssets);
                }
            }
        } else if(level == 3) {
            for(AssetsTypeTree assetsTypeTree : dataList) {
                if(assetsTypeTree.assetsThirdLevelName != null
                        && assetsTypeTree.assetsThirdLevelName.equals(parentGroupLabel)
                        && assetsTypeTree.assetsFourthLevelName != null) {

                    DataCarrier_Assets dataCarrier = new DataCarrier_Assets(assetsTypeTree.assetsFourthLevelName, assetsTypeTree.assetsFourthLevelId, 3);
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
