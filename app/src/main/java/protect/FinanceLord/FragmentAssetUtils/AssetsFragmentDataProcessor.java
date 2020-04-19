package protect.FinanceLord.FragmentAssetUtils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import protect.FinanceLord.Database.AssetsTypeQuery;
import protect.FinanceLord.Database.AssetsValue;
import protect.FinanceLord.Database.AssetsValueDao;
import protect.FinanceLord.NetWorthCalculatorUtils.AssetsValueExtractor;
import protect.FinanceLord.NetWorthCalculatorUtils.AssetsValueInjector;
import protect.FinanceLord.NetWorthCalculatorUtils.NetWorthCalculator;
import protect.FinanceLord.ui.NetWorthEditReports.AssetsFragment;
import protect.FinanceLord.ui.NetWorthEditReports.DateUtils;

public class AssetsFragmentDataProcessor {

    private List<AssetsTypeQuery> dataList;
    private List<AssetsValue> assetsValues;

    public AssetsFragmentDataProcessor(List<AssetsTypeQuery> dataList, List<AssetsValue> assetsValues) {
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


    // calculators

    private long getAssetsId(String assetsName) {
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

    private List<Long> getAssetsIDsBelongsTo(String assetsName) {
        if (TextUtils.isEmpty(assetsName)) {
            return new ArrayList<>();
        }
        List assetsIDs = new ArrayList();
        if ("Ownership interests".equals(assetsName)) {
            for (AssetsTypeQuery query : dataList) {
                if (query.assetsThirdLevelName != null && query.assetsThirdLevelName.equals(assetsName) && query.assetsFourthLevelName != null) {
                    assetsIDs.add(query.assetsFourthLevelId);
                }
            }
        } else if ("Retirement accounts".equals(assetsName)) {
            for (AssetsTypeQuery query : dataList) {
                if (query.assetsThirdLevelName != null && query.assetsThirdLevelName.equals(assetsName) && query.assetsFourthLevelName != null) {
                    assetsIDs.add(query.assetsFourthLevelId);
                }
            }
        } else if ("Taxable accounts".equals(assetsName)) {
            for (AssetsTypeQuery query : dataList) {
                if (query.assetsThirdLevelName != null && query.assetsThirdLevelName.equals(assetsName) && query.assetsFourthLevelName != null) {
                    assetsIDs.add(query.assetsFourthLevelId);
                }
            }
        } else if ("Liquid assets".equals(assetsName)) {
            for (AssetsTypeQuery query : dataList) {
                if (query.assetsSecondLevelName != null && query.assetsSecondLevelName.equals(assetsName) && query.assetsThirdLevelName != null) {
                    assetsIDs.add(query.assetsThirdLevelId);
                }
            }
        } else if ("Personal assets".equals(assetsName)) {
            for (AssetsTypeQuery query : dataList) {
                if (query.assetsSecondLevelName != null && query.assetsSecondLevelName.equals(assetsName) && query.assetsThirdLevelName != null) {
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
        List<Long> assetsIDs = this.getAssetsIDsBelongsTo("Liquid assets");
        float totalLiquidAssets = 0;
        for (Long assetsId : assetsIDs) {
            AssetsValue assetsValue = this.getAssetsValue(assetsId);
            if (assetsValue != null) {
                totalLiquidAssets += assetsValue.getAssetsValue();
            }
        }

        return totalLiquidAssets;
    }

    public float calculateTotalPersonalAssets(){

        List<Long> assetsIDs = this.getAssetsIDsBelongsTo("Personal assets");
        float totalPersonalAssets = 0;

        for (Long assetsId : assetsIDs) {
            AssetsValue assetsValue = this.getAssetsValue(assetsId);
            if (assetsValue != null) {
                totalPersonalAssets += assetsValue.getAssetsValue();
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

        List<Long> assetsIDs = this.getAssetsIDsBelongsTo("Ownership interests");
        float totalOwnershipInterest = 0;

        for (Long assetsId : assetsIDs) {
            AssetsValue assetsValue = this.getAssetsValue(assetsId);
            if (assetsValue != null) {
                totalOwnershipInterest += assetsValue.getAssetsValue();
            }
        }

        return totalOwnershipInterest;
    }

    public float calculateTotalRetirementAccounts() {
        List<Long> assetsIDs = this.getAssetsIDsBelongsTo("Retirement accounts");
        float totalRetirementAccounts = 0;

        for (Long assetId : assetsIDs) {
            AssetsValue assetsValue = this.getAssetsValue(assetId);
            if (assetsValue != null) {
                totalRetirementAccounts += assetsValue.getAssetsValue();
            }
        }

        return totalRetirementAccounts;
    }

    public float calculateTotalTaxableAccounts(){
        List<Long> assetsIDs = this.getAssetsIDsBelongsTo("Taxable accounts");
        float totalTotalTaxableAccounts = 0;
        for (Long assetsId : assetsIDs) {
            AssetsValue assetsValue = this.getAssetsValue(assetsId);
            if (assetsValue != null) {
                totalTotalTaxableAccounts += assetsValue.getAssetsValue();
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
            this.assetsValues.add(totalAssetsValue);

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
            ownershipInterestsAssetsValue.setAssetsValue(totalOwnershipInterests);
            ownershipInterestsAssetsValue.setDate(new Date().getTime());

            assetsValueDao.insertAssetValue(ownershipInterestsAssetsValue);
        }
    }
}