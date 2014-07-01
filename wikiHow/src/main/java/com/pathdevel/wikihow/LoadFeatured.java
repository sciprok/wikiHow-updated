package com.pathdevel.wikihow;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

public class LoadFeatured extends AsyncTask<Void, Void, ArrayList<Article>> {

    ListView lvResult;
    CardAdapter cardAdapter;
    ProgressDialog progressDialog;
    Context ctx;

    public LoadFeatured(Context ctx, ListView lvResult, CardAdapter cardAdapter) {
        this.ctx = ctx;
        this.lvResult = lvResult;
        this.cardAdapter = cardAdapter;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = ProgressDialog.show(this.ctx, null, ctx.getResources().getString(R.string.loading_featured_dialog), true, false);
    }

    protected ArrayList<Article> doInBackground(Void... voids) {
        ArrayList<Article> featured = new ArrayList<Article>();

        String MAIN_PAGE_URL = ctx.getResources().getString(R.string.wikihow_local_main_page);

        try {
            String CHROME_UAGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36";
            Element mainPage = Jsoup.connect(MAIN_PAGE_URL).userAgent(CHROME_UAGENT).get().body();

            //parsing the page for featured articles
            for (Element thumbnail : mainPage.select("div.thumbnail")) {
                Element link = thumbnail.child(0);
                Element image = link.child(0);
                Element name = link.child(1).child(0).getElementsByTag("span").first();

                featured.add(new Article(link.attr("href"), name.text(), image.attr("src")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return featured;
    }

    @Override
    protected void onPostExecute(ArrayList<Article> articles) {
        this.cardAdapter = new CardAdapter(this.ctx, articles);
        this.lvResult.setAdapter(cardAdapter);

        progressDialog.dismiss();
    }
}
