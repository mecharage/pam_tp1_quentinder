package com.example.hoangjim.quentinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

	private void onLikeClicked() {
		CurrentState.getInstance().decideLike();
		finish();
	}

	private void onNopeClicked() {
		CurrentState.getInstance().decideNope();
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("Coucou", "onCreate detail");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		final Button likeButton = (Button) findViewById(R.id.likeButton);
		likeButton.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					onLikeClicked();
					return true;
				}
				return false;
			}
		});

		final Button nopeButton = (Button) findViewById(R.id.nopeButton);
		nopeButton.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					onNopeClicked();
					return true;
				}
				return false;
			}
		});
	}


	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		loadPerson(CurrentState.getInstance().getCurrentPerson());
	}

	private void loadPerson(Person person) {
		setTitle(person.firstName);

		final ImageView imgView = (ImageView) findViewById(R.id.userPic);
		Picasso.with(this).load(person.picUrl).error(R.mipmap.ic_launcher).into(imgView);

		final TextView last = (TextView) findViewById(R.id.userLastname);
		last.setText(person.lastName );

		final TextView age = (TextView) findViewById(R.id.userAge);
		age.setText( person.age + " ans");

		final TextView location = (TextView) findViewById(R.id.userLocation);
		location.setText(person.location );

		final TextView email = (TextView) findViewById(R.id.userEmail);
		email.setText(person.email);
	}
}
