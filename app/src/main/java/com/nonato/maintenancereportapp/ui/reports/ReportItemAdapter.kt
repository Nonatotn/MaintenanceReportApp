package com.nonato.maintenancereportapp.ui.reports

import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nonato.maintenancereportapp.R
import com.nonato.maintenancereportapp.data.model.ReportItem

class ReportItemAdapter : ListAdapter<ReportItem, RecyclerView.ViewHolder>(DiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return getItem(position).itemType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_report_item_text, parent, false)
            TextItemViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_report_item_image, parent, false)
            ImageItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if (holder is TextItemViewHolder) holder.bind(item)
        else if (holder is ImageItemViewHolder) holder.bind(item)
    }

    inner class TextItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.tvItemText)
        fun bind(item: ReportItem) {
            textView.text = item.text
        }
    }

    inner class ImageItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imgItemPhoto)
        private val captionView: TextView = itemView.findViewById(R.id.tvItemCaption)
        fun bind(item: ReportItem) {
            captionView.text = item.caption
            // Carrega imagem do URI (pode usar Glide/Picasso, mas usaremos decodificação simples)
            val uri = Uri.parse(item.imageUri)
            val bitmap = MediaStore.Images.Media.getBitmap(itemView.context.contentResolver, uri)
            imageView.setImageBitmap(bitmap)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ReportItem>() {
        override fun areItemsTheSame(old: ReportItem, new: ReportItem) = old.id == new.id
        override fun areContentsTheSame(old: ReportItem, new: ReportItem) = old == new
    }
}
