// MainActivity.kt

package tn.esprit.version_checker

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.version_checker.network.RetrofitClient
import tn.esprit.version_checker.network.VersionInfo // Import correct VersionInfo class

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkAndUpdateAppVersion()
    }

    private fun checkAndUpdateAppVersion() {
        val currentVersion = BuildConfig.VERSION_NAME

        // Fetch the latest version from the server
        val call = RetrofitClient.instance.getLatestVersion()
        call.enqueue(object : Callback<VersionInfo> {
            override fun onResponse(call: Call<VersionInfo>, response: Response<VersionInfo>) {
                if (response.isSuccessful) {
                    val latestVersion = response.body()?.version
                    val isMandatory = response.body()?.mandatory ?: false

                    if (latestVersion != null && latestVersion > currentVersion) {
                        // Show popup to install new version
                        showInstallPopup(isMandatory)
                    } else {
                        Log.d("VersionUpdate", "App version is up to date")
                    }
                } else {
                    Log.e("VersionUpdate", "Failed to get latest version. Error code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<VersionInfo>, t: Throwable) {
                Log.e("VersionUpdate", "Error fetching latest version: ${t.message}", t)
            }
        })
    }

    private fun showInstallPopup(isMandatory: Boolean) {
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_update, null)

        val tvTitle: TextView = dialogView.findViewById(R.id.tvTitle)
        val tvMessage: TextView = dialogView.findViewById(R.id.tvMessage)
        val btnInstall: Button = dialogView.findViewById(R.id.btnInstall)
        val btnCancel: Button = dialogView.findViewById(R.id.btnCancel)

        val builder = AlertDialog.Builder(this)
            .setView(dialogView)

        val dialog = builder.create()

        if (!isMandatory) {
            btnCancel.visibility = View.VISIBLE
            btnCancel.setOnClickListener { dialog.dismiss() }
        }

        btnInstall.setOnClickListener {
            // Implement installation logic here, such as opening a download link
            // Example: openPlayStoreForAppUpdate()
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val window = dialog.window
        if (window != null) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(window.attributes)
            layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
            layoutParams.gravity = Gravity.CENTER
            window.attributes = layoutParams
        }
        dialog.show()
    }

}
