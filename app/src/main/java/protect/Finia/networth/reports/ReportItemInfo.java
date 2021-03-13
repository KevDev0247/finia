package protect.Finia.networth.reports;

/**
 * The class to store important information associated with both assets and liabilities.
 * Report item refers to the item that displays the data and take the user to the report sheet.
 *
 * @author Owner  Kevin Zhijun Wang
 * created on 2020/05/04
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
