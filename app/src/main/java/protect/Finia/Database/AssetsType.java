package protect.Finia.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * The model for the database entity AssetsType
 * AssetsType is used to store all the types of assets and their parent types
 * to keep track of their locations in the data structure.
 *
 * @author Owner  Kevin Zhijun Wang
 * created on 2020/02/29
 */
@Entity(tableName = "AssetsType")
public class AssetsType {

    @PrimaryKey(autoGenerate = true)
    private int assetsId;

    @ColumnInfo(name = "assetsName")
    private String assetsName;

    @ColumnInfo(name = "assetsParentType")
    private String assetsParentType;

    @Ignore
    public AssetsType() { }

    public AssetsType(int assetsId, String assetsName, String assetsParentType) {
        this.assetsId = assetsId;
        this.assetsName = assetsName;
        this.assetsParentType = assetsParentType;
    }

    public int getAssetsId()    {return assetsId;}
    public String getAssetsName()   {return this.assetsName;}
    public String getAssetsParentType()     {return this.assetsParentType;}


    public void setAssetsId(int assetsId)   {this.assetsId = assetsId;}
    public void setAssetsName(String assetsName)    {this.assetsName = assetsName;}
    public void setAssetsParentType(String assetsParentType)   {this.assetsParentType = assetsParentType;}
}