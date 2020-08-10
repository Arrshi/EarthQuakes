package com.hfad.ideasworld.ui.earthquake

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hfad.ideasworld.database.DataBaseEarthQuake
import com.hfad.ideasworld.databinding.ErathQuakeItemBinding


class EarthQuakeAdapter(private val clickListener: OnClickListenerEarthQuake) :ListAdapter<DataBaseEarthQuake,EarthQuakeAdapter.ViewHolder>(EarthQuakeDiffCallBack()){
    class ViewHolder private constructor(private val binding: ErathQuakeItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(item: DataBaseEarthQuake,clickListener: OnClickListenerEarthQuake){
            binding.data=item
            binding.clickListener=clickListener
            binding.executePendingBindings()
        }
        companion object{
            fun from(parent: ViewGroup):ViewHolder{
                val inflater=LayoutInflater.from(parent.context)
                val binding=ErathQuakeItemBinding.inflate(inflater,parent,false)
                return ViewHolder(binding)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item,clickListener)
    }
}

class EarthQuakeDiffCallBack:DiffUtil.ItemCallback<DataBaseEarthQuake>(){
    override fun areItemsTheSame(
        oldItem: DataBaseEarthQuake,
        newItem: DataBaseEarthQuake
    ): Boolean {
        return oldItem.publicID==newItem.publicID
    }

    override fun areContentsTheSame(
        oldItem: DataBaseEarthQuake,
        newItem: DataBaseEarthQuake
    ): Boolean {
       return oldItem==newItem
    }

}

class OnClickListenerEarthQuake(val clickListener:(item:String)->Unit){
    fun setListener(earthQuake: DataBaseEarthQuake)=clickListener(earthQuake.publicID)
}
