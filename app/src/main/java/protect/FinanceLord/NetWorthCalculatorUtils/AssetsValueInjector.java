package protect.FinanceLord.NetWorthCalculatorUtils;

import android.content.Context;

import java.util.List;

import protect.FinanceLord.Database.AssetsValue;
import protect.FinanceLord.Database.AssetsValueDao;
import protect.FinanceLord.Database.FinanceLordDatabase;

public class AssetsValueInjector {

    Context context;
    Long date;
    FinanceLordDatabase database;
    AssetsValueDao assetsValueDao;

    public AssetsValueInjector(Context context, Long date){
        this.context = context;
        this.date = date;

        database = FinanceLordDatabase.getInstance(context);
        assetsValueDao = database.assetsValueDao();
    }

    public void insertParentAssetsValue(List<Float> parentAssets){

        AssetsValue totalAssets = new AssetsValue();
        totalAssets.setAssetsValue(parentAssets.get(0));
        totalAssets.setAssetsId(35);
        totalAssets.setDate(date);
        assetsValueDao.insertAssetValue(totalAssets);

        AssetsValue liquidAssets = new AssetsValue();
        liquidAssets.setAssetsValue(parentAssets.get(1));
        liquidAssets.setAssetsId(32);
        liquidAssets.setDate(date);
        assetsValueDao.insertAssetValue(liquidAssets);

        AssetsValue investedAssets = new AssetsValue();
        investedAssets.setAssetsValue(parentAssets.get(2));
        investedAssets.setAssetsId(33);
        investedAssets.setDate(date);
        assetsValueDao.insertAssetValue(investedAssets);

        AssetsValue personalAssets = new AssetsValue();
        personalAssets.setAssetsValue(parentAssets.get(3));
        personalAssets.setAssetsId(34);
        personalAssets.setDate(date);
        assetsValueDao.insertAssetValue(personalAssets);

        AssetsValue taxableAccounts = new AssetsValue();
        taxableAccounts.setAssetsValue(parentAssets.get(4));
        taxableAccounts.setAssetsId(29);
        taxableAccounts.setDate(date);
        assetsValueDao.insertAssetValue(taxableAccounts);

        AssetsValue retirementAccounts = new AssetsValue();
        retirementAccounts.setAssetsValue(parentAssets.get(5));
        retirementAccounts.setAssetsId(30);
        retirementAccounts.setDate(date);
        assetsValueDao.insertAssetValue(retirementAccounts);

        AssetsValue ownershipInterest = new AssetsValue();
        ownershipInterest.setAssetsValue(parentAssets.get(6));
        ownershipInterest.setAssetsId(31);
        ownershipInterest.setDate(date);
        assetsValueDao.insertAssetValue(ownershipInterest);
    }
}
