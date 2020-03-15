package protect.FinanceLord.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Assets")
public class Assets {
    @PrimaryKey(autoGenerate = true)
    private int assetsId;

    @ColumnInfo(name = "assetsValue")
    private float assetsValue;

    @ColumnInfo(name = "assetsName")
    private String assetsName;

    @ColumnInfo(name = "assetsParentType")
    private String assetsParentType;

    @Ignore
    public Assets(){

    }

    public Assets(int assetsId,float assetsValue,String assetsName,String assetsParentType){
        this.assetsId = assetsId;
        this.assetsValue = assetsValue;
        this.assetsName = assetsName;
        this.assetsParentType = assetsParentType;
    }

    public int getAssetsId()    {return assetsId;}
    public String getAssetsName()   {return this.assetsName;}
    public String getAssetsParentType()     {return this.assetsParentType;}
    public float getAssetsValue()       {return  this.assetsValue;}

    public void setAssetsId(int assetsId)   {this.assetsId = assetsId;}
    public void setAssetsValue (float assetsValue)      {this.assetsValue = assetsValue;}
    public void setAssetsName(String assetsName)    {this.assetsName = assetsName;}
    public void setAssetsParentType(String assetsParentType)   {this.assetsParentType = assetsParentType;}
}
