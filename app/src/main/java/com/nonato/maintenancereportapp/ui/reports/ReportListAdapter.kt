package com.nonato.maintenancereportapp.ui.reports

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nonato.maintenancereportapp.R
import com.nonato.maintenancereportapp.data.model.Report

class ReportListAdapter(private val onClick: (Report) -> Unit) :
    ListAdapter<Report, ReportListAdapter.ReportViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_report, parent, false)
        return ReportViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTv: TextView = itemView.findViewById(R.id.tvReportTitle)
        private val clientTv: TextView = itemView.findViewById(R.id.tvReportClient)
        fun bind(report: Report) {
            titleTv.text = report.title
            clientTv.text = "Cliente: ${report.client}"
            itemView.setOnClickListener { onClick(report) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Report>() {
        override fun areItemsTheSame(old: Report, new: Report) = old.id == new.id
        override fun areContentsTheSame(old: Report, new: Report) = old == new
    }
}
