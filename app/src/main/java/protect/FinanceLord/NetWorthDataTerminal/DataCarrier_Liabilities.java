package protect.FinanceLord.NetWorthDataTerminal;

public class DataCarrier_Liabilities {

    public String liabilitiesTypeName;
    public int liabilitiesId;
    public int level;

    public DataCarrier_Liabilities(String liabilitiesTypeName, int liabilitiesId, int level){
        this.liabilitiesTypeName = liabilitiesTypeName;
        this.liabilitiesId = liabilitiesId;
        this.level = level;
    }
}
