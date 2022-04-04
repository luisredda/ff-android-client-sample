package io.harness.sample.cfsdk

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import io.harness.cfsdk.R

class ModulesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val descriptionView = view.findViewById<TextView>(R.id.feature_description)
    val logo = view.findViewById<ImageView>(R.id.feature_logo)
    val trialPeriod = view.findViewById<TextView>(R.id.trial_period)
    val newBanner = view.findViewById<ImageView>(R.id.new_banner)
    val mainCardLayout = view.findViewById<MaterialCardView>(R.id.main_card_layout)

    init {
        view.findViewById<View>(R.id.enable_button).visibility = View.VISIBLE
        trialPeriod.visibility = View.VISIBLE

        newBanner.visibility = View.INVISIBLE
    }
}