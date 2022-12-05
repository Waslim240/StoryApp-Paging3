package com.waslim.storyapp.view.activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.waslim.storyapp.R
import com.waslim.storyapp.databinding.ActivityHomeBinding
import com.waslim.storyapp.model.Constants
import com.waslim.storyapp.model.showLoading
import com.waslim.storyapp.model.showToast
import com.waslim.storyapp.view.adapter.LoadingStateAdapter
import com.waslim.storyapp.view.adapter.StoryPagingAdapter
import com.waslim.storyapp.viewmodel.HomeViewModel
import com.waslim.storyapp.viewmodel.UserTokenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val userTokenViewModel by viewModels<UserTokenViewModel>()
    private val homeViewModel by viewModels<HomeViewModel>()
    private val adapterListStory: StoryPagingAdapter by lazy(::StoryPagingAdapter)
    private var doubleBackToExit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkToken()
        setRecyclerView()
        fabAdd()
        doubleBackExit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_option, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.logout -> {
            logout()
            true
        }

        R.id.about -> {
            aboutMe()
            true
        }

        R.id.setting -> {
            setting()
            true
        }

        R.id.maps -> {
            maps()
            true
        }

        else -> true
    }

    private fun checkToken() = userTokenViewModel.getToken().observe(this) {
        when {
            it != "" -> {
                homeViewModel.getAllStories(Constants.BEARER + it).observe(this) { data ->
                    if (data != null) adapterListStory.submitData(lifecycle, data)
                    else showToast(getString(R.string.gagal_memuat_data))
                }
            } else -> showToast(getString(R.string.un_authorization))
        }
    }

    private fun setRecyclerView() = binding.apply {
        rvStory.layoutManager = LinearLayoutManager(applicationContext)
        rvStory.adapter = adapterListStory.withLoadStateFooter(
            LoadingStateAdapter {
                adapterListStory.retry()
            }
        )
        rvStory.setHasFixedSize(true)
    }

    private fun fabAdd() = binding.fabAddStory.setOnClickListener {
        startActivity(Intent(this, AddStoryActivity::class.java))
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    private fun logout() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.konfirmasi_logout))
            .setMessage(getString(R.string.yakin_ingin_keluar))

            .setPositiveButton(getString(R.string.ya)){ _: DialogInterface, _: Int ->
                showLoading(true, binding.progressBarHome)
                Handler(Looper.getMainLooper()).postDelayed({
                    showToast(getString(R.string.berhasil_keluar))
                    startActivity(Intent(applicationContext, LoginActivity::class.java))
                    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
                    finish()
                    userTokenViewModel.removeToken()
                }, Constants.DELAY)
            }

            .setNegativeButton(getString(R.string.tidak)){ dialogInterface: DialogInterface, _: Int ->
                showToast(getString(R.string.tidak_jadi_keluar))
                dialogInterface.dismiss()
            }
            .show()
    }

    private fun maps() = startActivity(Intent(this, MapsActivity::class.java))

    private fun aboutMe() = startActivity(Intent(this, AboutMeActivity::class.java))

    private fun setting() = startActivity(Intent(this, SettingActivity::class.java))

    private fun doubleBackExit() = onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            when {
                doubleBackToExit -> {
                    finish()
                }
                else -> {
                    doubleBackToExit = true
                    showToast(getString(R.string.tekan_lagi_untuk_keluar))
                    Handler(Looper.getMainLooper()).postDelayed({
                        kotlin.run {
                            doubleBackToExit = false
                        }
                    }, Constants.DELAY)
                }
            }
        }
    })

    override fun onResume() {
        super.onResume()
        checkToken()
    }
}