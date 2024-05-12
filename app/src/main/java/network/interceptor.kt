package network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation
import util.Authorized

class BearerTokenInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val sharedPreferences = context.getSharedPreferences("prefs_usuario", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token_jwt", null)
        val originalRequest = chain.request()

        val withToken = originalRequest.tag(retrofit2.Invocation::class.java)?.method()?.getAnnotation(Authorized::class.java) != null

        val newRequest = if (withToken && token != null) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }

        return chain.proceed(newRequest)
    }
}