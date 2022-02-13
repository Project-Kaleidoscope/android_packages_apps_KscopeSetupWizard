package ink.kaleidoscope.setupwizard.pages

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import androidx.activity.result.contract.ActivityResultContracts

import com.android.internal.app.LocalePicker
import com.android.internal.app.LocaleStore;

import ink.kaleidoscope.setupwizard.Page
import ink.kaleidoscope.setupwizard.R

import java.util.*


class WelcomePage : Page() {

    override val showActionBar: Boolean = false

    private lateinit var mLanguageView: TextView
    private lateinit var mNextBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_welcome_page, container, false)
        mLanguageView = rootView.findViewById(R.id.welcome_language)
        mNextBtn = rootView.findViewById(R.id.welcome_btn)

        initLanguageView()
        initNextBtn()
        return rootView
    }

    private fun initLanguageView() {
        mLanguageView.text = LocalePicker.getLocales()[0].displayName

        val launcher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            val li = it.data?.getSerializableExtra("localeInfo") as LocaleStore.LocaleInfo?
            li?.let {
                LocalePicker.updateLocale(it.locale)
            }
        }

        mLanguageView.setOnClickListener {
            launcher.launch(Intent().apply {
                component = ComponentName(
                    "com.android.settings",
                    "com.android.settings.localepicker.LocalePickerWithRegionActivity"
                )
            })
        }
    }

    private fun initNextBtn() {
        mNextBtn.setOnClickListener {
            onNextBtnClicked()
        }
    }
}
