package com.felix.music.core

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.felix.utils.utils.ITAG

/**
 * @Author: Mingfa.Huang
 * @Date: 2020/8/14
 * @Des: BaseAdp
 */
abstract class BaseBindingAdp<T, vb : ViewBinding> :
    RecyclerView.Adapter<BaseBindingAdp.CommonVH>(), ITAG {

    var datas = mutableListOf<T>()
        set(value) {
            field.clear()
            field.addAll(value)
            notifyDataSetChanged()
        }

    fun addData(vararg data: T) {
        datas.addAll(data)
        notifyDataSetChanged()
    }

    fun addData(data: List<T>) {
        datas.addAll(data)
        notifyDataSetChanged()
    }

    abstract fun getBindging(parent: ViewGroup, viewType: Int): vb

    abstract fun onDataChange(binding: vb, data: T, pos: Int, size: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonVH =
        getBindging(parent, viewType).let {
            CommonVH(it.root, it)
        }


    var onItemClickListener: ((view: View, data: T, position: Int, size: Int) -> Unit)? = null


    override fun getItemCount() = datas.size

    override fun onBindViewHolder(holder: CommonVH, position: Int) {
        onDataChange(holder.binding as vb, datas[position], position, datas.size)
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(it, datas[position], position, datas.size)
        }
    }

    class CommonVH(view: View, var binding: ViewBinding) : RecyclerView.ViewHolder(view)
}