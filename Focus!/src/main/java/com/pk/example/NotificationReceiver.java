package com.pk.example;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.pk.example.entity.MinNotificationEntity;
import com.pk.example.entity.PrevNotificationEntity;
import com.pk.example.entity.PreviousNotificationListEntity;
import com.pk.example.entity.ProfileEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.pk.example.NLService.ADD_PROFILE;
import static com.pk.example.NLService.INSERT_NOTIFICATION;
import static com.pk.example.NLService.REMOVE_PROFILE;

/**
 * Created by kathe on 10/21/2017.
 */

public class NotificationReceiver extends BroadcastReceiver {
    private AppDatabase db;

    @Override
    public void onReceive(Context context, Intent intent) {
//        Log.i("intent ","intent "+intent.getExtras().toString());
        db = AppDatabase.getDatabase(context);
        ArrayList<String> profiles;

        switch(intent.getAction()) {
            case ADD_PROFILE:
                new UpdateProfile(intent.getStringExtra("name"), true).execute();
                break;
            case REMOVE_PROFILE:
                String name = intent.getStringExtra("name");
                new UpdateProfile(name, false).execute();
                // change this for a schedule w/ multiple profiles ending
                profiles = new ArrayList<String>();
                profiles.add(name);
                new ChangePrevNotifications(profiles).execute();
                break;
            case INSERT_NOTIFICATION:
                String packageName, title, text;
                packageName = intent.getStringExtra("packageName");
                title = intent.getStringExtra("title");
                text = intent.getStringExtra("text");
                profiles = intent.getStringArrayListExtra("profiles");
                new InsertNotification(packageName, title, text, profiles).execute();
                break;
        }
//        String temp = intent.getExtras().getString("info")+ "\n----------------------------------------------" + txtView.getText();
//        txtView.setText(temp+"");
    }

    private class UpdateProfile extends AsyncTask<Void, Void, Void> {
        private String profileName;
        private boolean active;

        public UpdateProfile(String profileName, boolean active){
            super();
            this.profileName = profileName;
            this.active = active;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ProfileEntity profile = db.profileDao().loadProfileSync(profileName);
            profile.setActive(active);
            db.profileDao().update(profile);
            return null;
        }
    }

    private class InsertNotification extends AsyncTask<Void, Void, Void> {
        String packageName, title, text;
        ArrayList<String> profiles;

        public InsertNotification(String packageName, String title, String text, ArrayList<String> profiles){
            super();
            this.packageName = packageName;
            this.title = title;
            this.text = text;
            this.profiles = profiles;
        }

        @Override
        protected Void doInBackground(Void... params) {
            for(String prof: profiles){
               MinNotificationEntity notif = new MinNotificationEntity(new MinNotification(packageName, title + " --- " + text, new Date(), prof));
                db.minNotificationDao().insert(notif);
            }
            return null;
        }
    }

    private class ChangePrevNotifications extends AsyncTask<Void, Void, Void> {
        ArrayList<String> profiles;

        public ChangePrevNotifications(ArrayList<String> profiles){
            super();
        }

        @Override
        protected Void doInBackground(Void... params) {
            db.prevNotificationDao().deleteAll();
            for(String profile: profiles) {
                List<MinNotificationEntity> minNotifs = db.minNotificationDao().loadMinNotificationsFromProfileSync(profile);
                for (MinNotificationEntity notif : minNotifs) {
                    MinNotification mn = new MinNotification(notif.getAppName(), notif.getNotificationContext(), notif.getDate(), notif.getProfileName());
                    db.prevNotificationDao().insert(new PrevNotificationEntity(mn));
                    db.minNotificationDao().delete(notif);
                }
            }

            return null;
        }
    }

}