package protect.FinanceLord;

import android.app.Activity;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import protect.FinanceLord.Database.AssetsType;
import protect.FinanceLord.Database.AssetsTypeDao;
import protect.FinanceLord.Database.AssetsValue;
import protect.FinanceLord.Database.AssetsValueDao;
import protect.FinanceLord.Database.Budgets;
import protect.FinanceLord.Database.BudgetsDao;
import protect.FinanceLord.Database.FinanceLordDatabase;
import protect.FinanceLord.Database.LiabilitiesType;
import protect.FinanceLord.Database.LiabilitiesTypeDao;
import protect.FinanceLord.Database.LiabilitiesValue;
import protect.FinanceLord.Database.LiabilitiesValueDao;
import protect.FinanceLord.Database.Transactions;
import protect.FinanceLord.Database.TransactionsDao;

public class DatabaseInitialization{
    WeakReference<Activity> context;
    public DatabaseInitialization(Activity context) {
        this.context = new WeakReference<>(context);
    }

    public void initAssetTypeDb(){
        FinanceLordDatabase database = FinanceLordDatabase.getInstance(context.get());
        AssetsTypeDao assetsTypeDao = database.assetsTypeDao();

        List<AssetsType> allAssetsType = assetsTypeDao.queryAllAssetsType();

        Log.d("DatabaseInitialization", "initialize assets type Db, types: " + allAssetsType);

        if (allAssetsType.size() > 0){
            return;
        }

        List<AssetsType> assets = new ArrayList<>();

        //Level 3 objects
        AssetsType type1 = new AssetsType();
        AssetsType type2 = new AssetsType();
        AssetsType type3 = new AssetsType();
        AssetsType type4 = new AssetsType();
        AssetsType type5 = new AssetsType();
        AssetsType type6 = new AssetsType();

        type1.setAssetsName("Checking accounts");
        type2.setAssetsName("Savings accounts");
        type3.setAssetsName("Money market accounts");
        type4.setAssetsName("Savings bonds");
        type5.setAssetsName("CD's");
        type6.setAssetsName("Cash value of life insurance");

        type1.setAssetsParentType("Liquid assets");
        type2.setAssetsParentType("Liquid assets");
        type3.setAssetsParentType("Liquid assets");
        type4.setAssetsParentType("Liquid assets");
        type5.setAssetsParentType("Liquid assets");
        type6.setAssetsParentType("Liquid assets");

        AssetsType type7 = new AssetsType();
        AssetsType type8 = new AssetsType();

        AssetsType type9 = new AssetsType();
        AssetsType type10 = new AssetsType();
        AssetsType type11 = new AssetsType();
        AssetsType type12 = new AssetsType();
        AssetsType type13 = new AssetsType();
        AssetsType type14 = new AssetsType();
        AssetsType type15 = new AssetsType();

        AssetsType type16 = new AssetsType();
        AssetsType type17 = new AssetsType();
        AssetsType type18 = new AssetsType();
        AssetsType type19 = new AssetsType();
        AssetsType type20 = new AssetsType();
        AssetsType type21 = new AssetsType();


        type7.setAssetsName("Brokerage");
        type8.setAssetsName("Others");

        type7.setAssetsParentType("Taxable accounts");
        type8.setAssetsParentType("Taxable accounts");


        type9.setAssetsName("IRA");
        type10.setAssetsName("Roth_IRA");
        type11.setAssetsName("401k");
        type12.setAssetsName("SEP_IRA");
        type13.setAssetsName("Pension");
        type14.setAssetsName("Annuity");
        type15.setAssetsName("Keogh or other plan");

        type9.setAssetsParentType("Retirement accounts");
        type10.setAssetsParentType("Retirement accounts");
        type11.setAssetsParentType("Retirement accounts");
        type12.setAssetsParentType("Retirement accounts");
        type13.setAssetsParentType("Retirement accounts");
        type14.setAssetsParentType("Retirement accounts");
        type15.setAssetsParentType("Retirement accounts");


        type16.setAssetsName("Real estate");
        type17.setAssetsName("Sole proprietorship");
        type18.setAssetsName("Partnership");
        type19.setAssetsName("C corporation");
        type20.setAssetsName("S corporation");
        type21.setAssetsName("Limited liability company");

        type16.setAssetsParentType("Ownership interests");
        type17.setAssetsParentType("Ownership interests");
        type18.setAssetsParentType("Ownership interests");
        type19.setAssetsParentType("Ownership interests");
        type20.setAssetsParentType("Ownership interests");
        type21.setAssetsParentType("Ownership interests");


        AssetsType type22 = new AssetsType();
        AssetsType type23 = new AssetsType();
        AssetsType type24 = new AssetsType();
        AssetsType type25 = new AssetsType();
        AssetsType type26 = new AssetsType();
        AssetsType type27 = new AssetsType();
        AssetsType type28 = new AssetsType();

        type22.setAssetsName("Principal home");
        type23.setAssetsName("Vacation home");
        type24.setAssetsName("Vehicles");
        type25.setAssetsName("Home furnishings");
        type26.setAssetsName("Collections");
        type27.setAssetsName("Luxury goods");
        type28.setAssetsName("Other");

        type22.setAssetsParentType("Personal assets");
        type23.setAssetsParentType("Personal assets");
        type24.setAssetsParentType("Personal assets");
        type25.setAssetsParentType("Personal assets");
        type26.setAssetsParentType("Personal assets");
        type27.setAssetsParentType("Personal assets");
        type28.setAssetsParentType("Personal assets");

        //Level 2 objects
        AssetsType type29 = new AssetsType();
        AssetsType type30 = new AssetsType();
        AssetsType type31 = new AssetsType();

        type29.setAssetsName("Taxable accounts");
        type30.setAssetsName("Retirement accounts");
        type31.setAssetsName("Ownership interests");

        type29.setAssetsParentType("Invested assets");
        type30.setAssetsParentType("Invested assets");
        type31.setAssetsParentType("Invested assets");

        //Level 1 objects
        AssetsType type32 = new AssetsType();
        AssetsType type33 = new AssetsType();
        AssetsType type34 = new AssetsType();

        type32.setAssetsName("Liquid assets");
        type33.setAssetsName("Invested assets");
        type34.setAssetsName("Personal assets");

        type32.setAssetsParentType("Total Assets");
        type33.setAssetsParentType("Total Assets");
        type34.setAssetsParentType("Total Assets");

        //Level 0 object
        AssetsType type35 = new AssetsType();

        type35.setAssetsName("Total Assets");

        assets.add(type1);
        assets.add(type2);
        assets.add(type3);
        assets.add(type4);
        assets.add(type5);
        assets.add(type6);
        assets.add(type7);
        assets.add(type8);
        assets.add(type9);
        assets.add(type10);
        assets.add(type11);
        assets.add(type12);
        assets.add(type13);
        assets.add(type14);
        assets.add(type15);
        assets.add(type16);
        assets.add(type17);
        assets.add(type18);
        assets.add(type19);
        assets.add(type20);
        assets.add(type21);
        assets.add(type22);
        assets.add(type23);
        assets.add(type24);
        assets.add(type25);
        assets.add(type26);
        assets.add(type27);
        assets.add(type28);
        assets.add(type29);
        assets.add(type30);
        assets.add(type31);
        assets.add(type32);
        assets.add(type33);
        assets.add(type34);
        assets.add(type35);

        assetsTypeDao.insertAssetsTypes(assets);

        Log.d("DatabaseInitialization", "initialize assets type Db finished");
    }

    public void initAssetValueDb(){
        FinanceLordDatabase database = FinanceLordDatabase.getInstance(context.get());
        AssetsValueDao assetsValueDao = database.assetsValueDao();

        List<AssetsValue> allAssetsValue = assetsValueDao.queryAllAssetsValue();
        if (allAssetsValue.size() > 0){
            return;
        }
    }

    public void initLiabilityTypeDb(){
        FinanceLordDatabase database = FinanceLordDatabase.getInstance(context.get());
        LiabilitiesTypeDao liabilitiesTypeDao = database.liabilitiesTypeDao();

        List<LiabilitiesType> allLiabilities = liabilitiesTypeDao.queryAllLiabilities();
        if (allLiabilities.size() > 0){
            return;
        }

        List<LiabilitiesType> liabilities = new ArrayList<>();

        //Level 2 objects
        LiabilitiesType type_1 = new LiabilitiesType();
        LiabilitiesType type_2 = new LiabilitiesType();
        LiabilitiesType type_3 = new LiabilitiesType();

        type_1.setLiabilitiesName("Credit card balances");
        type_2.setLiabilitiesName("Estimated income tax owed");
        type_3.setLiabilitiesName("Other outstanding bills");

        type_1.setLiabilitiesParentType("Current liabilities");
        type_2.setLiabilitiesParentType("Current liabilities");
        type_3.setLiabilitiesParentType("Current liabilities");

        LiabilitiesType type_4 = new LiabilitiesType();
        LiabilitiesType type_5 = new LiabilitiesType();
        LiabilitiesType type_6 = new LiabilitiesType();
        LiabilitiesType type_7 = new LiabilitiesType();
        LiabilitiesType type_8 = new LiabilitiesType();
        LiabilitiesType type_9 = new LiabilitiesType();
        LiabilitiesType type_10 = new LiabilitiesType();
        LiabilitiesType type_11 = new LiabilitiesType();

        type_4.setLiabilitiesName("Home mortgage");
        type_5.setLiabilitiesName("Home equity loan");
        type_6.setLiabilitiesName("Mortgage on rental properties");
        type_7.setLiabilitiesName("Car loans");
        type_8.setLiabilitiesName("Student loans");
        type_9.setLiabilitiesName("Total Business Loans");
        type_10.setLiabilitiesName("Life insurance policy loans");
        type_11.setLiabilitiesName("Other long time debt");

        type_4.setLiabilitiesParentType("Long term liabilities");
        type_5.setLiabilitiesParentType("Long term liabilities");
        type_6.setLiabilitiesParentType("Long term liabilities");
        type_7.setLiabilitiesParentType("Long term liabilities");
        type_8.setLiabilitiesParentType("Long term liabilities");
        type_9.setLiabilitiesParentType("Long term liabilities");
        type_10.setLiabilitiesParentType("Long term liabilities");
        type_11.setLiabilitiesParentType("Long term liabilities");

        //Level 1 objects
        LiabilitiesType type_12 = new LiabilitiesType();
        LiabilitiesType type_13 = new LiabilitiesType();

        type_12.setLiabilitiesName("Current liabilities");
        type_13.setLiabilitiesName("Long term liabilities");

        type_12.setLiabilitiesParentType("Total liabilities");
        type_13.setLiabilitiesParentType("Total liabilities");

        //Level 0 objects
        LiabilitiesType type_14 = new LiabilitiesType();

        type_14.setLiabilitiesName("Total liabilities");

        liabilities.add(type_1);
        liabilities.add(type_2);
        liabilities.add(type_3);
        liabilities.add(type_4);
        liabilities.add(type_5);
        liabilities.add(type_6);
        liabilities.add(type_7);
        liabilities.add(type_8);
        liabilities.add(type_9);
        liabilities.add(type_10);
        liabilities.add(type_11);
        liabilities.add(type_12);
        liabilities.add(type_13);
        liabilities.add(type_14);

        liabilitiesTypeDao.insertLiabilitiesTypes(liabilities);
    }

    public void initLiabilityValueDb(){
        FinanceLordDatabase database = FinanceLordDatabase.getInstance(context.get());
        LiabilitiesValueDao liabilitiesValueDao = database.liabilitiesValueDao();

        List<LiabilitiesValue> allLiabilitiesValue = liabilitiesValueDao.queryAllLiabilities();
        if (allLiabilitiesValue.size() > 0){
            return;
        }
    }

    public void initBudgetDb(){
        FinanceLordDatabase database = FinanceLordDatabase.getInstance(context.get());
        BudgetsDao budgetsDao = database.budgetsDao();

        List<Budgets> allBudgets = budgetsDao.queryAllBudgets();
        if (allBudgets.size() > 0){
            return;
        }
    }

    public void initTransactionDb(){
        FinanceLordDatabase database = FinanceLordDatabase.getInstance(context.get());
        TransactionsDao transactionsDao = database.transactionsDao();

        List<Transactions> allTransactions = transactionsDao.queryAllTransaction();
        if (allTransactions.size() > 0){
            return;
        }
    }
}