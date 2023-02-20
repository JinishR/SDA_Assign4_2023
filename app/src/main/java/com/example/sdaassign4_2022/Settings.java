package com.example.sdaassign4_2022;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

public class Settings extends Fragment {

    private EditText borrowerNameEditText;
    private EditText borrowerIDEditText;
    private EditText emailEditText;
    private Button saveDetailsButton;
    private Button clearPreferencesButton;
    private SharedPreferences sharedPreferences;

    public Settings() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        borrowerNameEditText = view.findViewById(R.id.userName);
        borrowerIDEditText = view.findViewById(R.id.borrowerID);
        emailEditText = view.findViewById(R.id.email);
        saveDetailsButton = view.findViewById(R.id.button_save);
        clearPreferencesButton = view.findViewById(R.id.clear_preferences_button);

        sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE);

        // Restore preferences
        borrowerNameEditText.setText(sharedPreferences.getString(getString(R.string.borrower_name_key), ""));
        borrowerIDEditText.setText(sharedPreferences.getString(getString(R.string.borrower_id_key), ""));
        emailEditText.setText(sharedPreferences.getString(getString(R.string.email_key), ""));

        saveDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDetails();
            }
        });

        clearPreferencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearPreferences();
            }
        });

        return view;
    }

    private void saveDetails() {
        String borrowerName = borrowerNameEditText.getText().toString();
        String borrowerID = borrowerIDEditText.getText().toString();
        String email = emailEditText.getText().toString();

        if (borrowerName.isEmpty() || borrowerID.isEmpty() || email.isEmpty()) {
            Toast.makeText(requireActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(requireActivity(), "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.borrower_name_key), borrowerName);
        editor.putString(getString(R.string.borrower_id_key), borrowerID);
        editor.putString(getString(R.string.email_key), email);
        editor.apply();

        Toast.makeText(requireActivity(), "Details saved", Toast.LENGTH_SHORT).show();
    }

    private void clearPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        borrowerNameEditText.setText("");
        borrowerIDEditText.setText("");
        emailEditText.setText("");

        Toast.makeText(requireActivity(), "Preferences cleared", Toast.LENGTH_SHORT).show();
    }

}
