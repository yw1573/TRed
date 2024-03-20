package com.yw1573.tred.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yw1573.tred.databinding.TableItemBinding
import com.yw1573.tred.databinding.TableItemItemBinding
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
        val binding = TableItemItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(bloodSugars[position])
    }

    override fun getItemCount(): Int = bloodSugars.size

    class ViewHolder(private val binding: TableItemItemBinding) : RecyclerView.ViewHolder(binding.root) {
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
        val binding = TableItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: OuterAdapter.ViewHolder, position: Int) {
        val date = keys[position]
        val list = bloodSugarMap[date] ?: emptyList()
        holder.bind(date, list)
    }

    override fun getItemCount(): Int = bloodSugarMap.size

    class ViewHolder(private val binding: TableItemBinding, private val context: Context) : RecyclerView.ViewHolder
        (binding.root) {
        private val innerAdapter = InnerAdapter()

        init {
            binding.innerRv.layoutManager = LinearLayoutManager(context)
            binding.innerRv.adapter = innerAdapter
            binding.innerRv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        fun bind(date: String, list: List<DisplayData>) {
            binding.date = date
            binding.size = list.size.toString()
            innerAdapter.setBloodSugars(list)
            binding.executePendingBindings()
        }
    }
}

