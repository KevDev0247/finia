package protect.FinanceLord.NetWorthDataStructure;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import protect.FinanceLord.Database.LiabilitiesValue;
import protect.FinanceLord.DAOs.LiabilitiesValueDao;
import protect.FinanceLord.R;

/**
 * The data processor to retrieve or insert any data or data collection from the LiabilitiesValueTree Structure.
 * The idea behind this utility class is to provide methods to perform actions like read, edit, group, and delete on each node.
 * Methods for READ action : getLiabilityValue, getAllLiabilitiesValues, getLiabilitiesId.
 * Methods for EDIT action : setLiabilityValue, setAllLiabilitiesValues.
 * Methods for GROUP action : getLiabilitiesChildrenNodeIDs.
 * Methods for DELETE action : clearAllLiabilitiesValues.
 * Each node of the LiabilitiesValueTree is an LiabilitiesValue object containing information about the asset item including id, name, time.
 * The structure of the LiabilitiesValueTree is mapped from the LiabilitiesTypeTree that was queried from the database entity LiabilitiesType.
 * However, in this case, LiabilitiesValueTree is dependent on LiabilitiesTypeTree as Liabilities Type tree provides each node with
 * its relationship with the other nodes.
 * The LiabilitiesTypeTree is represented by a list of all the leaf nodes of the LiabilitiesTypes, which are the all the items of liabilities.
 * These leaf nodes contains the information of their parent node and their parent node. The list representation makes it more compatible
 * with the SQLite database operations.
 * The date object currentTime refers to the current time associated with the data source --- list of LiabilitiesValue objects.
 * The data source of this processor is the list of LiabilitiesValue objects.
 * When calculating the sum value of each parent node, the Depth-First Search Algorithm is used to traverse the tree.
 * The method to calculate the root value will call the methods to calculate the child nodes of the root and then their child nodes
 * until the leaf node is reached. The value of the leaf nodes will be returned for the summation of their parent node and other nodes
 * on the same level. This algorithm allows each calculation method to be efficient and independent and thus reusable.
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 * @see LiabilitiesValue
 */
public class ValueTreeProcessor_Liabilities {

    private Context context;
    private Date currentTime;
    private List<TypeTreeLeaf_Liabilities> typeTreeLeaves;
    private List<LiabilitiesValue> liabilitiesValues;

    public ValueTreeProcessor_Liabilities(List<TypeTreeLeaf_Liabilities> typeTreeLeaves, List<LiabilitiesValue> liabilitiesValues, Date currentTime, Context context) {
        this.context = context;
        this.currentTime = currentTime;
        this.typeTreeLeaves = typeTreeLeaves;
        this.liabilitiesValues = liabilitiesValues;
    }

    /**
     * Retrieve a LiabilitiesValue object of an individual node.
     *
     * @author Owner  Kevin Zhijun Wang
     * @param liabilitiesId the id of the LiabilitiesValue object
     * @return an LiabilitiesValue object if the id inputted matched the id of LiabilitiesValue object.
     *         null if no the id inputted doesn't match the id of any objects in the list.
     */
    public LiabilitiesValue getLiabilityValue(int liabilitiesId) {
        for (LiabilitiesValue liabilitiesValue: liabilitiesValues) {
            if (liabilitiesValue.getLiabilitiesId() == liabilitiesId) {
                return liabilitiesValue;
            }
        }
        return null;
    }

    /**
     * Assign values to some parameters of the LiabilitiesValue object and push the object into the list.
     *
     * @author Owner  Kevin Zhijun Wang
     * @param liabilityId the id of the LiabilitiesValue object
     * @param liabilityValue the value of an liability
     */
    public void setLiabilityValue(int liabilityId, float liabilityValue) {
        LiabilitiesValue liabilitiesValue = this.getLiabilityValue(liabilityId);
        if (liabilitiesValue != null){
            liabilitiesValue.setLiabilitiesValue(liabilityValue);
        } else {
            liabilitiesValue = new LiabilitiesValue();
            liabilitiesValue.setLiabilitiesId(liabilityId);
            liabilitiesValue.setLiabilitiesValue(liabilityValue);
            this.liabilitiesValues.add(liabilitiesValue);
        }
    }

    /**
     * Retrieve all LiabilitiesValue objects  from the data resource list -- liabilitiesValues.
     *
     * @author Owner  Kevin Zhijun Wang
     * @return List of LiabilitiesValue objects
     */
    public List<LiabilitiesValue> getAllLiabilitiesValues() {
        return liabilitiesValues;
    }

    /**
     * Assign the updated data resource list to the data resource list in the processor.
     * Data resource list refers to the list of LiabilitiesValue objects for DataProcessor to process.
     * Processing data in this class refers to calculation and retrieve certain groups of data.
     *
     * @author Owner  Kevin Zhijun Wang
     * @param liabilitiesValues the list of LiabilitiesValue objects an updated data source list
     */
    public void setAllLiabilitiesValues(List<LiabilitiesValue> liabilitiesValues) {
        this.liabilitiesValues = liabilitiesValues;
    }

    /**
     * Clear the data resource list liabilitiesValues.
     * Similar to clear the cache.
     *
     * @author Owner  Kevin Zhijun Wang
     */
    public void clearAllLiabilitiesValues() {
        this.liabilitiesValues.clear();
    }

    /**
     * Retrieve the id of the node in the Type Tree data structure.
     *
     * @author Owner  Kevin Zhijun Wang
     * @param liabilitiesName the name of the asset category or item
     * @return an integer value represents the id of the node in the data structure.
     */
    private int getLiabilitiesId(String liabilitiesName) {
        for (TypeTreeLeaf_Liabilities liabilitiesTypeTreeLeaf : typeTreeLeaves) {
            if (liabilitiesTypeTreeLeaf.liabilitiesFirstLevelName != null && liabilitiesTypeTreeLeaf.liabilitiesFirstLevelName.equals(liabilitiesName)){
                return liabilitiesTypeTreeLeaf.liabilitiesFirstLevelId;
            } else if (liabilitiesTypeTreeLeaf.liabilitiesSecondLevelName != null && liabilitiesTypeTreeLeaf.liabilitiesSecondLevelName.equals(liabilitiesName)){
                return liabilitiesTypeTreeLeaf.liabilitiesSecondLevelId;
            } else if (liabilitiesTypeTreeLeaf.liabilitiesThirdLevelName != null && liabilitiesTypeTreeLeaf.liabilitiesThirdLevelName.equals(liabilitiesName)){
                return liabilitiesTypeTreeLeaf.liabilitiesThirdLevelId;
            }
        }
        return 0;
    }

    /**
     * Retrieve the id of the parent node in the Tree data structure that stores the categories.
     * Then traverse the tree structure and add all the id's of the child nodes to a list and return the list.
     *
     * @author Owner  Kevin Zhijun Wang
     * @param liabilitiesName the name of the liability category or item
     * @return an integer value represents the id of the node in the data structure
     */
    private List getLiabilitiesChildNodeIDs(String liabilitiesName) {
        if (TextUtils.isEmpty(liabilitiesName)) {
            return new ArrayList<>();
        }

        List liabilitiesIDs = new ArrayList();
        if (context.getString(R.string.short_term_liabilities_name).equals(liabilitiesName)) {
            for (TypeTreeLeaf_Liabilities liabilitiesTypeTreeLeaf : typeTreeLeaves){
                if (liabilitiesTypeTreeLeaf.liabilitiesSecondLevelName != null
                        && liabilitiesTypeTreeLeaf.liabilitiesSecondLevelName.equals(liabilitiesName)
                        && liabilitiesTypeTreeLeaf.liabilitiesThirdLevelName != null) {
                    liabilitiesIDs.add(liabilitiesTypeTreeLeaf.liabilitiesThirdLevelId);
                }
            }
        } else if (context.getString(R.string.long_term_liabilities_name).equals(liabilitiesName)) {
            for (TypeTreeLeaf_Liabilities liabilitiesTypeTreeLeaf : typeTreeLeaves){
                if (liabilitiesTypeTreeLeaf.liabilitiesSecondLevelName != null
                        && liabilitiesTypeTreeLeaf.liabilitiesSecondLevelName.equals(liabilitiesName)
                        && liabilitiesTypeTreeLeaf.liabilitiesThirdLevelName != null){
                    liabilitiesIDs.add(liabilitiesTypeTreeLeaf.liabilitiesThirdLevelId);
                }
            }
        }

        return liabilitiesIDs;
    }

    /**
     * Calculate the total liabilities value.
     * The methods to calculate the value of its children nodes are called.
     * Then, the value of the children is summed and returned.
     *
     * @author Owner  Kevin Zhijun Wang
     * @return a float value represents the total liabilities value.
     */
    private float calculateTotalLiability() {
        float totalLongTermLiabilities = calculateTotalLongTermLiabilities();
        float totalShortTermLiabilities = calculateTotalShortTermLiabilities();
        float totalLiabilities = totalShortTermLiabilities + totalLongTermLiabilities;

        return totalLiabilities;
    }

    /**
     * Calculate the total long term liabilities value.
     * First, the children's id's are retrieved by calling getLiabilitiesChildrenNodeIDs method.
     * Then, during the traversing process, each item and its parameters are accessed with the id's.
     * The methods traverse the items in current level as well as under long term liabilities.
     * and calculate the total value of all children nodes.
     *
     * @author Owner  Kevin Zhijun Wang
     * @return a float value represents the total long term liabilities value.
     */
    private float calculateTotalLongTermLiabilities() {
        List<Integer> liabilitiesIDs = this.getLiabilitiesChildNodeIDs(context.getString(R.string.long_term_liabilities_name));
        float totalLongTermLiabilities = 0;
        for (int liabilitiesId : liabilitiesIDs) {
            LiabilitiesValue liabilitiesValue = this.getLiabilityValue(liabilitiesId);

            if (liabilitiesValue != null) {
                Log.d("Long Term id", String.valueOf(liabilitiesValue.getLiabilitiesId()));
                Log.d("Long Term value", String.valueOf(liabilitiesValue.getLiabilitiesValue()));

                totalLongTermLiabilities += liabilitiesValue.getLiabilitiesValue();
            } else {
                Log.d("Long Term Liabilities","null");
            }
        }

        return totalLongTermLiabilities;
    }

    /**
     * Calculate the total short term liabilities value.
     * First, the children's id's are retrieved by calling getLiabilitiesChildrenNodeIDs method.
     * Then, during the traversing process, each item and its parameters are accessed with the id's.
     * The methods traverse the items in current level as well as under short term liabilities.
     * and calculate the total value of all children nodes.
     *
     * @author Owner  Kevin Zhijun Wang
     * @return a float value represents the total short term liabilities value.
     */
    private float calculateTotalShortTermLiabilities() {
        List<Integer> liabilitiesIDs = this.getLiabilitiesChildNodeIDs(context.getString(R.string.short_term_liabilities_name));
        float totalShortTermLiabilities = 0;
        for (int liabilitiesId : liabilitiesIDs) {
            LiabilitiesValue liabilitiesValue = this.getLiabilityValue(liabilitiesId);

            if (liabilitiesValue != null) {
                Log.d("Long Term id", String.valueOf(liabilitiesValue.getLiabilitiesId()));
                Log.d("Long Term value", String.valueOf(liabilitiesValue.getLiabilitiesValue()));

                totalShortTermLiabilities += liabilitiesValue.getLiabilitiesValue();
            } else {
                Log.d("Long Term Liabilities","null");
            }
        }

        return totalShortTermLiabilities;
    }

    /**
     * Insert or update the parent node LiabilitiesValue.
     * First, the parent node values are calculated through calling the corresponding calculation methods.
     * Then, retrieve the object of each node and set the values to the parameters of LiabilitiesValue.
     * Finally, insert or update the object LiabilitiesValue.
     *
     * @author Owner  Kevin Zhijun Wang
     * @param liabilitiesValueDao the data access object to insert or update LiabilitiesValue object
     */
    public void insertOrUpdateParentLiabilities(LiabilitiesValueDao liabilitiesValueDao) {
        float totalLiabilities = this.calculateTotalLiability();
        float totalShortTermLiabilities = this.calculateTotalShortTermLiabilities();
        float totalLongTermLiabilities = this.calculateTotalLongTermLiabilities();

        int totalLiabilitiesId = this.getLiabilitiesId(context.getString(R.string.total_liabilities_name));
        int shortTermLiabilitiesId = this.getLiabilitiesId(context.getString(R.string.short_term_liabilities_name));
        int longTernLiabilitiesId = this.getLiabilitiesId(context.getString(R.string.long_term_liabilities_name));

        /* total liabilities value */
        LiabilitiesValue totalLiabilitiesValue = this.getLiabilityValue(totalLiabilitiesId);
        if (totalLiabilitiesValue != null) {
            totalLiabilitiesValue.setLiabilitiesValue(totalLiabilities);
            totalLiabilitiesValue.setDate(currentTime.getTime());

            Log.d("DataProcessorL", "Total Liabilities value is " + totalLiabilitiesValue.getLiabilitiesValue() +
                    " Insert time is " + new Date(totalLiabilitiesValue.getDate()));

            liabilitiesValueDao.updateLiabilityValue(totalLiabilitiesValue);
        } else {
            totalLiabilitiesValue = new LiabilitiesValue();
            totalLiabilitiesValue.setLiabilitiesId(totalLiabilitiesId);
            totalLiabilitiesValue.setLiabilitiesValue(totalLiabilities);
            totalLiabilitiesValue.setDate(currentTime.getTime());

            Log.d("DataProcessorL", "Total Liabilities value is " + totalLiabilitiesValue.getLiabilitiesValue() +
                    " Insert time is " + new Date(totalLiabilitiesValue.getDate()));

            liabilitiesValueDao.insertLiabilityValue(totalLiabilitiesValue);
        }

        /* total short term liabilities value */
        LiabilitiesValue shortTermLiabilitiesValue = this.getLiabilityValue(shortTermLiabilitiesId);
        if (shortTermLiabilitiesValue != null) {
            shortTermLiabilitiesValue.setLiabilitiesValue(totalShortTermLiabilities);
            shortTermLiabilitiesValue.setDate(currentTime.getTime());

            Log.d("DataProcessorL", "Short Term Liabilities value is " + shortTermLiabilitiesValue.getLiabilitiesValue() +
                    " Insert time is " + new Date(shortTermLiabilitiesValue.getDate()));

            liabilitiesValueDao.updateLiabilityValue(shortTermLiabilitiesValue);
        } else {
            shortTermLiabilitiesValue = new LiabilitiesValue();
            shortTermLiabilitiesValue.setLiabilitiesId(shortTermLiabilitiesId);
            shortTermLiabilitiesValue.setLiabilitiesValue(totalShortTermLiabilities);
            shortTermLiabilitiesValue.setDate(currentTime.getTime());

            Log.d("DataProcessorL", "Short Term Liabilities value is " + shortTermLiabilitiesValue.getLiabilitiesValue() +
                    " Insert time is " + new Date(shortTermLiabilitiesValue.getDate()));

            liabilitiesValueDao.insertLiabilityValue(shortTermLiabilitiesValue);
        }

        /* total long term liabilities value */
        LiabilitiesValue longTermLiabilitiesValue = this.getLiabilityValue(longTernLiabilitiesId);
        if (longTermLiabilitiesValue != null) {
            longTermLiabilitiesValue.setLiabilitiesValue(totalLongTermLiabilities);
            longTermLiabilitiesValue.setDate(currentTime.getTime());

            Log.d("DataProcessorL", "Long Term Liabilities value is " + longTermLiabilitiesValue.getLiabilitiesValue() +
                    " Insert time is " + new Date(longTermLiabilitiesValue.getDate()));

            liabilitiesValueDao.updateLiabilityValue(longTermLiabilitiesValue);
        } else {
            longTermLiabilitiesValue = new LiabilitiesValue();
            longTermLiabilitiesValue.setLiabilitiesId(longTernLiabilitiesId);
            longTermLiabilitiesValue.setLiabilitiesValue(totalLongTermLiabilities);
            longTermLiabilitiesValue.setDate(currentTime.getTime());

            Log.d("DataProcessorL", "Long Term Liabilities value is " + longTermLiabilitiesValue.getLiabilitiesValue() +
                    " Insert time is " + new Date(longTermLiabilitiesValue.getDate()));

            liabilitiesValueDao.insertLiabilityValue(longTermLiabilitiesValue);
        }
    }
}
