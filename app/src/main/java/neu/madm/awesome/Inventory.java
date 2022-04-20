package neu.madm.awesome;

public class Inventory {
    private String mName;
    private String mImageUrl;
    private String mDescription;

    public Inventory(String mName, String mImageUrl, String mDescription) {
        this.mName = mName;
        this.mImageUrl = mImageUrl;
        this.mDescription = mDescription;
    }

    public Inventory() {
        this.mName = "new item";
        this.mImageUrl = "";
        this.mDescription = "";
    }

    public String getmName() {
        return mName;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    @Override
    public String toString() {
        return String.format("Item name: %s\nDescription: %s\n%s", mName, mDescription, mImageUrl);
    }
}
