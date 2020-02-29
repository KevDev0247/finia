package protect.FinanceLord.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "LiabilitiesValue")
public class LiabilitiesValue {
    @PrimaryKey(autoGenerate = true)
    private int liabilitiesId;

    @ColumnInfo(name = "liabilitiesValue")
    private int liabilitiesValue;

    @Ignore
    public LiabilitiesValue(){

    }

    public LiabilitiesValue (int liabilitiesId, int liabilitiesValue){
        this.liabilitiesId = liabilitiesId;
        this.liabilitiesValue = liabilitiesValue;
    }

    public int getLiabilitiesId()   {return liabilitiesId;}
    public int getLiabilitiesValue()    {return liabilitiesValue;}
    public void setLiabilitiesId(int liabilitiesId)     {this.liabilitiesId = liabilitiesId;}
    public void setLiabilitiesValue(int liabilitiesValue)       {this.liabilitiesValue = liabilitiesValue;}
}
