package protect.FinanceLord.NetWorthPastReportsList;

/**
 * The data model to store data to be displayed on the report item.
 * Report item refers to the item in the list of reports
 * that displays the data and take the user to the report sheet.
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
public class ReportItemsDataModel {
    public String time;
    public float netWorthValue;
    public String difference;

    public ReportItemsDataModel(String time, float netWorthValue, String difference){
        this.time = time;
        this.netWorthValue = netWorthValue;
        this.difference = difference;
    }
}
