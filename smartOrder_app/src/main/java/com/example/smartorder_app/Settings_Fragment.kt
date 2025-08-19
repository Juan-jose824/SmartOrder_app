package com.example.smartorder_app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import android.widget.Switch
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartorder_app.utils.AppConfig
import com.example.smartorder_app.utils.ContactRestaurant
import com.example.smartorder_app.utils.PrefsManager
import com.example.smartorder_app.utils.RestaurantAdapter
import com.example.smartorder_app.utils.RestaurantData
import com.example.smartorder_app.utils.WorkingDays
import com.google.android.material.button.MaterialButton

class Settings_Fragment : Fragment() {

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout del fragment
        val view = inflater.inflate(R.layout.settings, container, false)

        val spinnerTheme: Spinner = view.findViewById(R.id.spinnerTheme)
        val switchReservaciones: Switch = view.findViewById(R.id.switchReservaciones)
        val switchHorario: Switch = view.findViewById(R.id.switchHorario)
        val switchDescripcion: Switch = view.findViewById(R.id.switchDescripcion)
        val switchNotificaciones: Switch = view.findViewById(R.id.switchNotificaciones)
        val switchAlertas: Switch = view.findViewById(R.id.switchAlertas)
        val switchLocal: Switch = view.findViewById(R.id.switchLocal)
        val switchReportes: Switch = view.findViewById(R.id.switchReportes)
        val switchGraficas: Switch = view.findViewById(R.id.switchGraficas)
        val switchUsuarios: Switch = view.findViewById(R.id.switchUsuarios)
        val switchAlimentos: Switch = view.findViewById(R.id.switchAlimentos)
        val switchLugares: Switch = view.findViewById(R.id.switchLugares)
        val switchReservas: Switch = view.findViewById(R.id.switchReservas)
        val logoutButton: MaterialButton = view.findViewById(R.id.logoutButton)

        val context = requireContext()
        var config = PrefsManager.loadConfig(context)

        // Cargar valores guardados
        switchReservaciones.isChecked = config.reservations
        switchHorario.isChecked = config.publicWorkTime
        switchDescripcion.isChecked = config.publicDescription
        switchNotificaciones.isChecked = config.notifications
        switchAlertas.isChecked = config.alerts
        switchLocal.isChecked = config.onlyRestaurant
        switchReportes.isChecked = config.reports
        switchGraficas.isChecked = config.grafics
        switchUsuarios.isChecked = config.adminUsers
        switchAlimentos.isChecked = config.adminFoods
        switchLugares.isChecked = config.adminPlaces
        switchReservas.isChecked = config.adminReservations

        // Listeners para guardar cambios en vivo
        val saveChanges = {
            config = AppConfig(
                user = config.user,
                theme = spinnerTheme.selectedItem.toString(),
                reservations = switchReservaciones.isChecked,
                publicWorkTime = switchHorario.isChecked,
                publicDescription = switchDescripcion.isChecked,
                notifications = switchNotificaciones.isChecked,
                alerts = switchAlertas.isChecked,
                onlyRestaurant = switchLocal.isChecked,
                reports = switchReportes.isChecked,
                grafics = switchGraficas.isChecked,
                adminUsers = switchUsuarios.isChecked,
                adminFoods = switchAlimentos.isChecked,
                adminPlaces = switchLugares.isChecked,
                adminReservations = switchReservas.isChecked,
            )
            PrefsManager.saveConfig(context, config)
        }

        switchReservaciones.setOnCheckedChangeListener { _, _ -> saveChanges() }
        switchHorario.setOnCheckedChangeListener { _, _ -> saveChanges() }
        switchDescripcion.setOnCheckedChangeListener { _, _ -> saveChanges() }
        switchNotificaciones.setOnCheckedChangeListener { _, _ -> saveChanges() }
        switchAlertas.setOnCheckedChangeListener { _, _ -> saveChanges() }
        switchLocal.setOnCheckedChangeListener { _, _ -> saveChanges() }
        switchReportes.setOnCheckedChangeListener { _, _ -> saveChanges() }
        switchGraficas.setOnCheckedChangeListener { _, _ -> saveChanges() }
        switchUsuarios.setOnCheckedChangeListener { _, _ -> saveChanges() }
        switchAlimentos.setOnCheckedChangeListener { _, _ -> saveChanges() }
        switchLugares.setOnCheckedChangeListener { _, _ -> saveChanges() }
        switchReservas.setOnCheckedChangeListener { _, _ -> saveChanges() }

        // Logout
        logoutButton.setOnClickListener {
            PrefsManager.clear(requireContext())
            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().finish()
        }

        return view
    }
}