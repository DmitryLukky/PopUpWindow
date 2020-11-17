package com.wexberry.popupwindow

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class AdapterPopUp(val context: Context) : RecyclerView.Adapter<AdapterPopUp.MyViewHolder>() {
    var itemList: List<ItemList> = mutableListOf()

    private var selectedItem: Int = -1 // Ячейка выбрана
    var callback: RecyclerViewCallbacks<ItemList>? = null

    // Добавляем item
    fun addItem(list: List<ItemList>) {
        itemList = list.toMutableList()
        notifyDataSetChanged() // Обновляем список
    }

    // Выбранная ячейка
    fun selectedItem(position: Int) {
        selectedItem = position
        notifyItemChanged(position) // Обновляем итем
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemList[position]

        // Устанавливаем текст
        holder.nameItem.text = item.name
        // Устанавливаем картинку
        if (item.image != 0) {
            holder.imageItem.background = ContextCompat.getDrawable(context, item.image)
        }
        // Устанавливаем иконку выбранной ячейки
        if (item.icon != 0) {
            holder.selectedIcon.background = ContextCompat.getDrawable(context, item.icon)
        }
        // Устанавливаем цвет текста
        if (item.defColorText != 0) {
            holder.nameItem.setTextColor(ContextCompat.getColorStateList(context, item.defColorText))//ContextCompat.getColorStateList(context, item.defColorText)
        }
        // Устанавливаем цвет фона ячейки
        if (item.defColorBackground != 0) {
            holder.layoutRecyclerView.background = ContextCompat.getDrawable(context, item.defColorBackground)
        }

        // Если ячейка выбрана, то меняем цвета и показываем/скрываем иконку
        if (position == selectedItem) {

            // Картинка становится другим цветом когда ячейка выбрана
//          if (item.image != 0) {
//              holder.imageItem.backgroundTintList = ContextCompat.getColorStateList(context, R.color.purple_700)
//          }

            // Устанавливаем цвет текста выбранной ячейки
            if (item.selectColorText != 0) {
                holder.nameItem.setTextColor(ContextCompat.getColorStateList(context, item.selectColorText))
            }
            // Устанавливаем цвет фона выбранной ячейки
            if (item.selectColorBackground != 0) {
                holder.layoutRecyclerView.background = ContextCompat.getDrawable(context, item.selectColorBackground)
            }
            // holder.nameItem.setTextColor(ContextCompat.getColor(context, R.color.purple_700))
            holder.selectedIcon.visibility = View.VISIBLE
        } else {
            holder.selectedIcon.visibility = View.INVISIBLE
        }
    }

    fun setOnClick(click: RecyclerViewCallbacks<ItemList>) {
        callback = click
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_view, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class MyViewHolder(item: View) : RecyclerView.ViewHolder(item) {

        var nameItem: TextView = itemView.findViewById(R.id.name_item) // Текст в ячейке
        var imageItem: ImageView = itemView.findViewById(R.id.image_item) // Иконка в ячейке
        var selectedIcon: ImageView = itemView.findViewById(R.id.selected_item) // Иконка для выбранной ячейке (появляется только у выбранных ячейках)
        var layoutRecyclerView: ConstraintLayout = itemView.findViewById(R.id.items_constraint_layout) // Layout с итемами
        var defColorText = nameItem.textColors  // Цвет текста в ячейке
        var selectColorText = nameItem.textColors // Цвет текста в выбранной ячейке
        var defColorBackground = layoutRecyclerView.background // Цвет фона ячейки
        var selectColorBackground = layoutRecyclerView.background // Цвет фона выбранной ячейки

        init {
            setClickListener(layoutRecyclerView)
        }

        private fun setClickListener(view: View) {
            view.setOnClickListener {
                callback?.onItemClick(it, adapterPosition, itemList[adapterPosition])
            }
        }
    }
}