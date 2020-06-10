package protect.FinanceLord.Database;

public class LiabilitiesTypeTreeLeaf {

    public int liabilitiesFirstLevelId;
    public String liabilitiesFirstLevelName;
    public int liabilitiesSecondLevelId;
    public String liabilitiesSecondLevelName;
    public int liabilitiesThirdLevelId;
    public String liabilitiesThirdLevelName;

    public String toString(){
        return "liabilitiesFirstLevelId" + liabilitiesFirstLevelId + "\n" + "liabilitiesFirstLevelName" + liabilitiesFirstLevelName + "\n"
                + "liabilitiesSecondLevelId" + liabilitiesSecondLevelId + "\n" + "liabilitiesSecondLevelName" + liabilitiesSecondLevelName + "\n"
                + "liabilitiesThirdLevelId" + liabilitiesThirdLevelId + "\n" + "liabilitiesThirdLevelName" + liabilitiesThirdLevelName;
    }
}
