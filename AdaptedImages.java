package dragosholban.com.androidpuzzlegame;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

public class ImageAdapter extends BaseAdapter {
    
    private Context con;
    private AssetManager manager;
    private String[] saved;

    public ImageAdapter(Context c) {

        con = c;
        manager = con.getAssets();

        //adding images to file
        try {
            saved  = manager.list("img");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getCount() {
        return saved.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }


    public View getView(final int position, View changeview, ViewGroup parent) {

        if (changeview == null) {

            final LayoutInflater layoutInflater = LayoutInflater.from(con);
            changeview = layoutInflater.inflate(R.layout.grid_element, null);
        }

        final ImageView imageView = changeview.findViewById(R.id.gridImageview);
        imageView.setImageBitmap(null);

        //created the view

        imageView.post(new Runnable() {
            @Override
            public void run() {

                new AsyncTask<Void, Void, Void>() {

                    private Bitmap map;
                    @Override

                    protected Void back(Void... voids) {

                        map = getPicFromAsset(imageView, saved[position]);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void v) {
                        super.onPostExecute(v);
                        imageView.setImageBitmap(map);
                    }
                }.execute();
            }
        });

        return changeview;
    }

    private Bitmap getPicFromAsset(ImageView imageView, String assetName) {

        int viewWidth = imageView.getWidth();
        int viewHeight = imageView.getHeight();

        if(viewWidth == 0 || viewHeight == 0) {
            return null;
        }

        try {

            InputStream is = manager.open("img/" + assetName);
            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is, new Rect(-1, -1, -1, -1), options);

            int imageWidth = options.outWidth;
            int imageHeight = options.outHeight;

            int scaleFactor = Math.min(imageWidth/viewWidth, imageHeight/viewHeight);
            is.reset();

            options.inJustDecodeBounds = false;
            options.inSampleSize = scaleFactor;
            options.inPurgeable = true;

            return BitmapFactory.decodeStream(is, new Rect(-1, -1, -1, -1), options);
            
        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }
}
