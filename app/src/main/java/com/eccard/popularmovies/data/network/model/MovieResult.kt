package com.eccard.filmesfamosos.data.network.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import com.eccard.filmesfamosos.AppConstants
import java.net.MalformedURLException
import java.net.URL

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "unused")
@Entity(tableName = "movie", primaryKeys = [("id")])
data class MovieResult(
        val id: Int,
        val vote_count: Int,
        val video: Boolean,
        val vote_average: Float,
        val title: String,
        val popularity: Float,
        val poster_path: String?,
        val original_language: String,
        val original_title: String,
        val overview: String,
        val release_date: String
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

    constructor(source: Parcel) : this(
            source.readInt(),
            source.readInt(),
            1 == source.readInt(),
            source.readFloat(),
            source.readString(),
            source.readFloat(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeInt(vote_count)
        writeInt((if (video) 1 else 0))
        writeFloat(vote_average)
        writeString(title)
        writeFloat(popularity)
        writeString(poster_path)
        writeString(original_language)
        writeString(original_title)
        writeString(overview)
        writeString(release_date)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<MovieResult> = object : Parcelable.Creator<MovieResult> {
            override fun createFromParcel(source: Parcel): MovieResult = MovieResult(source)
            override fun newArray(size: Int): Array<MovieResult?> = arrayOfNulls(size)
        }
    }
}