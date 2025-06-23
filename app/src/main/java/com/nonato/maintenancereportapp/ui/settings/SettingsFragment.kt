package com.nonato.maintenancereportapp.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment


class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding  // assumindo ViewBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Carrega SharedPreferences
        val prefs = requireContext().getSharedPreferences("settings", 0)
        binding.inputName.setText(prefs.getString("user_name", ""))
        binding.inputEmail.setText(prefs.getString("user_email", ""))
        binding.inputPhone.setText(prefs.getString("user_phone", ""))

        binding.btnSaveSettings.setOnClickListener {
            // Salva dados no SharedPreferences
            prefs.edit().apply {
                putString("user_name", binding.inputName.text.toString())
                putString("user_email", binding.inputEmail.text.toString())
                putString("user_phone", binding.inputPhone.text.toString())
                apply()
            }
            Toast.makeText(context, "Configurações salvas", Toast.LENGTH_SHORT).show()
        }
    }
}
