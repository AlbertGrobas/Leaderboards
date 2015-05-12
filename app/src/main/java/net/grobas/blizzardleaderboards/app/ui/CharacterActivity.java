package net.grobas.blizzardleaderboards.app.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.grobas.blizzardleaderboards.R;
import net.grobas.blizzardleaderboards.app.domain.Profile;
import net.grobas.blizzardleaderboards.app.util.Constants;
import net.grobas.blizzardleaderboards.core.LeaderboardDataService;

import butterknife.InjectView;
import rx.Observer;
import rx.Subscription;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CharacterActivity extends BaseActivity implements Observer<Profile> {

    @InjectView(R.id.character_name) TextView characterName;
    private Subscription profileSubscription;
    private Profile characterProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        String name = getIntent().getStringExtra(Constants.INTENT_CHARACTER_NAME);
        String realmName = getIntent().getStringExtra(Constants.INTENT_REALM_NAME);
        setToolbarTitle(name);
        setToolbarSubtitle(realmName);

        profileSubscription = AppObservable.bindActivity(
                this, LeaderboardDataService.getInstance().getCharacterProfile(realmName, name))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(profileSubscription != null && !profileSubscription.isUnsubscribed())
            profileSubscription.unsubscribe();
    }

    @Override
    protected void createNavDrawerContents() {
        LinearLayout parent = (LinearLayout) findViewById(R.id.drawer_contents);
        LinearLayout contents = (LinearLayout) getLayoutInflater().inflate(R.layout.navdrawer_profile, parent, false);
        parent.addView(contents);

    }

    @Override
    protected void updateContents() {

    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onNext(Profile profile) {
        characterProfile = profile;
        updateDataProfile();
    }

    private void updateDataProfile() {
        characterName.setText(characterProfile.getName());
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
