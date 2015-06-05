package net.grobas.blizzardleaderboards.app.ui;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.grobas.blizzardleaderboards.R;
import net.grobas.blizzardleaderboards.app.domain.CharacterProfile;
import net.grobas.blizzardleaderboards.app.util.Constants;
import net.grobas.blizzardleaderboards.core.LeaderboardDataService;

import butterknife.InjectView;
import rx.Observer;
import rx.Subscription;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class CharacterActivity extends BaseActivity implements Observer<CharacterProfile> {

    private Subscription profileSubscription;
    private CharacterProfile characterProfile;
    private String name;
    private String realmName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        name = getIntent().getStringExtra(Constants.INTENT_CHARACTER_NAME);
        realmName = getIntent().getStringExtra(Constants.INTENT_REALM_NAME);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(name);

        getCharacterProfile(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(profileSubscription != null && !profileSubscription.isUnsubscribed())
            profileSubscription.unsubscribe();
    }

    @Override
    protected void createNavDrawerContents(NavigationView mNavigationView) {
        LinearLayout parent = (LinearLayout) findViewById(R.id.drawer_contents);
        LinearLayout contents = (LinearLayout) getLayoutInflater().inflate(R.layout.navdrawer_profile, parent, false);
        parent.addView(contents);

    }

    @Override
    protected void updateContents() {
        getCharacterProfile(true);
    }

    private void getCharacterProfile(boolean forceUpdate) {
        profileSubscription = AppObservable.bindActivity(
            this, LeaderboardDataService.getInstance().getCharacterProfile(realmName, name, forceUpdate))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .subscribe(this);
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onNext(CharacterProfile profile) {
        characterProfile = profile;
        updateDataProfile();
    }

    private void updateDataProfile() {

    }

    @Override
    public void onError(Throwable e) {
        Log.e("CharacterProfile", "Error retrieving character profile", e);
    }

    public void onOptionClick(View view) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_character, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
