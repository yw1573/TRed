package com.yw1573.tred.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yw1573.tred.MainActivity
import com.yw1573.tred.R
import util.BloodSugar
import util.StringUtils

/**
 * 适配器
 * @property bloodSugarList List<BloodSugar>
 * @property phaseStr Array<String>
 * @constructor
 */
class TableViewAdapter(private val bloodSugarList: List<BloodSugar>) :
    RecyclerView.Adapter<TableViewAdapter.RowViewHolder>() {
    private val phaseStr = MainActivity.phaseStr

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.table_list_item, parent, false)
        return RowViewHolder(itemView)
    }

    private fun setHeaderBg(view: View) {
        view.setBackgroundResource(R.drawable.bg_table_cell)
    }

    private fun setContentBg(view: View) {
        view.setBackgroundResource(R.drawable.bg_table_cell)
    }

    override fun onBindViewHolder(holder: RowViewHolder, position: Int) {
        val rowPos = holder.bindingAdapterPosition

        if (rowPos == 0) {
            // Header Cells. Main Headings appear here
            holder.apply {
                setHeaderBg(txtID)
                setHeaderBg(txtDateTime)
                setHeaderBg(txtPhase)
                setHeaderBg(txtValue)

                txtID.text = "序号"
                txtDateTime.text = "时间"
                txtPhase.text = "阶段"
                txtValue.text = "血糖"
            }
        } else {
            val modal = bloodSugarList[rowPos - 1]

            holder.apply {
                setContentBg(txtID)
                setContentBg(txtDateTime)
                setContentBg(txtPhase)
                setContentBg(txtValue)

                txtID.text = modal.id.toString()
                txtDateTime.text = StringUtils.conversionTime(modal.timestamp, "yyyy年MM月dd日 HH:mm")
                txtPhase.text = modal.phase
                txtValue.text = modal.value.toString()
            }
        }
    }

    override fun getItemCount(): Int {
        return bloodSugarList.size + 1 // one more to add header row
    }

    inner class RowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtID: TextView = itemView.findViewById(R.id.ViewTableID)
        val txtDateTime: TextView = itemView.findViewById(R.id.ViewTableDateTime)
        val txtPhase: TextView = itemView.findViewById(R.id.ViewTablePhase)
        val txtValue: TextView = itemView.findViewById(R.id.ViewTableBloodSugar)
    }
}
