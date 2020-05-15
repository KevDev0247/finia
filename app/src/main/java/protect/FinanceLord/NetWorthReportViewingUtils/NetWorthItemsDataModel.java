package protect.FinanceLord.NetWorthReportViewingUtils;

public class NetWorthItemsDataModel {
    public String itemName;
    public String itemValue;
    public String difference;

    public NetWorthItemsDataModel(String itemName, String itemValue, String difference){
        this.itemName = itemName;
        this.itemValue = itemValue;
        this.difference = difference;
    }
}
