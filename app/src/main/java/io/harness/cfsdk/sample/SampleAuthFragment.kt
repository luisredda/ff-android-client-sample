package io.harness.cfsdk.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import io.harness.cfsdk.*
import io.harness.cfsdk.cloud.model.Target
import io.harness.cfsdk.logging.CfLog

class SampleAuthFragment : Fragment() {

    private val logTag = SampleAuthFragment::class.simpleName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_sample_auth, container, false)

        val aptivAccount = view.findViewById<TextView>(R.id.aptiv_account)
        val aptivAccountSpinner: View = view.findViewById(R.id.aptinv_spinner_view)
        setView(aptivAccount, aptivAccountSpinner, Constants.Account.APTIV)

        val experianAccount = view.findViewById<TextView>(R.id.experian_account)
        val experianAccountSpinner: View = view.findViewById(R.id.experian_spinner_view)
        setView(experianAccount, experianAccountSpinner, Constants.Account.EXPERIAN)

        val fiservAccount = view.findViewById<TextView>(R.id.fiserv_account)
        val fiservAccountSpinne: View = view.findViewById(R.id.fiserv_spinner_view)
        setView(fiservAccount, fiservAccountSpinne, Constants.Account.FISERV)

        val harnessAccount = view.findViewById<TextView>(R.id.harness_account)
        val harnessAccountSpinner: View = view.findViewById(R.id.harness_spinner_view)
        setView(harnessAccount, harnessAccountSpinner, Constants.Account.HARNESS)

        val paoloAccount = view.findViewById<TextView>(R.id.paolo_account)
        val paoloAccountSpinner: View = view.findViewById(R.id.paolo_spinner_view)
        setView(paoloAccount, paoloAccountSpinner, Constants.Account.PAOLO)

        return view
    }

    private fun open(

        account: Constants.Account,
        views: List<View>
    ) {

        val accName = account.accountName
        val target = Target().identifier(accName)

        Constants.selectedAccount = accName

        val remoteConfiguration = CfConfiguration.builder()
            .enableStream(true)
            .pollingInterval(10)
            .build()

        CfClient.getInstance()
            .initialize(

                context,
                Constants.CF_SDK_API_KEY,
                remoteConfiguration,
                target
            ) { _, result ->

                activity?.let {
                    it.runOnUiThread {

                        views.forEach { view ->

                            view.visibility = View.GONE
                        }
                        if (result.isSuccess) {

                            fragmentManager?.beginTransaction()
                                ?.replace(R.id.main_fragment_holder, FeaturesFragment.newInstance())
                                ?.addToBackStack("FeaturesFragment")
                                ?.commit()
                        } else {

                            val e = result.error
                            var msg = "Initialization error"
                            e?.let { err ->

                                err.message?.let { errMsg ->
                                    msg = errMsg
                                }
                                CfLog.OUT.e(logTag, msg, err)
                            }
                            Toast.makeText(it, msg, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
    }

    private fun setView(

        textView: TextView,
        spinner: View,
        account: Constants.Account
    ) {

        textView.text = account.accountName
        textView.setOnClickListener {

            spinner.visibility = View.VISIBLE
            open(account, listOf(spinner))
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SampleAuthFragment()
    }
}