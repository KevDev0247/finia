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
