import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.glintup.databinding.ItemImageBinding
import models.User

class ImagenAdapter(private val userList: List<User>) :
    RecyclerView.Adapter<ImagenAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            //val resourceImg = user.profile_picture.toIntOrNull()

            //if (resourceImg != null) {
              //  binding.imageView.setImageResource(resourceImg)
            //} else {

            //}


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


