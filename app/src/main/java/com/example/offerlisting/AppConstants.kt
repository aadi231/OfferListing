package com.example.offerlisting

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

object AppConstants {
    const val OFFER_LISTING_URL = "http://demo0891151.mockable.io/"


    fun navigateToTargetFragment(requiredActivity : FragmentActivity, targetFragment : Fragment) {
        requiredActivity.supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, targetFragment)
            .commit()
    }

    fun navigateToTargetFragmentWithBackStack(requiredActivity : FragmentActivity, targetFragment : Fragment) {
        requiredActivity.supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, targetFragment)
            .addToBackStack(null)
            .commit()
    }
}