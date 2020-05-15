package protect.FinanceLord.NetWorthPastReportsListUtils;

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
