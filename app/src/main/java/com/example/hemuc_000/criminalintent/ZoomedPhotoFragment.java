package com.example.hemuc_000.criminalintent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by hemuc_000 on 7/11/2016.
 */
public class ZoomedPhotoFragment extends DialogFragment {

    private ImageView mImageView;
    private String mBitmapPath;
    private static final String BITMAP_ARGS="pics";
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v= LayoutInflater.from(getActivity()).inflate(R.layout.zoomed_photo,null);
        mImageView= (ImageView)v.findViewById(R.id.zoomed_image_view);
        mBitmapPath=(String)getArguments().getSerializable(BITMAP_ARGS);
        Bitmap bitmap=PictureUtils.getScaledBitMap(mBitmapPath,getActivity());
        mImageView.setImageBitmap(bitmap);
        return new AlertDialog.Builder(getActivity()).setView(v).create();
    }
    public static ZoomedPhotoFragment newInstance(String path){
        Bundle args= new Bundle();
        args.putSerializable(BITMAP_ARGS, path);

        ZoomedPhotoFragment photoFrag= new ZoomedPhotoFragment();
        photoFrag.setArguments(args);

        return(photoFrag);

    }
}
