package classes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import models.message

class ChatAdapter(private val messages: List<message>, private val currentUser: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    /*companion object {
        const val VIEW_TYPE_USER = 1
        const val VIEW_TYPE_OTHER = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].sender) VIEW_TYPE_USER else VIEW_TYPE_OTHER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_USER) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message_user, parent, false)
            UserMessageViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message_other, parent, false)
            OtherMessageViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if (holder is UserMessageViewHolder) {
            holder.bind(message)
        } else if (holder is OtherMessageViewHolder) {
            holder.bind(message)
        }
    }

    override fun getItemCount(): Int = messages.size

    class UserMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)

        fun bind(message: Message) {
            messageTextView.text = message.content
        }
    }

    class OtherMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)

        fun bind(message: Message) {
            messageTextView.text = message.content
        }
    }*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }


}