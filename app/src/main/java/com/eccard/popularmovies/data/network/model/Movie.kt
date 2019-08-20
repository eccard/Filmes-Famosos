package com.eccard.popularmovies.data.network.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import com.eccard.popularmovies.AppConstants
import java.net.MalformedURLException
import java.net.URL

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "unused")
@Entity(tableName = "movie", primaryKeys = [("id")])
data class Movie(
        val id: Int,
        val vote_count: Int,
        val video: Boolean,
        val vote_average: Float,
        val title: String,
        val backdrop_path: String?,
        val popularity: Float,
        val poster_path: String?,
        val original_language: String,
        val original_title: String,
        val overview: String,
        val release_date: String,
        var bookmarked: Boolean = false
) : Parcelable {
    fun generatePosterUrl(): String? {
        val uri = Uri.parse(AppConstants.ENDPOINT_MOVIES_POSTER_BASE_URL + poster_path)

        var url: String?

        try {
            url = URL(uri.toString()).toString()
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            url = null
        }

        return url
    }

    fun getBackdropPath(): String {
        return AppConstants.BASE_BACKDROP_PATH + backdrop_path
    }

    constructor(source: Parcel) : this(
    source.readInt(),
    source.readInt(),
    1 == source.readInt(),
    source.readFloat(),
    source.readString(),
    source.readString(),
    source.readFloat(),
    source.readString(),
    source.readString(),
    source.readString(),
    source.readString(),
    source.readString(),
    1 == source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeInt(vote_count)
        writeInt((if (video) 1 else 0))
        writeFloat(vote_average)
        writeString(title)
        writeString(backdrop_path)
        writeFloat(popularity)
        writeString(poster_path)
        writeString(original_language)
        writeString(original_title)
        writeString(overview)
        writeString(release_date)
        writeInt((if (bookmarked) 1 else 0))
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Movie> = object : Parcelable.Creator<Movie> {
            override fun createFromParcel(source: Parcel): Movie = Movie(source)
            override fun newArray(size: Int): Array<Movie?> = arrayOfNulls(size)
        }
    }
}