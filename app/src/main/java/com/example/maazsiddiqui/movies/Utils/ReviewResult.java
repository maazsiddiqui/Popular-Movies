package com.example.maazsiddiqui.movies.Utils;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "reviews", foreignKeys =
@ForeignKey(entity = MovieResult.class, parentColumns = "id", childColumns = "movieID", onDelete = CASCADE))
public class ReviewResult implements Parcelable {

    public String author;
    public String content;
    @PrimaryKey
    @NonNull
    public String id;
    public String url;
    public Integer movieID;

    @Ignore
    public ReviewResult(String author, String content, String id, String url) {
        this.author = author;
        this.content = content;
        this.id = id;
        this.url = url;
    }

    public ReviewResult(String author, String content, String id, String url, Integer movieID) {
        this.author = author;
        this.content = content;
        this.id = id;
        this.url = url;
        this.movieID = movieID;
    }

    protected ReviewResult(Parcel in) {
        author = in.readString();
        content = in.readString();
        id = in.readString();
        url = in.readString();
        if (in.readByte() == 0) {
            movieID = null;
        } else {
            movieID = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(id);
        dest.writeString(url);
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

    public static final Creator<ReviewResult> CREATOR = new Creator<ReviewResult>() {
        @Override
        public ReviewResult createFromParcel(Parcel in) {
            return new ReviewResult(in);
        }

        @Override
        public ReviewResult[] newArray(int size) {
            return new ReviewResult[size];
        }
    };

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public Integer getMovieID() {
        return movieID;
    }
}
