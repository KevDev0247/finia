package protect.FinanceLord.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Liabilities")
public class Liabilities {
    @PrimaryKey(autoGenerate = true)
    private int liabilitiesId;

    @ColumnInfo(name = "liabilitiesValue")
    private float liabilitiesValue;

    @ColumnInfo(name = "liabilitiesName")
    private String liabilitiesName;

    @ColumnInfo(name = "liabilitiesParentType")
    private String liabilitiesParentType;

    @Ignore
    public Liabilities(){

    }

    public Liabilities(int liabilitiesId,float liabilitiesValue, String liabilitiesName,String liabilitiesParentType){
        this.liabilitiesId = liabilitiesId;
        this.liabilitiesValue = liabilitiesValue;
        this.liabilitiesName = liabilitiesName;
        this.liabilitiesParentType = liabilitiesParentType;
    }

    public int getLiabilitiesId()   {return liabilitiesId;}
    public String getLiabilitiesName()   {return liabilitiesName;}
    public String getLiabilitiesParentType()    {return liabilitiesParentType;}
    public float getLiabilitiesValue()      {return liabilitiesValue;}
    public void setLiabilitiesId(int liabilitiesId)   {this.liabilitiesId = liabilitiesId;}
    public void setLiabilitiesName(String liabilitiesName)   {this.liabilitiesName = liabilitiesName;}
    public void setLiabilitiesParentType(String liabilitiesParentType)    {this.liabilitiesParentType = liabilitiesParentType;}
    public void setLiabilitiesValue(float liabilitiesValue)     {this.liabilitiesValue = liabilitiesValue;}
}
