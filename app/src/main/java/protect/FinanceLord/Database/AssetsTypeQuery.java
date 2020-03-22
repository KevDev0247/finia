package protect.FinanceLord.Database;

public class AssetsTypeQuery {
    public int secondLevelId;
    public String assetsCategory;
    public String assetsSubType;
    public int thirdLevelId;
    public String assetsName;

    public String toString() {
        return "secondLevelId:" + secondLevelId + "\n" + "assetsCategory: " + assetsCategory + "\nassetsSubType: " + assetsSubType + "\n,thirdLevelId: " + thirdLevelId
                 + "\nassetsName: " + assetsName;
    }
}
