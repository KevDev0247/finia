package protect.FinanceLord.NetWorthReportTemplateUtils;

public class NetWorthItemsDataModel {
    public String itemName;
    public float itemValue;
    public float difference;

    public NetWorthItemsDataModel(String itemName, float itemValue, float difference){
        this.itemName = itemName;
        this.itemValue = itemValue;
        this.difference = difference;
    }
}
