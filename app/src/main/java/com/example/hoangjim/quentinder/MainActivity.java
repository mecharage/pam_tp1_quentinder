package com.example.hoangjim.quentinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.Years;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

	private void loadPerson(Person person) {
		if(person == null)
			return;

		final ImageView imgView = (ImageView) findViewById(R.id.userPic);
		Picasso.with(this).load(person.picUrl).error(R.mipmap.ic_launcher).into(imgView);

		final TextView txtView = (TextView) findViewById(R.id.userNameAndAge);
		txtView.setText(person.firstName + ", " + person.age + " ans");
	}

	private void onLikeClicked() {
		loadPerson(CurrentState.getInstance().popNextPerson());
	}

	private void onNopeClicked() {
		loadPerson(CurrentState.getInstance().popNextPerson());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("Coucou", "onCreate main");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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

		RequestQueue rq = Volley.newRequestQueue(this);
		String url = "https://randomuser.me/api/?format=json&results=50&nat=fr";

		JsonObjectRequest stringRequest = new JsonObjectRequest(
				url,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						//Log.i("Volley", response.toString());
						try {
							receiveUserData(response);
						} catch (JSONException e) {
							Log.e("Volley", Log.getStackTraceString(e));
						}
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("Volley", error.getMessage());
					}
				}
		);

		rq.add(stringRequest);
	}

	@Override
	public void onAttachedToWindow() {
		Log.d("Coucou", "onAttachedToWindow");
		super.onAttachedToWindow();
		loadPerson(CurrentState.getInstance().getCurrentPerson());
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadPerson(CurrentState.getInstance().getCurrentPerson());
	}

	private void receiveUserData(JSONObject response) throws JSONException {
		JSONArray results = response.getJSONArray("results");
		DateTime now = DateTime.now();
		for (int i = 0; i < results.length(); ++i) {
			JSONObject user = results.getJSONObject(i).getJSONObject("user");
			JSONObject name = user.getJSONObject("name");
			String firstName = name.getString("first");
			String lastName = name.getString("last");
			firstName = String.format("%c%s", Character.toUpperCase(firstName.charAt(0)), firstName.substring(1));
			lastName = String.format("%c%s", Character.toUpperCase(lastName.charAt(0)), lastName.substring(1));
			CurrentState.getInstance().addPerson(new Person(
					firstName,
					lastName,
					user.getJSONObject("picture").getString("large"),
					Years.yearsBetween(new DateTime(user.getLong("dob") * 1000l), now).getYears(),
					user.getString("email"),
					user.getJSONObject("location").getString("city")
			));
		}

		loadPerson(CurrentState.getInstance().getCurrentPerson());
	}

	public void userImageClicked(View view) {
		Intent intent = new Intent(this, DetailActivity.class);
		startActivity(intent);
	}
}
