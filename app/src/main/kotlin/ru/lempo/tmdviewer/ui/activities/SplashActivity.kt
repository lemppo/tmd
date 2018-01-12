package ru.lempo.tmdviewer.ui.activities

import com.arellomobile.mvp.presenter.InjectPresenter
import ru.lempo.tmdviewer.R
import ru.lempo.tmdviewer.databinding.ActivitySplashBinding
import ru.lempo.tmdviewer.presentation.SplashPresenter
import ru.lempo.tmdviewer.presentation.SplashView
import ru.lempo.tmdviewer.ui.common.BaseActivity

/**
 * Author: Oksana Pokrovskaya
 * Email: lempo.developer@gmail.com
 */
class SplashActivity : BaseActivity<ActivitySplashBinding>(), SplashView {

    override val layout = R.layout.activity_splash

    @InjectPresenter
    lateinit var presenter: SplashPresenter

    override fun ready() {
        MoviesListActivity.start(this)
        finish()
    }
}