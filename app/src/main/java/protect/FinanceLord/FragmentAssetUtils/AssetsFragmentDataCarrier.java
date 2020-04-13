package protect.FinanceLord.FragmentAssetUtils;

public class AssetsFragmentDataCarrier {

    public String assetsTypeName;
    public int assetsId;
    public int level;

    public AssetsFragmentDataCarrier(String assetsTypeName, int assetsId, int level) {
        this.assetsTypeName = assetsTypeName;
        this.assetsId = assetsId;
        this.level = level;
    }
}
