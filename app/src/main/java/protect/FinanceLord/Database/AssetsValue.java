package protect.FinanceLord.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "AssetsValue")
public class AssetsValue {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "assetsId")
    private int assetsId;

    @ColumnInfo(name = "assetsValue")
    private float assetsValue;

    @ColumnInfo(name = "date")
    private Long date;

    @Ignore
    public AssetsValue(){

    }

    public AssetsValue(int assetsId, float assetsValue, Long date){
        this.assetsId = assetsId;
        this.assetsValue = assetsValue;
        this.date = date;
    }

    public int getAssetsId()        {return this.assetsId;}
    public float getAssetsValue()       {return  this.assetsValue;}
    public Long getDate()       {return date;}

    public void setAssetsId (int assetsId)      {this.assetsId = assetsId;}
    public void setAssetsValue (float assetsValue)      {this.assetsValue = assetsValue;}
    public void setDate (Long date)     {this.date = date;}
}
