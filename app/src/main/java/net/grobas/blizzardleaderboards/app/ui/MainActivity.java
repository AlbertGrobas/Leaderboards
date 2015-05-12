package net.grobas.blizzardleaderboards.app.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.squareup.otto.Subscribe;

import net.grobas.blizzardleaderboards.R;
import net.grobas.blizzardleaderboards.app.domain.Leaderboard;
import net.grobas.blizzardleaderboards.app.domain.Row;
import net.grobas.blizzardleaderboards.app.ui.adapter.LeaderboardAdapter;
import net.grobas.blizzardleaderboards.app.ui.custom.DividerItemDecoration;
import net.grobas.blizzardleaderboards.app.ui.custom.DrawerTextView;
import net.grobas.blizzardleaderboards.app.util.Constants;
import net.grobas.blizzardleaderboards.core.LeaderboardDataService;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;
import rx.Observer;
import rx.Subscription;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements SearchView.OnQueryTextListener,
        MenuItemCompat.OnActionExpandListener, Observer<Leaderboard> {

    private final static String SAVE_BRACKET_STATE = "bracket_state";
    private final static String SAVE_FIRST_VIEW_STATE = "first_view_state";
    private final static String SAVE_SELECTED_ID = "selected_id";

    @InjectView(R.id.widget_listView) RecyclerView mRecyclerView;
    @InjectView(R.id.loading_layer) FrameLayout loadingLayout;
    @InjectView(R.id.error_layer) FrameLayout errorLayout;

    private DrawerTextView currentSelected;
    private int selectedId = R.id.drawer_2v2;
    private MenuItem mSearchMenuItem;
    private LeaderboardAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private int currentBracket = 0;
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
            firstViewVisible = savedInstanceState.getInt(SAVE_FIRST_VIEW_STATE, 0);
            selectedId = savedInstanceState.getInt(SAVE_SELECTED_ID, R.id.drawer_2v2);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(1));
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
        intent.putExtra(Constants.INTENT_REALM_NAME, row.getRealmSlug());
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(SAVE_BRACKET_STATE, currentBracket);
        outState.putInt(SAVE_FIRST_VIEW_STATE, mLayoutManager.findFirstVisibleItemPosition());
        outState.putInt(SAVE_SELECTED_ID, selectedId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentBracket = savedInstanceState.getInt(SAVE_BRACKET_STATE, 0);
        firstViewVisible = savedInstanceState.getInt(SAVE_FIRST_VIEW_STATE, 0);
        selectedId = savedInstanceState.getInt(SAVE_SELECTED_ID, R.id.drawer_2v2);
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
    public void onCompleted() { }

    @Override
    public void onError(Throwable e) {
        Log.e("Leaderboard", "Error retrieving data", e);
        stopLoading();
        errorLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNext(Leaderboard leaderboard) {
        stopLoading();
        if(mAdapter == null) {
            mAdapter = new LeaderboardAdapter(MainActivity.this, leaderboard);
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
        setDataFromService(true);
    }

    private void setDataFromService(boolean forceUpdate) {
        leaderboardSubscription = AppObservable.bindActivity(this,
                LeaderboardDataService.getInstance().getLeaderboard(Constants.BRACKET_LIST[currentBracket], forceUpdate))
                .cache()
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
    protected void createNavDrawerContents() {
        LinearLayout parent = (LinearLayout) findViewById(R.id.drawer_contents);
        parent.addView(getLayoutInflater().inflate(R.layout.navdrawer_main, parent, false));

        currentSelected = ButterKnife.findById(parent, selectedId);
        currentSelected.setBackgroundResource(R.drawable.drawer_item_selected_background);
        currentSelected.setTextColor(getResources().getColor(R.color.drawer_item_text_selected));
    }

    public void onSettingsClick(View v) {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    public void onLicenseClick(View v) {
        //TODO
    }

    public void onBracketClick(View v) {
        DrawerTextView textView = (DrawerTextView) v;
        if(textView.getId() == currentSelected.getId())
            return;

        currentSelected.setBackgroundResource(R.drawable.drawer_item_background);
        currentSelected.setTextColor(getResources().getColor(R.color.drawer_text_color));
        textView.setBackgroundResource(R.drawable.drawer_item_selected_background);
        textView.setTextColor(getResources().getColor(R.color.drawer_item_text_selected));

        switch (v.getId()) {
            case R.id.drawer_2v2:
                currentBracket = 0;
                setToolbarTitle(R.string.arena2v2);
                break;
            case R.id.drawer_3v3:
                currentBracket = 1;
                setToolbarTitle(R.string.arena3v3);
                break;
            case R.id.drawer_5v5:
                currentBracket = 2;
                setToolbarTitle(R.string.arena5v5);
                break;
            case R.id.drawer_rbg:
                currentBracket = 3;
                setToolbarTitle(R.string.ratedbg);
                break;
        }
        currentSelected = textView;
        selectedId = v.getId();
        firstViewVisible = 0;
        changeBracket();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_clear_cache:
                Realm.deleteRealmFile(this);
                break;
            case R.id.action_sortbyname:
                sortListBy(Constants.SORT_BY_NAME);
                break;
            case R.id.action_sortbyrating:
                sortListBy(Constants.SORT_BY_RATING);
                break;
            case R.id.action_sortbyserver:
                sortListBy(Constants.SORT_BY_REALM);
                break;
            case R.id.action_sortbypos:
                sortListBy(Constants.SORT_BY_RAKING);
                break;
            case R.id.action_sortbywins:
                sortListBy(Constants.SORT_BY_SEASON_WINS);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private void sortListBy(int sort) {
        mAdapter.setSortBy(sort);
    }

    private void changeBracket() {
        startLoading();
        setDataFromService(false);
        closeNavDrawer();
    }

}
