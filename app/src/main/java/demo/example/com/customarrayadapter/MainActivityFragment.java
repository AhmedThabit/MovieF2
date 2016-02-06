package demo.example.com.customarrayadapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

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
 * A fragment containing the list view of Android versions.
 */

public class MainActivityFragment extends Fragment {

    public String movieReviewJsonStr;
    public String id;
    //FetchTrailer movieTrailer=new FetchTrailer();
    //MyMovie[] myMovie =new MyMovie[20];

    public ArrayList<MyMovie> myMovie = new ArrayList<MyMovie>();
    private MyMovieAdapter mMyMovieAdapter;
    GridView gridView;

    private void updateMovies(){
        FetchMovieTask movieTask = new FetchMovieTask();
        SharedPreferences sharedPrefs=PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sort_by= sharedPrefs.getString(
                getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_popular)
        );

        movieTask.execute(sort_by);//popularity.desc   //top_rated.desc
    }

    @Override
    public void onStart() {
        updateMovies();
        super.onStart();
    }

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.mainactivityfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateMovies();
            return true;
        }
        if (id==R.id.action_settings){
            //startActivity(new Intent(this,SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMyMovieAdapter = new MyMovieAdapter(getActivity(), myMovie);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) rootView.findViewById(R.id.gridview_movies);
        gridView.setAdapter(mMyMovieAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Toast.makeText(getActivity(), movie.toString(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), detailsActivity.class);

                String movie_photo_path = myMovie.get(i).getImage();
                String movie_title = myMovie.get(i).getTitle();
                String movi_date = myMovie.get(i).getRelease_date();
                String movie_vote = myMovie.get(i).getVote_average();
                String overView= myMovie.get(i).getOverView();
                String Movie_id= myMovie.get(i).getId();
               // String ReviewContent=myMovie.get(i).getReview();
                //---
               // Bundle bundle = new Bundle();
                //bundle.putString("edttext", "From Activity");
                // set Fragmentclass Arguments
                //Fragmentclass fragobj = new Fragmentclass();
                //fragobj.setArguments(bundle);
                //--

                intent.putExtra("pic", movie_photo_path);
                intent.putExtra("tit", movie_title);
                intent.putExtra("date", movi_date);
                intent.putExtra("vote", movie_vote);
                intent.putExtra("overView",overView);
                intent.putExtra("id",Movie_id);
               // intent.putExtra("content",ReviewContent);

                startActivity(intent);
            }
        });
        return rootView;
    }

    public class FetchMovieTask extends AsyncTask<String, Void, Void> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        /*
        //start test
         */
        //get url of images (done)
        private ArrayList<MyMovie> getMovieDataFromJson(String movieJsonStrr)
                throws JSONException

        {

            ArrayList<MyMovie> res = new ArrayList<MyMovie>();
            // These are the names of the JSON objects that need to be extracted.
            final String OWM_RESULTS = "results";
            final String OWM_POSTER_PATH = "poster_path";


            JSONObject movieJson = new JSONObject(movieJsonStrr);
            JSONArray movieArray = movieJson.getJSONArray(OWM_RESULTS);


            for (int i = 0; i < movieArray.length(); i++) {

                MyMovie img = new MyMovie();
                JSONObject oneMovieData = movieArray.getJSONObject(i);

              //  movieTrailer.execute( oneMovieData.getString("id"));

                //JSONObject movieReviewJson = new JSONObject(movieReviewJsonStr);


                //set values in image from json
                img.setImage("http://image.tmdb.org/t/p/w185/" + oneMovieData.getString(OWM_POSTER_PATH));
                img.setRelease_date(oneMovieData.getString("release_date"));
                img.setTitle(oneMovieData.getString("original_title"));
                img.setVote_average(oneMovieData.getString("vote_average"));
                img.setOverView(oneMovieData.getString("overview"));
                img.setId(oneMovieData.getString("id"));
               // img.setReview(movieReviewJson.getString("content"));

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
            //String sort_by = "popularity.desc";

            try {
                // Construct the URL for the movie query
                //
                //http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=31b7afd5b799003e694992042035d57c
                //
                // URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7");
                // String baseUrl = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc";
                // String apiKey = "&api_key=" + BuildConfig.The_Movie_dB_ApiKey;

                final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";

                final String SORT_BY_PARAM = "sort_by";
                final String API_KEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_BY_PARAM, Params[0])
                        .appendQueryParameter(API_KEY_PARAM, BuildConfig.The_Movie_dB_ApiKey)
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
                myMovie = getMovieDataFromJson(movieJsonStr);
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
            if (myMovie != null) {

                mMyMovieAdapter.myClear();
                for (int i = 0; i < myMovie.size(); i++) {
                    mMyMovieAdapter.add(myMovie.get(i));
                    Log.v("----Ex","-----"+i);
                }
                //gridView.setAdapter(mMyMovieAdapter);
                mMyMovieAdapter.notifyDataSetChanged();

            }
        }
    }



}