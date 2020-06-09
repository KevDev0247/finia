package protect.FinanceLord.NetWorthDataTerminal;

/**
 * The data carrier to carry assets data in AssetsAdapter and AssetsTypeProcessor
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
public class DataCarrier_Assets {

    public String assetsTypeName;
    public int assetsTypeId;
    public int level;

    DataCarrier_Assets(String assetsTypeName, int assetsTypeId, int level) {
        this.assetsTypeName = assetsTypeName;
        this.assetsTypeId = assetsTypeId;
        this.level = level;
    }
}
