package protect.FinanceLord.NetWorthDataTerminal;

public class DataCarrier_Assets {

    public String assetsTypeName;
    public int assetsTypeId;
    public int level;

    public DataCarrier_Assets(String assetsTypeName, int assetsTypeId, int level) {
        this.assetsTypeName = assetsTypeName;
        this.assetsTypeId = assetsTypeId;
        this.level = level;
    }
}
