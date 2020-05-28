package protect.FinanceLord.NetWorthDataTerminal;

import java.util.ArrayList;
import java.util.List;

import protect.FinanceLord.Database.AssetsTypeQuery;

public class TypeProcessor_Assets {

    private List<AssetsTypeQuery> dataList;

    public TypeProcessor_Assets(List<AssetsTypeQuery> dataList){
        this.dataList = dataList;
    }

    public List<DataCarrier_Assets> getSubGroup(String parentGroupLabel, int level) {
        List<DataCarrier_Assets> subGroupAssets = new ArrayList<>();

        if (level == 0) {
            for(AssetsTypeQuery assetsTypeQuery : dataList) {
                if (assetsTypeQuery.assetsFirstLevelName != null) {

                    DataCarrier_Assets dataCarrier = new DataCarrier_Assets(assetsTypeQuery.assetsFirstLevelName, assetsTypeQuery.assetsFirstLevelId, 0);
                    addTypeToSubGroup(dataCarrier, subGroupAssets);
                }
            }
        } else if(level == 1) {
            for(AssetsTypeQuery assetsTypeQuery : dataList) {
                if (assetsTypeQuery.assetsFirstLevelName != null
                        && assetsTypeQuery.assetsFirstLevelName.equals(parentGroupLabel)
                        && assetsTypeQuery.assetsSecondLevelName != null) {

                    DataCarrier_Assets dataCarrier = new DataCarrier_Assets(assetsTypeQuery.assetsSecondLevelName, assetsTypeQuery.assetsSecondLevelId, 1);
                    addTypeToSubGroup(dataCarrier, subGroupAssets);
                }
            }
        } else if(level == 2) {
            for(AssetsTypeQuery assetsTypeQuery : dataList) {
                if (assetsTypeQuery.assetsSecondLevelName != null
                        && assetsTypeQuery.assetsSecondLevelName.equals(parentGroupLabel)
                        && assetsTypeQuery.assetsThirdLevelName != null) {

                    DataCarrier_Assets dataCarrier = new DataCarrier_Assets(assetsTypeQuery.assetsThirdLevelName, assetsTypeQuery.assetsThirdLevelId, 2);
                    addTypeToSubGroup(dataCarrier, subGroupAssets);
                }
            }
        } else if(level == 3) {
            for(AssetsTypeQuery assetsTypeQuery : dataList) {
                if(assetsTypeQuery.assetsThirdLevelName != null
                        && assetsTypeQuery.assetsThirdLevelName.equals(parentGroupLabel)
                        && assetsTypeQuery.assetsFourthLevelName != null) {

                    DataCarrier_Assets dataCarrier = new DataCarrier_Assets(assetsTypeQuery.assetsFourthLevelName, assetsTypeQuery.assetsFourthLevelId, 3);
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
