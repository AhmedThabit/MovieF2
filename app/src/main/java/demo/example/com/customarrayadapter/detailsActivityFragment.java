package demo.example.com.customarrayadapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class detailsActivityFragment extends Fragment {
    String testId;
    Intent extras;
    MyMovie fetchMovieDetails = new MyMovie();

    //Trailer Varibles
    public ArrayList<Trailer> myTrailer = new ArrayList<Trailer>();
    private TrailerAdapter trailerAdapter;
    ListView listView;
    TrailerMovieTask trailerMovieTask = new TrailerMovieTask();

    //Review Variables
    public ArrayList<Review> myReview = new ArrayList<Review>();
    private ReviewAdapter mReviewAdapter;
    ListView reviewListView;
    ReviewMovieTask reviewMovieTask = new ReviewMovieTask();


    //Favorite
    CheckBox checkBox;
    int i = 0;
    static final String DEFAULT = "N/A";

    public void save() {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyFavorites", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //editor.putString("f" + i, fetchMovieDetails.getId());
        //editor.putInt("maxCount", i);
        //sharedPreferences.getAll();
        editor.putString(fetchMovieDetails.getId(), "true");
        i++;
        //sharedPreferences.getAll();
        editor.commit();
    }

    public void load() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyFavorites", Context.MODE_PRIVATE);
        //Log.v("***----", sharedPreferences.getString(fetchMovieDetails.getId(), DEFAULT));
        String id = sharedPreferences.getString(fetchMovieDetails.getId(), DEFAULT);

        if (id.equals(DEFAULT)) {

        } else {
            checkBox.setChecked(true);
        }
    }
    public void remove(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyFavorites", Context.MODE_PRIVATE);
        SharedPreferences.Editor  editor=sharedPreferences.edit();
        editor.remove(fetchMovieDetails.getId());
        editor.commit();
    }

    public detailsActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (fetchMovieDetails.getId() == null) {
            extras = getActivity().getIntent();
        }
        //Trailer onStart()
        //   TrailerMovieTask trailerMovieTask = new TrailerMovieTask();
        // trailerMovieTask.execute(fetchMovieDetails.getId());//"12708" //34673


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Context context = getActivity();
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        extras = getActivity().getIntent();

        //Log.v("----getID",fetchMovieDetails.getId().toString());
        fetchMovieDetails.setTitle(extras.getStringExtra("tit"));
        fetchMovieDetails.setImage(extras.getStringExtra("pic"));
        fetchMovieDetails.setRelease_date(extras.getStringExtra("date"));
        fetchMovieDetails.setVote_average(extras.getStringExtra("vote"));
        fetchMovieDetails.setOverView(extras.getStringExtra("overView"));
        fetchMovieDetails.setId(extras.getStringExtra("id"));

        //Trailer execute
        trailerMovieTask.execute(fetchMovieDetails.getId());

        //Review execute
        reviewMovieTask.execute(fetchMovieDetails.getId());

        //Favorite
        //load(fetchMovieDetails.getId());

        TextView titletextView = (TextView) view.findViewById(R.id.titleTextView);
        titletextView.setText(fetchMovieDetails.getTitle());

        ImageView imageView = (ImageView) view.findViewById(R.id.PosterImageView);
        Picasso.with(context)
                .load(fetchMovieDetails.getImage())
                .into(imageView);

        TextView rleaseDate = (TextView) view.findViewById(R.id.rleaseDateTextView);
        rleaseDate.setText(fetchMovieDetails.getRelease_date());

        TextView vote = (TextView) view.findViewById(R.id.voteAvarageTextView);
        vote.setText(fetchMovieDetails.getVote_average());

        TextView overViewTextView = (TextView) view.findViewById(R.id.overViewTextView);
        overViewTextView.setText(fetchMovieDetails.getOverView());
        //Trailer onCreateView
        trailerAdapter = new TrailerAdapter(getActivity(), myTrailer);
        //Review onCreateView
        mReviewAdapter = new ReviewAdapter(getActivity(), myReview);


        // Get a reference to the ListView, and attach this adapter to it.
        listView = (ListView) view.findViewById(R.id.listview_trialer);
        listView.setAdapter(trailerAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intentYou = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + myTrailer.get(i).getKey()));
                if (intentYou.resolveActivity(getActivity().getPackageManager()) != null)
                    startActivity(intentYou);
            }
        });

        // Get a reference to the ListView, and attach this adapter to it.
        reviewListView = (ListView) view.findViewById(R.id.listview_review);
        reviewListView.setAdapter(mReviewAdapter);
        // reviewListView.setScrollContainer(false);
        reviewListView.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });


        checkBox = (CheckBox) view.findViewById(R.id.checkBox_favorite);
        //checkBox.setChecked(true);load();
        load();
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    save();
                    Toast.makeText(getActivity(), "Added to favorite :)", Toast.LENGTH_SHORT).show();
                }
                if (((CheckBox) v).isChecked() == false) {
                    remove();
                    Toast.makeText(getActivity(), "un favorite :(", Toast.LENGTH_SHORT).show();
                }
            }

        });


        return view;

    }

    //----Start--- TrailerMovieTask ----/------/*----------

    public class TrailerMovieTask extends AsyncTask<String, Void, Void> {

        private final String LOG_TAG = TrailerMovieTask.class.getSimpleName();

        /*
        //start test
         */
        //get url of images (done)
        private ArrayList<Trailer> getMovieDataFromJson(String movieJsonStrr)
                throws JSONException

        {

            ArrayList<Trailer> res = new ArrayList<Trailer>();
            // These are the names of the JSON objects that need to be extracted.
            final String OWM_RESULTS = "results";

            JSONObject movieJson = new JSONObject(movieJsonStrr);
            JSONArray movieArray = movieJson.getJSONArray(OWM_RESULTS);

            for (int i = 0; i < movieArray.length(); i++) {

                Trailer img = new Trailer();
                JSONObject oneMovieData = movieArray.getJSONObject(i);

                //set values in image from json
                img.setImage("http://img.youtube.com/vi/" + oneMovieData.getString("key") + "/0.jpg");
                int ii = i + 1;
                img.setVersionName(oneMovieData.getString("type") + " " + ii);
                img.setVersionNumber(oneMovieData.getString("name"));
                img.setKey(oneMovieData.getString("key"));

                res.add(img);
            }


            return res;
        }
        /*
        //end test
         */


        //connect to url
        //read respons as JSON then save it by movieJsonStr
        @Override
        protected Void doInBackground(String... Params) {

            /*
            JSON
            */
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;

            try {

                final String BASE_URL = "http://api.themoviedb.org/3/movie/" + Params[0] + "/videos";
                final String API_KEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY_PARAM, "31b7afd5b799003e694992042035d57c")
                        .build();

                URL url = new URL(builtUri.toString());

                Log.v("Built URI", builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieJsonStr = buffer.toString();
                Log.v("movie JSON String : ", movieJsonStr);

            } catch (IOException e) {
                Log.e("MainActivityFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            }

            try {
                myTrailer = getMovieDataFromJson(movieJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("MainActivityFragment", "Error closing stream", e);
                    }
                }

            }
            return null;
        }


        @Override
        protected void onPostExecute(Void a) {
            if (myTrailer != null) {
                trailerAdapter.clear();
                for (int i = 0; i < myTrailer.size(); i++) {
                    trailerAdapter.add(myTrailer.get(i));
                    Log.v("----Ex", "-----" + i);
                }
                listView.setAdapter(trailerAdapter);
                trailerAdapter.notifyDataSetChanged();

            }
        }
    }

    //-----End-- TrailerMovieTask ----/------/*----------


    //Start ---- ReviewMovieTask -----


    public class ReviewMovieTask extends AsyncTask<String, Void, Void> {

        private final String LOG_TAG = ReviewMovieTask.class.getSimpleName();

        /*
        //start test
         */
        //get url of images (done)
        private ArrayList<Review> getReviewDataFromJson(String movieReviewJsonStrr)
                throws JSONException {

            ArrayList<Review> res = new ArrayList<Review>();
            // These are the names of the JSON objects that need to be extracted.
            final String OWM_RESULTS = "results";
            final String AUTHOR = "author";
            final String CONTENT = "content";

            JSONObject movieJson = new JSONObject(movieReviewJsonStrr);
            JSONArray movieArray = movieJson.getJSONArray(OWM_RESULTS);


            for (int i = 0; i < movieArray.length(); i++) {

                Review tempReview = new Review();
                JSONObject oneMovieReviewData = movieArray.getJSONObject(i);

                tempReview.setVersionNameAuthor(oneMovieReviewData.getString(AUTHOR));
                tempReview.setVersionContent(oneMovieReviewData.getString(CONTENT));

                res.add(tempReview);

            }


            return res;
        }

        //connect to url
        //read respon as JSON then save it by movieJsonStr
        @Override
        protected Void doInBackground(String... Params) {


            /*
            JSON
            */
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieReviewJsonStr = null;

            try {

                final String BASE_URL_REVIEW = "http://api.themoviedb.org/3/movie/" + Params[0] + "/reviews";
                final String API_KEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(BASE_URL_REVIEW).buildUpon()
                        .appendQueryParameter(API_KEY_PARAM, "31b7afd5b799003e694992042035d57c")
                        .build();
                URL url = new URL(builtUri.toString());

                Log.v("Built URI", builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieReviewJsonStr = buffer.toString();
                Log.v("movie R JSON String : ", movieReviewJsonStr);

            } catch (IOException e) {
                Log.e("MainActivityFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            }

            try {
                myReview = getReviewDataFromJson(movieReviewJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("MainActivityFragment", "Error closing stream", e);
                    }
                }

            }
            return null;
        }


        @Override
        protected void onPostExecute(Void a) {
            if (myReview != null) {
                mReviewAdapter.clear();
                for (int i = 0; i < myReview.size(); i++) {
                    mReviewAdapter.add(myReview.get(i));
                    Log.v("----Ex", "-----" + i);
                }
                reviewListView.setAdapter(mReviewAdapter);
                mReviewAdapter.notifyDataSetChanged();

            }
        }
    }


    //End ---- ReviewMovieTask -----


}






