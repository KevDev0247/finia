package protect.FinanceLord.NetWorthSwipeDashboard;

/**
 * The data model that contains the information for each item in the net worth dashboard cards.
 * This data model act as a source to provide data for each item.
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
public class NetWorthCardsDataModel {

    private int imageId;
    private String title;
    private String details;

    public NetWorthCardsDataModel(int imageId, String title, String details){
        this.imageId = imageId;
        this.title = title;
        this.details = details;
    }

    int getImageId()     { return imageId; }

    String getTitle()    { return title; }

    String getDetails()      { return details; }
}
