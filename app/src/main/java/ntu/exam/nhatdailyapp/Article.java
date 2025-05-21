package ntu.exam.nhatdailyapp;
import java.io.Serializable;
public class Article implements Serializable {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return link.equals(article.link); // So sánh dựa trên link là đủ vì link là duy nhất
    }

    @Override
    public int hashCode() {
        return link.hashCode();
    }
}
