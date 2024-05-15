import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.glintup.databinding.ItemImageBinding
import models.User
import models.user.getImageRequest
import com.bumptech.glide.Glide
import network.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class ImagenAdapter(private val userList: List<User>, context: Context) :
    RecyclerView.Adapter<ImagenAdapter.ImageViewHolder>() {

        val contexto = context

    inner class ImageViewHolder(val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            val resourceImg = user.profile_picture[0]

            user.profile_picture?.let { imageUrl ->

                val id = getImageRequest(imageUrl[0])
                RetrofitClient.create(contexto).fetchImage(id).enqueue(object :
                    Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            response.body()?.let { responseBody ->
                                // Create a temporary file in your application's cache directory
                                val imageFile = File(itemView.context.cacheDir, "temp_$imageUrl.jpg")
                                var outputStream: FileOutputStream? = null
                                try {
                                    outputStream = FileOutputStream(imageFile)
                                    outputStream.write(responseBody.bytes())
                                } catch (e: Exception) {
                                    Log.e("IMAGE", "Error writing to file", e)
                                } finally {
                                    outputStream?.close()
                                }
                                Glide.with(itemView.context)
                                    .load(imageFile)
                                    .into(binding.imageView)

                                // Optionally, delete the file after Glide has done loading it
                                imageFile.deleteOnExit()
                                Log.i("RESPONSE FROM IMAGE", "SUCCESSS FUCKERRR SHIT")
                            }
                        } else {
                            Log.i("RESPONSE FROM IMAGE", "FAILURE")

                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.i("RESPONSE FROM IMAGE", "SERVER IMAGE ISSUE")
                    }
                })
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    override fun getItemCount(): Int = userList.size
}


