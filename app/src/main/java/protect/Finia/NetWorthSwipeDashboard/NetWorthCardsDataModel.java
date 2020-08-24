package protect.Finia.NetWorthSwipeDashboard;

/**
 * The data model that contains the information for each item in the net worth dashboard cards.
 * This data model act as a source to provide data for each item.
 *
 * @author Owner  Kevin Zhijun Wang
 * created on 2020/04/10
 */
public class NetWorthCardsDataModel {

    private int imageId;
    private String title;
    private String value;

    public NetWorthCardsDataModel(int imageId, String title, String value){
        this.imageId = imageId;
        this.title = title;
        this.value = value;
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
}
