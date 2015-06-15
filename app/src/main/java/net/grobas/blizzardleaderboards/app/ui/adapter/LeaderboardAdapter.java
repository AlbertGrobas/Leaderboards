package net.grobas.blizzardleaderboards.app.ui.adapter;

import android.content.Context;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import net.grobas.blizzardleaderboards.R;
import net.grobas.blizzardleaderboards.app.domain.Leaderboard;
import net.grobas.blizzardleaderboards.app.domain.Row;
import net.grobas.blizzardleaderboards.app.util.Constants;
import net.grobas.blizzardleaderboards.app.util.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>
        implements Filterable {

    private Context context;
    private int[] classColors;
    private List<Row> backupData;
    private List<Row> searchData;
    private SortedList<Row> leaderboardData;

    private int currentSortBy = Constants.SORT_BY_RANKING;
    private boolean isSortAscending = true;
    private SearchFilter mFilter;
    private FilterSpec mFilterSpec;

    public LeaderboardAdapter(Context context, Leaderboard leaderboard, FilterSpec filterSpec, int order) {
        this.context = context;
        this.mFilter = new SearchFilter();
        this.backupData = leaderboard.getRows();
        this.searchData = leaderboard.getRows();
        this.classColors = context.getResources().getIntArray(R.array.class_colors);
        this.leaderboardData = new SortedList<>(Row.class, new LeaderboardSortCallback(), leaderboard.getRows().size());
        this.mFilterSpec = (filterSpec == null) ? new FilterSpec() : filterSpec;
        this.currentSortBy = order;
        getFilter().filter("");
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Row row = leaderboardData.get(i);

        int ranking = row.getRanking();
        if(ranking == 1) viewHolder.rank.setBackgroundResource(R.drawable.ranking_textview_background_gold);
        else if(ranking == 2) viewHolder.rank.setBackgroundResource(R.drawable.ranking_textview_background_silver);
        else if(ranking == 3) viewHolder.rank.setBackgroundResource(R.drawable.ranking_textview_background_bronze);
        else if(row.getFactionId() == 0) viewHolder.rank.setBackgroundResource(R.drawable.ranking_textview_background_alliance);
        else viewHolder.rank.setBackgroundResource(R.drawable.ranking_textview_background_horde);

        viewHolder.rank.setText(String.valueOf(ranking));
        viewHolder.name.setText(row.getName());
        viewHolder.name.setTextColor(classColors[row.getClassId() - 1]);
        viewHolder.realm.setText(row.getRealmName());
        viewHolder.rating.setText(String.valueOf(row.getRating()));

        String wins = String.valueOf(row.getSeasonWins());
        String losses = String.valueOf(row.getSeasonLosses());

        SpannableString span = new SpannableString(wins + "/" + losses);
        span.setSpan(new ForegroundColorSpan(context.getResources().
                getColor(R.color.wins)), 0, wins.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        span.setSpan(new ForegroundColorSpan(context.getResources().
                getColor(R.color.losses)), wins.length() + 1, span.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        viewHolder.season.setText(span);
        viewHolder.faction.setImageResource(row.getFactionId() == 0 ? R.drawable.ic_alliance : R.drawable.ic_horde);

        int raceId = row.getRaceId();
        viewHolder.race.setImageResource(row.getGenderId() == 0 ? Constants.MALE_RACES[raceId - 1] : Constants.FEMALE_RACES[raceId - 1]);
        viewHolder.spec.setImageResource(Constants.SPEC_CLASSES[row.getClassId() - 1]);
        viewHolder.itemPosition = i;
    }

    @Override
    public int getItemCount() {
        return leaderboardData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.leaderboard_row, viewGroup, false);
        ViewHolder vHolder = new ViewHolder(v);
        v.setBackgroundResource((i == 0) ? R.drawable.item_background : R.drawable.item_background_odd);
        return vHolder;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public void setSortBy(int sortBy) {
        isSortAscending = (currentSortBy != sortBy) || !isSortAscending;
        this.currentSortBy = sortBy;
        mFilter.filter("");
    }

    public void update(List<Row> data) {
        this.backupData = data;
        this.searchData = data;
        mFilter.filter("");
    }

    private void initData(List<Row> data) {
        leaderboardData.beginBatchedUpdates();
        while (leaderboardData.size() > 0) {
            leaderboardData.removeItemAt(leaderboardData.size() - 1);
        }
        for(Row r : data) {
            leaderboardData.add(r);
        }
        leaderboardData.endBatchedUpdates();
    }

    public void clear() {
        leaderboardData.beginBatchedUpdates();
        while (leaderboardData.size() > 0) {
            leaderboardData.removeItemAt(leaderboardData.size() - 1);
        }
        leaderboardData.endBatchedUpdates();
    }

    public void clearAll() {
        searchData = backupData;
        //initData(backupData);
        mFilter.filter("");
    }

    public void filterFaction(int factionId) {
        mFilterSpec.toggleFaction(factionId);
        mFilter.filter("");
    }

    public void filterClass(int classId) {
        mFilterSpec.toggleClass(classId);
        mFilter.filter("");
    }

    public void filterRace(int raceId) {
        mFilterSpec.toggleRace(raceId);
        mFilter.filter("");
    }

    public void clearFilter() {
        mFilterSpec.setDefaultValues();
        mFilter.filter("");
    }

    public FilterSpec getFilterSpec() {
        return mFilterSpec;
    }

    private class SearchFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Row> filtered = new ArrayList<>();
            String query = constraint.toString().toLowerCase();

            for(int i = 0; i< backupData.size(); i++) {
                Row r = backupData.get(i);
                if(mFilterSpec.isRowValid(r)) {
                    if (TextUtils.isEmpty(query) || r.getName().toLowerCase().contains(query) ||
                            r.getRealmName().toLowerCase().contains(query)) {
                        filtered.add(r);
                    }
                }
            }

            results.values = filtered;
            results.count = filtered.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            searchData = (List<Row>) results.values;
            initData(searchData);
        }
    }

    private class LeaderboardSortCallback extends SortedList.Callback<Row> {

        @Override
        public boolean areContentsTheSame(Row oldItem, Row newItem) {
            return oldItem.getRanking() == newItem.getRanking() &&
                oldItem.getRating() == newItem.getRating() &&
                oldItem.getSeasonWins() == newItem.getSeasonWins() &&
                oldItem.getSeasonLosses() == newItem.getSeasonLosses();
        }

        @Override
        public boolean areItemsTheSame(Row item1, Row item2) {
            return item1.getRealmId() == item2.getRealmId() &&
                    item1.getName().contentEquals(item2.getName());
        }

        @Override
        public int compare(Row o1, Row o2) {
            if(!isSortAscending) {
                Row temp = o1;
                o1 = o2;
                o2 = temp;
            }
            switch(currentSortBy) {
                case Constants.SORT_BY_RANKING:
                    return o1.getRanking() - o2.getRanking();
                case Constants.SORT_BY_NAME:
                    return o1.getName().compareToIgnoreCase(o2.getName());
                case Constants.SORT_BY_RATING:
                    return o2.getRating() - o1.getRating();
                case Constants.SORT_BY_REALM:
                    return o1.getRealmSlug().compareToIgnoreCase(o2.getRealmSlug());
                case Constants.SORT_BY_SEASON_WINS:
                    return o2.getSeasonWins() - o1.getSeasonWins();
            }
            return -1;
        }

        @Override
        public void onInserted(int position, int count) {
            notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemRangeChanged(position, count);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @InjectView(R.id.row_pvp_rank) TextView rank;
        @InjectView(R.id.row_pvp_name) TextView name;
        @InjectView(R.id.row_pvp_realm) TextView realm;
        @InjectView(R.id.row_pvp_rating) TextView rating;
        @InjectView(R.id.row_pvp_season) TextView season;
        @InjectView(R.id.row_pvp_faction) ImageView faction;
        @InjectView(R.id.row_pvp_race) ImageView race;
        @InjectView(R.id.row_pvp_class) ImageView spec;
        int itemPosition;

        ViewHolder(View parent) {
            super(parent);
            ButterKnife.inject(this, parent);
            parent.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Row row = leaderboardData.get(itemPosition);
            EventBus.getInstance().post(new ClickLeaderboardResponse(row));
        }
    }

    public class ClickLeaderboardResponse {

        private Row rowClicked;

        public ClickLeaderboardResponse(Row warcraftPvpRow) {
            this.rowClicked = warcraftPvpRow;
        }

        public Row getLeaderboardItem() {
            return rowClicked;
        }
    }

}
