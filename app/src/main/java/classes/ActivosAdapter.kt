package classes

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.glintup.databinding.DisponiblestrowBinding
import models.testingUser
import models.user.getImageRequest
import network.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class ActivosAdapter(private val context: Context, private val buttonClickListener: OnButtonClickListener) : RecyclerView.Adapter<ActivosAdapter.UserViewHolder>() {

    private var userList: List<testingUser>? = null

    interface OnButtonClickListener {
        fun onButtonClick(user: testingUser)
    }

    inner class UserViewHolder(private val binding: DisponiblestrowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: testingUser) {
            binding.apply {
                nombre.text = "${user.user.name}"

                // Verifica si la URL de la imagen de perfil no es nula antes de intentar establecerla en el ImageView
                user.user.profile_picture?.let { imageUrl ->

                    val id = getImageRequest(imageUrl[0])
                    RetrofitClient.create(context).fetchImage(id).enqueue(object :
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
                                        .into(binding.perfilImg)

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
                boton.setOnClickListener {
                    buttonClickListener.onButtonClick(user)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = DisponiblestrowBinding.inflate(inflater, parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList?.get(position)
        user?.let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int {
        return userList?.size ?: 0
    }

    fun setUsers(users: List<testingUser>?) {
        userList = users
        notifyDataSetChanged() // Notificar al RecyclerView que los datos han cambiado
    }
}
