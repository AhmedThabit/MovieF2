package demo.example.com.customarrayadapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.squareup.picasso.Picasso;

import java.util.List;



public class MyMovieAdapter extends BaseAdapter {
    private static final String LOG_TAG = MyMovieAdapter.class.getSimpleName();
    Context context;

    List <MyMovie> myMovies;

    public MyMovieAdapter(Activity context, List<MyMovie> myMovies) {

        this.myMovies = myMovies;
        this.context = context;
    }

    public void add(MyMovie img){
        myMovies.add(img);
    }

    public void myClear(){
        myMovies.clear();
    }


    @Override
    public int getCount() {
        return myMovies.size();
    }

    @Override
    public MyMovie getItem(int position) {
        return myMovies.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the MyMovie object from the ArrayAdapter at the appropriate position
        MyMovie myMovie = getItem(position);


        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item_movie, parent, false);
        }

        android.widget.ImageView iconView = (android.widget.ImageView) convertView.findViewById(R.id.grid_item_icon);

        Log.e("url", myMovie.image);
        Picasso.with(context)
                .load(myMovie.image)
                .into(iconView);
        
        return convertView;
    }

}
