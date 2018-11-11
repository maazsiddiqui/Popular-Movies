package com.example.maazsiddiqui.movies.Utils;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "videos", foreignKeys =
@ForeignKey(entity = MovieResult.class, parentColumns = "id", childColumns = "movieID", onDelete = CASCADE))
public class VideoResult implements Parcelable {

    @PrimaryKey
    @NonNull
    public String id;
    public String iso6391;
    public String iso31661;
    public String key;
    public String name;
    public String site;
    public Integer size;
    public String type;
    public Integer movieID;

    @Ignore
    public VideoResult(String id, String iso6391, String iso31661, String key, String name, String site, Integer size, String type) {
        this.id = id;
        this.iso6391 = iso6391;
        this.iso31661 = iso31661;
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;
    }

    public VideoResult(String id, String iso6391, String iso31661, String key, String name, String site, Integer size, String type, Integer movieID) {
        this.id = id;
        this.iso6391 = iso6391;
        this.iso31661 = iso31661;
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;
        this.movieID = movieID;
    }

    protected VideoResult(Parcel in) {
        id = in.readString();
        iso6391 = in.readString();
        iso31661 = in.readString();
        key = in.readString();
        name = in.readString();
        site = in.readString();
        if (in.readByte() == 0) {
            size = null;
        } else {
            size = in.readInt();
        }
        type = in.readString();
        if (in.readByte() == 0) {
            movieID = null;
        } else {
            movieID = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(iso6391);
        dest.writeString(iso31661);
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(site);
        if (size == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(size);
        }
        dest.writeString(type);
        if (movieID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(movieID);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VideoResult> CREATOR = new Creator<VideoResult>() {
        @Override
        public VideoResult createFromParcel(Parcel in) {
            return new VideoResult(in);
        }

        @Override
        public VideoResult[] newArray(int size) {
            return new VideoResult[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getIso6391() {
        return iso6391;
    }

    public String getIso31661() {
        return iso31661;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public Integer getSize() {
        return size;
    }

    public String getType() {
        return type;
    }

    public Integer getMovieID() {
        return movieID;
    }
}
