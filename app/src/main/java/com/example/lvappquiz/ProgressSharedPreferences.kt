import android.content.Context
import android.content.SharedPreferences
import com.example.lvappquiz.questions.QuestionType

class ProgressSharedPreferences(context: Context) {
    private val PREFS_NAME = "com.example.lvappquiz.shared_prefs"
    private val sharedPref: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    fun getProgress(questionType: QuestionType): Int {
        return sharedPref.getInt(questionType.toString(), 0)
    }
    fun saveProgress(questionType: QuestionType, progress: Int) {
        with (sharedPref.edit()) {
            putInt(questionType.toString(), progress)
            apply()
        }
    }
}
