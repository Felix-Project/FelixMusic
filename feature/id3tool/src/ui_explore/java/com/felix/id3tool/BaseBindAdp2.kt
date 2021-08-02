package com.felix.id3tool

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.felix.utils.utils.ITAG
import kotlinx.coroutines.MainScope

/**
 * @Author: Mingfa.Huang
 * @Date: 2020/8/14
 * @Des: BaseAdp
 */
abstract class BaseBindAdp2<T : Selectable, vb1 : ViewBinding, vb2 : ViewBinding> :
    RecyclerView.Adapter<BaseBindAdp2.CommonVH>(), ITAG {
    companion object {
        const val VIEW_TYPE1 = 1
        const val VIEW_TYPE2 = 2
    }

    protected val scope by lazy {
        MainScope()
    }

    protected val ViewGroup.layoutInflater
        get() = LayoutInflater.from(context)

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

    override fun getItemViewType(position: Int): Int {
        return if (datas[position].select()) VIEW_TYPE1 else VIEW_TYPE2
    }

    abstract fun getBindging(parent: ViewGroup, viewType: Int): ViewBinding

    abstract fun onDataChange(binding: vb1, data: T, pos: Int, size: Int)
    abstract fun onDataChange2(binding: vb2, data: T, pos: Int, size: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonVH =
        getBindging(parent, viewType).let {
            CommonVH(it.root, it)
        }


    var onItemClickListener: ((view: View, data: T, position: Int, size: Int) -> Unit)? = null

    var onLongItemClickListener: ((view: View, data: T, position: Int, size: Int) -> Boolean)? =
        null

    override fun getItemCount() = datas.size

    override fun onBindViewHolder(holder: CommonVH, position: Int) {
        if (datas[position].select()) {
            onDataChange(holder.binding as vb1, datas[position], position, datas.size)
        } else {
            onDataChange2(holder.binding as vb2, datas[position], position, datas.size)
        }
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(it, datas[position], position, datas.size)
        }
        holder.itemView.setOnLongClickListener {
            onLongItemClickListener?.invoke(it, datas[position], position, datas.size)
                ?: false
        }
    }

    class CommonVH(view: View, var binding: ViewBinding) :
        RecyclerView.ViewHolder(view)
}

interface Selectable {
    fun select(): Boolean
}