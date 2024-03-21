package com.yw1573.tred.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.yw1573.tred.SplashActivity
import com.yw1573.tred.R
import com.yw1573.tred.databinding.TableItemBinding
import com.yw1573.tred.databinding.TableItemItemBinding
import com.yw1573.tred.fragment.DisplayData
import com.yw1573.tred.fragment.TableFragment
import util.StringUtils

// 点击接口
interface OnItemClickListener {
    fun onItemClick(data: DisplayData)
}

// 长按接口
interface OnItemLongClickListener {
    fun onItemLongClick(position: Int): Boolean
}

class InnerAdapter : RecyclerView.Adapter<InnerAdapter.ViewHolder>() {
    private lateinit var bloodSugars: MutableList<DisplayData>
    private lateinit var onItemClickListener: OnItemClickListener
    private lateinit var onItemLongClickListener: OnItemLongClickListener


    // 点击
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    // 长按
    fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
        this.onItemLongClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setBloodSugars(list: List<DisplayData>) {
        // 虽然传参为不可变List，但是传入后用可变List接收，让可变List完成后续逻辑
        this.bloodSugars = list.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TableItemItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, onItemClickListener, onItemLongClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(bloodSugars[position])
    }

    override fun getItemCount(): Int = bloodSugars.size

    fun getItem(position: Int): DisplayData {
        return bloodSugars[position]
    }

    class ViewHolder(
        private val binding: TableItemItemBinding,
        private val onItemClickListener: OnItemClickListener,
        private val onItemLongClickListener: OnItemLongClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onItemClickListener.onItemClick(binding.bloodSugar ?: return@setOnClickListener)
            }

            binding.root.setOnLongClickListener {
                onItemLongClickListener.onItemLongClick(
                    bindingAdapterPosition
                )
            }
        }

        fun bind(bloodSugar: DisplayData) {
            binding.bloodSugar = bloodSugar
            binding.executePendingBindings()
        }
    }
}


class OuterAdapter(
    private val context: Context
) : RecyclerView.Adapter<OuterAdapter.ViewHolder>() {
    private lateinit var bloodSugarMap: Map<String, List<DisplayData>>
    private lateinit var keys: List<String>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = TableItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, context, this@OuterAdapter)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val date = keys[position]
        val list = bloodSugarMap[date] ?: emptyList()
        holder.bind(date, list)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setBloodSugarMap(bloodSugarMap: Map<String, List<DisplayData>>) {
        this.bloodSugarMap = bloodSugarMap
        this.keys = bloodSugarMap.keys.toList()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = bloodSugarMap.size

    class ViewHolder(
        private val binding: TableItemBinding, private val context: Context, private val outerAdapter: OuterAdapter
    ) : RecyclerView.ViewHolder(binding.root) {
        private val innerAdapter = InnerAdapter()

        init {
            // 点击事件
            innerAdapter.setOnItemClickListener(object : OnItemClickListener {
                override fun onItemClick(data: DisplayData) {
                    Log.d("TRed", data.id.toString())
                    MaterialDialog(context).show {
                        icon(R.mipmap.icon)
                        title(R.string.database_information)
                        message(
                            text = "序号: ${data.id}\n" + "时间: ${data.date} ${data.time}\n" + "标签: ${data.phase}\n" + "血糖: ${data.value}\n" + "标准: ${data.standard}"
                        )
                    }
                }
            })
            // 长按事件
            innerAdapter.setOnItemLongClickListener(object : OnItemLongClickListener {
                override fun onItemLongClick(position: Int): Boolean {
                    val item = innerAdapter.getItem(position)
                    MaterialDialog(context).show {
                        icon(R.mipmap.icon)
                        title(R.string.confirm_deletion_message)
                        message(
                            text = "序号: ${item.id}\n时间: " + StringUtils.conversionTime(
                                item.timestamp, "yyyy年MM月dd日 HH:mm"
                            ) + "\n" + "标签: " + item.phase + "\n" + "血糖: " + item.value
                        )

                        positiveButton(R.string.string_cancel) {
                            Log.d("TRed", "取消删除操作")
                        }
                        negativeButton(R.string.string_confirm) {
                            Log.d("TRed", "数据库删除成功: $position")
                            SplashActivity.dbHelper?.delete(item.id)
                            val bloodSugarMap = SplashActivity.dbHelper!!.bloodSugarNormalization()
                            outerAdapter.setBloodSugarMap(bloodSugarMap)
                        }
                        getActionButton(WhichButton.POSITIVE).updateTextColor(Color.BLACK)
                        getActionButton(WhichButton.NEGATIVE).updateTextColor(Color.BLACK)
                    }
                    return false
                }
            })
            binding.innerRv.layoutManager = LinearLayoutManager(context)
            binding.innerRv.adapter = innerAdapter
            binding.innerRv.addItemDecoration(
                DividerItemDecoration(
                    context, DividerItemDecoration.VERTICAL
                )
            )
        }

        fun bind(date: String, list: List<DisplayData>) {
            binding.date = date
            binding.size = list.size.toString()
            innerAdapter.setBloodSugars(list)
            binding.executePendingBindings()
        }
    }
}

