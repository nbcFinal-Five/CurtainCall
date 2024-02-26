
import com.nbc.curtaincall.data.model.ShowListModel
import retrofit2.http.GET
import retrofit2.http.Query

interface KopisApi {
    @GET("pblprfr")
    suspend fun fetchShowList(
        @Query("stdate") stdate: String = "20240101",
        @Query("eddate") eddate: String = "20240630",
        @Query("cpage") cpage: String = "1",
        @Query("rows") rows: String = "10",
    ): ShowListModel
}