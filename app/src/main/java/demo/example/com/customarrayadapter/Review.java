package demo.example.com.customarrayadapter;


public class Review {
    String versionNameAuthor;
    String versionContent;

    public Review(){

    }

    public String getVersionNameAuthor() {
        return versionNameAuthor;
    }

    public void setVersionNameAuthor(String versionNameAuthor) {
        this.versionNameAuthor = versionNameAuthor;
    }

    public String getVersionContent() {
        return versionContent;
    }

    public void setVersionContent(String versionContent) {
        this.versionContent = versionContent;
    }


    public Review(String vName, String vNumber, String image)
    {
        this.versionNameAuthor = vName;
        this.versionContent = vNumber;

    }

}