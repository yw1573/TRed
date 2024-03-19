package com.yw1573.tred.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yw1573.tred.databinding.TableListItemBinding
import com.yw1573.tred.databinding.TableListItemItemBinding
import com.yw1573.tred.fragment.DisplayData



class InnerAdapter : RecyclerView.Adapter<InnerAdapter.ViewHolder>() {
    private var bloodSugars: List<DisplayData> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setBloodSugars(list: List<DisplayData>) {
        this.bloodSugars = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TableListItemItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(bloodSugars[position])
    }

    override fun getItemCount(): Int = bloodSugars.size

    class ViewHolder(private val binding: TableListItemItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bloodSugar: DisplayData) {
            binding.bloodSugar = bloodSugar
            binding.executePendingBindings()
        }
    }
}

class OuterAdapter(private val bloodSugarMap: Map<String, List<DisplayData>>, private val context: Context) :
    RecyclerView.Adapter<OuterAdapter.ViewHolder>() {
    private val keys = bloodSugarMap.keys.toList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OuterAdapter.ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = TableListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: OuterAdapter.ViewHolder, position: Int) {
        val date = keys[position]
        val list = bloodSugarMap[date] ?: emptyList()
        holder.bind(date, list)
    }

    override fun getItemCount(): Int = bloodSugarMap.size

    class ViewHolder(private val binding: TableListItemBinding, private val context: Context) : RecyclerView.ViewHolder
        (binding.root) {
        private val innerAdapter = InnerAdapter()

        init {
            binding.innerRv.layoutManager = LinearLayoutManager(context)
            binding.innerRv.adapter = innerAdapter
        }

        fun bind(date: String, list: List<DisplayData>) {
            binding.date = date
            binding.bloodSugars = list
            innerAdapter.setBloodSugars(list)
            binding.executePendingBindings()
        }
    }
}

