package com.share.responsive.sharemarket;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by Binoy on 11/20/2017.
 */

public class Tab1Fragment extends Fragment{
    ImageButton fb,favorties;
    boolean isFavClicked=false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.tab1_fragment,container,false);
       fb=(ImageButton)view.findViewById(R.id.fb);
       favorties=(ImageButton)view.findViewById(R.id.favorites);
       favorties.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(!isFavClicked){
               favorties.setImageResource(R.drawable.filled);
               isFavClicked=true;
               }else{
                   favorties.setImageResource(R.drawable.empty);
                   isFavClicked=false;
               }
           }
       });
        return view;
    }
}
