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
    private String value;
    private String details;

    public NetWorthCardsDataModel(int imageId, String title, String value, String details){
        this.imageId = imageId;
        this.title = title;
        this.value = value;
        this.details = details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getImageId() {
        return imageId;
    }

    public String getTitle() {
        return title;
    }

    public String getValue() {
        return value;
    }

    public String getDetails() {
        return details;
    }
}
