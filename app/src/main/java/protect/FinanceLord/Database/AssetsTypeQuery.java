package protect.FinanceLord.Database;

public class AssetsTypeQuery {
    public int assetsFirstLevelId;
    public String assetsFirstLevelName;
    public int assetsSecondLevelId;
    public String assetsSecondLevelName;
    public int assetsThirdLevelId;
    public String assetsThirdLevelName;
    public int assetsFourthLevelId;
    public String assetsFourthLevelName;

    public String toString() {
        return  "FirstLevelId:" + assetsFirstLevelId + "\n" + "assetsFirstLevelName: " + assetsFirstLevelName + "\n"
                + "assetsSecondLevelId:" + assetsSecondLevelId + "\n" + "assetsSecondLevelName: " + assetsSecondLevelName + "\n"
                + "assetsThirdLevelId:" + assetsThirdLevelId + "\n"  + "assetsThirdLevelType: " + assetsThirdLevelName + "\n"
                + "assetsFourthLevelId:" + assetsFourthLevelId + "\n" + "assetsFourthLevelName: " + assetsFourthLevelName;
    }
}