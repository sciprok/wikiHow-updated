package com.pathdevel.wikihow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.InputStream;
import java.util.Map;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    static int total_connections = 0; //making this to count how many images are loading at a time

    private Article article;
    private Map<Article, Bitmap> bmImages;
    private ListView listView;
    private int position;
    private ImageView iv;
    private Context ctx;

    public DownloadImageTask(Context ctx, Article a, Map<Article, Bitmap> bmImages, ViewGroup listView, int position, ImageView iv) {
        this.ctx = ctx;
        this.article = a;
        this.bmImages = bmImages;
        this.listView = (ListView) listView;
        this.position = position;
        this.iv = iv;
    }

    @Override
    protected void onPreExecute() {
        total_connections++;

        if (total_connections == 1) {
            ((Activity) ctx).setProgressBarIndeterminateVisibility(true);
        }
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11;

        //getting image from the net
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();

            return null;
        }

        //resizing the image to lower memory usage
        int width = mIcon11.getWidth();
        int height = mIcon11.getHeight();
        int newHeight = 300; //this is constant for now
        float scale = ((float) newHeight) / height;

        Matrix bMatrix = new Matrix();
        bMatrix.postScale(scale, scale); //scaling the image by matrix

        Bitmap resizedBitmap = Bitmap.createBitmap(mIcon11, 0, 0, width, height, bMatrix, true); //resizing
        mIcon11.recycle(); //it can be free now!

        /*
        mIcon11
        Улетай на крыльях ветра
        Ты в край родной, родная песня наша,
        Туда, где мы тебя свободно пели,
        Где было так привольно нам с тобою.
         */

        return resizedBitmap;
    }

    protected void onPostExecute(Bitmap result) {
        if (result != null) {
            if (this.bmImages.containsKey(this.article))
                this.bmImages.remove(this.article); //remove loading bitmap if we already put a loading image here

            this.bmImages.put(this.article, result); //put a resulted bitmap

            if (this.position >= this.listView.getFirstVisiblePosition() && this.position <= this.listView.getLastVisiblePosition()) {
                this.iv.setImageBitmap(result); //set bitmap IF we only see it on screen
                //else it will cause in a bug where images are set to different ImageViews for some reason
            }
        }

        total_connections--;
        if (total_connections == 0)
            ((Activity) ctx).setProgressBarIndeterminateVisibility(false);
    }
}
