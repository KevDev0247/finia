package protect.Finia.NetWorthPastReportsList;

/**
 * The class to store important information associated with both assets and liabilities.
 * Report item refers to the item that displays the data and take the user to the report sheet.
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
public class ReportItemInfo {
    public int totalAssetsId;
    public float totalAssetsValue;
    public String totalAssetsDate;
    public int totalLiabilitiesId;
    public float totalLiabilitiesValue;
    public String totalLiabilitiesDate;
    public float netWorthValue;
}
