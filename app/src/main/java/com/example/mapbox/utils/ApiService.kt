package com.example.mapbox.utils

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Created by Mayank Choudhary on 07-05-2021.
 * mayankchoudhary00@gmail.com
 */


interface ApiService {
    @GET("api.php")
    fun getWikiSearch(
        @Query("action") action: String="query",
        @Query("formatversion") formatVersion: String="2",
        @Query("prop") prop: String="pageimages|pageterms",
        @Query("format") format: String="json",
        @Query("generator") generator: String="prefixsearch",
        @Query("redirects") redirects: String = "",
        @Query("piprop") piprop: String="thumbnail",
        @Query("pithumbsize") pithumbsize: String="200",
        @Query("pilimit") pilimit: String="50",
        @Query("wbptterms") wbptterms: String="description",
        @Query("gpssearch") gpssearch: String="",
        @Query("gpslimit") gpslimit: String="50",
        //@Query("titles") titles: String?
    )

    companion object {
        const val BASE_URL = Constants.BASE_URL
    }
}