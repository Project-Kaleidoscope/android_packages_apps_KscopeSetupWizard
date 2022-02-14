package ink.kaleidoscope.setupwizard

import android.content.Context

import androidx.fragment.app.Fragment

import ink.kaleidoscope.setupwizard.pages.AgreementPage
import ink.kaleidoscope.setupwizard.pages.DonePage
import ink.kaleidoscope.setupwizard.pages.GmsPage
import ink.kaleidoscope.setupwizard.pages.WelcomePage

import java.lang.RuntimeException

class Pages {
    companion object {
        private val registeredPages: Array<Class<*>> = arrayOf(
            /* Register pages here.
             * They will appear in order */
            WelcomePage::class.java,
            AgreementPage::class.java,
            GmsPage::class.java,
            DonePage::class.java,
        )

        fun createPage(position: Int): Page {
            val ret = registeredPages[position].newInstance()
            if (ret !is Page)
                throw RuntimeException("You must extend Page class for a new page")
            return ret
        }

        fun getPageCount(): Int {
            return registeredPages.size
        }
    }
}

open class Page : Fragment() {
    companion object {
        /* Let the page save its position because we need to
           make sure that it's still available after recreating. */
        const val POSITION = "position"
    }

    /* Whether action bar should be shown.
     * You can set it to false when you have a custom next button. */
    open val showActionBar = true

    /* Override this function when you want to customize prev button function. */
    open fun onPrevBtnClicked() {
        (requireActivity() as MainActivity).gotoPrevPage()
    }

    /* Override this function when you want to customize next button function. */
    open fun onNextBtnClicked() {
        (requireActivity() as MainActivity).gotoNextPage()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        /* Register itself to mPages because ViewPager2 is not maintaining this */
        (requireActivity() as MainActivity).mPages[requireArguments().getInt(POSITION)] = this
    }

    override fun onResume() {
        super.onResume()
        refreshActionBarStatus()
    }

    private fun refreshActionBarStatus() {
        if (showActionBar)
            (requireActivity() as MainActivity).showActionBarIfCurrent(this)
        else
            (requireActivity() as MainActivity).hideActionBarIfCurrent(this)
    }
}
