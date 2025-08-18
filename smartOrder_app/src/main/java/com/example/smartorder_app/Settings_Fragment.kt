package com.example.smartorder_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartorder_app.utils.ContactRestaurant
import com.example.smartorder_app.utils.RestaurantAdapter
import com.example.smartorder_app.utils.RestaurantData
import com.example.smartorder_app.utils.WorkingDays

class Settings_Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout del fragment
        val view = inflater.inflate(R.layout.settings, container, false)

        return view
    }
}