package com.example.smartorder_app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.Fragment
import com.example.smartorder_app.utils.PrefsManager
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONObject


class ProfileFragment : Fragment(), MessageClient.OnMessageReceivedListener {

    private lateinit var pressureView: TextView
    private lateinit var pulseView: TextView
    private lateinit var xGyroView: TextView
    private lateinit var yGyroView: TextView
    private lateinit var zGyroView: TextView

    private lateinit var fullName: TextView
    private lateinit var email: TextView
    private lateinit var cellphone: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout del fragment
        val view = inflater.inflate(R.layout.profile, container, false)

        val User = PrefsManager.loadUser(requireContext())

        Log.i("Perfil", User.toString())

        fullName = view.findViewById(R.id.fullName)
        email = view.findViewById(R.id.email)
        cellphone = view.findViewById(R.id.cellphone)

        fullName.text = "${User?.name?.name} ${User?.name?.fatherName} ${User?.name?.motherName}" ?: "Nombre de usuario"
        email.text = User?.email ?: "Correo no disponible"
        cellphone.text = "${User?.cellphone?.countryCode} ${User?.cellphone?.number}" ?: "No disponible"

        pressureView = view.findViewById(R.id.spOPhone)
        pulseView = view.findViewById(R.id.pulse)
        xGyroView = view.findViewById(R.id.xGyroscopePhone)
        yGyroView = view.findViewById(R.id.yGyroscopePhone)
        zGyroView = view.findViewById(R.id.zGyroscopePhone)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Wearable.getMessageClient(requireContext()).addListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Wearable.getMessageClient(requireContext()).removeListener(this)
    }

    override fun onMessageReceived(event: MessageEvent) {
        val data = String(event.data)
        Log.d("Phone", "Mensaje recibido desde reloj: $data")

        try {
            val json = JSONObject(data)
            val bpm = json.getInt("bpm")
            val pressure = json.getInt("pressure")

            val gyro = json.getJSONObject("gyroscope")
            val x = gyro.getInt("x")
            val y = gyro.getInt("y")
            val z = gyro.getInt("z")

            // Actualizar UI (en el hilo principal)
            activity?.runOnUiThread {
                pulseView.text = "$bpm"
                pressureView.text = "$pressure hPa"

                xGyroView.text = "x:$x"
                yGyroView.text = "y:$y"
                zGyroView.text = "z:$z"
            }

        } catch (e: Exception) {
            Log.e("Phone", "Error procesando JSON", e)
        }
    }
}