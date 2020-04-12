package protect.FinanceLord;

import android.util.Log;

import java.util.List;

import protect.FinanceLord.Database.AssetsTypeQuery;
import protect.FinanceLord.Database.AssetsValue;

public class NetWorthCalculator {
    List<AssetsValue> assetsValues;
    List<AssetsTypeQuery> assetsTypeQueries;
    AssetsValueExtractor assetsValueExtractor;

    public NetWorthCalculator(AssetsValueExtractor assetsValueExtractor){
        this.assetsValueExtractor = assetsValueExtractor;

        assetsTypeQueries = assetsValueExtractor.getAssetsTypeQueries();
        assetsValues = assetsValueExtractor.getAssetsValues();
    }

    public float calculateTotalAssets(){

        float totalInvestedAssets = calculateTotalInvestedAssets();
        float totalLiquidAssets = calculateTotalLiquidAssets();
        float totalPersonalAssets = calculateTotalPersonalAssets();
        float totalAssets = totalInvestedAssets + totalLiquidAssets + totalPersonalAssets;

        return totalAssets;
    }

    public float calculateTotalLiquidAssets(){

        float totalLiquidAssets = 0;
        for (AssetsTypeQuery assetsTypeQuery: assetsTypeQueries){
            if (assetsTypeQuery.assetsSecondLevelId == 32){
                for (AssetsValue assetsValue: assetsValues){
                    if (assetsValue.getAssetsId() == assetsTypeQuery.assetsThirdLevelId){
                        totalLiquidAssets += assetsValue.getAssetsValue();
                    } else {
                        continue;
                    }
                }
            }
        }
        Log.d("Print liquid assets",String.valueOf(totalLiquidAssets));

        return totalLiquidAssets;
    }

    public float calculateTotalPersonalAssets(){

        float totalPersonalAssets = 0;
        for (AssetsTypeQuery assetsTypeQuery: assetsTypeQueries){
            if (assetsTypeQuery.assetsSecondLevelId == 34){
                for (AssetsValue assetsValue: assetsValues){
                    if (assetsValue.getAssetsId() == assetsTypeQuery.assetsThirdLevelId){
                        totalPersonalAssets += assetsValue.getAssetsValue();
                    } else {
                        continue;
                    }
                }
            }
        }
        Log.d("Print personal assets",String.valueOf(totalPersonalAssets));

        return totalPersonalAssets;
    }

    public float calculateTotalInvestedAssets(){

        float totalOwnershipInterests = calculateTotalOwnershipInterests();
        float totalRetirementAccounts = calculateTotalRetirementAccounts();
        float totalTaxableAccounts = calculateTotalTaxableAccounts();
        float totalInvestedAssets = totalOwnershipInterests + totalRetirementAccounts + totalTaxableAccounts;

        return totalInvestedAssets;
    }

    public float calculateTotalOwnershipInterests(){

        float totalOwnershipInterest = 0;
        for (AssetsTypeQuery assetsTypeQuery: assetsTypeQueries){
            if (assetsTypeQuery.assetsThirdLevelId == 31){
                for (AssetsValue assetsValue: assetsValues){
                    if (assetsValue.getAssetsId() == assetsTypeQuery.assetsFourthLevelId){
                        totalOwnershipInterest += assetsValue.getAssetsValue();
                    }
                }
            } else {
                continue;
            }
        }

        return totalOwnershipInterest;
    }

    public float calculateTotalRetirementAccounts(){

        float totalRetirementAccounts = 0;
        for (AssetsTypeQuery assetsTypeQuery: assetsTypeQueries){
            if (assetsTypeQuery.assetsThirdLevelId == 30){
                for (AssetsValue assetsValue: assetsValues){
                    if(assetsValue.getAssetsId() == assetsTypeQuery.assetsFourthLevelId){
                        totalRetirementAccounts += assetsValue.getAssetsValue();
                    }
                }
            } else {
                continue;
            }
        }

        return totalRetirementAccounts;
    }

    public float calculateTotalTaxableAccounts(){

        float totalTotalTaxableAccounts = 0;
        for (AssetsTypeQuery assetsTypeQuery: assetsTypeQueries){
            if(assetsTypeQuery.assetsThirdLevelId == 29){
                for (AssetsValue assetsValue: assetsValues){
                    if (assetsValue.getAssetsId() == assetsTypeQuery.assetsThirdLevelId){
                        totalTotalTaxableAccounts += assetsValue.getAssetsValue();
                    } else {
                        continue;
                    }
                }
            }
        }

        return totalTotalTaxableAccounts;
    }
}
