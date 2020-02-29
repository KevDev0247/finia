package protect.FinanceLord;

import android.app.Activity;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import protect.FinanceLord.Database.AssetsType;
import protect.FinanceLord.Database.AssetsTypeDao;
import protect.FinanceLord.Database.AssetsValue;
import protect.FinanceLord.Database.AssetsValueDao;
import protect.FinanceLord.Database.FinanceLordDatabase;
import protect.FinanceLord.Database.LiabilitiesType;
import protect.FinanceLord.Database.LiabilitiesTypeDao;
import protect.FinanceLord.Database.LiabilitiesValue;
import protect.FinanceLord.Database.LiabilitiesValueDao;

public class DatabaseInitialization{
    WeakReference<Activity> context;
    public DatabaseInitialization(Activity context) {
        this.context = new WeakReference<>(context);
    }

    public void initAssetTypeDb(){
        FinanceLordDatabase database = Room.databaseBuilder(context.get(), FinanceLordDatabase.class,"AssetTypeDb").build();
        AssetsTypeDao assetsTypeDao = database.assetsTypeDao();

        List<AssetsType> allAssetsType = assetsTypeDao.queryAllAssets();
        if (allAssetsType.size() > 0){
            return;
        }

        List<AssetsType> assetsTypes = new ArrayList<>();

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


        AssetsType type22 = new AssetsType();
        AssetsType type23 = new AssetsType();
        AssetsType type24 = new AssetsType();
        AssetsType type25 = new AssetsType();
        AssetsType type26 = new AssetsType();
        AssetsType type27 = new AssetsType();
        AssetsType type28 = new AssetsType();

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
        AssetsType type29 = new AssetsType();
        AssetsType type30 = new AssetsType();
        AssetsType type31 = new AssetsType();

        type29.setAssetsName("Taxable accounts");
        type30.setAssetsName("Retirement accounts");
        type31.setAssetsName("Business ownership interests");

        type29.setAssetsParentType("Invested assets");
        type30.setAssetsParentType("Invested assets");
        type31.setAssetsParentType("Invested assets");

        //Level 1 objects
        AssetsType type32 = new AssetsType();
        AssetsType type33 = new AssetsType();
        AssetsType type34 = new AssetsType();

        type32.setAssetsName("Liquid asset");
        type33.setAssetsName("Invested asset");
        type34.setAssetsName("Personal asset");

        type32.setAssetsParentType("Total Asset");
        type33.setAssetsParentType("Total Asset");
        type34.setAssetsParentType("Total Asset");

        //Level 0 object
        AssetsType type35 = new AssetsType();

        type35.setAssetsName("Total Asset");

        assetsTypes.add(type1);
        assetsTypes.add(type2);
        assetsTypes.add(type3);
        assetsTypes.add(type4);
        assetsTypes.add(type5);
        assetsTypes.add(type6);
        assetsTypes.add(type7);
        assetsTypes.add(type8);
        assetsTypes.add(type9);
        assetsTypes.add(type10);
        assetsTypes.add(type11);
        assetsTypes.add(type12);
        assetsTypes.add(type13);
        assetsTypes.add(type14);
        assetsTypes.add(type15);
        assetsTypes.add(type16);
        assetsTypes.add(type17);
        assetsTypes.add(type18);
        assetsTypes.add(type19);
        assetsTypes.add(type20);
        assetsTypes.add(type21);
        assetsTypes.add(type22);
        assetsTypes.add(type23);
        assetsTypes.add(type24);
        assetsTypes.add(type25);
        assetsTypes.add(type26);
        assetsTypes.add(type27);
        assetsTypes.add(type28);
        assetsTypes.add(type29);
        assetsTypes.add(type30);
        assetsTypes.add(type31);
        assetsTypes.add(type32);
        assetsTypes.add(type33);
        assetsTypes.add(type34);
        assetsTypes.add(type35);
    }

    public void initAssetValueDb(){
        FinanceLordDatabase database = Room.databaseBuilder(context.get(), FinanceLordDatabase.class, "AssetValueDb").build();
        AssetsValueDao assetsValueDao = database.assetsValueDao();

        List<AssetsValue> allAssetsValue = assetsValueDao.queryAllAssets();
        if (allAssetsValue.size() > 0){
            return;
        }

        List<AssetsValue> assetsValues = new ArrayList<>();

        //level 3 objects
        AssetsValue Value1 = new AssetsValue();
        AssetsValue Value2 = new AssetsValue();
        AssetsValue Value3 = new AssetsValue();
        AssetsValue Value4 = new AssetsValue();
        AssetsValue Value5 = new AssetsValue();
        AssetsValue Value6 = new AssetsValue();

        AssetsValue Value7 = new AssetsValue();
        AssetsValue Value8 = new AssetsValue();

        AssetsValue Value9 = new AssetsValue();
        AssetsValue Value10 = new AssetsValue();
        AssetsValue Value11 = new AssetsValue();
        AssetsValue Value12 = new AssetsValue();
        AssetsValue Value13 = new AssetsValue();
        AssetsValue Value14 = new AssetsValue();
        AssetsValue Value15 = new AssetsValue();

        AssetsValue Value16 = new AssetsValue();
        AssetsValue Value17 = new AssetsValue();
        AssetsValue Value18 = new AssetsValue();
        AssetsValue Value19 = new AssetsValue();
        AssetsValue Value20 = new AssetsValue();
        AssetsValue Value21 = new AssetsValue();

        AssetsValue Value22 = new AssetsValue();
        AssetsValue Value23 = new AssetsValue();
        AssetsValue Value24 = new AssetsValue();
        AssetsValue Value25 = new AssetsValue();
        AssetsValue Value26 = new AssetsValue();
        AssetsValue Value27 = new AssetsValue();
        AssetsValue Value28 = new AssetsValue();

        //Level 2 objects
        AssetsValue Value29 = new AssetsValue();
        AssetsValue Value30 = new AssetsValue();
        AssetsValue Value31 = new AssetsValue();

        //Level 1 objects
        AssetsValue Value32 = new AssetsValue();
        AssetsValue Value33 = new AssetsValue();
        AssetsValue Value34 = new AssetsValue();

        //Level 0 objects
        AssetsValue Value35 = new AssetsValue();

        assetsValues.add(Value1);
        assetsValues.add(Value2);
        assetsValues.add(Value3);
        assetsValues.add(Value4);
        assetsValues.add(Value5);
        assetsValues.add(Value6);
        assetsValues.add(Value7);
        assetsValues.add(Value8);
        assetsValues.add(Value9);
        assetsValues.add(Value10);
        assetsValues.add(Value11);
        assetsValues.add(Value12);
        assetsValues.add(Value13);
        assetsValues.add(Value14);
        assetsValues.add(Value15);
        assetsValues.add(Value16);
        assetsValues.add(Value17);
        assetsValues.add(Value18);
        assetsValues.add(Value19);
        assetsValues.add(Value20);
        assetsValues.add(Value21);
        assetsValues.add(Value22);
        assetsValues.add(Value23);
        assetsValues.add(Value24);
        assetsValues.add(Value25);
        assetsValues.add(Value26);
        assetsValues.add(Value27);
        assetsValues.add(Value28);
        assetsValues.add(Value29);
        assetsValues.add(Value30);
        assetsValues.add(Value31);
        assetsValues.add(Value32);
        assetsValues.add(Value33);
        assetsValues.add(Value34);
        assetsValues.add(Value35);
    }

    public void initLiabilityTypeDb(){
        FinanceLordDatabase database = Room.databaseBuilder(context.get(), FinanceLordDatabase.class, "LiabilityTypeDb").build();
        LiabilitiesTypeDao liabilitiesTypeDao = database.liabilitiesTypeDao();

        List<LiabilitiesType> allLiabilitiesType = liabilitiesTypeDao.queryAllLiabilities();
        if (allLiabilitiesType.size() > 0){
            return;
        }

        List<LiabilitiesType> liabilitiesTypes = new ArrayList<>();

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

        type_12.setLiabilitiesParentType("Liabilities");
        type_13.setLiabilitiesParentType("Liabilities");

        //Level 0 objects
        LiabilitiesType type_14 = new LiabilitiesType();

        type_14.setLiabilitiesName("Liabilities");

        liabilitiesTypes.add(type_1);
        liabilitiesTypes.add(type_2);
        liabilitiesTypes.add(type_3);
        liabilitiesTypes.add(type_4);
        liabilitiesTypes.add(type_5);
        liabilitiesTypes.add(type_6);
        liabilitiesTypes.add(type_7);
        liabilitiesTypes.add(type_8);
        liabilitiesTypes.add(type_9);
        liabilitiesTypes.add(type_10);
        liabilitiesTypes.add(type_11);
        liabilitiesTypes.add(type_12);
        liabilitiesTypes.add(type_13);
        liabilitiesTypes.add(type_14);
    }

    public void initLiabilityValueDb(){
        FinanceLordDatabase database = Room.databaseBuilder(context.get(), FinanceLordDatabase.class, "liabilityValueDb").build();
        LiabilitiesValueDao liabilitiesValueDao = database.liabilitiesValueDao();

        List<LiabilitiesValue> allLiabilitiesValue = liabilitiesValueDao.queryAllLiabilities();
        if (allLiabilitiesValue.size() > 0){
            return;
        }

        List<LiabilitiesValue> liabilitiesValues = new ArrayList<>();

        //Level 2 objects
        LiabilitiesValue Value_1 = new LiabilitiesValue();
        LiabilitiesValue Value_2 = new LiabilitiesValue();
        LiabilitiesValue Value_3 = new LiabilitiesValue();

        LiabilitiesValue Value_4 = new LiabilitiesValue();
        LiabilitiesValue Value_5 = new LiabilitiesValue();
        LiabilitiesValue Value_6 = new LiabilitiesValue();
        LiabilitiesValue Value_7 = new LiabilitiesValue();
        LiabilitiesValue Value_8 = new LiabilitiesValue();
        LiabilitiesValue Value_9 = new LiabilitiesValue();
        LiabilitiesValue Value_10 = new LiabilitiesValue();
        LiabilitiesValue Value_11 = new LiabilitiesValue();

        //Level 1 objects
        LiabilitiesValue Value_12 = new LiabilitiesValue();
        LiabilitiesValue Value_13 = new LiabilitiesValue();

        //Level 0 objects
        LiabilitiesValue Value_14 = new LiabilitiesValue();

        liabilitiesValues.add(Value_1);
        liabilitiesValues.add(Value_2);
        liabilitiesValues.add(Value_3);
        liabilitiesValues.add(Value_4);
        liabilitiesValues.add(Value_5);
        liabilitiesValues.add(Value_6);
        liabilitiesValues.add(Value_7);
        liabilitiesValues.add(Value_8);
        liabilitiesValues.add(Value_9);
        liabilitiesValues.add(Value_10);
        liabilitiesValues.add(Value_11);
        liabilitiesValues.add(Value_12);
        liabilitiesValues.add(Value_13);
        liabilitiesValues.add(Value_14);
    }
}