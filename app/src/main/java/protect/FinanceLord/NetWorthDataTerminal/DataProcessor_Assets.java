package protect.FinanceLord.NetWorthDataTerminal;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import protect.FinanceLord.Database.AssetsTypeQuery;
import protect.FinanceLord.Database.AssetsValue;
import protect.FinanceLord.Database.AssetsValueDao;

public class DataProcessor_Assets {

    private List<AssetsTypeQuery> dataList;
    private List<AssetsValue> assetsValues;

    public DataProcessor_Assets(List<AssetsTypeQuery> dataList, List<AssetsValue> assetsValues) {
        this.dataList = dataList;
        this.assetsValues = assetsValues;
    }

    public AssetsValue findAssetsValue(long assetsId) {
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
            // this still uses the old mechanism (automatically retrieve the current time)
            //assetsValue.setDate(new Date().getTime());
            this.assetsValues.add(assetsValue);
        }
    }

    public List<AssetsValue> getAllAssetsValues() {
        return this.assetsValues;
    }

    public void setAllAssetsValues(List<AssetsValue> assetsValues) {
        this.assetsValues = assetsValues;
    }

    public List<DataCarrier_Assets> getSubSet(String parentGroupLabel, int level) {
        List<DataCarrier_Assets> subGroupAssets = new ArrayList<>();

        if (level == 0) {
            for(AssetsTypeQuery assetsTypeQuery : dataList) {
                if (assetsTypeQuery.assetsFirstLevelName != null) {

                    DataCarrier_Assets dataCarrier = new DataCarrier_Assets(assetsTypeQuery.assetsFirstLevelName, assetsTypeQuery.assetsFirstLevelId, 0);
                    addCarrierIfNotExists(dataCarrier, subGroupAssets);
                }
            }
        } else if(level == 1) {
            for(AssetsTypeQuery assetsTypeQuery : dataList) {
                if (assetsTypeQuery.assetsFirstLevelName != null
                        && assetsTypeQuery.assetsFirstLevelName.equals(parentGroupLabel)
                        && assetsTypeQuery.assetsSecondLevelName != null) {

                    DataCarrier_Assets dataCarrier = new DataCarrier_Assets(assetsTypeQuery.assetsSecondLevelName, assetsTypeQuery.assetsSecondLevelId, 1);
                    addCarrierIfNotExists(dataCarrier, subGroupAssets);
                }
            }
        } else if(level == 2) {
            for(AssetsTypeQuery assetsTypeQuery : dataList) {
                if (assetsTypeQuery.assetsSecondLevelName != null
                        && assetsTypeQuery.assetsSecondLevelName.equals(parentGroupLabel)
                        && assetsTypeQuery.assetsThirdLevelName != null) {

                    DataCarrier_Assets dataCarrier = new DataCarrier_Assets(assetsTypeQuery.assetsThirdLevelName, assetsTypeQuery.assetsThirdLevelId, 2);
                    addCarrierIfNotExists(dataCarrier, subGroupAssets);
                }
            }
        } else if(level == 3) {
            for(AssetsTypeQuery assetsTypeQuery : dataList) {
                if(assetsTypeQuery.assetsThirdLevelName != null
                        && assetsTypeQuery.assetsThirdLevelName.equals(parentGroupLabel)
                        && assetsTypeQuery.assetsFourthLevelName != null) {

                    DataCarrier_Assets dataCarrier = new DataCarrier_Assets(assetsTypeQuery.assetsFourthLevelName, assetsTypeQuery.assetsFourthLevelId, 3);
                    addCarrierIfNotExists(dataCarrier, subGroupAssets);
                }
            }
        }

        return subGroupAssets;
    }

    void addCarrierIfNotExists(DataCarrier_Assets assetsFragmentDataCarrier, List<DataCarrier_Assets> subGroupAssets) {
        for(DataCarrier_Assets dataCarrier : subGroupAssets) {
            if(dataCarrier.assetsId == assetsFragmentDataCarrier.assetsId && dataCarrier.assetsId != 0) {
                return;
            }
        }
        subGroupAssets.add(assetsFragmentDataCarrier);
    }



    private int getAssetsId(String assetsName) {
        for(AssetsTypeQuery query : dataList) {
            if (query.assetsFirstLevelName != null && query.assetsFirstLevelName.equals(assetsName)) {
                return query.assetsFirstLevelId;
            } else if (query.assetsSecondLevelName != null && query.assetsSecondLevelName.equals(assetsName)) {
                return query.assetsSecondLevelId;
            } else if (query.assetsThirdLevelName != null && query.assetsThirdLevelName.equals(assetsName)) {
                return query.assetsThirdLevelId;
            } else if (query.assetsFourthLevelName != null && query.assetsFourthLevelName.equals(assetsName)) {
                return query.assetsFourthLevelId;
            }
        }
        return 0;
    }

    private List<Integer> getAssetsIDsBelongsTo(String assetsName) {

        if (TextUtils.isEmpty(assetsName)) {
            return new ArrayList<>();
        }

        List assetsIDs = new ArrayList();
        if ("Ownership interests".equals(assetsName)) {
            for (AssetsTypeQuery query : dataList) {
                if (query.assetsThirdLevelName != null
                        && query.assetsThirdLevelName.equals(assetsName)
                        && query.assetsFourthLevelName != null) {
                    assetsIDs.add(query.assetsFourthLevelId);
                }
            }
        } else if ("Retirement accounts".equals(assetsName)) {
            for (AssetsTypeQuery query : dataList) {
                if (query.assetsThirdLevelName != null
                        && query.assetsThirdLevelName.equals(assetsName)
                        && query.assetsFourthLevelName != null) {
                    assetsIDs.add(query.assetsFourthLevelId);
                }
            }
        } else if ("Taxable accounts".equals(assetsName)) {
            for (AssetsTypeQuery query : dataList) {
                if (query.assetsThirdLevelName != null
                        && query.assetsThirdLevelName.equals(assetsName)
                        && query.assetsFourthLevelName != null) {
                    assetsIDs.add(query.assetsFourthLevelId);
                }
            }
        } else if ("Liquid assets".equals(assetsName)) {
            for (AssetsTypeQuery query : dataList) {
                if (query.assetsSecondLevelName != null
                        && query.assetsSecondLevelName.equals(assetsName)
                        && query.assetsThirdLevelName != null) {
                    assetsIDs.add(query.assetsThirdLevelId);
                }
            }
        } else if ("Personal assets".equals(assetsName)) {
            for (AssetsTypeQuery query : dataList) {
                if (query.assetsSecondLevelName != null
                        && query.assetsSecondLevelName.equals(assetsName)
                        && query.assetsThirdLevelName != null) {
                    assetsIDs.add(query.assetsThirdLevelId);
                }
            }
        }
        return assetsIDs;
    }

    private AssetsValue getAssetsValue(long assetsId) {
        for (AssetsValue assetsValue : assetsValues) {
            if (assetsValue.getAssetsId() == assetsId) {
                return assetsValue;
            }
        }
        return null;
    }

    public float calculateTotalAssets() {
        float totalInvestedAssets = calculateTotalInvestedAssets();
        float totalLiquidAssets = calculateTotalLiquidAssets();
        float totalPersonalAssets = calculateTotalPersonalAssets();
        float totalAssets = totalInvestedAssets + totalLiquidAssets + totalPersonalAssets;

        return totalAssets;
    }

    public float calculateTotalLiquidAssets() {

        List<Integer> assetsIDs = this.getAssetsIDsBelongsTo("Liquid assets");
        float totalLiquidAssets = 0;
        for (int assetsId : assetsIDs) {
            AssetsValue assetsValue = this.getAssetsValue(assetsId);

            if (assetsValue != null) {

                Log.d("Liquid assets id", String.valueOf(assetsValue.getAssetsId()));
                Log.d("Liquid assets value", String.valueOf(assetsValue.getAssetsValue()));

                totalLiquidAssets += assetsValue.getAssetsValue();
            } else {
                Log.d("Liquid assets null", "null");
            }
        }

        return totalLiquidAssets;
    }

    public float calculateTotalPersonalAssets(){

        List<Integer> assetsIDs = this.getAssetsIDsBelongsTo("Personal assets");
        float totalPersonalAssets = 0;
        for (int assetsId : assetsIDs) {
            AssetsValue assetsValue = this.getAssetsValue(assetsId);

            if (assetsValue != null) {

                Log.d("Personal assets id", String.valueOf(assetsValue.getAssetsId()));
                Log.d("Personal assets value", String.valueOf(assetsValue.getAssetsValue()));

                totalPersonalAssets += assetsValue.getAssetsValue();
            } else {
                Log.d("Personal null", "null");
            }
        }

        return totalPersonalAssets;
    }

    public float calculateTotalInvestedAssets() {

        float totalOwnershipInterests = calculateTotalOwnershipInterests();
        float totalRetirementAccounts = calculateTotalRetirementAccounts();
        float totalTaxableAccounts = calculateTotalTaxableAccounts();
        float totalInvestedAssets = totalOwnershipInterests + totalRetirementAccounts + totalTaxableAccounts;

        return totalInvestedAssets;
    }

    public float calculateTotalOwnershipInterests() {

        List<Integer> assetsIDs = this.getAssetsIDsBelongsTo("Ownership interests");
        float totalOwnershipInterest = 0;
        for (int assetsId : assetsIDs) {
            AssetsValue assetsValue = this.getAssetsValue(assetsId);

            if (assetsValue != null) {

                Log.d("Ownership id", String.valueOf(assetsValue.getAssetsId()));
                Log.d("Ownership value", String.valueOf(assetsValue.getAssetsValue()));

                totalOwnershipInterest += assetsValue.getAssetsValue();
            } else {
                Log.d("Ownership null", "null");
            }
        }

        return totalOwnershipInterest;
    }

    public float calculateTotalRetirementAccounts() {

        List<Integer> assetsIDs = this.getAssetsIDsBelongsTo("Retirement accounts");
        float totalRetirementAccounts = 0;
        for (int assetId : assetsIDs) {
            AssetsValue assetsValue = this.getAssetsValue(assetId);

            if (assetsValue != null) {

                Log.d("Retirement id", String.valueOf(assetsValue.getAssetsId()));
                Log.d("Retirement value", String.valueOf(assetsValue.getAssetsValue()));

                totalRetirementAccounts += assetsValue.getAssetsValue();
            } else {
                Log.d("Retirement null", "null");
            }
        }

        return totalRetirementAccounts;
    }

    public float calculateTotalTaxableAccounts(){

        List<Integer> assetsIDs = this.getAssetsIDsBelongsTo("Taxable accounts");
        float totalTotalTaxableAccounts = 0;
        for (int assetsId : assetsIDs) {
            AssetsValue assetsValue = this.getAssetsValue(assetsId);

            if (assetsValue != null) {

                Log.d("Taxable id", String.valueOf(assetsValue.getAssetsId()));
                Log.d("Taxable value", String.valueOf(assetsValue.getAssetsValue()));

                totalTotalTaxableAccounts += assetsValue.getAssetsValue();
            } else {
                Log.d("Taxable null", "null");
            }
        }

        return totalTotalTaxableAccounts;
    }


    public void calculateParentAssets(AssetsValueDao assetsValueDao) {

        float totalAssets = this.calculateTotalAssets();
        float totalLiquidAssets = this.calculateTotalLiquidAssets();
        float totalInvestedAssets = this.calculateTotalInvestedAssets();
        float totalPersonalAssets = this.calculateTotalPersonalAssets();
        float totalTaxableAccounts = this.calculateTotalTaxableAccounts();
        float totalRetirementAccounts = this.calculateTotalRetirementAccounts();
        float totalOwnershipInterests = this.calculateTotalOwnershipInterests();

        Log.d("DataProcessorAssets", "Total Assets value is " + totalAssets);
        Log.d("DataProcessorAssets", "Liquid Assets value is " + totalLiquidAssets);
        Log.d("DataProcessorAssets", "Invested Assets value is " + totalInvestedAssets);
        Log.d("DataProcessorAssets", "Personal Assets value is " + totalPersonalAssets);
        Log.d("DataProcessorAssets", "Taxable Accounts value is " + totalTaxableAccounts);
        Log.d("DataProcessorAssets", "Retirement Accounts value is " + totalRetirementAccounts);
        Log.d("DataProcessorAssets", "Ownership Interests value is " + totalOwnershipInterests);

        long totalAssetsId = this.getAssetsId("Total Assets");
        long liquidAssetsId = this.getAssetsId("Liquid assets");
        long investedAssetsId = this.getAssetsId("Invested assets");
        long personalAssetsId = this.getAssetsId("Personal assets");
        long taxableAccountAssetsId = this.getAssetsId("Taxable accounts");
        long retirementAccountAssetsId = this.getAssetsId("Retirement accounts");
        long ownershipInterestsAssetsId = this.getAssetsId("Ownership interests");

        AssetsValue totalAssetsValue = this.findAssetsValue(totalAssetsId);
        if (totalAssetsValue != null) {
            totalAssetsValue.setAssetsValue(totalAssets);
            totalAssetsValue.setDate(new Date().getTime());
            assetsValueDao.updateAssetValue();
        } else {
            totalAssetsValue = new AssetsValue();
            totalAssetsValue.setAssetsId((int)totalAssetsId);
            totalAssetsValue.setAssetsValue(totalAssets);
            totalAssetsValue.setDate(new Date().getTime());
            // I don't know what this is for
            //this.assetsValues.add(totalAssetsValue);

            assetsValueDao.insertAssetValue(totalAssetsValue);
        }

        AssetsValue liquidAssetsValue = this.findAssetsValue(liquidAssetsId);
        if (liquidAssetsValue != null) {
            liquidAssetsValue.setAssetsValue(totalLiquidAssets);
            liquidAssetsValue.setDate(new Date().getTime());
            assetsValueDao.updateAssetValue();
        } else {
            liquidAssetsValue = new AssetsValue();
            liquidAssetsValue.setAssetsId((int)liquidAssetsId);
            liquidAssetsValue.setAssetsValue(totalLiquidAssets);
            liquidAssetsValue.setDate(new Date().getTime());

            assetsValueDao.insertAssetValue(liquidAssetsValue);
        }

        AssetsValue totalInvestedAssetsValue = this.findAssetsValue(investedAssetsId);
        if (totalInvestedAssetsValue != null) {
            totalInvestedAssetsValue.setAssetsValue(totalInvestedAssets);
            totalInvestedAssetsValue.setDate(new Date().getTime());

            assetsValueDao.updateAssetValue(totalInvestedAssetsValue);
        } else {
            totalInvestedAssetsValue = new AssetsValue();
            totalInvestedAssetsValue.setAssetsId((int)investedAssetsId);
            totalInvestedAssetsValue.setAssetsValue(totalInvestedAssets);
            totalInvestedAssetsValue.setDate(new Date().getTime());

            assetsValueDao.insertAssetValue(totalInvestedAssetsValue);
        }

        AssetsValue personalAssetsValue = this.findAssetsValue((int)personalAssetsId);
        if (personalAssetsValue != null) {
            personalAssetsValue.setAssetsValue(totalPersonalAssets);
            personalAssetsValue.setDate(new Date().getTime());
            assetsValueDao.updateAssetValue(personalAssetsValue);
        } else {
            personalAssetsValue = new AssetsValue();
            personalAssetsValue.setAssetsId((int)personalAssetsId);
            personalAssetsValue.setAssetsValue(totalPersonalAssets);
            personalAssetsValue.setDate(new Date().getTime());

            assetsValueDao.insertAssetValue(personalAssetsValue);
        }

        AssetsValue taxableAssetsValue = this.findAssetsValue(taxableAccountAssetsId);
        if (taxableAssetsValue != null) {
            taxableAssetsValue.setAssetsValue(totalTaxableAccounts);
            taxableAssetsValue.setDate(new Date().getTime());

            assetsValueDao.updateAssetValue(taxableAssetsValue);
        } else {
            taxableAssetsValue = new AssetsValue();
            taxableAssetsValue.setAssetsId((int)taxableAccountAssetsId);;
            taxableAssetsValue.setAssetsValue(totalTaxableAccounts);
            taxableAssetsValue.setDate(new Date().getTime());

            assetsValueDao.insertAssetValue(taxableAssetsValue);
        }

        AssetsValue retirementAccountAssetsValue = this.findAssetsValue(retirementAccountAssetsId);
        if (retirementAccountAssetsValue != null) {
            retirementAccountAssetsValue.setAssetsValue(totalRetirementAccounts);
            retirementAccountAssetsValue.setDate(new Date().getTime());

            assetsValueDao.updateAssetValue(retirementAccountAssetsValue);
        } else {
            retirementAccountAssetsValue = new AssetsValue();
            retirementAccountAssetsValue.setAssetsId((int)retirementAccountAssetsId);
            retirementAccountAssetsValue.setAssetsValue(totalRetirementAccounts);
            retirementAccountAssetsValue.setDate(new Date().getTime());

            assetsValueDao.insertAssetValue(retirementAccountAssetsValue);
        }

        AssetsValue ownershipInterestsAssetsValue = this.findAssetsValue(ownershipInterestsAssetsId);
        if (ownershipInterestsAssetsValue != null) {
            ownershipInterestsAssetsValue.setAssetsValue(totalOwnershipInterests);
            ownershipInterestsAssetsValue.setDate(new Date().getTime());

            assetsValueDao.updateAssetValue(ownershipInterestsAssetsValue);
        } else {
            ownershipInterestsAssetsValue = new AssetsValue();
            ownershipInterestsAssetsValue.setAssetsId((int) ownershipInterestsAssetsId);
            ownershipInterestsAssetsValue.setAssetsValue(totalOwnershipInterests);
            ownershipInterestsAssetsValue.setDate(new Date().getTime());

            assetsValueDao.insertAssetValue(ownershipInterestsAssetsValue);
        }
    }
}