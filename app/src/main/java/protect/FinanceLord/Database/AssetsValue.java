package protect.FinanceLord.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "AssetsValue")
public class AssetsValue {
    @PrimaryKey(autoGenerate = true)
    private int assetsId;

    @ColumnInfo(name = "assetsValue")
    private int assetsValue;

    @Ignore
    public AssetsValue(){

    }

    public AssetsValue(int assetsId, int assetsValue){
        this.assetsId = assetsId;
        this.assetsValue = assetsValue;
    }

    public int getAssetsId()    {return assetsId;}
    public int getAssetsValue()     {return assetsValue;}
    public void setAssetsId(int assetsId)   {this.assetsId = assetsId;}
    public void setAssetsValue(int assetsValue)     {this.assetsValue = assetsValue;}
}
