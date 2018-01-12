package ru.lempo.tmdviewer.ui.common

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.arellomobile.mvp.MvpAppCompatActivity
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import ru.lempo.tmdviewer.R

/**
 * Author: Oksana Pokrovskaya
 * Email: lempo.developer@gmail.com
 */
abstract class BaseActivity<B : ViewDataBinding> : MvpAppCompatActivity() {

    abstract val layout: Int
    open val title = 0
    open val menu = 0
    lateinit var binding: B
    var toolbar: Toolbar? = null
    var mIsRestoredToTop = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layout)
        toolbar = binding.root.findViewById(R.id.toolbar) as? Toolbar
        toolbar?.let {
            setSupportActionBar(it)
            supportActionBar!!.let { actionBar ->
                actionBar.setDisplayHomeAsUpEnabled(true)
                actionBar.setTitle(title)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (this.menu != 0) {
            menuInflater.inflate(this.menu, menu)
            return true
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}