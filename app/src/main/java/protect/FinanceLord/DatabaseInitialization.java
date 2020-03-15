package protect.FinanceLord;

import android.app.Activity;

import androidx.room.Room;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import protect.FinanceLord.Database.Assets;
import protect.FinanceLord.Database.AssetsDao;
import protect.FinanceLord.Database.Budgets;
import protect.FinanceLord.Database.BudgetsDao;
import protect.FinanceLord.Database.FinanceLordDatabase;
import protect.FinanceLord.Database.Liabilities;
import protect.FinanceLord.Database.LiabilitiesDao;

public class DatabaseInitialization{
    WeakReference<Activity> context;
    public DatabaseInitialization(Activity context) {
        this.context = new WeakReference<>(context);
    }

    public void initAssetDb(){
        FinanceLordDatabase database = Room.databaseBuilder(context.get(), FinanceLordDatabase.class,"AssetDb").build();
        AssetsDao assetsDao = database.assetsDao();

        List<Assets> allAssets = assetsDao.queryAllAssets();
        if (allAssets.size() > 0){
            return;
        }

        List<Assets> assets = new ArrayList<>();

        //Level 3 objects
        Assets type1 = new Assets();
        Assets type2 = new Assets();
        Assets type3 = new Assets();
        Assets type4 = new Assets();
        Assets type5 = new Assets();
        Assets type6 = new Assets();

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

        Assets type7 = new Assets();
        Assets type8 = new Assets();

        Assets type9 = new Assets();
        Assets type10 = new Assets();
        Assets type11 = new Assets();
        Assets type12 = new Assets();
        Assets type13 = new Assets();
        Assets type14 = new Assets();
        Assets type15 = new Assets();

        Assets type16 = new Assets();
        Assets type17 = new Assets();
        Assets type18 = new Assets();
        Assets type19 = new Assets();
        Assets type20 = new Assets();
        Assets type21 = new Assets();


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

        type9.setAssetsParentType("Retirement account");
        type10.setAssetsParentType("Retirement account");
        type11.setAssetsParentType("Retirement account");
        type12.setAssetsParentType("Retirement account");
        type13.setAssetsParentType("Retirement account");
        type14.setAssetsParentType("Retirement account");
        type15.setAssetsParentType("Retirement account");


        type16.setAssetsName("Real estate");
        type17.setAssetsName("Sole proprietorship");
        type18.setAssetsName("Partnership");
        type19.setAssetsName("C corporation");
        type20.setAssetsName("S corporation");
        type21.setAssetsName("Limited liability company");

        type16.setAssetsParentType("Business ownership interest");
        type17.setAssetsParentType("Business ownership interest");
        type18.setAssetsParentType("Business ownership interest");
        type19.setAssetsParentType("Business ownership interest");
        type20.setAssetsParentType("Business ownership interest");
        type21.setAssetsParentType("Business ownership interest");


        Assets type22 = new Assets();
        Assets type23 = new Assets();
        Assets type24 = new Assets();
        Assets type25 = new Assets();
        Assets type26 = new Assets();
        Assets type27 = new Assets();
        Assets type28 = new Assets();

        type22.setAssetsName("Principal home");
        type23.setAssetsName("Vacation home");
        type24.setAssetsName("Transportation");
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
        Assets type29 = new Assets();
        Assets type30 = new Assets();
        Assets type31 = new Assets();

        type29.setAssetsName("Taxable accounts");
        type30.setAssetsName("Retirement accounts");
        type31.setAssetsName("Business ownership interests");

        type29.setAssetsParentType("Invested assets");
        type30.setAssetsParentType("Invested assets");
        type31.setAssetsParentType("Invested assets");

        //Level 1 objects
        Assets type32 = new Assets();
        Assets type33 = new Assets();
        Assets type34 = new Assets();

        type32.setAssetsName("Liquid asset");
        type33.setAssetsName("Invested asset");
        type34.setAssetsName("Personal asset");

        type32.setAssetsParentType("Total Asset");
        type33.setAssetsParentType("Total Asset");
        type34.setAssetsParentType("Total Asset");

        //Level 0 object
        Assets type35 = new Assets();

        type35.setAssetsName("Total Asset");

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
    }

    public void initLiabilityDb(){
        FinanceLordDatabase database = Room.databaseBuilder(context.get(), FinanceLordDatabase.class, "LiabilityDb").build();
        LiabilitiesDao liabilitiesDao = database.liabilitiesDao();

        List<Liabilities> allLiabilities = liabilitiesDao.queryAllLiabilities();
        if (allLiabilities.size() > 0){
            return;
        }

        List<Liabilities> liabilities = new ArrayList<>();

        //Level 2 objects
        Liabilities type_1 = new Liabilities();
        Liabilities type_2 = new Liabilities();
        Liabilities type_3 = new Liabilities();

        type_1.setLiabilitiesName("Credit card balances");
        type_2.setLiabilitiesName("Estimated income tax owed");
        type_3.setLiabilitiesName("Other outstanding bills");

        type_1.setLiabilitiesParentType("Current liabilities");
        type_2.setLiabilitiesParentType("Current liabilities");
        type_3.setLiabilitiesParentType("Current liabilities");

        Liabilities type_4 = new Liabilities();
        Liabilities type_5 = new Liabilities();
        Liabilities type_6 = new Liabilities();
        Liabilities type_7 = new Liabilities();
        Liabilities type_8 = new Liabilities();
        Liabilities type_9 = new Liabilities();
        Liabilities type_10 = new Liabilities();
        Liabilities type_11 = new Liabilities();

        type_4.setLiabilitiesName("Home mortgage");
        type_5.setLiabilitiesName("Home equity loan");
        type_6.setLiabilitiesName("Mortgage on rental properties");
        type_7.setLiabilitiesName("Car loans");
        type_8.setLiabilitiesName("Student loans");
        type_9.setLiabilitiesName("Total Business Loans");
        type_10.setLiabilitiesName("Life insurance policy loans");
        type_11.setLiabilitiesName("Other long time debt");

        type_5.setLiabilitiesParentType("Long term liabilities");
        type_6.setLiabilitiesParentType("Long term liabilities");
        type_7.setLiabilitiesParentType("Long term liabilities");
        type_8.setLiabilitiesParentType("Long term liabilities");
        type_9.setLiabilitiesParentType("Long term liabilities");
        type_10.setLiabilitiesParentType("Long term liabilities");
        type_11.setLiabilitiesParentType("Long term liabilities");

        //Level 1 objects
        Liabilities type_12 = new Liabilities();
        Liabilities type_13 = new Liabilities();

        type_12.setLiabilitiesName("Current liabilities");
        type_13.setLiabilitiesName("Long term liabilities");

        type_12.setLiabilitiesParentType("Liabilities");
        type_13.setLiabilitiesParentType("Liabilities");

        //Level 0 objects
        Liabilities type_14 = new Liabilities();

        type_14.setLiabilitiesName("Liabilities");

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
    }

    public void initBudgetDb(){
        FinanceLordDatabase database = Room.databaseBuilder(context.get(), FinanceLordDatabase.class, "BudgetDb").build();
        BudgetsDao budgetsDao = database.budgetsDao();

        List<Budgets> allBudgets = budgetsDao.queryAllBudgets();
        if (allBudgets.size() > 0){
            return;
        }
    }
}