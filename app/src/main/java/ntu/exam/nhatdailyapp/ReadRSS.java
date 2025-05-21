package ntu.exam.nhatdailyapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadRSS extends AsyncTask<String, Void, ArrayList<Article>> {
    private static final String TAG = "ReadRSS";
    Context context;
    RecyclerView recyclerView;

    public ReadRSS(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @Override
    protected ArrayList<Article> doInBackground(String... strings) {
        ArrayList<Article> articles = new ArrayList<>();

        try {
            URL url = new URL(strings[0]);
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(url.openConnection().getInputStream(), "UTF_8");

            boolean insideItem = false;
            String title = "", link = "", pubDate = "", imageUrl = "", description = "";

            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String tagName = xpp.getName();
                    // Debug: In ra các tag để xem cấu trúc RSS
                    if (insideItem) {
                        Log.d(TAG, "Tag inside item: " + tagName);
                    }

                    if (tagName.equalsIgnoreCase("item")) {
                        insideItem = true;
                        // Reset các giá trị cho item mới
                        title = "";
                        link = "";
                        pubDate = "";
                        imageUrl = "";
                        description = "";
                    } else if (insideItem) {
                        if (tagName.equalsIgnoreCase("title")) {
                            title = xpp.nextText();
                        } else if (tagName.equalsIgnoreCase("link")) {
                            link = xpp.nextText();
                        } else if (tagName.equalsIgnoreCase("pubDate")) {
                            pubDate = xpp.nextText();
                        } else if (tagName.equalsIgnoreCase("description")) {
                            description = xpp.nextText();
                            // Tìm ảnh trong description nếu có
                            imageUrl = extractImageFromDescription(description);
                            if (!imageUrl.isEmpty()) {
                                Log.d(TAG, "Found image in description: " + imageUrl);
                            }
                        } else if (tagName.equalsIgnoreCase("enclosure")) {
                            // Xử lý thẻ enclosure nếu có
                            String type = xpp.getAttributeValue(null, "type");
                            if (type != null && type.startsWith("image/")) {
                                imageUrl = xpp.getAttributeValue(null, "url");
                                Log.d(TAG, "Found image in enclosure: " + imageUrl);
                            }
                        } else if (tagName.equalsIgnoreCase("media:content") ||
                                tagName.equalsIgnoreCase("content") ||
                                tagName.contains("media:")) {
                            // Xử lý các thẻ media khác nhau
                            String mediaUrl = xpp.getAttributeValue(null, "url");
                            if (mediaUrl != null && !mediaUrl.isEmpty()) {
                                imageUrl = mediaUrl;
                                Log.d(TAG, "Found image in media tag: " + imageUrl + " (tag: " + tagName + ")");
                            }
                        } else if (tagName.equalsIgnoreCase("image")) {
                            // Một số RSS có thẻ image riêng
                            int subEvent = xpp.next();
                            while (subEvent != XmlPullParser.END_TAG || !xpp.getName().equalsIgnoreCase("image")) {
                                if (subEvent == XmlPullParser.START_TAG && xpp.getName().equalsIgnoreCase("url")) {
                                    imageUrl = xpp.nextText();
                                    Log.d(TAG, "Found image in image tag: " + imageUrl);
                                    break;
                                }
                                subEvent = xpp.next();
                                if (subEvent == XmlPullParser.END_DOCUMENT) break;
                            }
                        }
                    }
                } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")) {
                    // Thêm item vào danh sách khi đọc xong
                    Article article = new Article(title, link, pubDate, imageUrl);
                    articles.add(article);
                    Log.d(TAG, "Added article: " + title + " with image: " + imageUrl);
                    insideItem = false;
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing RSS: " + e.getMessage(), e);
            e.printStackTrace();
        }

        return articles;
    }
    private String extractImageFromDescription(String description) {
        if (description == null || description.isEmpty()) {
            return "";
        }

        // Tìm thẻ img đầu tiên trong description
        Pattern pattern = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
        Matcher matcher = pattern.matcher(description);

        if (matcher.find()) {
            String imgUrl = matcher.group(1);
            Log.d(TAG, "Extracted image from description: " + imgUrl);
            return imgUrl;
        }

        return "";
    }

    private ArrayList<Article> parseRSSManually(String urlString) {
        ArrayList<Article> articles = new ArrayList<>();
        try {
            URL url = new URL(urlString);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder content = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            String rssContent = content.toString();

            // Tách các item
            String[] items = rssContent.split("<item>");
            for (int i = 1; i < items.length; i++) {
                String item = items[i];
                String title = extractTag(item, "title");
                String link = extractTag(item, "link");
                String pubDate = extractTag(item, "pubDate");
                String description = extractTag(item, "description");

                // Tìm URL hình ảnh
                String imageUrl = extractImageFromDescription(description);

                // Kiểm tra các thẻ media:content hoặc enclosure
                if (imageUrl.isEmpty()) {
                    Pattern mediaPattern = Pattern.compile("<(media:content|enclosure)[^>]+url\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
                    Matcher mediaMatcher = mediaPattern.matcher(item);
                    if (mediaMatcher.find()) {
                        imageUrl = mediaMatcher.group(2);
                    }
                }

                if (!title.isEmpty() && !link.isEmpty()) {
                    articles.add(new Article(title, link, pubDate, imageUrl));
                    Log.d(TAG, "Manually added article: " + title + " with image: " + imageUrl);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error manually parsing RSS: " + e.getMessage(), e);
        }
        return articles;
    }

    private String extractTag(String content, String tag) {
        Pattern pattern = Pattern.compile("<" + tag + ">(.*?)</" + tag + ">");
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    @Override
    protected void onPostExecute(ArrayList<Article> articles) {
        Log.d(TAG, "Articles loaded: " + articles.size());

        // Ghi log để debug
        for (int i = 0; i < Math.min(5, articles.size()); i++) {
            Article article = articles.get(i);
            Log.d(TAG, "Article #" + i + ": " + article.getTitle());
            Log.d(TAG, "  URL: " + article.getLink());
            Log.d(TAG, "  Image: " + article.getImageUrl());
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        ArticleAdapter adapter = new ArticleAdapter(context, articles);
        recyclerView.setAdapter(adapter);
    }
}