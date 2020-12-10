package kr.intin.ble_kotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.intin.ble_kotlin.data.entity.UseTime
import kr.intin.ble_kotlin.databinding.ItemDbBinding

class DBAdapter : RecyclerView.Adapter<DBAdapter.DBViewHolder>(){

    var list = arrayListOf<UseTime>()                                                       //usetime array 생성

    class DBViewHolder(val binding: ItemDbBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DBViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)                            //부모의 context를 가져옴
        val binding = ItemDbBinding.inflate(layoutInflater, parent, false)     //부모 레이아웃 바인딩
        return DBViewHolder(binding)                                                        //바인딩 리턴
    }

    override fun onBindViewHolder(holder: DBViewHolder, position: Int) {
        holder.binding.db = list[position]                                                  //바인드된 view 객체에 리스트 저장
    }

    override fun getItemCount(): Int {
        return list.size                                                                    //리스트 사이즈 리턴
    }
}