package com.example.sdaassign4_2022;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firestore.admin.v1.Index;
import com.google.firestore.v1.StructuredQuery;

import java.text.DateFormat;
import java.util.Calendar;

public class CheckOut extends AppCompatActivity {

    TextView mDisplaySummary;
    DatabaseReference database;

    Calendar mDateAndTime = Calendar.getInstance();
    String bookId, bookName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        // Set the toolbar we have overridden
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find the summary textview
        mDisplaySummary = findViewById(R.id.orderSummary);

        // Get the book ID and name from the intent extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            bookId = extras.getString("bookId");
            bookName = extras.getString("bookName");
        }

        // Initialize Firebase database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();


        DatabaseReference bookRef = database.getReference("books").child(bookId);

        // Attach a listener to the book reference to retrieve its availability status
        bookRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Retrieve the availability status of the book
                boolean isAvailable = dataSnapshot.child("isAvailable").getValue(Boolean.class);

                // Update the UI elements accordingly
                TextView bookNameTextView = findViewById(R.id.bookName);
                TextView availabilityTextView = findViewById(R.id.availabilityStatus);

                bookNameTextView.setText(bookName);
                availabilityTextView.setText(isAvailable ? "Available" : "Unavailable");

                // Disable SEND ORDER and SELECT DATE buttons if the book is unavailable
                Button sendOrderButton = findViewById(R.id.sendOrderButton);
                Button selectDateButton = findViewById(R.id.selectDateButton);

                sendOrderButton.setEnabled(isAvailable);
                selectDateButton.setEnabled(isAvailable);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle database error
            }
        });
    }

    public void onDateClicked(View v) {
        // Get the current date and time
        int year = mDateAndTime.get(Calendar.YEAR);
        int month = mDateAndTime.get(Calendar.MONTH);
        int dayOfMonth = mDateAndTime.get(Calendar.DAY_OF_MONTH);

        // Create a date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                    // Update the date and time object
                    mDateAndTime.set(selectedYear, selectedMonth, selectedDayOfMonth);

                    // Update the display
                    updateDateAndTimeDisplay();
                }, year, month, dayOfMonth);

        // Set the minimum date to today
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

        // Set the maximum date to two weeks from today
        Calendar maxDate = Calendar.getInstance();
        maxDate.add(Calendar.DAY_OF_MONTH, 14);
        datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());

        // Show the dialog
        datePickerDialog.show();
    }

    private void updateDateAndTimeDisplay() {
        // Format the selected date and time as a string
        String summary = String.format("Selected date: %s\nCurrent time: %s",
                DateFormat.getDateInstance().format(mDateAndTime.getTime()),
                DateFormat.getTimeInstance().format(mDateAndTime.getTime()));

        // Update the summary display
        mDisplaySummary.setText(summary);
    }

    public void onSendOrderClicked(View v) {
        // Get the current date and time
        // Get a reference to the Firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        long orderTime = System.currentTimeMillis();

        // Get the borrower
        EditText borrowerEditText = findViewById(R.id.borrower);
        String borrower = borrowerEditText.getText().toString();

        // Get the order details
        String orderDetails = mDisplaySummary.getText().toString();

// Create a new order object

        Order order = new Order(bookId, bookName, borrower, orderDetails, orderTime);



        // Add the order to the database
        DatabaseReference ordersRef = database.getReference("orders");
        ordersRef.push().setValue(order);

        // Display a confirmation message
        Toast.makeText(this, "Order sent successfully!", Toast.LENGTH_SHORT).show();

        // Finish the activity
        finish();
    }
}