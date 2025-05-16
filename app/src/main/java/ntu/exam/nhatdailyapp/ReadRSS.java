package ntu.exam.nhatdailyapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.util.ArrayList;

public class ReadRSS extends AsyncTask<String, Void, ArrayList<Article>> {
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
            factory.setNamespaceAware(false);
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(url.openConnection().getInputStream(), "UTF_8");

            boolean insideItem = false;
            String title = "", link = "", pubDate = "", imageUrl = "";

            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equalsIgnoreCase("item")) {
                        insideItem = true;
                    } else if (xpp.getName().equalsIgnoreCase("title") && insideItem) {
                        title = xpp.nextText();
                    } else if (xpp.getName().equalsIgnoreCase("link") && insideItem) {
                        link = xpp.nextText();
                    } else if (xpp.getName().equalsIgnoreCase("pubDate") && insideItem) {
                        pubDate = xpp.nextText();
                    }
                    else if ((xpp.getName().equalsIgnoreCase("media:content") ||
                            xpp.getName().equalsIgnoreCase("enclosure")) && insideItem) {
                        imageUrl = xpp.getAttributeValue(null, "url");
                    }
                } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")) {
                    articles.add(new Article(title, link, pubDate, imageUrl));
                    insideItem = false;
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return articles;
    }

    @Override
    protected void onPostExecute(ArrayList<Article> articles) {
        Log.d("ReadRSS", "Articles loaded: " + articles.size());
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        ArticleAdapter adapter = new ArticleAdapter(context, articles);
        recyclerView.setAdapter(adapter);
    }
}

