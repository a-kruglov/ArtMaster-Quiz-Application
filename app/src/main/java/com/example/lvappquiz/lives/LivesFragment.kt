package com.example.lvappquiz.lives
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import com.example.lvappquiz.R

class LivesFragment : Fragment() {

    // Массив для хранения ImageView элементов жизней
    private lateinit var lives: Array<ImageView>

    // Получение экземпляра ViewModel
    private val viewModel: LivesViewModel by activityViewModels()

    // Метод вызывается при создании представления фрагмента
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Загрузка разметки фрагмента
        return inflater.inflate(R.layout.fragment_lives, container, false)
    }

    // Метод вызывается после создания представления фрагмента
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Получение массива идентификаторов ImageView элементов из ресурсов
        val livesIds = resources.obtainTypedArray(R.array.lives_ids)

        // Инициализация массива жизней с использованием полученных идентификаторов
        lives = Array(livesIds.length()) { i ->
            view.findViewById(livesIds.getResourceId(i, -1))
        }

        // Наблюдение за изменением количества жизней в ViewModel
        viewModel.lives.observe(viewLifecycleOwner) { onLivesChanged(it) }

        // Освобождение ресурсов, используемых для массива идентификаторов
        livesIds.recycle()
    }

    // Метод вызывается при изменении количества жизней
    private fun onLivesChanged(newLives: Int) {
        if (newLives > 0) {
            // Отображение видимых ImageView элементов для каждой жизни
            for (i in 0 until newLives) {
                lives[i].visibility = View.VISIBLE
            }
            // Скрытие невидимых ImageView элементов для оставшихся жизней
            for (i in newLives until lives.size) {
                lives[i].visibility = View.INVISIBLE
            }
        }
    }
}

