package protect.Finia.Database;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import protect.Finia.DAOs.AssetsTypeDao;
import protect.Finia.DAOs.AssetsValueDao;
import protect.Finia.DAOs.BudgetsTypeDao;
import protect.Finia.DAOs.BudgetsValueDao;
import protect.Finia.DAOs.LiabilitiesTypeDao;
import protect.Finia.DAOs.LiabilitiesValueDao;
import protect.Finia.DAOs.TransactionsDao;
import protect.Finia.R;

/**
 * The class to initialize all the database entities.
 * The initialization of database includes adding default categories into the database.
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
public class DatabaseInitialization{

    private Context context;

    public DatabaseInitialization(Context context) {
        this.context = context;
    }

    public void initAssetTypeDb() {
        FiniaDatabase database = FiniaDatabase.getInstance(context);
        AssetsTypeDao assetsTypeDao = database.assetsTypeDao();

        List<AssetsType> allAssetsTypes = assetsTypeDao.queryAllAssetsType();
        if (allAssetsTypes.size() > 0) {
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

        assetsTypeDao.insertAssetsTypes(assetsTypes);

        Log.d("DatabaseInitialization", "initialize assetsTypes type Db finished");
    }

    public void initAssetValueDb() {
        FiniaDatabase database = FiniaDatabase.getInstance(context);
        AssetsValueDao assetsValueDao = database.assetsValueDao();

        List<AssetsValue> allAssetsValues = assetsValueDao.queryAllAssetsValue();
        if (allAssetsValues.size() > 0) {
            return;
        }
    }

    public void initLiabilityTypeDb() {
        FiniaDatabase database = FiniaDatabase.getInstance(context);
        LiabilitiesTypeDao liabilitiesTypeDao = database.liabilitiesTypeDao();

        List<LiabilitiesType> allLiabilitiesTypes = liabilitiesTypeDao.queryAllLiabilities();
        if (allLiabilitiesTypes.size() > 0) {
            return;
        }

        List<LiabilitiesType> liabilitiesTypes = new ArrayList<>();

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

        liabilitiesTypeDao.insertLiabilitiesTypes(liabilitiesTypes);
    }

    public void initLiabilityValueDb() {
        FiniaDatabase database = FiniaDatabase.getInstance(context);
        LiabilitiesValueDao liabilitiesValueDao = database.liabilitiesValueDao();

        List<LiabilitiesValue> allLiabilitiesValues = liabilitiesValueDao.queryAllLiabilities();
        if (allLiabilitiesValues.size() > 0) {
            return;
        }
    }

    public void initBudgetTypeDb() {
        FiniaDatabase database = FiniaDatabase.getInstance(context);
        BudgetsTypeDao budgetsTypeDao = database.budgetsTypeDao();

        List<BudgetsType> allBudgetsTypes = budgetsTypeDao.queryAllBudgetsTypes();
        if (allBudgetsTypes.size() > 0){
            return;
        }

        List<BudgetsType> budgetsTypes = new ArrayList<>();

        BudgetsType type1 = new BudgetsType();
        BudgetsType type2 = new BudgetsType();
        BudgetsType type3 = new BudgetsType();
        BudgetsType type4 = new BudgetsType();
        BudgetsType type5 = new BudgetsType();
        BudgetsType type6 = new BudgetsType();
        BudgetsType type7 = new BudgetsType();
        BudgetsType type8 = new BudgetsType();
        BudgetsType type9 = new BudgetsType();
        BudgetsType type10 = new BudgetsType();
        BudgetsType type11 = new BudgetsType();
        BudgetsType type12 = new BudgetsType();
        BudgetsType type13 = new BudgetsType();
        BudgetsType type14 = new BudgetsType();
        BudgetsType type15 = new BudgetsType();
        BudgetsType type16 = new BudgetsType();

        type1.setBudgetsName(context.getString(R.string.housing_name));
        type2.setBudgetsName(context.getString(R.string.transportation_name));
        type3.setBudgetsName(context.getString(R.string.food_name));
        type4.setBudgetsName(context.getString(R.string.utilities_name));
        type5.setBudgetsName(context.getString(R.string.health_care_name));
        type6.setBudgetsName(context.getString(R.string.insurance_name));
        type7.setBudgetsName(context.getString(R.string.household_supplies_name));
        type8.setBudgetsName(context.getString(R.string.personal_spending_name));
        type9.setBudgetsName(context.getString(R.string.debt_name));
        type10.setBudgetsName(context.getString(R.string.education_name));
        type11.setBudgetsName(context.getString(R.string.savings_name));
        type12.setBudgetsName(context.getString(R.string.entertainment_name));
        type13.setBudgetsName(context.getString(R.string.salary_name));
        type14.setBudgetsName(context.getString(R.string.dividend_name));
        type15.setBudgetsName(context.getString(R.string.rent_name));
        type16.setBudgetsName(context.getString(R.string.interests_name));

        budgetsTypes.add(type1);
        budgetsTypes.add(type2);
        budgetsTypes.add(type3);
        budgetsTypes.add(type4);
        budgetsTypes.add(type5);
        budgetsTypes.add(type6);
        budgetsTypes.add(type7);
        budgetsTypes.add(type8);
        budgetsTypes.add(type9);
        budgetsTypes.add(type10);
        budgetsTypes.add(type11);
        budgetsTypes.add(type12);
        budgetsTypes.add(type13);
        budgetsTypes.add(type14);
        budgetsTypes.add(type15);
        budgetsTypes.add(type16);

        budgetsTypeDao.insertBudgetsTypes(budgetsTypes);
    }

    public void initBudgetValueDb() {
        FiniaDatabase database = FiniaDatabase.getInstance(context);
        BudgetsValueDao budgetsDao = database.budgetsValueDao();

        List<BudgetsValue> allBudgetsValues = budgetsDao.queryAllBudgets();
        if (allBudgetsValues.size() > 0) {
            return;
        }
    }

    public void initTransactionDb() {
        FiniaDatabase database = FiniaDatabase.getInstance(context);
        TransactionsDao transactionsDao = database.transactionsDao();

        List<Transactions> allTransactions = transactionsDao.queryAllTransaction();
        if (allTransactions.size() > 0) {
            return;
        }
    }
}