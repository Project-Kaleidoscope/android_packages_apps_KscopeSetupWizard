package ink.kaleidoscope.setupwizard.pages

import android.app.StatusBarManager
import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import ink.kaleidoscope.setupwizard.DEBUG_DO_NOT_DISABLE
import ink.kaleidoscope.setupwizard.MainActivity
import ink.kaleidoscope.setupwizard.Page
import ink.kaleidoscope.setupwizard.R

class DonePage : Page() {

    override val showActionBar: Boolean = false

    private lateinit var mButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_done_page, container, false)
        mButton = rootView.findViewById(R.id.done_btn)
        mButton.setOnClickListener {
            doneSetupwizard()
        }
        return rootView
    }

    private fun doneSetupwizard() {
        val activity = requireActivity()

        Settings.Global.putInt(
            activity.getContentResolver(),
            Settings.Global.DEVICE_PROVISIONED,
            1
        )
        Settings.Secure.putInt(
            activity.getContentResolver(),
            Settings.Secure.USER_SETUP_COMPLETE,
            1
        )

        val pm: PackageManager = activity.packageManager
        val name = ComponentName(activity, MainActivity::class.java)

        if (!DEBUG_DO_NOT_DISABLE)
            pm.setComponentEnabledSetting(
                name, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )

        MainActivity.sStatusBarManager?.apply {
            setDisabledForSetup(false)
        }

        activity.finish()
    }
}
