package protect.FinanceLord.NetWorthSwipeDashboard;

public class NetWorthCardsDataModel {

    private int imageId;
    private String title;
    private String details;

    public NetWorthCardsDataModel(int imageId, String title, String details){
        this.imageId = imageId;
        this.title = title;
        this.details = details;
    }

    public int getImageId()     { return imageId; }

    public String getTitle()    { return title; }

    public String getDetails()      { return details; }
}
