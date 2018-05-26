package com.example.furyou.lbcvision;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class ItemAdapter extends ArrayAdapter<Product> {

    //tweets est la liste des models à afficher
    public ItemAdapter(Context context, List<Product> products) {
        super(context, 0, products);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_layout,parent, false);
        }

        ProductViewHolder viewHolder = (ProductViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new ProductViewHolder();
            viewHolder.photo = (ImageView) convertView.findViewById(R.id.photo);
            viewHolder.description = (TextView) convertView.findViewById(R.id.description);
            viewHolder.prix = (TextView) convertView.findViewById(R.id.prix);
            viewHolder.category = (TextView) convertView.findViewById(R.id.category);
            viewHolder.adresse = (TextView) convertView.findViewById(R.id.adresse);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Tweet> tweets
        Product prod = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        try {
            Drawable drawable = Drawable.createFromStream((InputStream) new URL(prod.getPhoto()).getContent(), "src");
            viewHolder.photo.setImageDrawable(drawable);
        }catch (Exception e){}
            new DownLoadImageTask(viewHolder.photo).execute(prod.getPhoto());
            viewHolder.description.setText(prod.getDescription());
            viewHolder.prix.setText("Price: " + prod.getPrix());
            viewHolder.category.setText("Category: " + prod.getCategory());
            viewHolder.adresse.setText("Location: " + prod.getAdresse());
            viewHolder.date.setText("Date: " + prod.getDate());
        //viewHolder.description.setText(prod);
        //viewHolder.avatar.setImageDrawable(new ColorDrawable(tweet.getColor()));

        return convertView;
    }



    private class ProductViewHolder{
        public ImageView photo;
        public TextView description;
        public TextView prix;
        public TextView category;
        public TextView adresse;
        public TextView date;
    }

    private class DownLoadImageTask extends AsyncTask<String,Void,Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        /*
            doInBackground(Params... params)
                Override this method to perform a computation on a background thread.
         */
        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){ // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }

        /*
            onPostExecute(Result result)
                Runs on the UI thread after doInBackground(Params...).
         */
        protected void onPostExecute(Bitmap result){
            imageView.setImageBitmap(result);
        }
    }
}