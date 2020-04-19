package protect.FinanceLord.FragmentAssetUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import protect.FinanceLord.Database.AssetsTypeQuery;
import protect.FinanceLord.Database.AssetsValue;

public class AssetsFragmentDataProcessor {

    private List<AssetsTypeQuery> dataList;
    private List<AssetsValue> assetsValues;

    public AssetsFragmentDataProcessor(List<AssetsTypeQuery> dataList, List<AssetsValue> assetsValues) {
        this.dataList = dataList;
        this.assetsValues = assetsValues;
    }

    public AssetsValue findAssetsValue(int assetsId) {
        for (AssetsValue assetsValue: this.assetsValues){
            if (assetsValue.getAssetsId() == assetsId){
                return assetsValue;
            }
        }
        return null;
    }

    public void setAssetValue(int assetId, float assetValue) {
        AssetsValue assetsValue = this.findAssetsValue(assetId);
        if (assetsValue != null) {
            assetsValue.setAssetsValue(assetValue);
            assetsValue.setDate(new Date().getTime());
        } else {
            assetsValue = new AssetsValue();
            assetsValue.setAssetsId(assetId);
            assetsValue.setAssetsValue(assetValue);
            assetsValue.setDate(new Date().getTime());
            this.assetsValues.add(assetsValue);
        }
    }

    public List<AssetsValue> getAllAssetsValues() {
        return this.assetsValues;
    }

    public void setAllAssetsValues(List<AssetsValue> assetsValues) {
        this.assetsValues = assetsValues;
    }

    public List<AssetsFragmentDataCarrier> getSubSet(String parentGroupLabel, int level) {
        List<AssetsFragmentDataCarrier> subGroupAssets = new ArrayList<>();

        if (level == 0) {
            for(AssetsTypeQuery assetsTypeQuery : dataList) {
                if (assetsTypeQuery.assetsFirstLevelName != null) {

                    AssetsFragmentDataCarrier dataCarrier = new AssetsFragmentDataCarrier(assetsTypeQuery.assetsFirstLevelName, assetsTypeQuery.assetsFirstLevelId, 0);
                    addCarrierIfNotExists(dataCarrier, subGroupAssets);
                }
            }
        } else if(level == 1) {
            for(AssetsTypeQuery assetsTypeQuery : dataList) {
                if (assetsTypeQuery.assetsFirstLevelName != null
                        && assetsTypeQuery.assetsFirstLevelName.equals(parentGroupLabel)
                        && assetsTypeQuery.assetsSecondLevelName != null) {

                    AssetsFragmentDataCarrier dataCarrier = new AssetsFragmentDataCarrier(assetsTypeQuery.assetsSecondLevelName, assetsTypeQuery.assetsSecondLevelId, 1);
                    addCarrierIfNotExists(dataCarrier, subGroupAssets);
                }
            }
        } else if(level == 2) {
            for(AssetsTypeQuery assetsTypeQuery : dataList) {
                if (assetsTypeQuery.assetsSecondLevelName != null
                        && assetsTypeQuery.assetsSecondLevelName.equals(parentGroupLabel)
                        && assetsTypeQuery.assetsThirdLevelName != null) {

                    AssetsFragmentDataCarrier dataCarrier = new AssetsFragmentDataCarrier(assetsTypeQuery.assetsThirdLevelName, assetsTypeQuery.assetsThirdLevelId, 2);
                    addCarrierIfNotExists(dataCarrier, subGroupAssets);
                }
            }
        } else if(level == 3) {
            for(AssetsTypeQuery assetsTypeQuery : dataList) {
                if(assetsTypeQuery.assetsThirdLevelName != null
                        && assetsTypeQuery.assetsThirdLevelName.equals(parentGroupLabel)
                        && assetsTypeQuery.assetsFourthLevelName != null) {

                    AssetsFragmentDataCarrier dataCarrier = new AssetsFragmentDataCarrier(assetsTypeQuery.assetsFourthLevelName, assetsTypeQuery.assetsFourthLevelId, 3);
                    addCarrierIfNotExists(dataCarrier, subGroupAssets);
                }
            }
        }
        return subGroupAssets;
    }

    void addCarrierIfNotExists(AssetsFragmentDataCarrier assetsFragmentDataCarrier, List<AssetsFragmentDataCarrier> subGroupAssets) {
        for(AssetsFragmentDataCarrier dataCarrier : subGroupAssets) {
            if(dataCarrier.assetsId == assetsFragmentDataCarrier.assetsId && dataCarrier.assetsId != 0) {
                return;
            }
        }
        subGroupAssets.add(assetsFragmentDataCarrier);
    }
}