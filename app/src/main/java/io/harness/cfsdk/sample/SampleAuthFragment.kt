package io.harness.cfsdk.sample

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import io.harness.cfsdk.*

class SampleAuthFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

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
        setView(harnessAccount, harnessAccountSpinner,Constants.Account.HARNESS)

        val paoloAccount = view.findViewById<TextView>(R.id.paolo_account)
        val paoloAccountSpinner:View = view.findViewById(R.id.paolo_spinner_view)
        setView(paoloAccount, paoloAccountSpinner, Constants.Account.PAOLO)

        return view
    }

    private fun open(account: Constants.Account) {
        Constants.selectedAccount = account.accountName
        val remoteConfiguration = CfConfiguration.builder()
            .enableStream(true)
            .pollingInterval(10)
            .target(account.accountName)
            .build()

        CfClient.getInstance().initialize(context, Constants.CF_SDK_API_KEY, remoteConfiguration) {
            Handler(Looper.getMainLooper()).post {
                fragmentManager?.beginTransaction()
                    ?.replace(R.id.main_fragment_holder, FeaturesFragment.newInstance())
                    ?.addToBackStack("FeaturesFragment")
                    ?.commit()
            }

        }
    }

    private fun setView(textView: TextView, spinner:View, account: Constants.Account) {
        textView.text = account.accountName
        textView.setOnClickListener {
            spinner.visibility = View.VISIBLE
            open(account)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SampleAuthFragment()
    }
}