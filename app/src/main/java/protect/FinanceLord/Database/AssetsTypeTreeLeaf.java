package protect.FinanceLord.Database;
// tree structure
public class AssetsTypeTreeLeaf {

    public int assetsFirstLevelId;
    public String assetsFirstLevelName;
    public int assetsSecondLevelId;
    public String assetsSecondLevelName;
    public int assetsThirdLevelId;
    public String assetsThirdLevelName;
    public int assetsFourthLevelId;
    public String assetsFourthLevelName;

    public String toString() {
        return  "assetsFirstLevelId:" + assetsFirstLevelId + "\n" + "assetsFirstLevelName: " + assetsFirstLevelName + "\n"
                + "assetsSecondLevelId:" + assetsSecondLevelId + "\n" + "assetsSecondLevelName: " + assetsSecondLevelName + "\n"
                + "assetsThirdLevelId:" + assetsThirdLevelId + "\n"  + "assetsThirdLevelType: " + assetsThirdLevelName + "\n"
                + "assetsFourthLevelId:" + assetsFourthLevelId + "\n" + "assetsFourthLevelName: " + assetsFourthLevelName;
    }
}
