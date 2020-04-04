package protect.FinanceLord.AssetsFragmentUtils;

public class AssetsFragmentDataCarrier {
    String assetsTypeName;
    //float assetsValue;
    int assetsId;
    int level;

    AssetsFragmentDataCarrier(String assetsTypeName, int assetsId, int level) {
        this.assetsTypeName = assetsTypeName;
        //this.assetsValue = assetsValue;
        this.assetsId = assetsId;
        this.level = level;
    }
}
