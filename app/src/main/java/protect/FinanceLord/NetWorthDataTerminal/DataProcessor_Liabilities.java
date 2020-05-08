package protect.FinanceLord.NetWorthDataTerminal;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import protect.FinanceLord.Database.LiabilitiesTypeQuery;
import protect.FinanceLord.Database.LiabilitiesValue;
import protect.FinanceLord.Database.LiabilitiesValueDao;
import protect.FinanceLord.R;

public class DataProcessor_Liabilities {

    private Context context;
    private Date currentTime;
    private List<LiabilitiesTypeQuery> dataList;
    private List<LiabilitiesValue> liabilitiesValues;

    public DataProcessor_Liabilities(List<LiabilitiesTypeQuery> dataList, List<LiabilitiesValue> liabilitiesValues, Date currentTime, Context context){
        this.context = context;
        this.currentTime = currentTime;
        this.dataList = dataList;
        this.liabilitiesValues = liabilitiesValues;
    }

    public LiabilitiesValue getLiabilitiesValue(long liabilitiesId){

        for (LiabilitiesValue liabilitiesValue: this.liabilitiesValues){
            if (liabilitiesValue.getLiabilitiesId() == liabilitiesId){
                return liabilitiesValue;
            }
        }
        return null;
    }

    public void setLiabilityValue(int assetId, float assetValue){

        LiabilitiesValue liabilitiesValue = this.getLiabilitiesValue(assetId);
        if (liabilitiesValue != null){
            liabilitiesValue.setLiabilitiesValue(assetValue);
        } else {
            liabilitiesValue = new LiabilitiesValue();
            liabilitiesValue.setLiabilitiesId(assetId);
            liabilitiesValue.setLiabilitiesValue(assetValue);
            this.liabilitiesValues.add(liabilitiesValue);
        }
    }

    public List<LiabilitiesValue> getAllLiabilitiesValues() {
        return liabilitiesValues;
    }

    public void setAllLiabilitiesValues(List<LiabilitiesValue> liabilitiesValues) {
        this.liabilitiesValues = liabilitiesValues;
    }

    public void clearAllLiabilitiesValues() {
        this.liabilitiesValues.clear();
    }

    public List<DataCarrier_Liabilities> getSubSet(String parentGroupLabel, int level){

        List<DataCarrier_Liabilities> subGroupLiabilities = new ArrayList<>();

        if (level == 0){
            for (LiabilitiesTypeQuery liabilitiesTypeQuery: dataList){
                if (liabilitiesTypeQuery.liabilitiesFirstLevelName != null){

                    DataCarrier_Liabilities dataCarrier = new DataCarrier_Liabilities(liabilitiesTypeQuery.liabilitiesFirstLevelName, liabilitiesTypeQuery.liabilitiesFirstLevelId, 0);
                    addCarrierIfNotExists(dataCarrier, subGroupLiabilities);
                }
            }
        }

        if (level == 1){
            for (LiabilitiesTypeQuery liabilitiesTypeQuery: dataList){
                if (liabilitiesTypeQuery.liabilitiesFirstLevelName != null
                        && liabilitiesTypeQuery.liabilitiesFirstLevelName == parentGroupLabel
                        && liabilitiesTypeQuery.liabilitiesSecondLevelName != null){

                    DataCarrier_Liabilities dataCarrier = new DataCarrier_Liabilities(liabilitiesTypeQuery.liabilitiesSecondLevelName, liabilitiesTypeQuery.liabilitiesSecondLevelId, 1);
                    addCarrierIfNotExists(dataCarrier, subGroupLiabilities);
                }
            }
        }

        if (level == 2){
            for (LiabilitiesTypeQuery liabilitiesTypeQuery: dataList){
                if (liabilitiesTypeQuery.liabilitiesSecondLevelName != null
                        && liabilitiesTypeQuery.liabilitiesSecondLevelName == parentGroupLabel
                        && liabilitiesTypeQuery.liabilitiesThirdLevelName != null){

                    DataCarrier_Liabilities dataCarrier = new DataCarrier_Liabilities(liabilitiesTypeQuery.liabilitiesThirdLevelName, liabilitiesTypeQuery.liabilitiesThirdLevelId, 2);
                    addCarrierIfNotExists(dataCarrier, subGroupLiabilities);
                }
            }
        }

        return subGroupLiabilities;
    }

    void addCarrierIfNotExists(DataCarrier_Liabilities liabilitiesFragmentDataCarrier, List<DataCarrier_Liabilities> subGroupLiabilities){

        for (DataCarrier_Liabilities dataCarrier: subGroupLiabilities){
            if (dataCarrier.liabilitiesId == liabilitiesFragmentDataCarrier.liabilitiesId && dataCarrier.liabilitiesId != 0){
                return;
            }

            subGroupLiabilities.add(liabilitiesFragmentDataCarrier);
        }
    }



    private int getLiabilitiesId(String liabilitiesName){

        for (LiabilitiesTypeQuery query : dataList){
            if (query.liabilitiesFirstLevelName != null && query.liabilitiesFirstLevelName.equals(liabilitiesName)){
                return query.liabilitiesFirstLevelId;
            } else if (query.liabilitiesSecondLevelName != null && query.liabilitiesSecondLevelName.equals(liabilitiesName)){
                return query.liabilitiesSecondLevelId;
            } else if (query.liabilitiesThirdLevelName != null && query.liabilitiesThirdLevelName.equals(liabilitiesName)){
                return query.liabilitiesThirdLevelId;
            }
        }
        return 0;
    }

    private List<Integer> getLiabilitiesIDsBelongTo(String liabilitiesName){

        if (TextUtils.isEmpty(liabilitiesName)){
            return new ArrayList<>();
        }

        List liabilitiesIDs = new ArrayList();
        if (context.getString(R.string.short_term_liabilities_name).equals(liabilitiesName)){
            for (LiabilitiesTypeQuery query: dataList){
                if (query.liabilitiesSecondLevelName != null
                        && query.liabilitiesSecondLevelName.equals(liabilitiesName)
                        && query.liabilitiesThirdLevelName != null){
                    liabilitiesIDs.add(query.liabilitiesThirdLevelId);
                }
            }
        } else if (context.getString(R.string.long_term_liabilities_name).equals(liabilitiesName)){
            for (LiabilitiesTypeQuery query : dataList){
                if (query.liabilitiesSecondLevelName != null
                        && query.liabilitiesSecondLevelName.equals(liabilitiesName)
                        && query.liabilitiesThirdLevelName != null){
                    liabilitiesIDs.add(query.liabilitiesThirdLevelId);
                }
            }
        }

        return liabilitiesIDs;
    }

    public float calculateTotalLiability(){

        float totalLongTermLiabilities = calculateTotalLongTermLiabilities();
        float totalShortTermLiabilities = calculateTotalShortTermLiabilities();
        float totalLiabilities = totalShortTermLiabilities + totalLongTermLiabilities;

        return totalLiabilities;
    }

    public float calculateTotalLongTermLiabilities(){

        List<Integer> liabilitiesIDs = this.getLiabilitiesIDsBelongTo(context.getString(R.string.long_term_liabilities_name));
        float totalLongTermLiabilities = 0;
        for (int liabilitiesId : liabilitiesIDs){
            LiabilitiesValue liabilitiesValue = this.getLiabilitiesValue(liabilitiesId);

            if (liabilitiesValue != null){
                Log.d("Long Term id", String.valueOf(liabilitiesValue.getLiabilitiesId()));
                Log.d("Long Term value", String.valueOf(liabilitiesValue.getLiabilitiesValue()));

                totalLongTermLiabilities += liabilitiesValue.getLiabilitiesValue();
            } else {
                Log.d("Long Term Liabilities","null");
            }
        }

        return totalLongTermLiabilities;
    }

    public float calculateTotalShortTermLiabilities(){

        List<Integer> liabilitiesIDs = this.getLiabilitiesIDsBelongTo(context.getString(R.string.short_term_liabilities_name));
        float totalShortTermLiabilities = 0;
        for (int liabilitiesId : liabilitiesIDs){
            LiabilitiesValue liabilitiesValue = this.getLiabilitiesValue(liabilitiesId);

            if (liabilitiesValue != null){
                Log.d("Long Term id", String.valueOf(liabilitiesValue.getLiabilitiesId()));
                Log.d("Long Term value", String.valueOf(liabilitiesValue.getLiabilitiesValue()));

                totalShortTermLiabilities += liabilitiesValue.getLiabilitiesValue();
            } else {
                Log.d("Long Term Liabilities","null");
            }
        }

        return totalShortTermLiabilities;
    }

    public void calculateAndInsertParentLiabilities(LiabilitiesValueDao liabilitiesValueDao) {

        float totalLiabilities = this.calculateTotalLiability();
        float totalShortTermLiabilities = this.calculateTotalShortTermLiabilities();
        float totalLongTermLiabilities = this.calculateTotalLongTermLiabilities();

        int totalLiabilitiesId = this.getLiabilitiesId(context.getString(R.string.total_liabilities_name));
        int shortTermLiabilitiesId = this.getLiabilitiesId(context.getString(R.string.short_term_liabilities_name));
        int longTernLiabilitiesId = this.getLiabilitiesId(context.getString(R.string.long_term_liabilities_name));

        LiabilitiesValue totalLiabilitiesValue = this.getLiabilitiesValue(totalLiabilitiesId);
        if (totalLiabilitiesValue != null){
            totalLiabilitiesValue.setLiabilitiesValue(totalLiabilities);
            totalLiabilitiesValue.setDate(currentTime.getTime());

            liabilitiesValueDao.updateLiabilityValue(totalLiabilitiesValue);
        } else {
            totalLiabilitiesValue = new LiabilitiesValue();
            totalLiabilitiesValue.setLiabilitiesId(totalLiabilitiesId);
            totalLiabilitiesValue.setLiabilitiesValue(totalLiabilities);
            totalLiabilitiesValue.setDate(currentTime.getTime());

            liabilitiesValueDao.insertLiabilityValue(totalLiabilitiesValue);
        }

        LiabilitiesValue shortTermLiabilitiesValue = this.getLiabilitiesValue(shortTermLiabilitiesId);
        if (shortTermLiabilitiesValue != null){
            shortTermLiabilitiesValue.setLiabilitiesValue(totalShortTermLiabilities);
            shortTermLiabilitiesValue.setDate(currentTime.getTime());

            liabilitiesValueDao.updateLiabilityValue(shortTermLiabilitiesValue);
        } else {
            shortTermLiabilitiesValue = new LiabilitiesValue();
            shortTermLiabilitiesValue.setLiabilitiesId(shortTermLiabilitiesId);
            shortTermLiabilitiesValue.setLiabilitiesValue(totalShortTermLiabilities);
            shortTermLiabilitiesValue.setDate(currentTime.getTime());

            liabilitiesValueDao.insertLiabilityValue(shortTermLiabilitiesValue);
        }

        LiabilitiesValue longTermLiabilitiesValue = this.getLiabilitiesValue(longTernLiabilitiesId);
        if (longTermLiabilitiesValue != null){
            longTermLiabilitiesValue.setLiabilitiesValue(totalLongTermLiabilities);
            longTermLiabilitiesValue.setDate(currentTime.getTime());

            liabilitiesValueDao.updateLiabilityValue(longTermLiabilitiesValue);
        } else {
            longTermLiabilitiesValue = new LiabilitiesValue();
            longTermLiabilitiesValue.setLiabilitiesId(longTernLiabilitiesId);
            longTermLiabilitiesValue.setLiabilitiesValue(totalLongTermLiabilities);
            longTermLiabilitiesValue.setDate(currentTime.getTime());

            liabilitiesValueDao.insertLiabilityValue(longTermLiabilitiesValue);
        }
    }
}
