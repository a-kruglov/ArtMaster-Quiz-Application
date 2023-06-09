package com.example.lvappquiz
import HarvardArtMuseumsAPI
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.lvappquiz.db.AppDatabase
import com.example.lvappquiz.db.DatabaseInitializer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        val databaseName = "app_database"
        val context: Context = applicationContext
        context.deleteDatabase(databaseName)
        AppDatabase.getInstance(context).close()

        CoroutineScope(Dispatchers.IO).launch {
            val databaseInitializer = DatabaseInitializer()
            databaseInitializer.insertData(this@MainActivity)
            Log.d("GeneralActivity", "Database insertion completed")

            HarvardArtMuseumsAPI.init()

            withContext(Dispatchers.Main) {
                openMenu()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun openMenu() {
        findNavController(R.id.nav_host_fragment).navigate(R.id.menuFragment)
    }
}
