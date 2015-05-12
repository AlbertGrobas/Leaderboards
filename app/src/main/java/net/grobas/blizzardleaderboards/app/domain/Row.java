package net.grobas.blizzardleaderboards.app.domain;

public class Row {

    private int ranking;
    private int rating;
    private String name;
    private int realmId;
    private String realmName;
    private String realmSlug;
    private int raceId;
    private int classId;
    private int specId;
    private int factionId;
    private int genderId;
    private int seasonWins;
    private int seasonLosses;
    private int weeklyWins;
    private int weeklyLosses;

    /**
     *
     * @return
     * The ranking
     */
    public int getRanking() {
        return ranking;
    }

    /**
     *
     * @param ranking
     * The ranking
     */
    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    /**
     *
     * @return
     * The rating
     */
    public int getRating() {
        return rating;
    }

    /**
     *
     * @param rating
     * The rating
     */
    public void setRating(int rating) {
        this.rating = rating;
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
     * The realmId
     */
    public int getRealmId() {
        return realmId;
    }

    /**
     *
     * @param realmId
     * The realmId
     */
    public void setRealmId(int realmId) {
        this.realmId = realmId;
    }

    /**
     *
     * @return
     * The realmName
     */
    public String getRealmName() {
        return realmName;
    }

    /**
     *
     * @param realmName
     * The realmName
     */
    public void setRealmName(String realmName) {
        this.realmName = realmName;
    }

    /**
     *
     * @return
     * The realmSlug
     */
    public String getRealmSlug() {
        return realmSlug;
    }

    /**
     *
     * @param realmSlug
     * The realmSlug
     */
    public void setRealmSlug(String realmSlug) {
        this.realmSlug = realmSlug;
    }

    /**
     *
     * @return
     * The raceId
     */
    public int getRaceId() {
        return raceId;
    }

    /**
     *
     * @param raceId
     * The raceId
     */
    public void setRaceId(int raceId) {
        this.raceId = raceId;
    }

    /**
     *
     * @return
     * The classId
     */
    public int getClassId() {
        return classId;
    }

    /**
     *
     * @param classId
     * The classId
     */
    public void setClassId(int classId) {
        this.classId = classId;
    }

    /**
     *
     * @return
     * The specId
     */
    public int getSpecId() {
        return specId;
    }

    /**
     *
     * @param specId
     * The specId
     */
    public void setSpecId(int specId) {
        this.specId = specId;
    }

    /**
     *
     * @return
     * The factionId
     */
    public int getFactionId() {
        return factionId;
    }

    /**
     *
     * @param factionId
     * The factionId
     */
    public void setFactionId(int factionId) {
        this.factionId = factionId;
    }

    /**
     *
     * @return
     * The genderId
     */
    public int getGenderId() {
        return genderId;
    }

    /**
     *
     * @param genderId
     * The genderId
     */
    public void setGenderId(int genderId) {
        this.genderId = genderId;
    }

    /**
     *
     * @return
     * The seasonWins
     */
    public int getSeasonWins() {
        return seasonWins;
    }

    /**
     *
     * @param seasonWins
     * The seasonWins
     */
    public void setSeasonWins(int seasonWins) {
        this.seasonWins = seasonWins;
    }

    /**
     *
     * @return
     * The seasonLosses
     */
    public int getSeasonLosses() {
        return seasonLosses;
    }

    /**
     *
     * @param seasonLosses
     * The seasonLosses
     */
    public void setSeasonLosses(int seasonLosses) {
        this.seasonLosses = seasonLosses;
    }

    /**
     *
     * @return
     * The weeklyWins
     */
    public int getWeeklyWins() {
        return weeklyWins;
    }

    /**
     *
     * @param weeklyWins
     * The weeklyWins
     */
    public void setWeeklyWins(int weeklyWins) {
        this.weeklyWins = weeklyWins;
    }

    /**
     *
     * @return
     * The weeklyLosses
     */
    public int getWeeklyLosses() {
        return weeklyLosses;
    }

    /**
     *
     * @param weeklyLosses
     * The weeklyLosses
     */
    public void setWeeklyLosses(int weeklyLosses) {
        this.weeklyLosses = weeklyLosses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Row row = (Row) o;

        if (ranking != row.ranking) return false;
        if (realmId != row.realmId) return false;
        if (seasonLosses != row.seasonLosses) return false;
        if (weeklyWins != row.weeklyWins) return false;
        if (weeklyLosses != row.weeklyLosses) return false;
        return name.equals(row.name);

    }

    @Override
    public int hashCode() {
        int result = ranking;
        result = 31 * result + name.hashCode();
        result = 31 * result + realmId;
        return result;
    }

}
