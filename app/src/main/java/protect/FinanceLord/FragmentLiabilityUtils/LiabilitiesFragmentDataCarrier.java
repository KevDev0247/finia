package protect.FinanceLord.FragmentLiabilityUtils;

public class LiabilitiesFragmentDataCarrier {

    public String liabilitiesTypeName;
    public int liabilitiesId;
    public int level;

    public LiabilitiesFragmentDataCarrier(String liabilitiesTypeName, int liabilitiesId, int level){
        this.liabilitiesTypeName = liabilitiesTypeName;
        this.liabilitiesId = liabilitiesId;
        this.level = level;
    }
}
