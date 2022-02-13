package ink.kaleidoscope.setupwizard

import android.animation.ObjectAnimator
import android.app.StatusBarManager
import android.os.Bundle
import android.util.FloatProperty
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.FrameLayout

import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

import java.lang.RuntimeException

class MainActivity : FragmentActivity() {

    companion object {
        private val ACTION_BAR_TRANSLATE_Y = object : FloatProperty<FrameLayout>("TranslateY") {
            override fun get(view: FrameLayout?): Float {
                return view?.translationY ?: 0f
            }

            override fun setValue(view: FrameLayout?, value: Float) {
                view?.translationY = value
            }
        }

        /* We should cache a global StatusBarManager because our activity get recreated
         * on language change. And applying setDisabledForSetup(true/false) on different
         * StatusBarManager objects are leading to the problem that the setup mode doesn't
         * quit properly. This can either be an AOSP bug or feature. */
        var sStatusBarManager: StatusBarManager? = null
    }

    /* Save references of Pages because ViewPager2 is not doing well */
    val mPages: HashMap<Int, Page> = HashMap()

    private lateinit var mViewPager: ViewPager2
    private lateinit var mActionBar: FrameLayout

    private val mFragmentAdapter = object : FragmentStateAdapter(this) {
        override fun getItemCount() = Pages.getPageCount()
        override fun createFragment(position: Int): Fragment {
            val page = Pages.createPage(position)
            /* This method is not called when fragments get recreated. Thus we need
             * to save positions to Pages and let them do the registry to mPages. So
             * that we can make sure mPages get refreshed every time Page recreated.
             * Note that the arguments is maintain by sdk so that they won't lost on
             * recreate. */
            page.arguments = Bundle().apply {
                putInt(Page.POSITION, position)
            }
            return page
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        actionBar?.hide()

        mViewPager = findViewById(R.id.mainViewPager)
        mActionBar = findViewById(R.id.action_bar)

        initViewPager()
        initActionBar()

        setSystemUIParams()
    }

    override fun onBackPressed() {}

    fun gotoPrevPage() {
        mViewPager.apply {
            if (currentItem > 0)
                currentItem--
        }
    }

    fun gotoNextPage() {
        mViewPager.apply {
            if (currentItem < Pages.getPageCount() - 1)
                currentItem++
        }
    }

    fun hideActionBarIfCurrent(page: Page) {
        if (getCurrentPage() == page)
            hideActionBar()
    }

    fun showActionBarIfCurrent(page: Page) {
        if (getCurrentPage() == page)
            showActionBar()
    }

    private fun hideActionBar() {
        val anim = ObjectAnimator.ofFloat(
            mActionBar,
            ACTION_BAR_TRANSLATE_Y,
            resources.getDimensionPixelSize(R.dimen.action_bar_height).toFloat()
        )
        anim.interpolator = DecelerateInterpolator()
        anim.start()
    }

    private fun showActionBar() {
        val anim = ObjectAnimator.ofFloat(mActionBar, ACTION_BAR_TRANSLATE_Y, 0f)
        anim.interpolator = DecelerateInterpolator()
        anim.start()
    }

    private fun getPage(position: Int): Page =
        mPages[position] ?: throw RuntimeException("Page is not saved. Position = $position")

    private fun getCurrentPage(): Page = getPage(mViewPager.currentItem)

    private fun setSystemUIParams() {
        ViewCompat.getWindowInsetsController(window.decorView)?.apply {
            isAppearanceLightStatusBars = true
        }

        sStatusBarManager =
            sStatusBarManager ?: getSystemService(StatusBarManager::class.java)?.apply {
                setDisabledForSetup(true)
            }
    }

    private fun initViewPager() {
        mViewPager.apply {
            isUserInputEnabled = false
            adapter = mFragmentAdapter
        }
    }

    private fun initActionBar() {
        val prevBtn = findViewById<Button>(R.id.prev_btn)
        val nextBtn = findViewById<Button>(R.id.next_btn)

        prevBtn.setOnClickListener {
            getCurrentPage().onPrevBtnClicked()
        }
        nextBtn.setOnClickListener {
            getCurrentPage().onNextBtnClicked()
        }
    }
}
