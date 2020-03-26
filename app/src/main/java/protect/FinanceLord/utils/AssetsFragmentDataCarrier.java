package protect.FinanceLord.utils;

public class AssetsFragmentDataCarrier {
    String assetsTypeName;
    int assetsId;
    int level;

    AssetsFragmentDataCarrier(String assetsTypeName, int assetsId, int level) {
        this.assetsTypeName = assetsTypeName;
        this.assetsId = assetsId;
        this.level = level;
    }
}
