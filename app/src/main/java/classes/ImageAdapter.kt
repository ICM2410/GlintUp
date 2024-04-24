// Importaciones necesarias para el adaptador
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.glintup.databinding.ItemImageBinding


class ImagenAdapter(private val images: List<Int>) :
    RecyclerView.Adapter<ImagenAdapter.ImageViewHolder>() {

    // Clase interna ImageViewHolder que extiende RecyclerView.ViewHolder
    inner class ImageViewHolder(val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root) {
        // Función bind que establece la imagen en la vista imageView
        fun bind(imageResId: Int) {
            binding.imageView.setImageResource(imageResId)
        }
    }

    // Función onCreateViewHolder que crea y devuelve un nuevo ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        // Inflar el diseño de elemento de la lista utilizando el enlace de datos ItemImageBinding
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        // Devolver una nueva instancia de ImageViewHolder con el enlace de datos
        return ImageViewHolder(binding)
    }

    // Enlaza los datos en una posición particular
    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        // Llamar a la función bind del ViewHolder para establecer la imagen en la posición dada
        holder.bind(images[position])
    }

    // Devuelve el numero total de elementos en la lista de imágenes
    override fun getItemCount(): Int = images.size
}
