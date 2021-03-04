package io.harness.cfsdk.sample

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.harness.cfsdk.R

class ModulesAdapter(val activityContext: Context): RecyclerView.Adapter<ModulesViewHolder>() {


    val list = mutableListOf<Module>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModulesViewHolder {
        val view = LayoutInflater.from(activityContext).inflate(R.layout.item_single_feature, parent, false)
        return ModulesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.count { it.enabed }
    }

    override fun onBindViewHolder(holder: ModulesViewHolder, position: Int) {
        val item = list.filter { it.enabed }[position]

        if (!item.enabed) {
            holder.itemView.visibility = View.GONE
        } else {
            holder.newBanner.visibility = if (item.moduleViewConfig.enableRibbon) View.VISIBLE else View.INVISIBLE
            holder.itemView.visibility = View.VISIBLE
            holder.mainCardLayout.setCardBackgroundColor(activityContext.resources.getColor(item.moduleViewConfig.backgroundColor))
            holder.descriptionView.text = item.description
            holder.logo.setImageResource(item.moduleViewConfig.imageSrc)
            holder.trialPeriod.text = String.format(activityContext.getString(R.string.trial_period), item.trialPeriod)
            holder.trialPeriod.setTextColor(activityContext.resources.getColor(item.moduleViewConfig.textColor))
        }
    }

}