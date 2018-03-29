package com.example.user.moveappstage1;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Adapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.example.user.moveappstage1.Adapter.ImageAdapter;
import com.example.user.moveappstage1.Adapter.ReviewAdapter;
import com.example.user.moveappstage1.Adapter.VideoAdapter;
import com.example.user.moveappstage1.Background.AsyanTaskLoaderC;
import com.example.user.moveappstage1.Background.AsyanTaskLoaderDetails;
import com.example.user.moveappstage1.Database.MovieContract;
import com.example.user.moveappstage1.Database.MovieDBHelper;
import com.example.user.moveappstage1.JsonParse.JsonAnalysis;
import com.example.user.moveappstage1.Modules.Reviews;
import com.example.user.moveappstage1.Modules.Videos;
import com.example.user.moveappstage1.Networking.Network;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements AsyanTaskLoaderDetails.AsyncTaskCompleteListenerDetails, VideoAdapter.OnItemClickListner {
    @BindView(R.id.DeatailSCroll)
    NestedScrollView scrollView;
    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;
    @BindView(R.id.probar_loading_ViedowDeatilng)
    ProgressBar mLoadingIndicator_ViedowDeatilng;
    @BindView(R.id.probar_loading_ReviewsDeatilng)
    ProgressBar mLoadingIndicator_ReviewsDeatilng;
    @BindView(R.id.Poster_Tv)
    ImageView Poster_tv;
    @BindView(R.id.favourit_btn)
    ImageView ImageView;
    @BindView(R.id.tv_title_txt)
    TextView Title_txt;
    @BindView(R.id.rate_tv_txt)
    TextView vote_avarage_txt;
    @BindView(R.id.tv_Relase_Date)
    TextView relase_data_txt;
    @BindView(R.id.tv_over_view)
    TextView over_View_txt;
    @BindView(R.id.ratingd_bar)
    RatingBar ratingBar;
    @BindView(R.id.tv_item_Videow)
    RecyclerView ViedoRecycler;
    @BindView(R.id.tv_item_Reviews)
    RecyclerView ReviewRecyler;
    GridLayoutManager layoutManager_Video, gridLayoutManager_Recycler;
    private VideoAdapter Video_adapter;
    private ReviewAdapter Review_adapter;
    public static final String Movies_Key = "Movies_Key";
    public static final String Movies_Fovurite = "Fovurite";
    public static final String Movies_Fovurite_Checked = "Fovuritechecked";
    public static final String Movies_Fovurite_Videos = "FovuriteVideos";
    public static final String Movies_Fovurite_Reviews = "FovuriteReviews";
    public static final String Fovurite_Button = "FavuriteButton";

    private static final int Loader_ID = 1995;
    public static String Poster_Image_V, Title_V, Release_Date_V, Vote_Average_V, Plot_Synopsis_V;
    public static String ID_V;
    public static String Api_URL_Vides;
    public static String Api_URL_Review;
    public static Boolean Checked_Favurite;

    public static ArrayList<String> urls = new ArrayList<>();
    private ArrayList<Reviews> InComingData_Reviews;
    private ArrayList<Videos> InComingData_Videos;
    private ArrayList<String> DString;
    ///for save instantstate
    private static final String GET_REVIews = "REviews";
    private static final String GET_Videos = "Videos";
    Boolean Favurite = false;
    ///if btn favourtie seclted
    boolean btn_Selcted = false;
    static ProgressDialog progressDialogl;

    //OnSave INstant State
    private Parcelable savedRecyclerLayoutState_review;
    private static final String BUNDLE_RECYCLER_LAYOUT_review = "recycler_layoutreview";

    private Parcelable savedRecyclerLayoutState_videos;
    private static final String BUNDLE_RECYCLER_LAYOUT_videos = "recycler_layout_videos";

    private static final String Main_Scroll = "Main_Scroll_View";
    //
    ///DataBASe
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Log.i("RECreatit", "Mario");
        initviews();
        iniToolbar();
        checkForIntentcoming();
        MakeUrls();
        if (savedInstanceState != null) {
            btn_Selcted = savedInstanceState.getBoolean(Fovurite_Button);
            final int[] position = savedInstanceState.getIntArray(Main_Scroll);
            if (position != null)
                scrollView.post(new Runnable() {
                    public void run() {
                        scrollView.scrollTo(position[0], position[1]);
                    }
                });
            if (savedRecyclerLayoutState_review != null)
                gridLayoutManager_Recycler.onRestoreInstanceState(savedRecyclerLayoutState_review);
            if (savedRecyclerLayoutState_videos != null)
                layoutManager_Video.onRestoreInstanceState(savedRecyclerLayoutState_videos);

            if (btn_Selcted) {
                InComingData_Videos = savedInstanceState.getParcelableArrayList(GET_Videos);
                InComingData_Reviews = savedInstanceState.getParcelableArrayList(GET_REVIews);
                Makeadapter();
            }

            getSupportLoaderManager().initLoader(Loader_ID, null, new AsyanTaskLoaderDetails(this, "", this, mLoadingIndicator_ViedowDeatilng, mLoadingIndicator_ReviewsDeatilng, ImageView));

        } else {
            GetDatainBackground();
        }
        DataBase();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        savedRecyclerLayoutState_review = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT_review);
        savedRecyclerLayoutState_videos = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT_videos);
    }

    private void SelectButton() {
        if (btn_Selcted) {
            ImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_favoruti_checked));
        } else {
            ImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_favoirut_unchecked));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SelectButton();
        if (DString != null) {
            Log.i("Mario", DString.size() + "");
        }

    }

    private void DataBase() {
        MovieDBHelper dbHelper = new MovieDBHelper(this);
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }

    private void GetDatainBackground() {

        if (Checked_Favurite) {
            Makeadapter();
        } else {
            String Query = "getData";
            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<String> GetData = loaderManager.getLoader(Loader_ID);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(Query, urls);
            if (Network.TestNetwork(this)) {
                if (GetData == null) {
                    loaderManager.initLoader(Loader_ID, bundle, new AsyanTaskLoaderDetails(this, Query, this, mLoadingIndicator_ViedowDeatilng, mLoadingIndicator_ReviewsDeatilng, ImageView));
                } else {
                    loaderManager.restartLoader(Loader_ID, bundle, new AsyanTaskLoaderDetails(this, Query, this, mLoadingIndicator_ViedowDeatilng, mLoadingIndicator_ReviewsDeatilng, ImageView));
                }
            }
        }
    }


    private void MakeUrls() {
        Api_URL_Vides = Network.MakeUrlToReviewOrViews(ID_V, Constats.Videos);
        Api_URL_Review = Network.MakeUrlToReviewOrViews(ID_V, Constats.Reviews);
        urls = new ArrayList<>();
        urls.add(Api_URL_Review);
        urls.add(Api_URL_Vides);

    }

    private void checkForIntentcoming() {
        Intent intent = getIntent();
        if (intent != null) {
            Movies movies = intent.getParcelableExtra(Movies_Key);
            Favurite = intent.getBooleanExtra(Movies_Fovurite, false);
            Poster_Image_V = movies.getMovie_Poster();
            Title_V = movies.getTitle();
            Release_Date_V = movies.getRelase_Date();
            Vote_Average_V = movies.getVote_average();
            Plot_Synopsis_V = movies.getPlot_synopsis();
            ID_V = movies.getID();
            Checked_Favurite = intent.getBooleanExtra(Movies_Fovurite_Checked, false);
            btn_Selcted = Checked_Favurite;
            SelectButton();
            if (Checked_Favurite) {
                InComingData_Reviews = intent.getParcelableArrayListExtra(Movies_Fovurite_Reviews);
                InComingData_Videos = intent.getParcelableArrayListExtra(Movies_Fovurite_Videos);
                Log.i("InComingData_Videos", InComingData_Videos.size() + "");
            }
            PutValuesInViews();
        }
    }

    private void PutValuesInViews() {
        toolbar.setTitle(Title_V);
        Picasso.with(this).load(Poster_Image_V).into(Poster_tv);
        Title_txt.setText(Title_V);
        vote_avarage_txt.setText(Vote_Average_V);
        if (Float.valueOf(Vote_Average_V) > 10)
            ratingBar.setRating(10);
        else {
            float value = Float.valueOf(Vote_Average_V) * .5f;
            ratingBar.setRating(value);
        }
        relase_data_txt.setText(FormattingDate(Release_Date_V));
        over_View_txt.setText(Plot_Synopsis_V);


        if (Favurite) {
            ImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_favoruti_checked));
        } else {
            ImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_favoirut_unchecked));
        }
    }

    private void iniToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initviews() {
/*
        mLoadingIndicator_ViedowDeatilng = findViewById(R.id.probar_loading_ViedowDeatilng);
        mLoadingIndicator_ReviewsDeatilng = findViewById(R.id.probar_loading_ReviewsDeatilng);
        toolbar = findViewById(R.id.toolbar);
        Poster_tv = findViewById(R.id.Poster_Tv);
        Title_txt = findViewById(R.id.tv_title_txt);
        vote_avarage_txt = findViewById(R.id.rate_tv_txt);
        ratingBar = findViewById(R.id.ratingd_bar);
        relase_data_txt = findViewById(R.id.tv_Relase_Date);
        over_View_txt = findViewById(R.id.tv_over_view);
        ImageView = findViewById(R.id.favourit_btn);
        ViedoRecycler = findViewById(R.id.tv_item_Videow);
        ReviewRecyler = findViewById(R.id.tv_item_Reviews);*/
        ButterKnife.bind(this);
        layoutManager_Video = new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);
        ViedoRecycler.setLayoutManager(layoutManager_Video);
        ViedoRecycler.setHasFixedSize(true);
        Video_adapter = new VideoAdapter(this, this);
        ViedoRecycler.setAdapter(Video_adapter);


        gridLayoutManager_Recycler = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        ReviewRecyler.setLayoutManager(gridLayoutManager_Recycler);
        ReviewRecyler.setHasFixedSize(true);
        Review_adapter = new ReviewAdapter(this);
        ReviewRecyler.setAdapter(Review_adapter);

        ReviewRecyler.setNestedScrollingEnabled(false);

    }

    private void Makeadapter() {
        if (InComingData_Videos != null) {
            Video_adapter.setLinks(InComingData_Videos);
            Review_adapter.setLinks(InComingData_Reviews);
            Log.i("REviewwww", InComingData_Reviews.size() + "");
        }
    }

    private String FormattingDate(String Date) {
        if (Date != "") {
            StringBuilder string_date = new StringBuilder();
            String[] splitsarray = Date.split("-");
            switch (splitsarray[1]) {
                case "01":
                    string_date.append("Jan");
                    break;
                case "02":
                    string_date.append("Feb");

                    break;
                case "03":
                    string_date.append("Mar");
                    break;
                case "04":
                    string_date.append("Apr");
                    break;
                case "05":
                    string_date.append("May");
                    break;
                case "06":
                    string_date.append("Jun");
                    break;
                case "07":
                    string_date.append("Jul");
                    break;
                case "08":
                    string_date.append("Aug");
                    break;
                case "09":
                    string_date.append("Sep");
                    break;
                case "10":
                    string_date.append("Oct");
                    break;
                case "11":
                    string_date.append("Nov");
                    break;
                case "12":
                    string_date.append("Dec");
                    break;
                default:
                    break;
            }
            string_date.append(" ");
            string_date.append(splitsarray[2]);
            string_date.append(",");
            string_date.append(splitsarray[0]);

            return string_date.toString();
        }
        return "";
    }

    private Target MakeTarget(final View view) {
        //                   important here

        //in this method i intened to get the background of toolbar from json
        //but i get probelm that i want to display home button and title on this
        //the probelm is that the background have many colors
        //this willn't display the homebutton and title when background is the same color
        //soo i make fixed photo
        //if you have any idea tell me in review thanks

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                // Bitmap b = Bitmap.createScaledBitmap(bitmap,50,50,false);
                BitmapDrawable icon = new BitmapDrawable(view.getResources(), bitmap);
                toolbar.setBackground(icon);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };
        return target;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_Share:
                Sharevideo();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onTaskComplete(Object Reviews, Object Videos) {
        InComingData_Reviews = (ArrayList<com.example.user.moveappstage1.Modules.Reviews>) Reviews;
        InComingData_Videos = (ArrayList<com.example.user.moveappstage1.Modules.Videos>) Videos;
        Makeadapter();
    }

    @Override
    public void onItemClicked(int i) {
        String videoId = InComingData_Videos.get(i).getKey();

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
        intent.putExtra("VIDEO_ID", videoId);
        startActivity(intent);
    }

    public void Favourit(View view) {
        if (Favurite) {
            ImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_favoirut_unchecked));
            Favurite = false;
            un_Favourite(ID_V);
            btn_Selcted = false;
        } else {
            ImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_favoruti_checked));
            Favurite = true;
            Add_Favourite();
            btn_Selcted = true;

        }
    }

    public void un_Favourite(String ID) {
        Uri uri = MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(ID).build();
        getContentResolver().delete(uri, null, null);
    }

    public void Add_Favourite() {
        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry.COLUMN_ID_Movie, ID_V);
        values.put(MovieContract.MovieEntry.COLUMN_Title, Title_V);
        values.put(MovieContract.MovieEntry.COLUMN_RelaseDate, Release_Date_V);
        values.put(MovieContract.MovieEntry.COLUMN_MoviePoster, Poster_Image_V);
        values.put(MovieContract.MovieEntry.COLUMN_VoteAverage, Vote_Average_V);
        values.put(MovieContract.MovieEntry.COLUMN_Plot_Synopsis, Plot_Synopsis_V);
        ////

        if (InComingData_Videos.size() > 0) {
            values.put(MovieContract.MovieEntry.COLUMN_Videos, PrapreVideoString(InComingData_Videos));
        } else {
            values.put(MovieContract.MovieEntry.COLUMN_Videos, " ");
        }
        if (InComingData_Reviews.size() > 0) {
            values.put(MovieContract.MovieEntry.COLUMN_Revieows, PrapreReviewsString(InComingData_Reviews));
            Log.i("InComingData_Reviews", PrapreReviewsString(InComingData_Reviews));
        } else {
            values.put(MovieContract.MovieEntry.COLUMN_Revieows, " ");
        }
        Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, values);

    }

    String PrapreVideoString(ArrayList<Videos> videoes) {
        //Fromate in DataBase       key/Trailer_name/VideoIamge/VideoLink,key/Trailer_name/VideoIamge/VideoLink
        StringBuilder builder = new StringBuilder();
        for (Videos video : videoes) {
            builder.append(video.getKey() + "marionagehpot");
            builder.append(video.getTrailer_name() + "marionagehpot");
            builder.append(video.getVideoIamge() + "marionagehpot");
            builder.append(video.getVideoLink());
            builder.append("marionagehpot123");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    String PrapreReviewsString(ArrayList<Reviews> reviews) {
        //Fromate in DataBase       Author/Content, Author/Content
        StringBuilder builder = new StringBuilder();
        for (Reviews reviews1 : reviews) {
            builder.append(reviews1.getAuthor() + "marionagehpot");
            builder.append(reviews1.getContent());
            builder.append("marionagehpot123");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(GET_REVIews, InComingData_Reviews);
        outState.putParcelableArrayList(GET_Videos, InComingData_Videos);
        outState.putBoolean(Fovurite_Button, btn_Selcted);
        //   outState.putIntArray(Main_Scroll,new int[]{ scrollView.getScrollX(), scrollView.getScrollY()});
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT_review, gridLayoutManager_Recycler.onSaveInstanceState());
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT_videos, layoutManager_Video.onSaveInstanceState());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }


    private void Sharevideo() {
        if (InComingData_Videos.size() > 0) {
            String type = "text/plain";
            String title = Title_V + " Trailer";
            String share = InComingData_Videos.get(0).getVideoLink() + "  --->  This Share By Movie App [Mario Nageh]";
            Intent shareIntent = ShareCompat.IntentBuilder.from(this).setChooserTitle(title).setType(type).setText(share).getIntent();
            if (shareIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(shareIntent);
            }
        }
    }


}
