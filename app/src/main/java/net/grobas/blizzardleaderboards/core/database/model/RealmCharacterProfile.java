package net.grobas.blizzardleaderboards.core.database.model;


import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class RealmCharacterProfile extends RealmObject {

    private long lastModified;
    private String name;
    @SerializedName("realm")
    private String realmName;
    private String battlegroup;
    @SerializedName("class")
    private int specClass;
    private int race;
    private int gender;
    private int level;
    private int achievementPoints;
    private String thumbnail;
    private String calcClass;
    private int totalHonorableKills;

    public int getAchievementPoints() {
        return achievementPoints;
    }

    public void setAchievementPoints(int achievementPoints) {
        this.achievementPoints = achievementPoints;
    }

    public String getBattlegroup() {
        return battlegroup;
    }

    public void setBattlegroup(String battlegroup) {
        this.battlegroup = battlegroup;
    }

    public String getCalcClass() {
        return calcClass;
    }

    public void setCalcClass(String calcClass) {
        this.calcClass = calcClass;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRace() {
        return race;
    }

    public void setRace(int race) {
        this.race = race;
    }

    public String getRealmName() {
        return realmName;
    }

    public void setRealmName(String realm) {
        this.realmName = realm;
    }

    public int getSpecClass() {
        return specClass;
    }

    public void setSpecClass(int specClass) {
        this.specClass = specClass;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getTotalHonorableKills() {
        return totalHonorableKills;
    }

    public void setTotalHonorableKills(int totalHonorableKills) {
        this.totalHonorableKills = totalHonorableKills;
    }
}
