package com.example.hoangjim.quentinder;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wenchao.cardstack.CardStack;

import org.joda.time.DateTime;
import org.joda.time.Years;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    CardsDataAdapter cardAdapter;

    public void onLikeClicked() {
        ((CardStack) findViewById(R.id.container)).discardTop(1);
    }

    public void onNopeClicked() {
        ((CardStack) findViewById(R.id.container)).discardTop(0);
    }

    public boolean onOptionItemsSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                Button b = (Button) findViewById(R.id.action_refresh);
                b.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            onLikeClicked();
                            return true;
                        }
                        return false;
                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action, menu);
        return super.onCreateOptionsMenu(menu);
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
                            List<Person> list = receiveUserData(response);
                            cardAdapter = new CardsDataAdapter(getApplicationContext(), 0, list);
                            CardStack mCardStack = (CardStack) findViewById(R.id.container);
                            mCardStack.setContentResource(R.layout.card_layout);
                            //cardAdapter.addAll(CurrentState.getInstance().getPersons());
                            mCardStack.setAdapter(cardAdapter);
                            mCardStack.setStackMargin(20);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        super.onAttachedToWindow();
        switch (CurrentState.getInstance().getDecision()) {
            case NONE:
                break;
            case LIKE:
                onLikeClicked();
                break;
            case NOPE:
                onNopeClicked();
                break;
        }
    }

    private List<Person> receiveUserData(JSONObject response) throws JSONException {
        List<Person> list = new ArrayList<>();
        JSONArray results = response.getJSONArray("results");
        DateTime now = DateTime.now();
        for (int i = 0; i < results.length(); ++i) {
            JSONObject user = results.getJSONObject(i).getJSONObject("user");
            JSONObject name = user.getJSONObject("name");
            String firstName = name.getString("first");
            String lastName = name.getString("last");
            firstName = String.format("%c%s", Character.toUpperCase(firstName.charAt(0)), firstName.substring(1));
            lastName = String.format("%c%s", Character.toUpperCase(lastName.charAt(0)), lastName.substring(1));
            list.add(new Person(
                    firstName,
                    lastName,
                    user.getJSONObject("picture").getString("large"),
                    Years.yearsBetween(new DateTime(user.getLong("dob") * 1000l), now).getYears(),
                    user.getString("email"),
                    user.getJSONObject("location").getString("city")
            ));
        }
        return list;
    }

    public void userImageClicked(View view) {
        CardStack mCardStack = (CardStack) findViewById(R.id.container);
        CurrentState.getInstance().setCurrentPerson(cardAdapter.getItem(mCardStack.getCurrIndex()));
        Intent intent = new Intent(this, DetailActivity.class);
        startActivity(intent);
    }
}
