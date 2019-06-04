package com.prod.artemus.comptecommun;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    int imageSelectionnee;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mesImages.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // Création d'une nouvelle ImageView pour chaque image
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(80, 80));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(10, 10, 10, 10);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mesImages[position]);
        return imageView;
    }

    // Tableau des images
    private Integer[] mesImages = {
            R.drawable.icone01, R.drawable.icone02,
            R.drawable.icone03, R.drawable.icone04,
            R.drawable.icone05, R.drawable.icone06,
            R.drawable.icone07, R.drawable.icone08,
            R.drawable.icone09
    };

    public void setImageSelectionnee(int i){
        imageSelectionnee = i;
    }
}