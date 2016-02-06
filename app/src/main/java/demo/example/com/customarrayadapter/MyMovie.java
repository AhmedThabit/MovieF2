package demo.example.com.customarrayadapter;


public class MyMovie {

    String image; // drawable reference id
    String title;
    String release_date;
    String vote_average;
    String overView;
    String id;
    String review;

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public MyMovie() {

    }

    public MyMovie(String img) {
        this.image = img;
       // this.title=img.getTitle();
        //this.release_date=img.getRelease_date();
        //this.vote_average=img.getVote_average();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }




    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}