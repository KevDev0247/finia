package protect.Finia.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * The model for the database entity LiabilitiesValue
 * LiabilitiesValue is used to store the value of each liability and the date they are recorded
 *
 * @author Owner  Kevin Zhijun Wang
 * created on 2020/02/29
 */
@Entity(tableName = "LiabilitiesValue")
public class LiabilitiesValue {

    @PrimaryKey(autoGenerate = true)
    private int liabilitiesPrimaryId;

    @ColumnInfo(name = "liabilitiesId")
    private int liabilitiesId;

    @ColumnInfo(name = "liabilitiesValue")
    private float liabilitiesValue;

    @ColumnInfo(name = "date")
    private Long date;

    @Ignore
    public LiabilitiesValue() { }

    public LiabilitiesValue(int liabilitiesPrimaryId, int liabilitiesId, float liabilitiesValue, Long date) {
        this.liabilitiesPrimaryId = liabilitiesPrimaryId;
        this.liabilitiesId = liabilitiesId;
        this.liabilitiesValue = liabilitiesValue;
        this.date = date;
    }

    public int getLiabilitiesPrimaryId()    {return liabilitiesPrimaryId;}
    public int getLiabilitiesId()       {return liabilitiesId;}
    public float getLiabilitiesValue()      {return liabilitiesValue;}
    public Long getDate()       {return date;}

    public void setLiabilitiesPrimaryId(int liabilitiesPrimaryId)   {this.liabilitiesPrimaryId = liabilitiesPrimaryId;}
    public void setLiabilitiesValue(float liabilitiesValue)     {this.liabilitiesValue = liabilitiesValue;}
    public void setLiabilitiesId(int liabilitiesId)      {this.liabilitiesId = liabilitiesId;}
    public void setDate(Long date)      {this.date = date;}
}
