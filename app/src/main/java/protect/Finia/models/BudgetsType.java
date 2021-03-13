package protect.Finia.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "BudgetsType")
public class BudgetsType implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int budgetsCategoryId;

    @ColumnInfo(name = "budgetsName")
    private String budgetsName;

    @Ignore
    public BudgetsType() { }

    public BudgetsType(int budgetsCategoryId, String budgetsName) {
        this.budgetsCategoryId = budgetsCategoryId;
        this.budgetsName = budgetsName;
    }

    public int getBudgetsCategoryId()   { return budgetsCategoryId;}
    public String getBudgetsName()     { return budgetsName;}

    public void setBudgetsCategoryId(int budgetsCategoryId)     { this.budgetsCategoryId = budgetsCategoryId; }
    public void setBudgetsName(String budgetsName) { this.budgetsName = budgetsName;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(budgetsCategoryId);
        dest.writeString(budgetsName);
    }

    protected BudgetsType(Parcel in) {
        budgetsCategoryId = in.readInt();
        budgetsName = in.readString();
    }

    public static final Creator<BudgetsType> CREATOR = new Creator<BudgetsType>() {
        @Override
        public BudgetsType createFromParcel(Parcel in) {
            return new BudgetsType(in);
        }

        @Override
        public BudgetsType[] newArray(int size) {
            return new BudgetsType[size];
        }
    };
}
