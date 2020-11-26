package kr.intin.ble_kotlin.adapter

import android.bluetooth.le.ScanResult
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.intin.ble_kotlin.databinding.ItemMainBinding
import kr.intin.ble_kotlin.dataclass.ScanResultData
import kr.intin.ble_kotlin.viewmodel.MainViewModel

class BLEAdapter(private val model: MainViewModel) : RecyclerView.Adapter<BLEViewHolder>() {

    private val list = arrayListOf<ScanResult?>()
    private val TAG = "BLEAdapter"
    private lateinit var itemClickListner: ItemClickListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BLEViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemMainBinding = ItemMainBinding.inflate(layoutInflater, parent, false)
        return BLEViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BLEViewHolder, position: Int) {

        holder.apply {
            bind(model, list[position])
            binding.item.setOnClickListener {
                itemClickListner.onClick(list[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addResult(result: ScanResult?) {

        if (list.isNotEmpty()) {
            (0 until list.size).forEach { i ->
                if (result?.device?.toString()?.equals(list[i]?.device?.toString()!!)!!) return
            }
        }
        list.add(result)
        notifyDataSetChanged()
    }

    interface ItemClickListener {
        fun onClick(scanResult: ScanResult?)
    }

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListner = itemClickListener
    }

    fun clearList () {
        list.clear()
    }
}

