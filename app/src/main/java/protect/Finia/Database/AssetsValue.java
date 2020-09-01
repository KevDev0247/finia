package protect.Finia.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * The model for the database entity AssetsType
 * AssetsType is used to store the value of each asset and the date they are recorded
 *
 * @author Owner  Kevin Zhijun Wang
 * created on 2020/02/29
 */
@Entity(tableName = "AssetsValue")
public class AssetsValue {

    @PrimaryKey(autoGenerate = true)
    private int assetsPrimaryId;

    @ColumnInfo(name = "assetsId")
    private int assetsId;

    @ColumnInfo(name = "assetsValue")
    private float assetsValue;

    @ColumnInfo(name = "date")
    private Long date;

    @Ignore
    public AssetsValue() { }

    public AssetsValue(int assetsPrimaryId, int assetsId, float assetsValue, Long date) {
        this.assetsPrimaryId = assetsPrimaryId;
        this.assetsId = assetsId;
        this.assetsValue = assetsValue;
        this.date = date;
    }

    public int getAssetsPrimaryId()       {return this.assetsPrimaryId;}
    public int getAssetsId()        {return this.assetsId;}
    public float getAssetsValue()       {return  this.assetsValue;}
    public Long getDate()       {return date;}

    public void setAssetsPrimaryId (int assetsPrimaryId)       {this.assetsPrimaryId = assetsPrimaryId;}
    public void setAssetsId(int assetsId)      {this.assetsId = assetsId;}
    public void setAssetsValue(float assetsValue)      {this.assetsValue = assetsValue;}
    public void setDate (Long date)     {this.date = date;}
}
