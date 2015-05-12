package net.grobas.blizzardleaderboards.app.domain;

public class Profile {

    private long lastModified;
    private String name;
    private String realm;
    private String battlegroup;
    private int _class;
    private int race;
    private int gender;
    private int level;
    private int achievementPoints;
    private String thumbnail;
    private String calcClass;
    private int totalHonorableKills;

    /**
     *
     * @return
     * The lastModified
     */
    public long getLastModified() {
        return lastModified;
    }

    /**
     *
     * @param lastModified
     * The lastModified
     */
    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The realm
     */
    public String getRealm() {
        return realm;
    }

    /**
     *
     * @param realm
     * The realm
     */
    public void setRealm(String realm) {
        this.realm = realm;
    }

    /**
     *
     * @return
     * The battlegroup
     */
    public String getBattlegroup() {
        return battlegroup;
    }

    /**
     *
     * @param battlegroup
     * The battlegroup
     */
    public void setBattlegroup(String battlegroup) {
        this.battlegroup = battlegroup;
    }

    /**
     *
     * @return
     * The _class
     */
    public int getClass_() {
        return _class;
    }

    /**
     *
     * @param _class
     * The class
     */
    public void setClass_(int _class) {
        this._class = _class;
    }

    /**
     *
     * @return
     * The race
     */
    public int getRace() {
        return race;
    }

    /**
     *
     * @param race
     * The race
     */
    public void setRace(int race) {
        this.race = race;
    }

    /**
     *
     * @return
     * The gender
     */
    public int getGender() {
        return gender;
    }

    /**
     *
     * @param gender
     * The gender
     */
    public void setGender(int gender) {
        this.gender = gender;
    }

    /**
     *
     * @return
     * The level
     */
    public int getLevel() {
        return level;
    }

    /**
     *
     * @param level
     * The level
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     *
     * @return
     * The achievementPoints
     */
    public int getAchievementPoints() {
        return achievementPoints;
    }

    /**
     *
     * @param achievementPoints
     * The achievementPoints
     */
    public void setAchievementPoints(int achievementPoints) {
        this.achievementPoints = achievementPoints;
    }

    /**
     *
     * @return
     * The thumbnail
     */
    public String getThumbnail() {
        return thumbnail;
    }

    /**
     *
     * @param thumbnail
     * The thumbnail
     */
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    /**
     *
     * @return
     * The calcClass
     */
    public String getCalcClass() {
        return calcClass;
    }

    /**
     *
     * @param calcClass
     * The calcClass
     */
    public void setCalcClass(String calcClass) {
        this.calcClass = calcClass;
    }

    /**
     *
     * @return
     * The totalHonorableKills
     */
    public int getTotalHonorableKills() {
        return totalHonorableKills;
    }

    /**
     *
     * @param totalHonorableKills
     * The totalHonorableKills
     */
    public void setTotalHonorableKills(int totalHonorableKills) {
        this.totalHonorableKills = totalHonorableKills;
    }


}
