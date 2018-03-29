package com.example.user.moveappstage1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Movie;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.user.moveappstage1.Adapter.ImageAdapter;
import com.example.user.moveappstage1.Adapter.Scroller;
import com.example.user.moveappstage1.Background.AsyanTaskLoaderC;
import com.example.user.moveappstage1.Background.LoaderForDB;
import com.example.user.moveappstage1.Database.MovieContract;
import com.example.user.moveappstage1.Database.MovieDBHelper;
import com.example.user.moveappstage1.JsonParse.JsonAnalysis;
import com.example.user.moveappstage1.Modules.Reviews;
import com.example.user.moveappstage1.Modules.Videos;
import com.example.user.moveappstage1.Networking.Network;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements ImageAdapter.OnItemClickListner, AsyanTaskLoaderC.AsyncTaskCompleteListener, LoaderForDB.LoaderBDCompleteListenerDetails {
    ////////DATABASE
    private SQLiteDatabase MOvie_DATABASE;
    private Cursor cursor;
    /////Viewsss
    MenuItem menuItem;
    @BindView(R.id.toolbar_mainactivity)
    Toolbar toolbar;
    GridLayoutManager gridLayoutManager;
    private ImageAdapter adapter;
    @BindView(R.id.tv_item)
    RecyclerView recyclerView;
    @BindView(R.id.probar_loading)
    ProgressBar probar_Loading_for_Data;
    private Menu optionsMenu;
    private int Selected_item = 0;  // to checked any Item has been Checked  // this will contain the id of item
    // APis Links
    private URL APi_Link_Most_Pooular;
    private URL APi_Link_Top_Rated;
    private URL Joker_URl;
    ////////////////////////////////////////////////////////////////////////////////////////
    private Scroller scrollListener;      /// this to get throw pages in Api
    /////////////////  For Knowing Most_Popular || Top_Rated is Selected
    private int Selecteditem = 0; // this will refer to any item has selected for compare
    // final private int Item_Will_Selected = 2;
    //////////////////             on Save Instance State String Keys         //////////////////////////////////////
    private final String Movies_Data_SK = "Get_Movies_From_onSaveInstant";
    private final String SavedLink_SK = "SavedLink";
    private final String Page_States_SK = "Pages_State";
    private final String Selected_Item_S = "Selected_item";
    private final String Selected_Item_Value = "Selected_item_Value";
    private final String Page_Count_SK = "Page_Count";
    private final String ScrollPosition = "ScrollPostion";
    ///////
    private Parcelable savedRecyclerLayoutState;

    //////////////////    Data Comming from Internet //////////
    private ArrayList<Movies> InComingData_Movies;
    private ArrayList<Movies> InComingData_Movies_For_FAvourite;
    private PagesState InComingData_PageState;
    private int PageCount;
    //////Loader
    private final int Loader_ID = 1997;
    private final int LoaderDB = 1991;
    int ScrollPostion;

    //DataStillLoadring   Fovurite
    boolean loading = false;
    ProgressDialog progressDialogl;
    boolean CheckedFavurite = false;
    private ArrayList<ArrayList<Videos>> videos = new ArrayList<>();
    private ArrayList<ArrayList<Reviews>> reviews = new ArrayList<>();
    //
    int CurrentPage = 0;
    /////


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initviews(); // for initait views
        MakeURLS();  //Set up Links
        GetScreenState();
        // Contains Key for all Valuses one Valuse not found means that all values not found
        RemakeValuesFromInsaveInstanceState(savedInstanceState);
        //DataBase
        DataBase();
        //   Log.i("Mario", "OnCreat");

    }

    private void GetScreenState() {
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            scrollListener.SetSpan(2);
        } else {
            scrollListener.SetSpan(4);
        }
    }

    private void DataBase() {
        MovieDBHelper dbHelper = new MovieDBHelper(this);
        MOvie_DATABASE = dbHelper.getReadableDatabase();
        GetFavuoritMovies();
    }

    private void GetFavuoritMovies() {
        loading = true;
        getSupportLoaderManager().initLoader(LoaderDB, null, new LoaderForDB(this, this));
    }

    private void RemakeValuesFromInsaveInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Log.i("Mariooooo", "a7a7");
            InComingData_Movies = new ArrayList<>();
            LoadPageFromLinks(1, 0);
        } else {
            if (savedRecyclerLayoutState != null)
                savedRecyclerLayoutState = savedInstanceState.getParcelable(ScrollPosition);
            gridLayoutManager.onRestoreInstanceState(savedRecyclerLayoutState);
            InComingData_Movies = savedInstanceState.getParcelableArrayList(Movies_Data_SK);
            InComingData_PageState = savedInstanceState.getParcelable(Page_States_SK);
            Selecteditem = savedInstanceState.getInt(Selected_Item_Value);
            Selected_item = savedInstanceState.getInt(Selected_Item_S);
            probar_Loading_for_Data.setVisibility(View.INVISIBLE);
            ReFreshAdapter();
        }
    }

    private boolean CheckInternetConnetion() {
        if (Network.TestNetwork(this)) {
            return true;
        } else {
            Toast.makeText(this, getResources().getString(R.string.No_Internet), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void initviews() {
        /*
        //Views
        toolbar = findViewById(R.id.toolbar_mainactivity);
        probar_Loading_for_Data = findViewById(R.id.probar_loading);
        recyclerView = findViewById(R.id.tv_item);*/
        ButterKnife.bind(this);
        //set up toolbar
        setSupportActionBar(toolbar);
        //Set up LayOutmanager
        gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        /// for refhrehing
        scrollListener = new Scroller(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (Selecteditem != 2) // Prvent Recycler from Lisnter
                    LoadPageFromLinks(page, totalItemsCount);
            }

        };

        recyclerView.addOnScrollListener(scrollListener);
        adapter = new ImageAdapter(MainActivity.this, this);
        recyclerView.setAdapter(adapter);
    }

    private void LoadPageFromLinks(int page, int totalItemsCount) {
        //PagesState state;
        Log.i("Pagggggess_pagessssss", totalItemsCount + "");
        PagesState pagesState = InComingData_PageState;
        if (page == 1) {
            CurrentPage = page;
            URL url = Network.MakeUrlForPages(Network.ApiLinks[Selecteditem], String.valueOf(page));
            Log.i("Pagggggess", url.toString());
            SendUrl(url);
        } else {
            if (page > Integer.valueOf(pagesState.getTotal_pages())) {
                Toast.makeText(this, getResources().getString(R.string.NoMoreFilms), Toast.LENGTH_SHORT).show();
            } else {
                URL url = Network.MakeUrlForPages(Network.ApiLinks[Selecteditem], String.valueOf(page));
                Log.i("PagggggessPagelog", url.toString());
                SendUrl(url);

            }
        }
    }

    private void LoadPageFromLinks(int page, String Linnk) {
        //PagesState state;
        Log.i("Pagggggess_pagessssss", "");

        PagesState pagesState = InComingData_PageState;
        if (page == 1) {

            URL url = Network.MakeUrlForPages(Linnk, String.valueOf(page));
            Log.i("Pagggggess", url.toString());
            SendUrl(url);

        } else {
            if (page > Integer.valueOf(pagesState.getTotal_pages())) {
                Toast.makeText(this, getResources().getString(R.string.NoMoreFilms), Toast.LENGTH_SHORT).show();
            } else {
                CurrentPage = page;
                URL url = Network.MakeUrlForPages(Linnk, String.valueOf(page));
                Log.i("Pagggggess", url.toString());
                SendUrl(url);

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bar, menu);

        // Selected_item is a int resourec of item has been selected before destroying
        // on this coded we checked the ites as checked
        // optionsMenu we will use this value to check whick item i selected on CheckWhichtemSelected() Function
        optionsMenu = menu;
        if (Selected_item != 0) {
            menu.findItem(Selected_item).setChecked(true);
        }

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_Most_Popular:
                CheckedFavurite = false;
                gridLayoutManager.removeAllViews();
                InComingData_Movies = new ArrayList<>();
                Item_MOst_PopularSelceted(item);
                return true;
            case R.id.menu_item_Top_Rated:
                CheckedFavurite = false;
                gridLayoutManager.removeAllViews();
                InComingData_Movies = new ArrayList<>();
                Item_Top_RatedSelceted(item);
                return true;
            case R.id.favorites:
                CheckedFavurite = true;
                Selecteditem = 2;
                gridLayoutManager.removeAllViews();
                InComingData_Movies = new ArrayList<>();
                Item_favuritselected(item);
                return true;
            case R.id.menu_Refersh:
                CheckInternetConnetion();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void Item_favuritselected(MenuItem item) {
        if (loading) {
            Log.i("Loadingf", loading + "");
            progressDialogl = new ProgressDialog(this);
            progressDialogl.setCancelable(false);
            progressDialogl.setTitle("Please Watting");
            progressDialogl.show();
        } else {
            Selecteditem = 2;
            item.setChecked(true);
            gridLayoutManager.removeAllViews();
            RefershForVavorites();
        }

    }

    private void PraperArrays() {
        if (cursor == null)
            return;
        int count = cursor.getCount();
        Log.i("NEw", "New" + "  " + count);

        ArrayList<Movies> movies = new ArrayList<>();
        videos = new ArrayList<>();
        reviews = new ArrayList<>();
        String Title, Relase_Date, Move_Poster, Vote_Avergae, Summry, Movie_ID, Favurite;
        if (cursor.moveToFirst()) {
            do {
                Log.i("NEwmario", "New" + "  " + count);
                Title = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_Title));
                Relase_Date = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RelaseDate));
                Move_Poster = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MoviePoster));
                Vote_Avergae = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VoteAverage));
                Summry = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_Plot_Synopsis));
                Movie_ID = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ID_Movie));
                movies.add(new Movies(Title, Relase_Date, Move_Poster, Vote_Avergae, Summry, Movie_ID));
                if (!cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_Videos)).isEmpty()) {
                    videos.add(MakeVideoesArray(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_Videos))));
                }
                if (!cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_Revieows)).isEmpty()) {
                    reviews.add(MakeReviewArray(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_Revieows))));
                    Log.i("NEwReview", "New" + "  " + cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_Revieows)));
                }
            } while (cursor.moveToNext());
        }
        InComingData_Movies_For_FAvourite = movies;
    }

    private ArrayList<Reviews> MakeReviewArray(String string) {
        ArrayList<Reviews> list = new ArrayList<>();


        if (!string.contains("marionagehpot123") && !string.contains("marionagehpot"))
            return list;
        if (string.contains("marionagehpot") && !string.contains("marionagehpot123")) {
            String Athuority, Content;
            ArrayList<Reviews> reviews = new ArrayList<>();
            Reviews review;
            String[] Cloumns = string.split("marionagehpot");
            Athuority = Cloumns[0];
            Content = Cloumns[1];
            review = new Reviews(Athuority, Content);
            reviews.add(review);

            return reviews;
        } else {


            String[] string1 = string.split("marionagehpot123");
            String Athuority, Content;
            ArrayList<Reviews> reviews = new ArrayList<>();
            Reviews review;
            for (int i = 0; i < string1.length; i++) {
                String[] Cloumns = string1[i].split("marionagehpot");
                Log.i("Cloumns", Cloumns[0].toString());
                Athuority = Cloumns[0];
                Content = Cloumns[1];
                review = new Reviews(Athuority, Content);
                Log.i("GodFathrt", Athuority);
                reviews.add(review);
            }
            return reviews;
        }
    }

    private ArrayList<Videos> MakeVideoesArray(String string) {
        // key/Trailer_name/VideoIamge/VideoLink
        ArrayList<Videos> list = new ArrayList<>();
        if (!string.contains("marionagehpot123") && !string.contains("marionagehpot"))
            return list;
        if (string.contains("marionagehpot") && !string.contains("marionagehpot123")) {

            String key, Trailer_name, VideoIamge, VideoLink;
            ArrayList<Videos> videos = new ArrayList<>();
            Videos videos1;
            String[] Cloumns = string.split("marionagehpot");
            key = Cloumns[0];
            Trailer_name = Cloumns[1];
            VideoIamge = Cloumns[2];
            VideoLink = Cloumns[3];
            videos1 = new Videos(key, Trailer_name, VideoIamge, VideoLink);
            videos.add(videos1);
            return videos;


        } else {


            String[] string1 = string.split("marionagehpot123");
            String key, Trailer_name, VideoIamge, VideoLink;
            ArrayList<Videos> videos = new ArrayList<>();
            Videos videos1;
            for (int i = 0; i < string1.length; i++) {
                String[] Cloumns = string1[i].split("marionagehpot");
                Log.i("Cloumns", Cloumns[0].toString() + "   " + Cloumns.length + "  " + string);

                key = Cloumns[0];
                Trailer_name = Cloumns[1];
                VideoIamge = Cloumns[2];
                VideoLink = Cloumns[3];
                videos1 = new Videos(key, Trailer_name, VideoIamge, VideoLink);
                videos.add(videos1);
            }
            return videos;
        }
    }

    private void Item_MOst_PopularSelceted(MenuItem item) {
        item.setChecked(true);
        Selecteditem = 0;
        LoadPageFromLinks(1, String.valueOf(APi_Link_Most_Pooular));
        ReFreshAdapter();
        scrollListener.remakeLayout();
        recyclerView.addOnScrollListener(scrollListener);
    }

    private void Item_Top_RatedSelceted(MenuItem item) {
        item.setChecked(true);                  // Mark item as checked
        Selecteditem = 1;
        LoadPageFromLinks(1, String.valueOf(APi_Link_Top_Rated));
        ReFreshAdapter();
        scrollListener.remakeLayout();
        recyclerView.addOnScrollListener(scrollListener);
    }

    public void MakeURLS() {
        // Set up the Url
        // and put it into All_Links[] to execute in background
        Network.MakeUrlWithApikey();// for connect with Api Keys
        APi_Link_Most_Pooular = Network.BuildURL(Network.API_LINK_POPULAR);
        APi_Link_Top_Rated = Network.BuildURL(Network.API_LINK_Top_Related);

        // For the Instance State On Roation Mode
        if (Selecteditem == 0) {
            Joker_URl = APi_Link_Most_Pooular;
        } else if (Selecteditem == 1) {
            Joker_URl = APi_Link_Top_Rated;
        }
    }

    private void SendUrl(URL url) {
        Joker_URl = url;
        String Query = "getData";
        android.support.v4.app.LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> GetData = loaderManager.getLoader(Loader_ID);
        Bundle bundle = new Bundle();
        bundle.putString(Query, Joker_URl.toString());
        if (CheckInternetConnetion()) {
            if (GetData == null) {
                Log.i("initLoader", "initLoader");
                loaderManager.initLoader(Loader_ID, bundle, new AsyanTaskLoaderC(this, Query, this, probar_Loading_for_Data));
            } else {
                Log.i("restartLoader", "restartLoader");

                loaderManager.restartLoader(Loader_ID, bundle, new AsyanTaskLoaderC(this, Query, this, probar_Loading_for_Data));
            }
        }
    }


    @Override
    public void onTaskComplete(Object Movies, Object PageState) {
        // For Load More Pages

        //    AddItemsToIncomingdataFroScrolling((ArrayList<Movies>) Movies);
        ArrayList<Movies> movies = (ArrayList<com.example.user.moveappstage1.Movies>) Movies;
        AddItemsToIncomingdataFroScrolling(movies);
        InComingData_PageState = (PagesState) PageState;
        ReFreshAdapter();

    }


    @Override
    public void ONLoaderOFDataBaseComplete(Object Cursor) {
        cursor = (Cursor) Cursor;
        loading = false;
        if (progressDialogl != null)
            progressDialogl.dismiss();
        PraperArrays();
        if (Selecteditem == 2) {
            RefershForVavorites();
        } else {
            ReFreshAdapter();
        }
    }

    void RefershForVavorites() {
        recyclerView.removeOnScrollListener(scrollListener);
        InComingData_Movies = InComingData_Movies_For_FAvourite;
        ReFreshAdapter();
    }


    // this AsyncTask For get Data ON Other Pages
    class GetDataPagesOnOtherThread extends AsyncTask<URL, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            probar_Loading_for_Data.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            try {
                String retunerformHtml = Network.GetDataFromHtml(url);
                return retunerformHtml;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            // stop loading
            probar_Loading_for_Data.setVisibility(View.INVISIBLE);
            ArrayList<Movies> movies = new ArrayList<>();
            if (!s.equals("") && s != null) {
                ////Analysis Json
                movies = JsonAnalysis.parseMoviesJson(s);
                AddItemsToIncomingdataFroScrolling(movies);
            }
        }
    }

    // when Get Data From Scolling we need To add this to Main Data In App
    private void AddItemsToIncomingdataFroScrolling(ArrayList<Movies> movies) {
        for (int i = 0; i < movies.size(); i++) {
            InComingData_Movies.add(movies.get(i));
        }
        ReFreshAdapter();
    }

    //This to Put Values On Adapter
    private void ReFreshAdapter() {
        adapter.setLinks(cursor, InComingData_Movies);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(Movies_Data_SK, InComingData_Movies);
        outState.putParcelable(Page_States_SK, InComingData_PageState); //save PageState
        outState.putString(SavedLink_SK, Joker_URl.toString());
        outState.putInt(Page_Count_SK, PageCount);
        // to Get Which Item Has Selected
        Selected_item = CheckWhichtemSelected();
        outState.putInt(Selected_Item_S, Selected_item);
        outState.putInt(Selected_Item_Value, Selecteditem);

        outState.putParcelable(ScrollPosition, gridLayoutManager.onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }


    // this function return the Resource Id Of Item Has Checked
    public int CheckWhichtemSelected() {
        //get item from OptionsMenu that has refer to Menu
        int Retunner;
        switch (Selecteditem) {
            case 0:
                Retunner = R.id.menu_item_Most_Popular;
                break;
            case 1:
                Retunner = R.id.menu_item_Most_Popular;
                break;
            case 2:
                Retunner = R.id.favorites;
                break;
            default:
                Retunner = 0;
        }
        return Retunner;
    }

    @Override
    public void onItemClicked(int i, Boolean Favurite) {
        for (int ii = 0; ii < InComingData_Movies_For_FAvourite.size(); ii++) {
            if (InComingData_Movies.get(i).getID().contains(InComingData_Movies_For_FAvourite.get(ii).getID())) {
                CheckedFavurite = true;
                break;
            } else {
                CheckedFavurite = false;
            }
        }
        //// On Items Click
        if (CheckedFavurite) {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailActivity.Movies_Key, InComingData_Movies.get(i));
            intent.putExtra(DetailActivity.Movies_Fovurite, Favurite);
            intent.putExtra(DetailActivity.Movies_Fovurite_Checked, CheckedFavurite);
            Log.i("CheckedFavurite", CheckedFavurite + "");
            intent.putExtra(DetailActivity.Movies_Fovurite_Videos, videos.get(i));
            intent.putExtra(DetailActivity.Movies_Fovurite_Reviews, reviews.get(i));
            Log.i("CheckedFavurite", reviews.get(i).size() + "");
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailActivity.Movies_Key, InComingData_Movies.get(i));
            intent.putExtra(DetailActivity.Movies_Fovurite, Favurite);
            Log.i("CheckedFavurite", CheckedFavurite + "");
            intent.putExtra(DetailActivity.Movies_Fovurite_Checked, CheckedFavurite);
            startActivity(intent);

        }
    }
/*
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int orientation = newConfig.orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            scrollListener.SetSpan(2);
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            scrollListener.SetSpan(4);
        }
    }
*/

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Mario", "onPause");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("Mario", "onStart");

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (InComingData_Movies != null)
            loading = true;
        getSupportLoaderManager().restartLoader(LoaderDB, null, new LoaderForDB(MainActivity.this, this));
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
