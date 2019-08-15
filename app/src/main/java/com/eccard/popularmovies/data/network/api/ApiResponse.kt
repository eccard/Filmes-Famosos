package com.eccard.popularmovies.data.network.api

import retrofit2.Response
import java.util.regex.Pattern

/**
 * Common class used by API responses.
 * @param <T> the type of the response object
</T> */
@Suppress("unused") // T is used in extending classes
sealed class ApiResponse<T> {
    companion object {
        fun <T> create(error: Throwable): ApiErrorResponse<T> {
            return ApiErrorResponse(error.message ?: "unknown error")
        }

        fun <T> create(response: Response<T>): ApiResponse<T> {
            return if (response.isSuccessful) {
                val body = response.body()
                if (body == null || response.code() == 204) {
                    ApiEmptyResponse()
                } else {
                    ApiSuccessResponse(
                            body = body,
                            linkHeader = response.headers()?.get("link")
                    )
                }
            } else {
                val msg = response.errorBody()?.string()
                val errorMsg = if (msg.isNullOrEmpty()) {
                    response.message()
                } else {
                    msg
                }
                ApiErrorResponse(errorMsg ?: "unknown error")
            }
        }
    }
}

/**
 * separate class for HTTP 204 responses so that we can make ApiSuccessResponse's body non-null.
 */
class ApiEmptyResponse<T> : ApiResponse<T>()

data class ApiSuccessResponse<T>(
        val body: T,
        val links: Map<String, String>
) : ApiResponse<T>() {
    constructor(body: T, linkHeader: String?) : this(
            body = body,
            links = linkHeader?.extractLinks() ?: emptyMap()
    )

    val nextPage: Int? by lazy(LazyThreadSafetyMode.NONE) {
//        links[NEXT_LINK]?.let { next ->
            val actualPageMatcher = PAGE_PATTERN.matcher(body.toString())
            val totalPageMatcher = TOTAL_PAGE_PATTERN.matcher(body.toString())
            if (!actualPageMatcher.find() || actualPageMatcher.groupCount() != 1) {
                null
            } else if (!totalPageMatcher.find() || totalPageMatcher.groupCount() != 1){
                null
            } else {
                try {
                    val actualPage = Integer.parseInt(actualPageMatcher.group(1))

                    val totalPage = Integer.parseInt(totalPageMatcher.group(1))

                    if (actualPage < totalPage){
                        actualPage + 1
                    } else {
                        null
                    }
                } catch (ex: NumberFormatException) {
//                    Log.w("ApiSuccessResponse","cannot parse next page from $next")
//                    Timber.w("cannot parse next page from %s", next)
                    null
                }
            }
//        }
    }

    companion object {
        private val LINK_PATTERN = Pattern.compile("<([^>]*)>[\\s]*;[\\s]*rel=\"([a-zA-Z0-9]+)\"")
        private val PAGE_PATTERN = Pattern.compile("\\bpage=(\\d+)")
        private val TOTAL_PAGE_PATTERN = Pattern.compile("\\btotal_pages=(\\d+)")
        private const val NEXT_LINK = "next"

        private fun String.extractLinks(): Map<String, String> {
            val links = mutableMapOf<String, String>()
            val matcher = LINK_PATTERN.matcher(this)

            while (matcher.find()) {
                val count = matcher.groupCount()
                if (count == 2) {
                    links[matcher.group(2)] = matcher.group(1)
                }
            }
            return links
        }

    }
}

data class ApiErrorResponse<T>(val errorMessage: String) : ApiResponse<T>()
