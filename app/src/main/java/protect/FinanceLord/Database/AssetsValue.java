package protect.FinanceLord.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

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
    public AssetsValue(){

    }

    public AssetsValue(int assetsPrimaryId, int assetsId, float assetsValue, Long date){
        this.assetsPrimaryId = assetsPrimaryId;
        this.assetsId = assetsId;
        this.assetsValue = assetsValue;
        this.date = date;
    }

    public int getAssetPrimaryId()       {return this.assetsPrimaryId;}
    public int getAssetId()        {return this.assetsId;}
    public float getAssetValue()       {return  this.assetsValue;}
    public Long getDate()       {return date;}

    public void setAssetPrimaryId (int assetsPrimaryId)       {this.assetsPrimaryId = assetsPrimaryId;}
    public void setAssetId(int assetsId)      {this.assetsId = assetsId;}
    public void setAssetValue(float assetsValue)      {this.assetsValue = assetsValue;}
    public void setDate (Long date)     {this.date = date;}
}
