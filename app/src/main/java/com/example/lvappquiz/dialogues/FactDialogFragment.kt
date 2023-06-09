package com.example.lvappquiz.dialogues

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.lvappquiz.databinding.FragmentFactBinding

class FactDialogFragment : DialogFragment() {

    // Поле для хранения связки с разметкой фрагмента
    private lateinit var binding: FragmentFactBinding

    // Переменная для слушателя закрытия диалогового окна
    var dialogCloseListener: DialogCloseListener? = null

    // Метод вызывается при создании представления фрагмента
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Настройка связки с разметкой фрагмента
        binding = FragmentFactBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Метод вызывается после создания представления фрагмента
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Получение строки "fact" из аргументов фрагмента
        val fact = arguments?.getString("fact")
        Log.d("FactDialogFragment", "Текст факта: $fact")

        // Установка текста факта в TextView
        binding.factTextView.text = fact

        // Назначение слушателя на кнопку "ОК"
        binding.okayButton.setOnClickListener {
            // Закрытие диалогового окна
            dismissAllowingStateLoss()

            // Вызов метода onDialogClose() у слушателя закрытия диалогового окна
            dialogCloseListener?.onDialogClose()
        }
    }

    // Метод вызывается перед отображением диалогового окна
    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            // Установка ширины и высоты диалогового окна
            dialog.window?.setLayout(width, height)
        }
    }

    // Интерфейс для слушателя закрытия диалогового окна
    interface DialogCloseListener {
        fun onDialogClose()
    }
}

