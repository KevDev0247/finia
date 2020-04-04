package protect.FinanceLord.AssetsFragmentUtils;

public class AssetsFragmentDataCarrier {
    public String assetsTypeName;
    //float assetsValue;
    public int assetsId;
    public int level;

    public AssetsFragmentDataCarrier(String assetsTypeName, int assetsId, int level) {
        this.assetsTypeName = assetsTypeName;
        //this.assetsValue = assetsValue;
        this.assetsId = assetsId;
        this.level = level;
    }
}
