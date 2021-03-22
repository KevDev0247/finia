package protect.Finia.datastructure;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import protect.Finia.models.AssetsValue;
import protect.Finia.dao.AssetsValueDao;
import protect.Finia.R;

/**
 * The data processor to retrieve or insert any data or data collection from the AssetsValueTree Structure.
 * The idea behind this utility class is to provide methods to perform actions like read, edit, group, and delete on each node.
 * Methods for READ action : getAssetValue, getAllAssetsValues, getAssetsId.
 * Methods for EDIT action : setAssetValue, setAllAssetsValues.
 * Methods for GROUP action : getAssetsChildrenNodeIDs.
 * Methods for DELETE action : clearAllAssetsValues.
 * Each node of the AssetsValueTree is an AssetsValue object containing information about the asset item including id, name, time.
 * The structure of the AssetsValueTree is mapped from the AssetsTypeTree that was queried from the database entity AssetsType.
 * However, in this case, AssetsValueTree is dependent on AssetsTypeTree as Assets Type tree provides each node with
 * its relationship with the other nodes.
 * The AssetsTypeTree is represented by a list of all the leaf nodes of the AssetsTypes, which are the all the items of assets.
 * These leaf nodes contains the information of their parent node and their parent node. The list representation makes it more compatible
 * with the SQLite database operations.
 * The date object currentTime refers to the current time associated with the data source --- list of AssetsValue objects.
 * The data source of this processor is the list of AssetsValue objects.
 * When calculating the sum value of each parent node, the Depth-First Search Algorithm is used to traverse the tree.
 * The method to calculate the root value will call the methods to calculate the child nodes of the root and then their child nodes
 * until the leaf node is reached. The value of the leaf nodes will be returned for the summation of their parent node and other nodes
 * on the same level. This algorithm allows each calculation method to be efficient and independent and thus reusable.
 *
 * @author Owner  Kevin Zhijun Wang
 * @see AssetsValue
 * created on 2020/03/25
 */
public class AssetsValueTreeProcessor {

    private Context context;
    private Date currentTime;
    private List<AssetsTypeTreeLeaf> assetsTypeTree;
    private List<AssetsValue> assetsValues;

    public AssetsValueTreeProcessor(List<AssetsTypeTreeLeaf> assetsTypeTree, List<AssetsValue> assetsValues, Date currentTime, Context context) {
        this.context = context;
        this.assetsTypeTree = assetsTypeTree;
        this.assetsValues = assetsValues;
        this.currentTime = currentTime;
    }

    /**
     * Retrieve an AssetsValue object of an individual node.
     *
     * @param assetsId the id of the AssetsValue object
     * @return an AssetsValue object if the id inputted matched the id of AssetsValue object.
     *         null if no the id inputted doesn't match the id of any objects in the list.
     */
    public AssetsValue getAssetValue(int assetsId) {
        for (AssetsValue assetsValue: assetsValues) {
            if (assetsValue.getAssetsId() == assetsId){
                return assetsValue;
            }
        }
        return null;
    }

    /**
     * Assign values to some parameters of the AssetsValue object and push the object into the list.
     *
     * @param assetId the id of the AssetsValue object
     * @param assetValue the value of an asset
     */
    public void setAssetValue(int assetId, float assetValue) {
        AssetsValue assetsValue = this.getAssetValue(assetId);
        if (assetsValue != null) {
            assetsValue.setAssetsValue(assetValue);
        } else {
            assetsValue = new AssetsValue();
            assetsValue.setAssetsId(assetId);
            assetsValue.setAssetsValue(assetValue);
            this.assetsValues.add(assetsValue);
        }
    }

    /**
     * Retrieve all AssetsValue objects from the data resource list -- assetsValues.
     *
     * @return List of AssetsValue objects
     */
    public List<AssetsValue> getAllAssetsValues() {
        return this.assetsValues;
    }

    /**
     * Assign the updated data resource list to the data resource list in the processor.
     * Data resource list refers to the list of AssetsValue objects for DataProcessor to process.
     * Processing data in this class refers to calculation and retrieve certain groups of data.
     *
     * @param assetsValues the list of AssetsValue objects representing an updated data source list
     */
    public void setAllAssetsValues(List<AssetsValue> assetsValues) {
        this.assetsValues = assetsValues;
    }

    /**
     * Clear the data resource list assetsValues.
     * Similar to clear the cache.
     *
     * @author Owner  Kevin Zhijun Wang
     */
    public void clearAllAssetsValues() {
        this.assetsValues.clear();
    }

    /**
     * Retrieve the id of the node in the Type Tree data structure.
     *
     * @param assetsName the name of the asset category or item
     * @return an integer value represents the id of the node in the data structure.
     */
    private int getAssetsId(String assetsName) {
        for(AssetsTypeTreeLeaf assetsTypeTreeLeaf : assetsTypeTree) {
            if (assetsTypeTreeLeaf.assetsFirstLevelName != null && assetsTypeTreeLeaf.assetsFirstLevelName.equals(assetsName)) {
                return assetsTypeTreeLeaf.assetsFirstLevelId;
            } else if (assetsTypeTreeLeaf.assetsSecondLevelName != null && assetsTypeTreeLeaf.assetsSecondLevelName.equals(assetsName)) {
                return assetsTypeTreeLeaf.assetsSecondLevelId;
            } else if (assetsTypeTreeLeaf.assetsThirdLevelName != null && assetsTypeTreeLeaf.assetsThirdLevelName.equals(assetsName)) {
                return assetsTypeTreeLeaf.assetsThirdLevelId;
            } else if (assetsTypeTreeLeaf.assetsFourthLevelName != null && assetsTypeTreeLeaf.assetsFourthLevelName.equals(assetsName)) {
                return assetsTypeTreeLeaf.assetsFourthLevelId;
            }
        }
        return 0;
    }

    /**
     * Retrieve the id of the parent node in the Tree data structure that stores the categories.
     * Then traverse the tree structure and add all the id's of the child nodes to a list and return the list.
     *
     * @param assetsName the name of the asset category or item
     * @return an integer value represents the id of the node in the data structure
     */
    private List<Integer> getAssetsChildrenNodeIDs(String assetsName) {
        if (TextUtils.isEmpty(assetsName)) {
            return new ArrayList<Integer>();
        }

        List<Integer> assetsIDs = new ArrayList<Integer>();
        if (context.getString(R.string.ownership_interest_name).equals(assetsName)) {
            for (AssetsTypeTreeLeaf assetsTypeTreeLeaf : assetsTypeTree) {
                if (assetsTypeTreeLeaf.assetsThirdLevelName != null
                        && assetsTypeTreeLeaf.assetsThirdLevelName.equals(assetsName)
                        && assetsTypeTreeLeaf.assetsFourthLevelName != null) {
                    assetsIDs.add(assetsTypeTreeLeaf.assetsFourthLevelId);
                }
            }
        } else if (context.getString(R.string.retirement_accounts_name).equals(assetsName)) {
            for (AssetsTypeTreeLeaf assetsTypeTreeLeaf : assetsTypeTree) {
                if (assetsTypeTreeLeaf.assetsThirdLevelName != null
                        && assetsTypeTreeLeaf.assetsThirdLevelName.equals(assetsName)
                        && assetsTypeTreeLeaf.assetsFourthLevelName != null) {
                    assetsIDs.add(assetsTypeTreeLeaf.assetsFourthLevelId);
                }
            }
        } else if (context.getString(R.string.taxable_accounts_name).equals(assetsName)) {
            for (AssetsTypeTreeLeaf assetsTypeTreeLeaf : assetsTypeTree) {
                if (assetsTypeTreeLeaf.assetsThirdLevelName != null
                        && assetsTypeTreeLeaf.assetsThirdLevelName.equals(assetsName)
                        && assetsTypeTreeLeaf.assetsFourthLevelName != null) {
                    assetsIDs.add(assetsTypeTreeLeaf.assetsFourthLevelId);
                }
            }
        } else if (context.getString(R.string.liquid_assets_name).equals(assetsName)) {
            for (AssetsTypeTreeLeaf assetsTypeTreeLeaf : assetsTypeTree) {
                if (assetsTypeTreeLeaf.assetsSecondLevelName != null
                        && assetsTypeTreeLeaf.assetsSecondLevelName.equals(assetsName)
                        && assetsTypeTreeLeaf.assetsThirdLevelName != null) {
                    assetsIDs.add(assetsTypeTreeLeaf.assetsThirdLevelId);
                }
            }
        } else if (context.getString(R.string.personal_assets_name).equals(assetsName)) {
            for (AssetsTypeTreeLeaf assetsTypeTreeLeaf : assetsTypeTree) {
                if (assetsTypeTreeLeaf.assetsSecondLevelName != null
                        && assetsTypeTreeLeaf.assetsSecondLevelName.equals(assetsName)
                        && assetsTypeTreeLeaf.assetsThirdLevelName != null) {
                    assetsIDs.add(assetsTypeTreeLeaf.assetsThirdLevelId);
                }
            }
        }
        return assetsIDs;
    }

    /**
     * Calculate the total assets value.
     * The methods to calculate the value of its children nodes are called.
     * Then, the value of the children is summed and returned.
     *
     * @return a float value represents the total assets value.
     */
    private float calculateTotalAssets() {
        float totalInvestedAssets = calculateTotalInvestedAssets();
        float totalLiquidAssets = calculateTotalLiquidAssets();
        float totalPersonalAssets = calculateTotalPersonalAssets();

        return totalInvestedAssets + totalLiquidAssets + totalPersonalAssets;
    }

    /**
     * Calculate the total liquid assets value.
     * First, the children's id's are retrieved by calling getAssetsChildrenNodeIDs method.
     * Then, during the traversing process, each item and its parameters are accessed with the id's.
     * The methods traverse the items in current level as well as under liquid assets
     * and calculate the total value of all children nodes.
     *
     * @return a float value represents the total liquid assets value.
     */
    private float calculateTotalLiquidAssets() {
        List<Integer> assetsIDs = this.getAssetsChildrenNodeIDs(context.getString(R.string.liquid_assets_name));
        float totalLiquidAssets = 0;
        for (int assetsId : assetsIDs) {
            AssetsValue assetsValue = this.getAssetValue(assetsId);

            if (assetsValue != null) {
                Log.d("Liquid assets id", String.valueOf(assetsValue.getAssetsId()));
                Log.d("Liquid assets value", String.valueOf(assetsValue.getAssetsValue()));

                totalLiquidAssets += assetsValue.getAssetsValue();
            } else {
                Log.d("Liquid assets", "null");
            }
        }

        return totalLiquidAssets;
    }

    /**
     * Calculate the total personal assets value.
     * First, the children's id's are retrieved by calling getAssetsChildrenNodeIDs method.
     * Then, during the traversing process, each item and its parameters are accessed with the id's.
     * The methods traverse the items in current level as well as under personal assets
     * and calculate the total value of all children nodes.
     *
     * @return a float value represents the total personal assets value.
     */
    private float calculateTotalPersonalAssets() {
        List<Integer> assetsIDs = this.getAssetsChildrenNodeIDs(context.getString(R.string.personal_assets_name));
        float totalPersonalAssets = 0;
        for (int assetsId : assetsIDs) {
            AssetsValue assetsValue = this.getAssetValue(assetsId);

            if (assetsValue != null) {
                Log.d("Personal assets id", String.valueOf(assetsValue.getAssetsId()));
                Log.d("Personal assets value", String.valueOf(assetsValue.getAssetsValue()));

                totalPersonalAssets += assetsValue.getAssetsValue();
            } else {
                Log.d("Personal assets", "null");
            }
        }

        return totalPersonalAssets;
    }

    /**
     * Calculate the total invested assets value.
     * The methods to calculate the value of its children nodes are called.
     * Then, the value of the children is summed and returned.
     *
     * @return a float value represents the total assets value.
     */
    private float calculateTotalInvestedAssets() {
        float totalOwnershipInterests = calculateTotalOwnershipInterests();
        float totalRetirementAccounts = calculateTotalRetirementAccounts();
        float totalTaxableAccounts = calculateTotalTaxableAccounts();

        return totalOwnershipInterests + totalRetirementAccounts + totalTaxableAccounts;
    }

    /**
     * Calculate the total ownership interests value.
     * First, the children's id's are retrieved by calling getAssetsChildrenNodeIDs method.
     * Then, during the traversing process, each item and its parameters are accessed with the id's.
     * The methods traverse the items in current level as well as under ownership interests
     * and calculate the total ownership interests of all children nodes.
     *
     * @return a float value represents the total ownership interests value.
     */
    private float calculateTotalOwnershipInterests() {
        List<Integer> assetsIDs = this.getAssetsChildrenNodeIDs(context.getString(R.string.ownership_interest_name));
        float totalOwnershipInterest = 0;
        for (int assetsId : assetsIDs) {
            AssetsValue assetsValue = this.getAssetValue(assetsId);

            if (assetsValue != null) {
                Log.d("Ownership id", String.valueOf(assetsValue.getAssetsId()));
                Log.d("Ownership value", String.valueOf(assetsValue.getAssetsValue()));

                totalOwnershipInterest += assetsValue.getAssetsValue();
            } else {
                Log.d("Ownership interests", "null");
            }
        }

        return totalOwnershipInterest;
    }

    /**
     * Calculate the total retirement accounts value.
     * First, the children's id's are retrieved by calling getAssetsChildrenNodeIDs method.
     * Then, during the traversing process, each item and its parameters are accessed with the id's.
     * The methods traverse the items in current level as well as under retirement accounts
     * and calculate the total retirement accounts of all children nodes.
     *
     * @return a float value represents the total retirement accounts value.
     */
    private float calculateTotalRetirementAccounts() {
        List<Integer> assetsIDs = this.getAssetsChildrenNodeIDs(context.getString(R.string.retirement_accounts_name));
        float totalRetirementAccounts = 0;
        for (int assetId : assetsIDs) {
            AssetsValue assetsValue = this.getAssetValue(assetId);

            if (assetsValue != null) {
                Log.d("Retirement id", String.valueOf(assetsValue.getAssetsId()));
                Log.d("Retirement value", String.valueOf(assetsValue.getAssetsValue()));

                totalRetirementAccounts += assetsValue.getAssetsValue();
            } else {
                Log.d("Retirement accounts", "null");
            }
        }

        return totalRetirementAccounts;
    }

    /**
     * Calculate the total taxable accounts value.
     * First, the children's id's are retrieved by calling getAssetsChildrenNodeIDs method.
     * Then, during the traversing process, each item and its parameters are accessed with the id's.
     * The methods traverse the items in current level as well as under taxable accounts
     * and calculate the total taxable accounts of all children nodes.
     *
     * @return a float value represents the total taxable accounts value.
     */
    private float calculateTotalTaxableAccounts(){
        List<Integer> assetsIDs = this.getAssetsChildrenNodeIDs(context.getString(R.string.taxable_accounts_name));
        float totalTotalTaxableAccounts = 0;
        for (int assetsId : assetsIDs) {
            AssetsValue assetsValue = this.getAssetValue(assetsId);

            if (assetsValue != null) {
                Log.d("Taxable id", String.valueOf(assetsValue.getAssetsId()));
                Log.d("Taxable value", String.valueOf(assetsValue.getAssetsValue()));

                totalTotalTaxableAccounts += assetsValue.getAssetsValue();
            } else {
                Log.d("Taxable accounts", "null");
            }
        }

        return totalTotalTaxableAccounts;
    }

    /**
     * Insert or update the parent node AssetsValue.
     * First, the parent node values are calculated through calling the corresponding calculation methods.
     * Then, retrieve the object of each node and set the values to the parameters of AssetsValue.
     * Finally, insert or update the object AssetsValue.
     *
     * @param assetsValueDao the data access object to insert or update AssetsValue object
     */
    public void insertOrUpdateParentAssets(AssetsValueDao assetsValueDao) {
        float totalAssets = this.calculateTotalAssets();
        float totalLiquidAssets = this.calculateTotalLiquidAssets();
        float totalInvestedAssets = this.calculateTotalInvestedAssets();
        float totalPersonalAssets = this.calculateTotalPersonalAssets();
        float totalTaxableAccounts = this.calculateTotalTaxableAccounts();
        float totalRetirementAccounts = this.calculateTotalRetirementAccounts();
        float totalOwnershipInterests = this.calculateTotalOwnershipInterests();

        int totalAssetsId = this.getAssetsId(context.getString(R.string.total_assets_name));
        int liquidAssetsId = this.getAssetsId(context.getString(R.string.liquid_assets_name));
        int investedAssetsId = this.getAssetsId(context.getString(R.string.invested_assets_name));
        int personalAssetsId = this.getAssetsId(context.getString(R.string.personal_assets_name));
        int taxableAccountAssetsId = this.getAssetsId(context.getString(R.string.taxable_accounts_name));
        int retirementAccountAssetsId = this.getAssetsId(context.getString(R.string.retirement_accounts_name));
        int ownershipInterestsAssetsId = this.getAssetsId(context.getString(R.string.ownership_interest_name));

        /* total Assets Value */
        AssetsValue totalAssetsValue = this.getAssetValue(totalAssetsId);
        if (totalAssetsValue != null) {
            totalAssetsValue.setAssetsValue(totalAssets);
            totalAssetsValue.setDate(currentTime.getTime());

            Log.d("DataProcessorA", "Total Assets value is " + totalAssetsValue.getAssetsValue() +
                    " Update time is " + new Date(totalAssetsValue.getDate()));

            assetsValueDao.updateAssetValue(totalAssetsValue);
        } else {
            totalAssetsValue = new AssetsValue();
            totalAssetsValue.setAssetsId(totalAssetsId);
            totalAssetsValue.setAssetsValue(totalAssets);
            totalAssetsValue.setDate(currentTime.getTime());

            Log.d("DataProcessorA", "Total Assets value is " + totalAssetsValue.getAssetsValue() +
                    " Insert time is " + new Date(totalAssetsValue.getDate()));

            assetsValueDao.insertAssetValue(totalAssetsValue);
        }

        /* liquid assets value */
        AssetsValue liquidAssetsValue = this.getAssetValue(liquidAssetsId);
        if (liquidAssetsValue != null) {
            liquidAssetsValue.setAssetsValue(totalLiquidAssets);
            liquidAssetsValue.setDate(currentTime.getTime());

            Log.d("DataProcessorA", "Liquid Assets value is " + liquidAssetsValue.getAssetsValue() +
                    " Update time is " + new Date(liquidAssetsValue.getDate()));

            assetsValueDao.updateAssetValue(liquidAssetsValue);
        } else {
            liquidAssetsValue = new AssetsValue();
            liquidAssetsValue.setAssetsId(liquidAssetsId);
            liquidAssetsValue.setAssetsValue(totalLiquidAssets);
            liquidAssetsValue.setDate(currentTime.getTime());

            Log.d("DataProcessorA", "Liquid Assets value is " + liquidAssetsValue.getAssetsValue() +
                    " Insert time is " + new Date(liquidAssetsValue.getDate()));

            assetsValueDao.insertAssetValue(liquidAssetsValue);
        }

        /* invested assets value */
        AssetsValue totalInvestedAssetsValue = this.getAssetValue(investedAssetsId);
        if (totalInvestedAssetsValue != null) {
            totalInvestedAssetsValue.setAssetsValue(totalInvestedAssets);
            totalInvestedAssetsValue.setDate(currentTime.getTime());

            Log.d("DataProcessorA", "Invested Assets value is " + totalInvestedAssetsValue.getAssetsValue() +
                    " Update time is " + new Date(totalInvestedAssetsValue.getDate()));

            assetsValueDao.updateAssetValue(totalInvestedAssetsValue);
        } else {
            totalInvestedAssetsValue = new AssetsValue();
            totalInvestedAssetsValue.setAssetsId(investedAssetsId);
            totalInvestedAssetsValue.setAssetsValue(totalInvestedAssets);
            totalInvestedAssetsValue.setDate(currentTime.getTime());

            Log.d("DataProcessorA", "Invested Assets value is " + totalInvestedAssetsValue.getAssetsValue() +
                    " Insert time is " + new Date(totalInvestedAssetsValue.getDate()));

            assetsValueDao.insertAssetValue(totalInvestedAssetsValue);
        }

        /* personal assets value*/
        AssetsValue personalAssetsValue = this.getAssetValue(personalAssetsId);
        if (personalAssetsValue != null) {
            personalAssetsValue.setAssetsValue(totalPersonalAssets);
            personalAssetsValue.setDate(currentTime.getTime());

            Log.d("DataProcessorA", "Personal Assets value is " + personalAssetsValue.getAssetsValue() +
                    " Update time is " + new Date(personalAssetsValue.getDate()));

            assetsValueDao.updateAssetValue(personalAssetsValue);
        } else {
            personalAssetsValue = new AssetsValue();
            personalAssetsValue.setAssetsId(personalAssetsId);
            personalAssetsValue.setAssetsValue(totalPersonalAssets);
            personalAssetsValue.setDate(currentTime.getTime());

            Log.d("DataProcessorA", "Personal Assets value is " + personalAssetsValue.getAssetsValue() +
                    " Insert time is " + new Date(personalAssetsValue.getDate()));

            assetsValueDao.insertAssetValue(personalAssetsValue);
        }

        /* taxable accounts assets value */
        AssetsValue taxableAccountsValue = this.getAssetValue(taxableAccountAssetsId);
        if (taxableAccountsValue != null) {
            taxableAccountsValue.setAssetsValue(totalTaxableAccounts);
            taxableAccountsValue.setDate(currentTime.getTime());

            Log.d("DataProcessorA", "Taxable Accounts value is " + taxableAccountsValue.getAssetsValue() +
                    " Update time is " + new Date(taxableAccountsValue.getDate()));

            assetsValueDao.updateAssetValue(taxableAccountsValue);
        } else {
            taxableAccountsValue = new AssetsValue();
            taxableAccountsValue.setAssetsId(taxableAccountAssetsId);;
            taxableAccountsValue.setAssetsValue(totalTaxableAccounts);
            taxableAccountsValue.setDate(currentTime.getTime());

            Log.d("DataProcessorA", "Taxable Accounts value is " + taxableAccountsValue.getAssetsValue() +
                    " Insert time is " + new Date(taxableAccountsValue.getDate()));

            assetsValueDao.insertAssetValue(taxableAccountsValue);
        }

        /* retirement accounts assets value */
        AssetsValue retirementAccountValue = this.getAssetValue(retirementAccountAssetsId);
        if (retirementAccountValue != null) {
            retirementAccountValue.setAssetsValue(totalRetirementAccounts);
            retirementAccountValue.setDate(currentTime.getTime());

            Log.d("DataProcessorA", "Retirement Accounts value is " + retirementAccountValue.getAssetsValue() +
                    " Update time is " + new Date(retirementAccountValue.getDate()));

            assetsValueDao.updateAssetValue(retirementAccountValue);
        } else {
            retirementAccountValue = new AssetsValue();
            retirementAccountValue.setAssetsId(retirementAccountAssetsId);
            retirementAccountValue.setAssetsValue(totalRetirementAccounts);
            retirementAccountValue.setDate(currentTime.getTime());

            Log.d("DataProcessorA", "Retirement Accounts value is " + retirementAccountValue.getAssetsValue() +
                    " Insert time is " + new Date(retirementAccountValue.getDate()));

            assetsValueDao.insertAssetValue(retirementAccountValue);
        }

        /* ownership interests assets value */
        AssetsValue ownershipInterestsValue = this.getAssetValue(ownershipInterestsAssetsId);
        if (ownershipInterestsValue != null) {
            ownershipInterestsValue.setAssetsValue(totalOwnershipInterests);
            ownershipInterestsValue.setDate(currentTime.getTime());

            Log.d("DataProcessorA", "Ownership Interests value is " + ownershipInterestsValue.getAssetsValue() +
                    " Update time is " + new Date(ownershipInterestsValue.getDate()));

            assetsValueDao.updateAssetValue(ownershipInterestsValue);
        } else {
            ownershipInterestsValue = new AssetsValue();
            ownershipInterestsValue.setAssetsId(ownershipInterestsAssetsId);
            ownershipInterestsValue.setAssetsValue(totalOwnershipInterests);
            ownershipInterestsValue.setDate(currentTime.getTime());

            Log.d("DataProcessorA", "Ownership Interests value is " + ownershipInterestsValue.getAssetsValue() +
                    " Insert time is " + new Date(ownershipInterestsValue.getDate()));

            assetsValueDao.insertAssetValue(ownershipInterestsValue);
        }
    }
}