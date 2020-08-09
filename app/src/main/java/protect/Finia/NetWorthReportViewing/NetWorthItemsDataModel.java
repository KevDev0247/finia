package protect.Finia.NetWorthReportViewing;

/**
 * The data model that contains the information for each item in the net worth list.
 * This data model act as a source to provide data for list view.
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
class NetWorthItemsDataModel {
    String itemName;
    String itemValue;
    String difference;

    NetWorthItemsDataModel(String itemName, String itemValue, String difference) {
        this.itemName = itemName;
        this.itemValue = itemValue;
        this.difference = difference;
    }
}
