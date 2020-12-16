package kr.intin.ble_kotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.intin.ble_kotlin.data.entity.UseTime
import kr.intin.ble_kotlin.databinding.ItemDbBinding

class DBAdapter : RecyclerView.Adapter<DBAdapter.DBViewHolder>(){

    var list = arrayListOf<UseTime>()
    val TAG = DBAdapter::class.java.simpleName

    class DBViewHolder(val binding: ItemDbBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UseTime) {
            with(binding) {
                db = item
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DBViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemDbBinding.inflate(layoutInflater, parent, false)
        return DBViewHolder(binding)

    }

    override fun onBindViewHolder(holder: DBViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

}
@BindingAdapter("index")
fun setIndex (textView: TextView, useTime: UseTime) {
    textView.text = useTime.index.toString()
}

@BindingAdapter("usedTime")
fun setUsedTime (textView: TextView, useTime: UseTime) {
    textView.text = useTime.usedTime?.toString()
}

@BindingAdapter("usedDate")
fun setUsedDate (textView: TextView, useTime: UseTime) {
    textView.text = useTime.usedDate
}

@BindingAdapter("startedTime")
fun setStartedTime (textView: TextView, usedTime: UseTime) {
    textView.text = usedTime.startedTime
}