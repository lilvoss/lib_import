package tn.esprit.version_checker

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import tn.esprit.version_checker.network.VersionInfo

interface versionApi {

    @POST("get") // Adjust endpoint if needed
    fun updateVersion(@Body versionInfo: VersionInfo): Call<ResponseBody>

    @GET("get")
    fun getLatestVersion(): Call<VersionInfo>
}
