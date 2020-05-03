package protect.FinanceLord.Database;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import protect.FinanceLord.R;

public class DatabaseInitialization{

    Context context;
    public DatabaseInitialization(Context context) {
        this.context = context;
    }

    public void initAssetTypeDb(){
        FinanceLordDatabase database = FinanceLordDatabase.getInstance(context);
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

        type1.setAssetsName(context.getString(R.string.checking_accounts_name));
        type2.setAssetsName(context.getString(R.string.savings_accounts_name));
        type3.setAssetsName(context.getString(R.string.money_market_accounts_name));
        type4.setAssetsName(context.getString(R.string.savings_bonds_name));
        type5.setAssetsName(context.getString(R.string.cds_name));
        type6.setAssetsName(context.getString(R.string.life_insurance_name));

        type1.setAssetsParentType(context.getString(R.string.liquid_assets_name));
        type2.setAssetsParentType(context.getString(R.string.liquid_assets_name));
        type3.setAssetsParentType(context.getString(R.string.liquid_assets_name));
        type4.setAssetsParentType(context.getString(R.string.liquid_assets_name));
        type5.setAssetsParentType(context.getString(R.string.liquid_assets_name));
        type6.setAssetsParentType(context.getString(R.string.liquid_assets_name));

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


        type7.setAssetsName(context.getString(R.string.brokerage_name));
        type8.setAssetsName(context.getString(R.string.others_name));

        type7.setAssetsParentType(context.getString(R.string.taxable_accounts_name));
        type8.setAssetsParentType(context.getString(R.string.taxable_accounts_name));


        type9.setAssetsName(context.getString(R.string.ira_name));
        type10.setAssetsName(context.getString(R.string.roth_ira_name));
        type11.setAssetsName(context.getString(R.string.retirement_savings_plan_name));
        type12.setAssetsName(context.getString(R.string.SEP_IRA_name));
        type13.setAssetsName(context.getString(R.string.pension__name));
        type14.setAssetsName(context.getString(R.string.annuity_name));
        type15.setAssetsName(context.getString(R.string.keogh_name));

        type9.setAssetsParentType(context.getString(R.string.retirement_accounts_name));
        type10.setAssetsParentType(context.getString(R.string.retirement_accounts_name));
        type11.setAssetsParentType(context.getString(R.string.retirement_accounts_name));
        type12.setAssetsParentType(context.getString(R.string.retirement_accounts_name));
        type13.setAssetsParentType(context.getString(R.string.retirement_accounts_name));
        type14.setAssetsParentType(context.getString(R.string.retirement_accounts_name));
        type15.setAssetsParentType(context.getString(R.string.retirement_accounts_name));


        type16.setAssetsName(context.getString(R.string.real_estate_name));
        type17.setAssetsName(context.getString(R.string.sole_proprietorship_name));
        type18.setAssetsName(context.getString(R.string.partnership_name));
        type19.setAssetsName(context.getString(R.string.c_corporation_name));
        type20.setAssetsName(context.getString(R.string.s_corporation_name));
        type21.setAssetsName(context.getString(R.string.limited_liability_company));

        type16.setAssetsParentType(context.getString(R.string.ownership_interest_name));
        type17.setAssetsParentType(context.getString(R.string.ownership_interest_name));
        type18.setAssetsParentType(context.getString(R.string.ownership_interest_name));
        type19.setAssetsParentType(context.getString(R.string.ownership_interest_name));
        type20.setAssetsParentType(context.getString(R.string.ownership_interest_name));
        type21.setAssetsParentType(context.getString(R.string.ownership_interest_name));


        AssetsType type22 = new AssetsType();
        AssetsType type23 = new AssetsType();
        AssetsType type24 = new AssetsType();
        AssetsType type25 = new AssetsType();
        AssetsType type26 = new AssetsType();
        AssetsType type27 = new AssetsType();
        AssetsType type28 = new AssetsType();

        type22.setAssetsName(context.getString(R.string.principal_home_name));
        type23.setAssetsName(context.getString(R.string.vacation_home_name));
        type24.setAssetsName(context.getString(R.string.vehicles_name));
        type25.setAssetsName(context.getString(R.string.home_furnishings_name));
        type26.setAssetsName(context.getString(R.string.collections_name));
        type27.setAssetsName(context.getString(R.string.luxury_goods_name));
        type28.setAssetsName(context.getString(R.string.other_name));

        type22.setAssetsParentType(context.getString(R.string.personal_assets_name));
        type23.setAssetsParentType(context.getString(R.string.personal_assets_name));
        type24.setAssetsParentType(context.getString(R.string.personal_assets_name));
        type25.setAssetsParentType(context.getString(R.string.personal_assets_name));
        type26.setAssetsParentType(context.getString(R.string.personal_assets_name));
        type27.setAssetsParentType(context.getString(R.string.personal_assets_name));
        type28.setAssetsParentType(context.getString(R.string.personal_assets_name));

        //Level 2 objects
        AssetsType type29 = new AssetsType();
        AssetsType type30 = new AssetsType();
        AssetsType type31 = new AssetsType();

        type29.setAssetsName(context.getString(R.string.taxable_accounts_name));
        type30.setAssetsName(context.getString(R.string.retirement_accounts_name));
        type31.setAssetsName(context.getString(R.string.ownership_interest_name));

        type29.setAssetsParentType(context.getString(R.string.invested_assets_name));
        type30.setAssetsParentType(context.getString(R.string.invested_assets_name));
        type31.setAssetsParentType(context.getString(R.string.invested_assets_name));

        //Level 1 objects
        AssetsType type32 = new AssetsType();
        AssetsType type33 = new AssetsType();
        AssetsType type34 = new AssetsType();

        type32.setAssetsName(context.getString(R.string.liquid_assets_name));
        type33.setAssetsName(context.getString(R.string.invested_assets_name));
        type34.setAssetsName(context.getString(R.string.personal_assets_name));

        type32.setAssetsParentType(context.getString(R.string.total_assets_name));
        type33.setAssetsParentType(context.getString(R.string.total_assets_name));
        type34.setAssetsParentType(context.getString(R.string.total_assets_name));

        //Level 0 object
        AssetsType type35 = new AssetsType();

        type35.setAssetsName(context.getString(R.string.total_assets_name));

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
        FinanceLordDatabase database = FinanceLordDatabase.getInstance(context);
        AssetsValueDao assetsValueDao = database.assetsValueDao();

        List<AssetsValue> allAssetsValue = assetsValueDao.queryAllAssetsValue();
        if (allAssetsValue.size() > 0){
            return;
        }
    }

    public void initLiabilityTypeDb(){
        FinanceLordDatabase database = FinanceLordDatabase.getInstance(context);
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

        type_1.setLiabilitiesName(context.getString(R.string.credit_card_balances_name));
        type_2.setLiabilitiesName(context.getString(R.string.income_tax_name));
        type_3.setLiabilitiesName(context.getString(R.string.outstanding_bills_name));

        type_1.setLiabilitiesParentType(context.getString(R.string.short_term_liabilities_name));
        type_2.setLiabilitiesParentType(context.getString(R.string.short_term_liabilities_name));
        type_3.setLiabilitiesParentType(context.getString(R.string.short_term_liabilities_name));

        LiabilitiesType type_4 = new LiabilitiesType();
        LiabilitiesType type_5 = new LiabilitiesType();
        LiabilitiesType type_6 = new LiabilitiesType();
        LiabilitiesType type_7 = new LiabilitiesType();
        LiabilitiesType type_8 = new LiabilitiesType();
        LiabilitiesType type_9 = new LiabilitiesType();
        LiabilitiesType type_10 = new LiabilitiesType();
        LiabilitiesType type_11 = new LiabilitiesType();

        type_4.setLiabilitiesName(context.getString(R.string.home_mortgage_name));
        type_5.setLiabilitiesName(context.getString(R.string.home_equity_loan_name));
        type_6.setLiabilitiesName(context.getString(R.string.mortgage_name));
        type_7.setLiabilitiesName(context.getString(R.string.car_loans_name));
        type_8.setLiabilitiesName(context.getString(R.string.student_loans_name));
        type_9.setLiabilitiesName(context.getString(R.string.total_business_loans_name));
        type_10.setLiabilitiesName(context.getString(R.string.life_insurance_policy_loans_name));
        type_11.setLiabilitiesName(context.getString(R.string.other_long_time_debt_name));

        type_4.setLiabilitiesParentType(context.getString(R.string.long_term_liabilities_name));
        type_5.setLiabilitiesParentType(context.getString(R.string.long_term_liabilities_name));
        type_6.setLiabilitiesParentType(context.getString(R.string.long_term_liabilities_name));
        type_7.setLiabilitiesParentType(context.getString(R.string.long_term_liabilities_name));
        type_8.setLiabilitiesParentType(context.getString(R.string.long_term_liabilities_name));
        type_9.setLiabilitiesParentType(context.getString(R.string.long_term_liabilities_name));
        type_10.setLiabilitiesParentType(context.getString(R.string.long_term_liabilities_name));
        type_11.setLiabilitiesParentType(context.getString(R.string.long_term_liabilities_name));

        //Level 1 objects
        LiabilitiesType type_12 = new LiabilitiesType();
        LiabilitiesType type_13 = new LiabilitiesType();

        type_12.setLiabilitiesName(context.getString(R.string.short_term_liabilities_name));
        type_13.setLiabilitiesName(context.getString(R.string.long_term_liabilities_name));

        type_12.setLiabilitiesParentType(context.getString(R.string.total_liabilities_name));
        type_13.setLiabilitiesParentType(context.getString(R.string.total_liabilities_name));

        //Level 0 objects
        LiabilitiesType type_14 = new LiabilitiesType();

        type_14.setLiabilitiesName(context.getString(R.string.total_liabilities_name));

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
        FinanceLordDatabase database = FinanceLordDatabase.getInstance(context);
        LiabilitiesValueDao liabilitiesValueDao = database.liabilitiesValueDao();

        List<LiabilitiesValue> allLiabilitiesValue = liabilitiesValueDao.queryAllLiabilities();
        if (allLiabilitiesValue.size() > 0){
            return;
        }
    }

    public void initBudgetDb(){
        FinanceLordDatabase database = FinanceLordDatabase.getInstance(context);
        BudgetsDao budgetsDao = database.budgetsDao();

        List<Budgets> allBudgets = budgetsDao.queryAllBudgets();
        if (allBudgets.size() > 0){
            return;
        }
    }

    public void initTransactionDb(){
        FinanceLordDatabase database = FinanceLordDatabase.getInstance(context);
        TransactionsDao transactionsDao = database.transactionsDao();

        List<Transactions> allTransactions = transactionsDao.queryAllTransaction();
        if (allTransactions.size() > 0){
            return;
        }
    }
}