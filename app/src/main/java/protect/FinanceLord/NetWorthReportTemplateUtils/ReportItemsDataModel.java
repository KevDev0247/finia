package protect.FinanceLord.NetWorthReportTemplateUtils;

public class ReportItemsDataModel {
    public String itemName;
    public float itemValue;
    public float difference;

    public ReportItemsDataModel(String itemName, float itemValue, float difference){
        this.itemName = itemName;
        this.itemValue = itemValue;
        this.difference = difference;
    }
}
