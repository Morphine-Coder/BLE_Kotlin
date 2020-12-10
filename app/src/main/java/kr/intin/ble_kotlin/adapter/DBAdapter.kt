package kr.intin.ble_kotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.intin.ble_kotlin.data.entity.UseTime
import kr.intin.ble_kotlin.databinding.ItemDbBinding

class DBAdapter : RecyclerView.Adapter<DBAdapter.DBViewHolder>(){

<<<<<<< HEAD
    var list = arrayListOf<UseTime>()                                                       //usetime array 생성
=======
    var list = arrayListOf<UseTime>()
>>>>>>> c6b32022a662bda61efeb35214f1a0da5400d90c

    class DBViewHolder(val binding: ItemDbBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DBViewHolder {
<<<<<<< HEAD
        val layoutInflater = LayoutInflater.from(parent.context)                            //부모의 context를 가져옴
        val binding = ItemDbBinding.inflate(layoutInflater, parent, false)     //부모 레이아웃 바인딩
        return DBViewHolder(binding)                                                        //바인딩 리턴
    }

    override fun onBindViewHolder(holder: DBViewHolder, position: Int) {
        holder.binding.db = list[position]                                                  //바인드된 view 객체에 리스트 저장
    }

    override fun getItemCount(): Int {
        return list.size                                                                    //리스트 사이즈 리턴
=======
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemDbBinding.inflate(layoutInflater, parent, false)
        return DBViewHolder(binding)

    }

    override fun onBindViewHolder(holder: DBViewHolder, position: Int) {
        holder.binding.db = list[position]
    }

    override fun getItemCount(): Int {
        return list.size
>>>>>>> c6b32022a662bda61efeb35214f1a0da5400d90c
    }
}