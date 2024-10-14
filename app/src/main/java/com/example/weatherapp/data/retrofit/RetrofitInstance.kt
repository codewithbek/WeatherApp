import com.example.weatherapp.data.remote.WeatherApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitInstance {
    private const val BASE_URL = "https://api.open-meteo.com/"

    private val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttp =
        OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).addInterceptor(
            Interceptor { chain ->
                val request = chain.request()
                val response =
                    request.newBuilder().header("content-type", "application/json").build()
                return@Interceptor chain.proceed(response)
            },
        ).readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS)
            .callTimeout(30, TimeUnit.SECONDS).build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder().baseUrl(BASE_URL).client(okHttp)
            .addConverterFactory(MoshiConverterFactory.create()).build()
    }

    val homeApi: WeatherApi by lazy {
        retrofit.create(WeatherApi::class.java)
    }
}
