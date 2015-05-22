package net.grobas.blizzardleaderboards.app.ui.adapter;


import android.os.Parcel;
import android.os.Parcelable;

import net.grobas.blizzardleaderboards.app.domain.Row;

public class FilterSpec implements Parcelable{

    private boolean factions[];
    private boolean classes[];
    private boolean races[];

    public FilterSpec() {
        setDefaultValues();
    }

    public void toggleFaction(int pos) {
        factions[pos] = !factions[pos];
    }

    public void toggleClass(int pos) {
        classes[pos - 1] = !classes[pos - 1];
    }

    public void toggleRace(int pos) {
        races[pos - 1] = !races[pos - 1];
    }

    public boolean isRowValid(Row row) {
        return factions[row.getFactionId()] && classes[row.getClassId() - 1] && races[row.getRaceId() - 1];
    }

    public void setDefaultValues() {
        factions = new boolean[]{true, true};
        classes = new boolean[]{true, true, true, true, true, true, true, true, true, true, true};
        races = new boolean[]{true, true, true, true, true, true, true, true, true, true, true, true, true};
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBooleanArray(factions);
        dest.writeBooleanArray(classes);
        dest.writeBooleanArray(races);
    }

    public static final Parcelable.Creator<FilterSpec> CREATOR = new Parcelable.Creator<FilterSpec>() {
        public FilterSpec createFromParcel(Parcel pc) {
            return new FilterSpec(pc);
        }
        public FilterSpec[] newArray(int size) {
            return new FilterSpec[size];
        }
    };

    public FilterSpec(Parcel pc){
        pc.readBooleanArray(factions);
        pc.readBooleanArray(classes);
        pc.readBooleanArray(races);
    }
}
