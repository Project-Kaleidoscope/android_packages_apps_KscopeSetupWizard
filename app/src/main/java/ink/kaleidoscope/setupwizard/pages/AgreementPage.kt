package ink.kaleidoscope.setupwizard.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.TextView

import ink.kaleidoscope.setupwizard.Page
import ink.kaleidoscope.setupwizard.R

import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.util.*

class AgreementPage : Page() {

    private lateinit var mAgreementView: TextView
    private lateinit var mScrollView: ScrollView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_agreement_page, container, false)
        mAgreementView = rootView.findViewById(R.id.agreement_view)
        mScrollView = rootView.findViewById(R.id.agreement_scrollView)
        mAgreementView.text = loadAgreement()
        return rootView
    }

    override fun onNextBtnClicked() {
        if (mScrollView.scrollY + mScrollView.height >= mAgreementView.height)
            super.onNextBtnClicked()
        else
            mScrollView.fling(10000)
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
