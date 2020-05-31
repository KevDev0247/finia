package protect.FinanceLord.TransactionEditingUtils;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class BudgetTypesDataModel implements Parcelable, Serializable {

    public int typeId;
    public String typeName;

    public BudgetTypesDataModel(int typeId, String typeName){
        this.typeName = typeName;
        this.typeId = typeId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(typeId);
        dest.writeString(typeName);
    }

    private BudgetTypesDataModel(Parcel in) {
        typeId = in.readInt();
        typeName = in.readString();
    }

    public static final Creator<BudgetTypesDataModel> CREATOR = new Creator<BudgetTypesDataModel>() {
        @Override
        public BudgetTypesDataModel createFromParcel(Parcel in) {
            return new BudgetTypesDataModel(in);
        }

        @Override
        public BudgetTypesDataModel[] newArray(int size) {
            return new BudgetTypesDataModel[size];
        }
    };
}
