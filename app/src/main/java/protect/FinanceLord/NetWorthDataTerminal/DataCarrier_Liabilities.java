package protect.FinanceLord.NetWorthDataTerminal;

/**
 * The data carrier to carry liabilities data in LiabilitiesAdapter and LiabilitiesTypeProcessor
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
public class DataCarrier_Liabilities {

    public String liabilitiesTypeName;
    public int liabilitiesId;
    public int level;

    DataCarrier_Liabilities(String liabilitiesTypeName, int liabilitiesId, int level){
        this.liabilitiesTypeName = liabilitiesTypeName;
        this.liabilitiesId = liabilitiesId;
        this.level = level;
    }
}
