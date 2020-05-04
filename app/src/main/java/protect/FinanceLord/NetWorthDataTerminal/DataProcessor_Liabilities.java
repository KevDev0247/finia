package protect.FinanceLord.NetWorthDataTerminal;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import protect.FinanceLord.Database.LiabilitiesTypeQuery;
import protect.FinanceLord.Database.LiabilitiesValue;
import protect.FinanceLord.R;

public class DataProcessor_Liabilities {

    private Context context;
    private Date date;
    private List<LiabilitiesTypeQuery> dataList;
    private List<LiabilitiesValue> liabilitiesValues;

    public DataProcessor_Liabilities(List<LiabilitiesTypeQuery> dataList, List<LiabilitiesValue> liabilitiesValues, Date date, Context context){
        this.context = context;
        this.date = date;
        this.dataList = dataList;
        this.liabilitiesValues = liabilitiesValues;
    }

    public LiabilitiesValue getLiabilitiesValue(int liabilitiesId){

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



    private int getAssetsId(String liabilitiesName){

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
}
