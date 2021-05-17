package io.harness.cfsdk.sample

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import io.harness.cfsdk.CfClient
import io.harness.cfsdk.cloud.oksse.model.StatusEvent
import io.harness.cfsdk.R
import io.harness.cfsdk.cloud.core.model.Evaluation
import io.harness.cfsdk.cloud.events.EvaluationListener
import io.harness.cfsdk.cloud.oksse.EventsListener
import io.harness.cfsdk.logging.CfLog

class FeaturesFragment : Fragment() {

    private val logTag = FeaturesFragment::class.simpleName

    private var mainView: View? = null
    private var helpButton: TextView? = null
    private var featureLogo: ImageView? = null
    private var adapter: ModulesAdapter? = null
    private var mainCardLayout: MaterialCardView? = null

    private lateinit var cv: Module
    private lateinit var ci: Module
    private lateinit var ce: Module
    private lateinit var cf: Module

    private lateinit var moreModulesView: TextView
    private lateinit var enabledModulesView: TextView

    private var eventsListener = EventsListener { event ->

        CfLog.OUT.v(logTag, "Event: ${event.eventType}")

        if (event.eventType == StatusEvent.EVENT_TYPE.EVALUATION_CHANGE) {
            val evaluation: Evaluation = event.extractPayload()
            when (evaluation.flag) {
                Constants.CFFlags.ENABLE_CE_MODULE.flag, Constants.CFFlags.TRIAL_LIMIT_CE.flag -> loadCE()
                Constants.CFFlags.ENABLE_CV_MODULE.flag, Constants.CFFlags.TRIAL_LIMIT_CV.flag -> loadCV()
                Constants.CFFlags.ENABLE_CI_MODULE.flag, Constants.CFFlags.TRIAL_LIMIT_CI.flag -> loadCI()
                Constants.CFFlags.ENABLE_CF_MODULE.flag, Constants.CFFlags.CF_RIBBON.flag -> loadCF()
            }
            Handler(Looper.getMainLooper()).post {
                adapter?.notifyDataSetChanged()
            }
        } else if (event.eventType == StatusEvent.EVENT_TYPE.EVALUATION_RELOAD) {

            loadCE()
            loadCV()
            loadCI()
            loadCF()

            adapter?.list?.forEach {
                applyMode(it)
            }

            applyOnHelp()
            Handler(Looper.getMainLooper()).post {
                applyMode()
                adapter?.notifyDataSetChanged()
            }
        }
    }

    private val darkModeListener: EvaluationListener = EvaluationListener {
        Handler(Looper.getMainLooper()).post {
            adapter?.list?.forEach { applyMode(it) }
            applyMode()
            adapter?.notifyDataSetChanged()
        }
    }

    private val globalHelpListener: EvaluationListener = EvaluationListener {
        Handler(Looper.getMainLooper()).post {
            applyOnHelp()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_features, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.modules_recycler_view)

        mainCardLayout = view.findViewById(R.id.main_card_layout)
        mainView = view.findViewById(R.id.main_view)
        enabledModulesView = view.findViewById(R.id.module_enabled_view)
        moreModulesView = view.findViewById(R.id.more_modules_view)
        featureLogo = view.findViewById(R.id.feature_logo)
        helpButton = view.findViewById(R.id.help_button)

        adapter = ModulesAdapter(activity!!)
        val manager = GridLayoutManager(activity, 2)
        manager.orientation = RecyclerView.VERTICAL
        recyclerView.layoutManager = manager

        recyclerView.adapter = adapter

        cv = Module(
            getString(R.string.cv_description),
            7,
            true,
            ModuleType.CV,
            ModuleViewConfig(R.color.white, R.color.black, ModuleType.CV.resourceLight)
        )
        ci = Module(
            getString(R.string.ci_description),
            7,
            true,
            ModuleType.CI,
            ModuleViewConfig(R.color.white, R.color.black, ModuleType.CI.resourceLight)
        )
        ce = Module(
            getString(R.string.ce_description),
            14,
            true,
            ModuleType.CE,
            ModuleViewConfig(R.color.white, R.color.black, ModuleType.CE.resourceLight)
        )
        cf = Module(
            getString(R.string.cf_description),
            30,
            true,
            ModuleType.CF,
            ModuleViewConfig(R.color.white, R.color.black, ModuleType.CF.resourceLight)
        )

        adapter?.list?.clear()
        adapter?.list?.add(cv)
        adapter?.list?.add(ci)
        adapter?.list?.add(ce)
        adapter?.list?.add(cf)

        val registerEventsOk = CfClient.getInstance().registerEventsListener(eventsListener)

        var registerEvaluationsOk = CfClient.getInstance().registerEvaluationListener(

            Constants.CFFlags.DARK_MODE.flag,
            darkModeListener
        )

        if (registerEvaluationsOk) {
            registerEvaluationsOk = CfClient.getInstance().registerEvaluationListener(

                Constants.CFFlags.ENABLE_GLOBAL_HELP.flag,
                globalHelpListener
            )
        }

        if (registerEventsOk && registerEvaluationsOk) {

            loadCE()
            loadCV()
            loadCI()
            loadCF()

            applyMode()
            adapter?.list?.forEach {

                applyMode(it)
            }
            applyOnHelp()

            adapter?.notifyDataSetChanged()
        } else {

            activity?.let {

                val msg = "Registration(s) failed"
                Toast.makeText(it, msg, Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    private fun applyMode(module: Module) {

        val darkMode = CfClient.getInstance().boolVariation(Constants.CFFlags.DARK_MODE.flag, false)
        module.moduleViewConfig.backgroundColor = if (darkMode) R.color.black else R.color.white
        module.moduleViewConfig.textColor = if (darkMode) R.color.white else R.color.black

        val imgSrc = if (darkMode) {
            module.moduleType.resourceDark
        } else {
            module.moduleType.resourceLight
        }
        module.moduleViewConfig.imageSrc = imgSrc
    }

    private fun applyMode() {
        activity?.let {

            if (it.isFinishing) {
                return
            }

            val darkMode: Boolean = CfClient.getInstance()
                .boolVariation(Constants.CFFlags.DARK_MODE.flag, false)

            val textColor = if (darkMode) R.color.white else R.color.black
            val backgroundColor = if (darkMode) R.color.black else R.color.white
            val imgSrc = if (darkMode) R.drawable.cd_dark else R.drawable.cd

            enabledModulesView.setTextColor(ContextCompat.getColor(it, textColor))
            moreModulesView.setTextColor(ContextCompat.getColor(it, textColor))
            mainView?.setBackgroundColor(ContextCompat.getColor(it, backgroundColor))
            mainCardLayout?.setCardBackgroundColor(ContextCompat.getColor(it, backgroundColor))

            featureLogo?.setImageResource(imgSrc)
        }
    }

    private fun applyOnHelp() {
        val helpEvaluation = CfClient.getInstance().boolVariation(
            Constants.CFFlags.ENABLE_GLOBAL_HELP.flag,
            false
        )
        val helpEnabled: Boolean = helpEvaluation
        helpButton?.visibility = if (helpEnabled) View.VISIBLE else View.GONE
    }

    private fun loadCF() {
        val cfEvaluation = CfClient.getInstance().boolVariation(
            Constants.CFFlags.ENABLE_CF_MODULE.flag,
            false
        )
        val cfTrialEvaluation = CfClient.getInstance()
            .numberVariation(Constants.CFFlags.TRIAL_LIMIT_CF.flag, 7.0)
        val cfRibbonEvaluation = CfClient.getInstance()
            .boolVariation(Constants.CFFlags.CF_RIBBON.flag, false)
        cf.enabed = cfEvaluation
        cf.trialPeriod = cfTrialEvaluation.toInt()
        cf.moduleViewConfig.enableRibbon = cfRibbonEvaluation

    }

    private fun loadCI() {
        val ciEvaluation = CfClient.getInstance().boolVariation(
            Constants.CFFlags.ENABLE_CI_MODULE.flag,
            false
        )
        ci.enabed = ciEvaluation
        val ciTrialEvaluation = CfClient.getInstance()
            .numberVariation(Constants.CFFlags.TRIAL_LIMIT_CI.flag, 7.0)
        ci.trialPeriod = ciTrialEvaluation
    }

    private fun loadCV() {
        val cvEvaluation = CfClient.getInstance().boolVariation(
            Constants.CFFlags.ENABLE_CV_MODULE.flag,
            false
        )
        cv.enabed = cvEvaluation
        val cvTrialEvaluation = CfClient.getInstance()
            .numberVariation(Constants.CFFlags.TRIAL_LIMIT_CV.flag, 7.0)
        cv.trialPeriod = cvTrialEvaluation
    }

    private fun loadCE() {
        val ceEvaluation = CfClient.getInstance().boolVariation(
            Constants.CFFlags.ENABLE_CE_MODULE.flag,
            false
        )
        ce.enabed = ceEvaluation
        val ceTrialEvaluation = CfClient.getInstance()
            .numberVariation(Constants.CFFlags.TRIAL_LIMIT_CE.flag, 7.0)
        ce.trialPeriod = ceTrialEvaluation
    }

    override fun onDestroyView() {
        super.onDestroyView()

        CfClient.getInstance().unregisterEventsListener(eventsListener)
        CfClient.getInstance().unregisterEvaluationListener(

            Constants.CFFlags.DARK_MODE.flag, darkModeListener
        )
        CfClient.getInstance().unregisterEvaluationListener(

            Constants.CFFlags.ENABLE_GLOBAL_HELP.flag,
            globalHelpListener
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() = FeaturesFragment()
    }
}