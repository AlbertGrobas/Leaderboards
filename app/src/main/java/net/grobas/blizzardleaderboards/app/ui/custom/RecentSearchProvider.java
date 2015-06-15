package net.grobas.blizzardleaderboards.app.ui.custom;

import android.content.SearchRecentSuggestionsProvider;

public class RecentSearchProvider extends SearchRecentSuggestionsProvider {

    public static final String AUTHORITY =
            RecentSearchProvider.class.getName();

    public static final int MODE = DATABASE_MODE_QUERIES;

    public RecentSearchProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }

}
