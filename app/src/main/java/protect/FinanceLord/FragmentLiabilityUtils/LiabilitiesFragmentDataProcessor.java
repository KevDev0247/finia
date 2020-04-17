package protect.FinanceLord.FragmentLiabilityUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import protect.FinanceLord.Database.LiabilitiesTypeQuery;
import protect.FinanceLord.Database.LiabilitiesValue;

public class LiabilitiesFragmentDataProcessor {

    private List<LiabilitiesTypeQuery> dataList;
    private List<LiabilitiesValue> liabilitiesValues;

    public LiabilitiesFragmentDataProcessor(List<LiabilitiesTypeQuery> dataList, List<LiabilitiesValue> liabilitiesValues){
        this.dataList = dataList;
        this.liabilitiesValues = liabilitiesValues;
    }

    public LiabilitiesValue findLiabilitiesValue(int liabilitiesId){
        for (LiabilitiesValue liabilitiesValue: this.liabilitiesValues){
            if (liabilitiesValue.getLiabilitiesId() == liabilitiesId){
                return liabilitiesValue;
            }
        }
        return null;
    }

    public void setLiabilityValue(int assetId, float assetValue){
        LiabilitiesValue liabilitiesValue = this.findLiabilitiesValue(assetId);
        if (liabilitiesValue != null){
            liabilitiesValue.setLiabilitiesValue(assetValue);
            liabilitiesValue.setDate(new Date().getTime());
        } else {
            liabilitiesValue = new LiabilitiesValue();
            liabilitiesValue.setLiabilitiesId(assetId);
            liabilitiesValue.setLiabilitiesValue(assetValue);
            liabilitiesValue.setDate(new Date().getTime());
            this.liabilitiesValues.add(liabilitiesValue);
        }
    }

    public List<LiabilitiesValue> getAllLiabilitiesValues() {
        return liabilitiesValues;
    }

    public void setAllLiabilitiesValues(List<LiabilitiesValue> liabilitiesValues) {
        this.liabilitiesValues = liabilitiesValues;
    }

    public List<LiabilitiesFragmentDataCarrier> getGroupSet(String parentGroupLabel, int level){
        List<LiabilitiesFragmentDataCarrier> subGroupLiabilities = new ArrayList<>();

        if (level == 0){
            for (LiabilitiesTypeQuery liabilitiesTypeQuery: dataList){
                if (liabilitiesTypeQuery.liabilitiesFirstLevelName != null){

                    LiabilitiesFragmentDataCarrier dataCarrier = new LiabilitiesFragmentDataCarrier(liabilitiesTypeQuery.liabilitiesFirstLevelName, liabilitiesTypeQuery.liabilitiesFirstLevelId, 0);
                    addCarrierIfNotExists(dataCarrier, subGroupLiabilities);
                }
            }
        }

        if (level == 1){
            for (LiabilitiesTypeQuery liabilitiesTypeQuery: dataList){
                if (liabilitiesTypeQuery.liabilitiesFirstLevelName != null
                        && liabilitiesTypeQuery.liabilitiesFirstLevelName == parentGroupLabel
                        && liabilitiesTypeQuery.liabilitiesSecondLevelName != null){

                    LiabilitiesFragmentDataCarrier dataCarrier = new LiabilitiesFragmentDataCarrier(liabilitiesTypeQuery.liabilitiesSecondLevelName, liabilitiesTypeQuery.liabilitiesSecondLevelId, 1);
                    addCarrierIfNotExists(dataCarrier, subGroupLiabilities);
                }
            }
        }

        if (level == 2){
            for (LiabilitiesTypeQuery liabilitiesTypeQuery: dataList){
                if (liabilitiesTypeQuery.liabilitiesSecondLevelName != null
                        && liabilitiesTypeQuery.liabilitiesSecondLevelName == parentGroupLabel
                        && liabilitiesTypeQuery.liabilitiesThirdLevelName != null){

                    LiabilitiesFragmentDataCarrier dataCarrier = new LiabilitiesFragmentDataCarrier(liabilitiesTypeQuery.liabilitiesThirdLevelName, liabilitiesTypeQuery.liabilitiesThirdLevelId, 2);
                    addCarrierIfNotExists(dataCarrier, subGroupLiabilities);
                }
            }
        }

        return subGroupLiabilities;
    }

    void addCarrierIfNotExists(LiabilitiesFragmentDataCarrier liabilitiesFragmentDataCarrier, List<LiabilitiesFragmentDataCarrier> subGroupLiabilities){
        for (LiabilitiesFragmentDataCarrier dataCarrier: subGroupLiabilities){
            if (dataCarrier.liabilitiesId == liabilitiesFragmentDataCarrier.liabilitiesId && dataCarrier.liabilitiesId != 0){
                return;
            }

            subGroupLiabilities.add(liabilitiesFragmentDataCarrier);
        }
    }
}
