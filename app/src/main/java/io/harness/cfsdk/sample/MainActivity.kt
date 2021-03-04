package io.harness.cfsdk.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.harness.cfsdk.CfClient
import io.harness.cfsdk.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().add(R.id.main_fragment_holder, SampleAuthFragment.newInstance())
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        println("main activity - destroy view")
        CfClient.getInstance()?.destroy()
    }
}