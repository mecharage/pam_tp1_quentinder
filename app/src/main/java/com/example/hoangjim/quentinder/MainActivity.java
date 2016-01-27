package com.example.hoangjim.quentinder;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.TaskStackBuilder;
import android.app.assist.AssistContent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SearchEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestHandler;
import com.squareup.picasso.UrlConnectionDownloader;

import org.joda.time.DateTime;
import org.joda.time.Years;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.CharSequence;
import java.lang.Override;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	private final List<Person> _persons = new ArrayList<>();

	private void loadPerson(Person person) {
		final ImageView imgView = (ImageView) findViewById(R.id.userPic);
		Picasso.with(MainActivity.this).load(person.picUrl).error(R.mipmap.ic_launcher).into(imgView);

		final TextView txtView = (TextView) findViewById(R.id.userNameAndAge);
		txtView.setText(person.name + ", " + person.age + " ans");
	}

	private Person popNextPerson() {
		Person person = _persons.get(0);
		_persons.remove(0);
		return person;
	}

	private void onLikeClicked() {
		loadPerson(popNextPerson());
	}

	private void onNopeClicked() {
		loadPerson(popNextPerson());
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Coucou", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		final Button likeButton = (Button) findViewById(R.id.likeButton);
		likeButton.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_UP) {
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
				if(event.getAction() == MotionEvent.ACTION_UP) {
					onNopeClicked();
					return true;
				}
				return false;
			}
		});
    }

    @Override
    public void onActionModeFinished(ActionMode mode) {
        Log.d("Coucou", "onActionModeFinished");
        super.onActionModeFinished(mode);
    }

    @Override
    public void onActionModeStarted(ActionMode mode) {
        Log.d("Coucou", "onActionModeStarted");
        super.onActionModeStarted(mode);
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        Log.d("Coucou", "onActivityReenter");
        super.onActivityReenter(resultCode, data);
    }

    @Override
    protected void onApplyThemeResource(Resources.Theme theme, int resid, boolean first) {
        Log.d("Coucou", "onApplyThemeResource");
        super.onApplyThemeResource(theme, resid, first);
    }

    @Override
    public void onAttachedToWindow() {
        Log.d("Coucou", "onAttachedToWindow");
        super.onAttachedToWindow();
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

    private void receiveUserData(JSONObject response) throws JSONException {
        JSONArray results = response.getJSONArray("results");
		DateTime now = DateTime.now();
        for(int i = 0; i< results.length();++i) {
            JSONObject user = results.getJSONObject(i).getJSONObject("user");
			String name = user.getJSONObject("name").getString("first");
			name = String.format("%c%s", Character.toUpperCase(name.charAt(0)), name.substring(1));
			_persons.add(new Person(
					name,
					Years.yearsBetween(new DateTime(user.getLong("dob") * 1000l), now).getYears(),
					user.getJSONObject("picture").getString("large")
			));
        }

		loadPerson(popNextPerson());
	}

    @Override
    public void onAttachFragment(Fragment fragment) {
        Log.d("Coucou", "onAttachFragment");
        super.onAttachFragment(fragment);
    }

    @Override
    protected void onChildTitleChanged(Activity childActivity, CharSequence title) {
        Log.d("Coucou", "onChildTitleChanged");
        super.onChildTitleChanged(childActivity, title);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.d("Coucou", "onContextItemSelected");
        return super.onContextItemSelected(item);
    }

    @Override
    public void onContextMenuClosed(Menu menu) {
        Log.d("Coucou", "onContextMenuClosed");
        super.onContextMenuClosed(menu);
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        Log.d("Coucou", "onCreate");
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        Log.d("Coucou", "onCreateContextMenu");
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Nullable
    @Override
    public CharSequence onCreateDescription() {
        Log.d("Coucou", "onCreateDescription");
        return super.onCreateDescription();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Log.d("Coucou", "onCreateDialog");
        return super.onCreateDialog(id);
    }

    @Nullable
    @Override
    protected Dialog onCreateDialog(int id, Bundle args) {
        Log.d("Coucou", "onCreateDialog");
        return super.onCreateDialog(id, args);
    }

    @Override
    public void onCreateNavigateUpTaskStack(TaskStackBuilder builder) {
        Log.d("Coucou", "onCreateNavigateUpTaskStack");
        super.onCreateNavigateUpTaskStack(builder);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("Coucou", "onCreateOptionsMenu");
        return super.onCreateOptionsMenu(menu);
    }

    @Nullable
    @Override
    public View onCreatePanelView(int featureId) {
        Log.d("Coucou", "onCreatePanelView");
        return super.onCreatePanelView(featureId);
    }

    @Override
    public boolean onCreateThumbnail(Bitmap outBitmap, Canvas canvas) {
        Log.d("Coucou", "onCreateThumbnail");
        return super.onCreateThumbnail(outBitmap, canvas);
    }

    @Override
    public void onDetachedFromWindow() {
        Log.d("Coucou", "onDetachedFromWindow");
        super.onDetachedFromWindow();
    }

    @Override
    public void onEnterAnimationComplete() {
        Log.d("Coucou", "onEnterAnimationComplete");
        super.onEnterAnimationComplete();
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        Log.d("Coucou", "onGenericMotionEvent");
        return super.onGenericMotionEvent(event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        Log.d("Coucou", "onKeyLongPress");
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        Log.d("Coucou", "onKeyMultiple");
        return super.onKeyMultiple(keyCode, repeatCount, event);
    }

    @Override
    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        Log.d("Coucou", "onKeyShortcut");
        return super.onKeyShortcut(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.d("Coucou", "onKeyUp");
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onNavigateUp() {
        Log.d("Coucou", "onNavigateUp");
        return super.onNavigateUp();
    }

    @Override
    public boolean onNavigateUpFromChild(Activity child) {
        Log.d("Coucou", "onNavigateUpFromChild");
        return super.onNavigateUpFromChild(child);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("Coucou", "onOptionsItemSelected");
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        Log.d("Coucou", "onOptionsMenuClosed");
        super.onOptionsMenuClosed(menu);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        Log.d("Coucou", "onPostCreate");
        super.onPostCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        Log.d("Coucou", "onPrepareDialog");
        super.onPrepareDialog(id, dialog);
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
        Log.d("Coucou", "onPrepareDialog");
        super.onPrepareDialog(id, dialog, args);
    }

    @Override
    public void onPrepareNavigateUpTaskStack(TaskStackBuilder builder) {
        Log.d("Coucou", "onPrepareNavigateUpTaskStack");
        super.onPrepareNavigateUpTaskStack(builder);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d("Coucou", "onPrepareOptionsMenu");
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onProvideAssistContent(AssistContent outContent) {
        Log.d("Coucou", "onProvideAssistContent");
        super.onProvideAssistContent(outContent);
    }

    @Override
    public void onProvideAssistData(Bundle data) {
        Log.d("Coucou", "onProvideAssistData");
        super.onProvideAssistData(data);
    }

    @Override
    public Uri onProvideReferrer() {
        Log.d("Coucou", "onProvideReferrer");
        return super.onProvideReferrer();
    }

    @Override
    protected void onRestart() {
        Log.d("Coucou", "onRestart");
        super.onRestart();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d("Coucou", "onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        Log.d("Coucou", "onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState, persistentState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        Log.d("Coucou", "onSaveInstanceState");
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public boolean onSearchRequested() {
        Log.d("Coucou", "onSearchRequested");
        return super.onSearchRequested();
    }

    @Override
    public boolean onSearchRequested(SearchEvent searchEvent) {
        Log.d("Coucou", "onSearchRequested");
        return super.onSearchRequested(searchEvent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("Coucou", "onTouchEvent");
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onTrackballEvent(MotionEvent event) {
        Log.d("Coucou", "onTrackballEvent");
        return super.onTrackballEvent(event);
    }

    @Override
    public void onTrimMemory(int level) {
        Log.d("Coucou", "onTrimMemory");
        super.onTrimMemory(level);
    }

    @Override
    public void onUserInteraction() {
        Log.d("Coucou", "onUserInteraction");
        super.onUserInteraction();
    }

    @Override
    protected void onUserLeaveHint() {
        Log.d("Coucou", "onUserLeaveHint");
        super.onUserLeaveHint();
    }

    @Override
    public void onVisibleBehindCanceled() {
        Log.d("Coucou", "onVisibleBehindCanceled");
        super.onVisibleBehindCanceled();
    }

    @Override
    public void onWindowAttributesChanged(WindowManager.LayoutParams params) {
        Log.d("Coucou", "onWindowAttributesChanged");
        super.onWindowAttributesChanged(params);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        Log.d("Coucou", "onWindowFocusChanged");
        super.onWindowFocusChanged(hasFocus);
    }

    @Nullable
    @Override
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        Log.d("Coucou", "onWindowStartingActionMode");
        return super.onWindowStartingActionMode(callback);
    }

    @Nullable
    @Override
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int type) {
        Log.d("Coucou", "onWindowStartingActionMode");
        return super.onWindowStartingActionMode(callback, type);
    }
}
