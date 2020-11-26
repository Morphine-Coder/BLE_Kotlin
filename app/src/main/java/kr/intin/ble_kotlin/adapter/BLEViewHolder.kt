package kr.intin.ble_kotlin.adapter

import android.app.Application
import android.bluetooth.le.ScanResult
import android.content.Context
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import kr.intin.ble_kotlin.databinding.ItemMainBinding
import kr.intin.ble_kotlin.dataclass.ScanResultData
import kr.intin.ble_kotlin.viewmodel.MainViewModel

class BLEViewHolder(val binding: ItemMainBinding) : RecyclerView.ViewHolder(binding.root){

    fun bind(model: MainViewModel, data: ScanResult?) {
//        binding.apply {
//            this.result = result
//            this.model =
//        }
        binding.executePendingBindings()
        binding.model = model
        binding.data = data
    }

}