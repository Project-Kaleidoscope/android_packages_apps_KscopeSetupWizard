package ink.kaleidoscope.setupwizard.pages

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ScrollView
import android.widget.TextView

import ink.kaleidoscope.setupwizard.Page
import ink.kaleidoscope.setupwizard.R

import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.util.*

class AgreementPage : Page() {

    private lateinit var mAgreementView: TextView
    private lateinit var mAgree: CheckBox
    private lateinit var mScrollView: ScrollView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_agreement_page, container, false)
        mAgreementView = rootView.findViewById(R.id.agreement_view)
        mAgree = rootView.findViewById(R.id.agreement_agree)
        mScrollView = rootView.findViewById(R.id.agreement_scrollView)
        mAgreementView.text = loadAgreement()
        return rootView
    }

    override fun onNextBtnClicked() {
        if (mAgree.isChecked)
            super.onNextBtnClicked()
        else
            AlertDialog.Builder(requireContext())
                .setTitle(R.string.agreement_alert_title)
                .setMessage(R.string.agreement_alert_message)
                .setPositiveButton(
                    android.R.string.ok
                ) { _, _ -> mScrollView.fling(10000) }
                .create().show()
    }

    private fun loadAgreement(): String {
        val locale = Locale.getDefault()
        val reader = try {
            InputStreamReader(requireActivity().assets.open("agreement-${locale.language}_${locale.country}.txt"))
        } catch (e: FileNotFoundException) {
            InputStreamReader(requireActivity().assets.open("agreement.txt"))
        }
        val ret = reader.readText()
        reader.close()
        return ret
    }
}
