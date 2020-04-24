package protect.FinanceLord.NetWorthDataTerminal;

public class DataCarrier_Assets {

    public String assetsTypeName;
    public int assetsId;
    public int level;

    public DataCarrier_Assets(String assetsTypeName, int assetsId, int level) {
        this.assetsTypeName = assetsTypeName;
        this.assetsId = assetsId;
        this.level = level;
    }
}
