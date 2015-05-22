package net.grobas.blizzardleaderboards.app.util;

import net.grobas.blizzardleaderboards.R;

public class Constants {

    public static final String INTENT_CHARACTER_NAME = "character_name";
    public static final String INTENT_REALM_NAME = "realm_name";

    public static final String PREFERENCES_HOST_NAME = "region_host";

    public static final String US_HOST = "http://us.battle.net";
    public static final String EU_HOST = "http://eu.battle.net";
    public static final String KR_HOST = "http://kr.battle.net";
    public static final String TW_HOST = "http://tw.battle.net";
    public static final String CH_HOST = "http://www.battlenet.com.cn";

    public static final String BRACKET_2v2 = "2v2";
    public static final String BRACKET_3v3 = "3v3";
    public static final String BRACKET_5v5 = "5v5";
    public static final String BRACKET_rbg = "rbg";

    public static final int SORT_BY_RANKING = 0;
    public static final int SORT_BY_NAME = 1;
    public static final int SORT_BY_RATING = 2;
    public static final int SORT_BY_REALM = 3;
    public static final int SORT_BY_SEASON_WINS = 4;

    public static final String[] BRACKET_LIST = {
            BRACKET_2v2,
            BRACKET_3v3,
            BRACKET_5v5,
            BRACKET_rbg
    };

    public static final int FACTION_ALLIANCE = 0;
    public static final int FACTION_HORDE = 1;

    public static final int CLASS_WARRIOR = 1;
    public static final int CLASS_PALADIN = 2;
    public static final int CLASS_HUNTER = 3;
    public static final int CLASS_ROGUE = 4;
    public static final int CLASS_PRIEST = 5;
    public static final int CLASS_DEATHKNIGHT = 6;
    public static final int CLASS_SHAMAN = 7;
    public static final int CLASS_MAGE = 8;
    public static final int CLASS_WARLOCK = 9;
    public static final int CLASS_MONK = 10;
    public static final int CLASS_DRUID = 11;

    public static final int RACE_HUMAN = 1;
    public static final int RACE_ORC = 2;
    public static final int RACE_DWARF = 3;
    public static final int RACE_NIGHT_ELF = 4;
    public static final int RACE_SCOURGE = 5;
    public static final int RACE_TAUREN = 6;
    public static final int RACE_GNOME = 7;
    public static final int RACE_TROLL = 8;
    public static final int RACE_GOBLIN = 9;
    public static final int RACE_BLOOD_ELF = 10;
    public static final int RACE_DRAENEI = 11;
    public static final int RACE_WORGEN = 12;
    public static final int RACE_PANDAREN = 13;

    public static final int[] MALE_RACES = {
        R.drawable.ic_race_human_male,
        R.drawable.ic_race_orc_male,
        R.drawable.ic_race_dwarf_male,
        R.drawable.ic_race_nightelf_male,
        R.drawable.ic_race_scourge_male,
        R.drawable.ic_race_tauren_male,
        R.drawable.ic_race_gnome_male,
        R.drawable.ic_race_troll_male,
        R.drawable.ic_race_goblin_male,
        R.drawable.ic_race_bloodelf_male,
        R.drawable.ic_race_draenei_male,
        R.drawable.ic_race_worgen_male,
        R.drawable.ic_race_pandaren_male };

    public static final int[] FEMALE_RACES = {
        R.drawable.ic_race_human_female,
        R.drawable.ic_race_orc_female,
        R.drawable.ic_race_dwarf_female,
        R.drawable.ic_race_nightelf_female,
        R.drawable.ic_race_scourge_female,
        R.drawable.ic_race_tauren_female,
        R.drawable.ic_race_gnome_female,
        R.drawable.ic_race_troll_female,
        R.drawable.ic_race_goblin_female,
        R.drawable.ic_race_bloodelf_female,
        R.drawable.ic_race_draenei_female,
        R.drawable.ic_race_worgen_female,
        R.drawable.ic_race_pandaren_female };

    public static final int[] SPEC_CLASSES = {
        R.drawable.ic_class_warrior,
        R.drawable.ic_class_paladin,
        R.drawable.ic_class_hunter,
        R.drawable.ic_class_rogue,
        R.drawable.ic_class_priest,
        R.drawable.ic_class_deathknight,
        R.drawable.ic_class_shaman,
        R.drawable.ic_class_mage,
        R.drawable.ic_class_warlock,
        R.drawable.ic_class_monk,
        R.drawable.ic_class_druid };

}
