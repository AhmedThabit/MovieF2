package demo.example.com.customarrayadapter;


public class Trailer {
    String versionName;
    String versionNumber;
    String image; // drawable reference id
    String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Trailer(){

    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Trailer(String vName, String vNumber, String image)
    {
        this.versionName = vName;
        this.versionNumber = vNumber;
        this.image = image;
    }

}