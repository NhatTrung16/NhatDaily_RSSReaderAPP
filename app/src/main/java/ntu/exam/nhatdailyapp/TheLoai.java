package ntu.exam.nhatdailyapp;

public class TheLoai {
    String name;
    int iconResId; // Resource ID của icon từ drawable
    String rssFeedUrl; // URL của RSS feed cho thể loại này

    public TheLoai(String name, int iconResId, String rssFeedUrl) {
        this.name = name;
        this.iconResId = iconResId;
        this.rssFeedUrl = rssFeedUrl;
    }

    public String getName() { return name; }
    public int getIconResId() { return iconResId; }
    public String getRssFeedUrl() { return rssFeedUrl; }


}