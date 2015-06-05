package net.grobas.blizzardleaderboards.app.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.squareup.otto.Subscribe;

import net.grobas.blizzardleaderboards.R;
import net.grobas.blizzardleaderboards.app.domain.Leaderboard;
import net.grobas.blizzardleaderboards.app.domain.Row;
import net.grobas.blizzardleaderboards.app.ui.adapter.FilterSpec;
import net.grobas.blizzardleaderboards.app.ui.adapter.LeaderboardAdapter;
import net.grobas.blizzardleaderboards.app.ui.custom.DividerItemDecoration;
import net.grobas.blizzardleaderboards.app.ui.custom.DrawerTextView;
import net.grobas.blizzardleaderboards.app.util.Constants;
import net.grobas.blizzardleaderboards.core.LeaderboardDataService;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;
import rx.Observer;
import rx.Subscription;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class MainActivity extends BaseActivity implements SearchView.OnQueryTextListener,
        MenuItemCompat.OnActionExpandListener, Observer<Leaderboard>,
        NavigationView.OnNavigationItemSelectedListener {

    private final static String SAVE_BRACKET_STATE = "bracket_state";
    private final static String SAVE_ORDER_STATE = "order_state";
    private final static String SAVE_FIRST_VIEW_STATE = "first_view_state";
    private final static String SAVE_BRACKET_SELECTED_ID = "bracket_id";
    private final static String SAVE_ORDER_SELECTED_ID = "order_id";
    private final static String SAVE_FILTER_SPEC = "filter_spec";

    @InjectView(R.id.widget_listView) RecyclerView mRecyclerView;
    @InjectView(R.id.loading_layer) FrameLayout loadingLayout;
    @InjectView(R.id.error_layer) FrameLayout errorLayout;

    @InjectViews({R.id.filter_alliance, R.id.filter_horde, R.id.filter_dk, R.id.filter_druid,
            R.id.filter_hunter, R.id.filter_mage, R.id.filter_monk, R.id.filter_paladin,
            R.id.filter_priest, R.id.filter_rogue, R.id.filter_shaman, R.id.filter_warlock,
            R.id.filter_warrior, R.id.filter_blood_elf, R.id.filter_draenei, R.id.filter_dwarf,
            R.id.filter_gnome, R.id.filter_goblin, R.id.filter_human, R.id.filter_orc,
            R.id.filter_pandaren, R.id.filter_tauren, R.id.filter_troll, R.id.filter_worgen,
            R.id.filter_night_elf, R.id.filter_undead}) List<CheckBox> filterList;

    private int selectedBracketId = R.id.menu_2v2;
    private int currentBracket = 0;

    private DrawerTextView currentOrderSelected;
    private int selectedOrderId = R.id.order_ranking;
    private int currentOrder = 0;

    private MenuItem mSearchMenuItem;
    private LeaderboardAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private FilterSpec filterSpec;

    private int firstViewVisible = 0;
    private String lastQuery;
    private long lastSearchTime = 0L;

    private Subscription leaderboardSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        if(savedInstanceState != null) {
            currentBracket = savedInstanceState.getInt(SAVE_BRACKET_STATE, 0);
            currentOrder = savedInstanceState.getInt(SAVE_ORDER_STATE, 0);
            firstViewVisible = savedInstanceState.getInt(SAVE_FIRST_VIEW_STATE, 0);
            selectedBracketId = savedInstanceState.getInt(SAVE_BRACKET_SELECTED_ID, R.id.menu_2v2);
            selectedOrderId = savedInstanceState.getInt(SAVE_ORDER_SELECTED_ID, R.id.order_ranking);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(1));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void onResume() {
        super.onResume();

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String currentHost = sharedPref.getString(Constants.PREFERENCES_HOST_NAME, Constants.EU_HOST);

        LeaderboardDataService.getInstance().setHost(currentHost);
        startLoading();
        setDataFromService(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(leaderboardSubscription != null && !leaderboardSubscription.isUnsubscribed())
            leaderboardSubscription.unsubscribe();
    }

    @Subscribe
    public void onClickLeaderboardResponse(LeaderboardAdapter.ClickLeaderboardResponse response) {
        Row row = response.getLeaderboardItem();
        Intent intent = new Intent(this, CharacterActivity.class);
        intent.putExtra(Constants.INTENT_CHARACTER_NAME, row.getName());
        intent.putExtra(Constants.INTENT_REALM_NAME, row.getRealmName());
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(SAVE_BRACKET_STATE, currentBracket);
        outState.putInt(SAVE_ORDER_STATE, currentOrder);
        outState.putInt(SAVE_FIRST_VIEW_STATE, mLayoutManager.findFirstVisibleItemPosition());
        outState.putInt(SAVE_BRACKET_SELECTED_ID, selectedBracketId);
        outState.putInt(SAVE_ORDER_SELECTED_ID, selectedOrderId);
        if(mAdapter != null && mAdapter.getFilterSpec() != null)
            outState.putParcelable(SAVE_FILTER_SPEC, mAdapter.getFilterSpec());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentBracket = savedInstanceState.getInt(SAVE_BRACKET_STATE, 0);
        currentOrder = savedInstanceState.getInt(SAVE_ORDER_STATE, 0);
        firstViewVisible = savedInstanceState.getInt(SAVE_FIRST_VIEW_STATE, 0);
        selectedBracketId = savedInstanceState.getInt(SAVE_BRACKET_SELECTED_ID, R.id.menu_2v2);
        selectedOrderId = savedInstanceState.getInt(SAVE_ORDER_SELECTED_ID, R.id.order_ranking);
        filterSpec = savedInstanceState.getParcelable(SAVE_FILTER_SPEC);
    }

    @Override
    public void onBackPressed() {
        if (isNavDrawerOpen())
            closeNavDrawer();
        else if (mSearchMenuItem.isActionViewExpanded())
            mSearchMenuItem.collapseActionView();
        else
            super.onBackPressed();
    }

    @Override
    public void onCompleted() {
        //TODO
    }

    @Override
    public void onError(Throwable e) {
        Timber.e(e, "Error on data service");
        stopLoading();
        errorLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNext(Leaderboard leaderboard) {
        stopLoading();
        if(mAdapter == null) {
            mAdapter = new LeaderboardAdapter(MainActivity.this, leaderboard, filterSpec, currentOrder);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.update(leaderboard.getRows());
        }
        mRecyclerView.getLayoutManager().scrollToPosition(firstViewVisible);
    }

    private void stopLoading() {
        loadingLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
        mSwipeSwipeRefreshLayout.setRefreshing(false);
    }

    private void startLoading() {
        if(mAdapter != null)
            mAdapter.clear();
        errorLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void updateContents() {
        firstViewVisible = 0;
        if(mSearchMenuItem.isActionViewExpanded())
            mSearchMenuItem.collapseActionView();
        if(errorLayout.isShown())
            startLoading();
        setDataFromService(true);
    }

    private void setDataFromService(boolean forceUpdate) {
        leaderboardSubscription = AppObservable.bindActivity(this,
            LeaderboardDataService.getInstance().getLeaderboard(Constants.BRACKET_LIST[currentBracket], forceUpdate))
                .delay(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mSearchMenuItem = menu.findItem(R.id.action_search);
        MenuItemCompat.setOnActionExpandListener(mSearchMenuItem, this);

        SearchView searchView = (SearchView) mSearchMenuItem.getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        //Avoid bug: this is called twice in some devices (ACTION_UP and ACTION_DOWN)
        long actualSearchTime = Calendar.getInstance().getTimeInMillis();
        if(actualSearchTime < lastSearchTime + 1000)
            return true;

        lastSearchTime = actualSearchTime;
        if(TextUtils.isEmpty(query)) {
            mAdapter.clearAll();
        } else {
            lastQuery = query;
            mAdapter.getFilter().filter(query);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        if(!TextUtils.isEmpty(lastQuery))
            mAdapter.clearAll();
        return true;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    @Override
    protected void createNavDrawerContents(NavigationView navigationView) {

        if(navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.getMenu().findItem(selectedBracketId).setChecked(true);
        }

        currentOrderSelected = ButterKnife.findById(this, selectedOrderId);
        currentOrderSelected.setBackgroundResource(R.drawable.drawer_item_selected_background);
        currentOrderSelected.setTextColor(getResources().getColor(R.color.drawer_item_text_selected));
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.menu_2v2:
                currentBracket = 0;
                setToolbarTitle(R.string.arena2v2);
                break;
            case R.id.menu_3v3:
                currentBracket = 1;
                setToolbarTitle(R.string.arena3v3);
                break;
            case R.id.menu_5v5:
                currentBracket = 2;
                setToolbarTitle(R.string.arena5v5);
                break;
            case R.id.menu_rbg:
                currentBracket = 3;
                setToolbarTitle(R.string.ratedbg);
                break;
            case R.id.menu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.menu_license:
                //TODO
                return true;
        }

        selectedBracketId = menuItem.getItemId();
        menuItem.setChecked(true);
        startLoading();
        setDataFromService(false);
        closeNavDrawer();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_show_drawer:
                openDrawer(Gravity.END);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @OnClick({R.id.order_name, R.id.order_rating, R.id.order_realm, R.id.order_ranking, R.id.order_season_wins})
    public void onClickOrder(DrawerTextView v) {
        int id = v.getId();

        if(currentOrderSelected.getId() != id) {
            currentOrderSelected.setBackgroundResource(R.drawable.drawer_item_background);
            currentOrderSelected.setTextColor(getResources().getColor(R.color.drawer_text_color));
            v.setBackgroundResource(R.drawable.drawer_item_selected_background);
            v.setTextColor(getResources().getColor(R.color.drawer_item_text_selected));
            currentOrderSelected = v;
            selectedOrderId = id;
        }

        switch (id) {
            case R.id.order_name:
                currentOrder = Constants.SORT_BY_NAME;
                break;
            case R.id.order_rating:
                currentOrder = Constants.SORT_BY_RATING;
                break;
            case R.id.order_realm:
                currentOrder = Constants.SORT_BY_REALM;
                break;
            case R.id.order_ranking:
                currentOrder = Constants.SORT_BY_RANKING;
                break;
            case R.id.order_season_wins:
                currentOrder = Constants.SORT_BY_SEASON_WINS;
        }

        mAdapter.setSortBy(currentOrder);
        closeNavDrawer();
    }

    @OnClick({R.id.filter_alliance, R.id.filter_horde})
    public void onClickFactionFilter(View v) {
        switch (v.getId()) {
            case R.id.filter_alliance:
                mAdapter.filterFaction(Constants.FACTION_ALLIANCE);
                break;
            case R.id.filter_horde:
                mAdapter.filterFaction(Constants.FACTION_HORDE);
        }
    }

    @OnClick({R.id.filter_dk, R.id.filter_druid, R.id.filter_hunter, R.id.filter_mage, R.id.filter_monk,
        R.id.filter_paladin, R.id.filter_priest, R.id.filter_rogue, R.id.filter_shaman, R.id.filter_warlock,
        R.id.filter_warrior})
    public void onClickClassFilter(View v) {
        switch (v.getId()) {
            case R.id.filter_warrior:
                mAdapter.filterClass(Constants.CLASS_WARRIOR);
                break;
            case R.id.filter_paladin:
                mAdapter.filterClass(Constants.CLASS_PALADIN);
                break;
            case R.id.filter_hunter:
                mAdapter.filterClass(Constants.CLASS_HUNTER);
                break;
            case R.id.filter_rogue:
                mAdapter.filterClass(Constants.CLASS_ROGUE);
                break;
            case R.id.filter_priest:
                mAdapter.filterClass(Constants.CLASS_PRIEST);
                break;
            case R.id.filter_dk:
                mAdapter.filterClass(Constants.CLASS_DEATHKNIGHT);
                break;
            case R.id.filter_shaman:
                mAdapter.filterClass(Constants.CLASS_SHAMAN);
                break;
            case R.id.filter_mage:
                mAdapter.filterClass(Constants.CLASS_MAGE);
                break;
            case R.id.filter_warlock:
                mAdapter.filterClass(Constants.CLASS_WARLOCK);
                break;
            case R.id.filter_monk:
                mAdapter.filterClass(Constants.CLASS_MONK);
                break;
            case R.id.filter_druid:
                mAdapter.filterClass(Constants.CLASS_DRUID);
        }

    }

    @OnClick({R.id.filter_blood_elf, R.id.filter_draenei, R.id.filter_dwarf, R.id.filter_gnome, R.id.filter_goblin,
        R.id.filter_human, R.id.filter_orc, R.id.filter_pandaren, R.id.filter_tauren, R.id.filter_troll,
            R.id.filter_worgen, R.id.filter_night_elf, R.id.filter_undead})
    public void onClickRaceFilter(View v) {
        switch (v.getId()) {
            case R.id.filter_human:
                mAdapter.filterRace(Constants.RACE_HUMAN);
                break;
            case R.id.filter_orc:
                mAdapter.filterRace(Constants.RACE_ORC);
                break;
            case R.id.filter_dwarf:
                mAdapter.filterRace(Constants.RACE_DWARF);
                break;
            case R.id.filter_night_elf:
                mAdapter.filterRace(Constants.RACE_NIGHT_ELF);
                break;
            case R.id.filter_undead:
                mAdapter.filterRace(Constants.RACE_SCOURGE);
                break;
            case R.id.filter_tauren:
                mAdapter.filterRace(Constants.RACE_TAUREN);
                break;
            case R.id.filter_gnome:
                mAdapter.filterRace(Constants.RACE_GNOME);
                break;
            case R.id.filter_troll:
                mAdapter.filterRace(Constants.RACE_TROLL);
                break;
            case R.id.filter_goblin:
                mAdapter.filterRace(Constants.RACE_GOBLIN);
                break;
            case R.id.filter_blood_elf:
                mAdapter.filterRace(Constants.RACE_BLOOD_ELF);
                break;
            case R.id.filter_draenei:
                mAdapter.filterRace(Constants.RACE_DRAENEI);
                break;
            case R.id.filter_worgen:
                mAdapter.filterRace(Constants.RACE_WORGEN);
                break;
            case R.id.filter_pandaren:
                mAdapter.filterRace(Constants.RACE_PANDAREN);
        }

    }

    @OnClick(R.id.filter_select_all)
    public void onClickSelectAll(View v) {
        mAdapter.clearFilter();

        for(CheckBox cb : filterList) {
            cb.setChecked(true);
        }
        closeNavDrawer();
    }

}
