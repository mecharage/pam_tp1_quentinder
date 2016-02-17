package com.example.hoangjim.quentinder;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by hoangjim on 11/02/2016.
 */
public class CardsDataAdapter extends ArrayAdapter<Person> {


    public CardsDataAdapter(Context context, int resource, List<Person> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, final View contentView, ViewGroup parent){

        Person person = getItem(position);
        TextView v = (TextView)(contentView.findViewById(R.id.userNameAndAge));


        final ImageView imgView = (ImageView) contentView.findViewById(R.id.userPic);
        Picasso.with(getContext()).load(person.picUrl).error(R.mipmap.ic_launcher).into(imgView);

        final TextView txtView = (TextView) contentView.findViewById(R.id.userNameAndAge);
        txtView.setText(person.firstName + ", " + person.age + " ans");

        return contentView;
    }
}

