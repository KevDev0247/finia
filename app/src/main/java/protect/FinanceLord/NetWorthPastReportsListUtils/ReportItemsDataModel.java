package protect.FinanceLord.NetWorthPastReportsListUtils;

public class ReportItemsDataModel {
    public String time;
    public float netWorthValue;
    public float difference;

    public ReportItemsDataModel(String time, float netWorthValue, float difference){
        this.time = time;
        this.netWorthValue = netWorthValue;
        this.difference = difference;
    }
}
