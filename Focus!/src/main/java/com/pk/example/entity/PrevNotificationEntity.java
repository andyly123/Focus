package com.pk.example.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.pk.example.clientui.MinNotification;

import java.util.Date;

/*
@Entity(tableName = "min_notifications", foreignKeys = @ForeignKey(entity = PreviousNotificationListEntity.class,
        parentColumns = "id",
        childColumns = "_id"))
*/
@Entity(tableName = "prev_notifications")
public class PrevNotificationEntity {

    @PrimaryKey(autoGenerate = true)
    private int _id;
    private String _appName;
    private String _notificationContext;
    // private Bitmap _appIcon;
    private Date _date;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public String getAppName() {
        return _appName;
    }

    public void setAppName(String appName) {
        this._appName = appName;
    }

    public String getNotificationContext() {
        return _notificationContext;
    }

    public void setNotificationContext(String notificationContext) {
        this._notificationContext = notificationContext;
    }
    /*
        @Override
        public Bitmap getAppIcon() {
            return _appIcon;
        }

        public void setAppIcon(Bitmap appIcon) {
            this._appIcon = appIcon;
        }
    */
    public Date getDate() {
        return _date;
    }

    public void setDate(Date date) {
        this._date = date;
    }

    public PrevNotificationEntity() {
    }

    public PrevNotificationEntity(MinNotification minNotification) {
        this._appName = minNotification.getAppName();
        this._notificationContext = minNotification.getNotificationContext();
        //this._appIcon = minNotification.getAppIcon();
        this._date = minNotification.getDate();
    }

}