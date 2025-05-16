package ntu.exam.nhatdailyapp;

public class Article {
    String title;
    String link;
    String pubDate;
    String imageUrl;

    public Article(String title, String link, String pubDate, String imageUrl) {
        this.title = title;
        this.link = link;
        this.pubDate = pubDate;
        this.imageUrl = imageUrl;
    }

    public String getTitle() { return title; }
    public String getLink() { return link; }
    public String getPubDate() { return pubDate; }
    public String getImageUrl() { return imageUrl; }
}
