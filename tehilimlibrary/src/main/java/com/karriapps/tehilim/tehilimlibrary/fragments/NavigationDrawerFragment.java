package com.karriapps.tehilim.tehilimlibrary.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import com.karriapps.tehilim.tehilimlibrary.GeneratorFactory;
import com.karriapps.tehilim.tehilimlibrary.MainActivity;
import com.karriapps.tehilim.tehilimlibrary.R;
import com.karriapps.tehilim.tehilimlibrary.fragments.dialogs.BookmarkDialog;
import com.karriapps.tehilim.tehilimlibrary.fragments.dialogs.YahrzeitDialog;
import com.karriapps.tehilim.tehilimlibrary.generators.TehilimGenerator;
import com.karriapps.tehilim.tehilimlibrary.generators.YahrzeitGenerator;
import com.karriapps.tehilim.tehilimlibrary.model.LastLocation;
import com.karriapps.tehilim.tehilimlibrary.model.Month;
import com.karriapps.tehilim.tehilimlibrary.model.callbacks.MonthSelected;
import com.karriapps.tehilim.tehilimlibrary.utils.App;
import com.karriapps.tehilim.tehilimlibrary.utils.PsalmsHelper;
import com.karriapps.tehilim.tehilimlibrary.utils.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NavigationDrawerFragment extends Fragment implements MonthSelected {

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private static final String TAG = NavigationDrawerFragment.class.getName();

    private DrawerLayout mDrawerLayout;
    private View mFragmentContainerView;
    private ListView mQuickList;
    private ListView mPrayersList;
    private ExpandableListView mBooksList;
    private TextView mDateTextView;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    private TehilimGenerator mGenerator;

    private Month mMonthSelector;
    private BookmarkDialog mBookmarksDialog;

    private ArrayAdapter<String> mQuickAdapter;
    private ArrayAdapter<String> mPrayersAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.side_panel, container, false);

        mBooksList = (ExpandableListView) view.findViewById(R.id.books_list);
        mPrayersList = (ListView) view.findViewById(R.id.additional_list);
        mQuickList = (ListView) view.findViewById(R.id.quick_list);
        mDateTextView = (TextView) view.findViewById(R.id.date_text_view);

        populateLists();

        Tools.updateListViewHeight(mBooksList);
        Tools.updateListViewHeight(mPrayersList);
        Tools.updateListViewHeight(mQuickList);
//        mBooksList.getAdapter().getView(1, null, mBooksList).setOnTouchListener(mOnTouchListener);

        mDateTextView.setText(
                App.getInstance().getHebrewDateFormatter().format(App.getInstance().getJewishCalendar())
                        + " - " + App.getInstance().getHebrewDateFormatter().formatDayOfWeek(App.getInstance().getJewishCalendar())
        );

        // Select either the default item (0) or the last selected item.
//        selectItem(mQuickList, mCurrentSelectedPosition);

        return view;
    }

    OnTouchListener mOnTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        }
    };

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    public void closeDrawer() {
        if (mDrawerLayout != null)
            mDrawerLayout.closeDrawers();
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        if (drawerLayout == null)
            return;
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.drawable.ic_drawer,             /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                ((MainActivity) getActivity()).restoreActionBar();
                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);

//        if (!mFromSavedInstanceState) {
//            mGenerator = new PsalmsGenerator(1, 151, 1, 23);
//            mCallbacks.onNavigationDrawerItemSelected(mGenerator, getString(R.string.all_tehilim));
//        }
    }

    private void populateLists() {
        String[] quickValues = new String[]{
                getString(R.string.last_position),
                String.format(getString(R.string.tehilim_for_day), App.getInstance().getHebrewDateFormatter().formatDayOfWeek(App.getInstance().getJewishCalendar())),
                String.format(getString(R.string.tehilim_for_day), App.getInstance().getHebrewDateFormatter().formatDayOfMonth(App.getInstance().getJewishCalendar())),
                getString(R.string.all_tehilim)
                //,getString(R.string.bookmarks)
        };
        mQuickAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, quickValues);
        mQuickList.setAdapter(mQuickAdapter);
        mQuickList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long arg3) {
                if (mQuickAdapter.getItem(position).equals(getString(R.string.last_position))) {
                    LastLocation loc = App.getInstance().getLastLocation();
                    switch (loc.getGeneratorType()) {
                        case CHAPTERS:
                            mGenerator = GeneratorFactory.createGeneratorFactory().getGenerator(loc.getValues());
                            mCallbacks.onNavigationDrawerItemSelected(mGenerator, loc.getName(), loc.getPosition());
                            break;
                        case NAME:
                            mGenerator = GeneratorFactory.createGeneratorFactory().getGenerator(loc.getName());
                            mCallbacks.onNavigationDrawerItemSelected(mGenerator, loc.getName(), loc.getPosition());
                            break;
                        case YAHRZEIT:
                            mGenerator = new YahrzeitGenerator(Tools.convertIntegersToList(loc.getValues()));
                            mCallbacks.onNavigationDrawerItemSelected(mGenerator, loc.getName(), loc.getPosition());
                            break;
                    }
                } else if (mQuickAdapter.getItem(position).equals(getString(R.string.all_tehilim))) {
                    mGenerator = GeneratorFactory.createGeneratorFactory().getGenerator(1, 151, 1, 23);
                    mCallbacks.onNavigationDrawerItemSelected(mGenerator, ((TextView) view).getText().toString());
                } else if (position == 1) {
                    setTehilimForDay(App.getInstance().getJewishCalendar().getDayOfWeek() - 1);
                    mCallbacks.onNavigationDrawerItemSelected(mGenerator, ((TextView) view).getText().toString());
                } else if (position == 2) {
                    OnMonthSelected(App.getInstance().getJewishCalendar().getJewishDayOfMonth());
                } else if (mQuickAdapter.getItem(position).equals(getString(R.string.bookmarks))) {
                    if (getResources().getBoolean(R.bool.tablet)) {
                        BookmarkDialog.newInstance(mCallbacks).show(getFragmentManager(), TAG);
                    } else {
                        BookmarkDialog frag = new BookmarkDialog();
                        frag.setCallbacks(mCallbacks);
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        transaction.add(R.id.tehilim_fragment_layout, frag)
                                .show(frag)
                                .addToBackStack(null)
                                .commit();
                    }
                }
            }
        });

        String[] prayersValues = new String[]{
                getString(R.string.sickTitle),
                getString(R.string.shiraTitle),
                getString(R.string.tikunKlaliTitle),
                getString(R.string.yahrzeitTitle)
        };

        mPrayersAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, prayersValues);
        mPrayersList.setAdapter(mPrayersAdapter);
        mPrayersList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long arg3) {
                String title = mPrayersAdapter.getItem(position);
                if (title.equals(getString(R.string.yahrzeitTitle))) {
                    YahrzeitDialog dialog = new YahrzeitDialog();
                    dialog.setCallback(mCallbacks);
                    dialog.show(getFragmentManager(), "yahrzeit");
                    return;
                }
                mGenerator = GeneratorFactory.createGeneratorFactory().getGenerator(title);
                mCallbacks.onNavigationDrawerItemSelected(mGenerator, title);
            }
        });

        SimpleExpandableListAdapter tehilimList = new SimpleExpandableListAdapter(getActivity(),
                createBooksGroups(),
                R.layout.expandable_list_item,
                R.layout.expandable_list_item,
                new String[]{"Group Item"},
                new int[]{android.R.id.text1},
                createChildGroups(),
                android.R.layout.simple_list_item_1,
                android.R.layout.simple_list_item_1,
                new String[]{"Sub Item"},
                new int[]{android.R.id.text1});
        mBooksList.setAdapter(tehilimList);
        mBooksList.setOnGroupClickListener(new OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {

                if (groupPosition == 2) {
                    mMonthSelector = new Month();
                    mMonthSelector.setListener(NavigationDrawerFragment.this);
                    mMonthSelector.show(getActivity().getSupportFragmentManager(), "month");
                    if (mDrawerLayout != null) {
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                    }
                    return true;
                }

                Tools.setListViewHeight(parent, groupPosition);

                return false;
            }
        });

        mBooksList.setOnChildClickListener(new OnChildClickListener() {
            @SuppressWarnings("unchecked")
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                /** Books List **/
                if (groupPosition == 0) {
                    switch (childPosition) {
                        case 0: //First Book
                            mGenerator = GeneratorFactory.createGeneratorFactory().getGenerator(1, PsalmsHelper.BOOK_TWO_START, 1, 23);
                            break;
                        case 1: //Second Book
                            mGenerator = GeneratorFactory.createGeneratorFactory().getGenerator(PsalmsHelper.BOOK_TWO_START, PsalmsHelper.BOOK_THREE_START, 0, 0);
                            break;
                        case 2: //Third Book
                            mGenerator = GeneratorFactory.createGeneratorFactory().getGenerator(PsalmsHelper.BOOK_THREE_START, PsalmsHelper.BOOK_FOUR_START, 0, 0);
                            break;
                        case 3: //Fourth Book
                            mGenerator = GeneratorFactory.createGeneratorFactory().getGenerator(PsalmsHelper.BOOK_FOUR_START, PsalmsHelper.BOOK_FIVE_START, 0, 0);
                            break;
                        case 4: //Fifth Book
                            mGenerator = GeneratorFactory.createGeneratorFactory().getGenerator(PsalmsHelper.BOOK_FIVE_START, 151, 1, 23);
                            break;
                    }
                }

                /** Days List **/
                if (groupPosition == 1) {
                    setTehilimForDay(childPosition);
                }
                if (mCallbacks != null) {
                    int selectedPostion = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition));
                    mCallbacks.onNavigationDrawerItemSelected(mGenerator, ((Map<String, String>) mBooksList.getItemAtPosition(selectedPostion)).get("Sub Item"));
                }
                return false;
            }
        });
    }

    protected void setTehilimForDay(int dayInWeak) {
        switch (dayInWeak) {
            case 0: //First Day
                mGenerator = GeneratorFactory.createGeneratorFactory().getGenerator(1, PsalmsHelper.DAY_TWO_START, 0, 0);
                break;
            case 1: //Second Day
                mGenerator = GeneratorFactory.createGeneratorFactory().getGenerator(PsalmsHelper.DAY_TWO_START, PsalmsHelper.DAY_THREE_START, 0, 0);
                break;
            case 2: //Third Day
                mGenerator = GeneratorFactory.createGeneratorFactory().getGenerator(PsalmsHelper.DAY_THREE_START, PsalmsHelper.DAY_FOUR_START, 0, 0);
                break;
            case 3: //Fourth Day
                mGenerator = GeneratorFactory.createGeneratorFactory().getGenerator(PsalmsHelper.DAY_FOUR_START, PsalmsHelper.DAY_FIVE_START, 0, 0);
                break;
            case 4: //Fifth Day
                mGenerator = GeneratorFactory.createGeneratorFactory().getGenerator(PsalmsHelper.DAY_FIVE_START, PsalmsHelper.DAY_SIX_START, 0, 0);
                break;
            case 5: //Sixth Day
                mGenerator = GeneratorFactory.createGeneratorFactory().getGenerator(PsalmsHelper.DAY_SIX_START, PsalmsHelper.DAY_SEVEN_START, 1, 23);
                break;
            case 6: //Seventh Day
                mGenerator = GeneratorFactory.createGeneratorFactory().getGenerator(PsalmsHelper.DAY_SEVEN_START, 151, 0, 0);
                break;
        }
    }

    private List<HashMap<String, String>> createBooksGroups() {

        ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> group = new HashMap<String, String>();
        group.put("Group Item", getString(R.string.books_title));
        result.add(group);
        group = new HashMap<String, String>();
        group.put("Group Item", getString(R.string.week_title));
        result.add(group);
        group = new HashMap<String, String>();
        group.put("Group Item", getString(R.string.month_title));
        result.add(group);

        return result;
    }

    private List<ArrayList<HashMap<String, String>>> createChildGroups() {
        ArrayList<ArrayList<HashMap<String, String>>> result = new ArrayList<ArrayList<HashMap<String, String>>>();

        String[] books = getResources().getStringArray(R.array.books);
        HashMap<String, String> child;
        ArrayList<HashMap<String, String>> secList = new ArrayList<HashMap<String, String>>();
        for (String book : books) {
            child = new HashMap<String, String>();
            child.put("Sub Item", book);
            secList.add(child);
        }
        result.add(secList);

        secList = new ArrayList<HashMap<String, String>>();
        String[] days = getResources().getStringArray(R.array.week_days);
        for (String day : days) {
            child = new HashMap<String, String>();
            child.put("Sub Item", day);
            secList.add(child);
        }
        result.add(secList);

        return result;
    }

    @Override
    public void OnMonthSelected(int day) {
        if (mMonthSelector != null && mMonthSelector.isVisible())
            mMonthSelector.dismiss();
        mGenerator = GeneratorFactory.createGeneratorFactory().getGenerator(App.getInstance().getPsalms().getMonthPsalm(day),
                App.getInstance().getPsalms().getMonthLastPsalm(day),
                App.getInstance().getPsalms().getMonthKufYudPsalm(day),
                App.getInstance().getPsalms().getMonthLastKufYudPsalm(day));
        if (mCallbacks != null)
            mCallbacks.onNavigationDrawerItemSelected(mGenerator, String.format(getString(R.string.dayInMonth), day));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && isDrawerOpen()) {
            inflater.inflate(R.menu.global, menu);
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle != null && mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.app_name);
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(TehilimGenerator generator, String title);

        void onNavigationDrawerItemSelected(TehilimGenerator generator, String title, int position);
    }
}
