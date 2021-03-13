package protect.Finia.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * The model for the database entity LiabilitiesType
 * LiabilitiesType is used to store all the types of liabilities and their parent types
 * to keep track of their locations in the data structure.
 *
 * @author Owner  Kevin Zhijun Wang
 * created on 2020/02/29
 */
@Entity(tableName = "LiabilitiesType")
public class LiabilitiesType {

    @PrimaryKey(autoGenerate = true)
    private int liabilitiesId;

    @ColumnInfo(name = "liabilitiesName")
    private String liabilitiesName;

    @ColumnInfo(name = "liabilitiesParentType")
    private String liabilitiesParentType;

    @Ignore
    public LiabilitiesType() { }

    public LiabilitiesType(int liabilitiesId, String liabilitiesName, String liabilitiesParentType) {
        this.liabilitiesId = liabilitiesId;
        this.liabilitiesName = liabilitiesName;
        this.liabilitiesParentType = liabilitiesParentType;
    }

    public int getLiabilitiesId()   {return liabilitiesId;}
    public String getLiabilitiesName()   {return liabilitiesName;}
    public String getLiabilitiesParentType()    {return liabilitiesParentType;}

    public void setLiabilitiesId(int liabilitiesId)   {this.liabilitiesId = liabilitiesId;}
    public void setLiabilitiesName(String liabilitiesName)   {this.liabilitiesName = liabilitiesName;}
    public void setLiabilitiesParentType(String liabilitiesParentType)    {this.liabilitiesParentType = liabilitiesParentType;}
}