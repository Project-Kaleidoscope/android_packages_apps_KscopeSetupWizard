package ink.kaleidoscope.setupwizard.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceFragmentCompat

import ink.kaleidoscope.setupwizard.Page
import ink.kaleidoscope.setupwizard.R

class GmsPage : Page() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        savedInstanceState ?: childFragmentManager.beginTransaction()
            .add(R.id.gms_pref, SettingsPreference(), "SettingsPreference").commit()
        return inflater.inflate(R.layout.fragment_gms_page, container, false)
    }

    class SettingsPreference : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.gms, rootKey)
        }
    }
}
