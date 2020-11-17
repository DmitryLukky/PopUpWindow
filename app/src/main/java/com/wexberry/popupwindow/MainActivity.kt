package com.wexberry.popupwindow

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var popUpWindow: PopupWindow? = null
    private var selectedItem: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnClick()
    }

    override fun onStop() {
        super.onStop()

        dismissPopup()
    }

    private fun btnClick() {
        btnShowPopUp.setOnClickListener {
            // Закрываем PopUp если он открыт и не открываем снова
            dismissPopup()
            // Показываем PopUp
            popUpWindow = showPopUp()
            // Указывает, будет ли всплывающее окно получать информацию о событиях касания за пределами его окна
            popUpWindow?.isOutsideTouchable = true
            // Может ли всплывающее окно захватить фокус
            popUpWindow?.isFocusable = true
            // Задаём высоту (без этого параметра не отображается тень)
            popUpWindow!!.elevation = 50F
            // Задает фон, который можно рисовать для этого всплывающего окна. Фон можно установить на null
            // Без этоого на Андроид 5 окно не будет закрывать при нажатии на пустое место экрана.
            popUpWindow?.setBackgroundDrawable(ColorDrawable(Color.WHITE)) // Без этоого на Андроид 5 окно не будет закрывать при нажатии на пустое место экрана.
            // PopUp появляется от левого нижнего угла кнопки
            popUpWindow?.showAsDropDown(btnShowPopUp)
        }
    }

    // Закрываем PopUp
    private fun dismissPopup() {
        popUpWindow?.let {
            if (it.isShowing) {
                Log.d("TAG", "${it.isShowing}")
                it.dismiss()

                // Меняем иконку в Button
                replaceIconButton(R.drawable.ic_baseline_expand_more)
            }
            popUpWindow = null
        }
    }

    // Меняем иконку в Button
    fun replaceIconButton(image: Int) {
        btnShowPopUp.setCompoundDrawablesWithIntrinsicBounds(0, 0, image, 0)
    }

    // Наполняем PopUp (Текст, картинки..)
    private fun getItemList(): List<ItemList> {
        val itemList = mutableListOf<ItemList>()

        itemList.add(ItemList(0, "Первая ячейка", 0, 0, 0, 0, 0))
        itemList.add(
            ItemList(
                R.drawable.ic_lamp,
                "Вторая ячейка",
                R.color.orange,
                R.color.green,
                R.color.white,
                R.color.red,
                R.drawable.ic_lamp
            )
        )
        itemList.add(
            ItemList(
                R.drawable.ic_lamp,
                "Третья ячейка",
                R.color.white,
                R.color.black,
                R.color.black,
                R.color.white,
                0
            )
        )
        itemList.add(ItemList(R.drawable.ic_lamp, "Четвертая ячейка", 0, 0, 0, 0, 0))

        return itemList
    }

    // Создаём PopUp
    private fun showPopUp(): PopupWindow {
        // Меняем иконку в Button
        replaceIconButton(R.drawable.ic_baseline_expand_less)

        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.popup_window, null)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewPopUp)

        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                DividerItemDecoration.VERTICAL
            )
        )
        val adapter = AdapterPopUp(this)
        adapter.addItem(getItemList())
        recyclerView.adapter = adapter
        adapter.selectedItem(selectedItem)

        adapter.setOnClick(object : RecyclerViewCallbacks<ItemList> {
            override fun onItemClick(view: View, position: Int, item: ItemList) {
                selectedItem = position
                btnShowPopUp.text = item.name
                dismissPopup()
            }
        })
        return PopupWindow(
            view,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}