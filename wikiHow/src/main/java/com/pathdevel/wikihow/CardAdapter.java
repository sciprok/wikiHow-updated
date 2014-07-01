package com.pathdevel.wikihow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CardAdapter extends BaseAdapter {

    static Bitmap loading_icon;
    private Context ctx;
    private LayoutInflater lInflater;
    private ArrayList<Article> objects;
    private Map<Article, Bitmap> objects_bitmaps;

    CardAdapter(Context context, ArrayList<Article> articles) {
        ctx = context;
        objects = articles;
        objects_bitmaps = new HashMap<Article, Bitmap>();
        loading_icon = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.image_loading);

        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null)
            view = lInflater.inflate(R.layout.list_card, parent, false);

        assert view != null; // view still can be null by some android os bug, so just a quick assert for this
        ImageView articleImage = (ImageView) view.findViewById(R.id.articleImage);

        TextView articleText = (TextView) view.findViewById(R.id.articleName);

        Article a = getArticle(position);

        //check if we already loaded a Bitmap for this article
        if (objects_bitmaps.containsKey(a)) {
            //if so, take it from memory
            articleImage.setImageBitmap(objects_bitmaps.get(a));
        } else {
            //else download it from the wikiHow site
            new DownloadImageTask(ctx, a, objects_bitmaps, parent, position, articleImage).execute(a.image_url);
            objects_bitmaps.put(a, loading_icon); //put temporary loading bitmap in the ImageView
            articleImage.setImageBitmap(loading_icon);
        }

        articleText.setText(a.name);

        return view;
    }

    private Article getArticle(int position) {
        return ((Article) getItem(position));
    }
}
