package com.r.travelmantics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    EditText txtTitle;
    EditText txtDescription;
    EditText txtPrice;
    TravelDeal deal;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUtil.openFbReference("traveldeals");
        firebaseDatabase = FirebaseUtil.firebaseDatabase;
        databaseReference = FirebaseUtil.databaseReference;

        txtTitle = findViewById(R.id.txtTitle);
        txtDescription = findViewById(R.id.txtDescription);
        txtPrice = findViewById(R.id.txtPrice);

        Intent intent = getIntent();
        TravelDeal deal = (TravelDeal) intent.getSerializableExtra("Deal");
        if(deal == null){
            deal = new TravelDeal();
        }
        this.deal = deal;
        txtTitle.setText(deal.getTitle());
        txtDescription.setText(deal.getDescription());
        txtPrice.setText(deal.getPrice());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.item_save){
            saveDeal();
            Toast.makeText(this, "Item Saved", Toast.LENGTH_LONG).show();
            clean();
            return true;
        }
        else if(item.getItemId() == R.id.item_delete)
        {
            deleteDeal();
            Toast.makeText(this, "Item Deleted", Toast.LENGTH_LONG).show();
            backToList();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clean() {
        txtTitle.setText("");
        txtDescription.setText("");
        txtPrice.setText("");
        txtTitle.requestFocus();
    }

    private void saveDeal() {
        deal.setTitle(txtTitle.getText().toString());
        deal.setDescription(txtDescription.getText().toString());
        deal.setPrice(txtPrice.getText().toString());
        if(deal.getId() == null){
            databaseReference.push().setValue(deal);
        }
        else {
            databaseReference.child(deal.getId()).setValue(deal);
        }

    }

    private void deleteDeal(){
        if(deal.getId() == null){
            Toast.makeText(this, "Create deal first", Toast.LENGTH_SHORT).show();;
        }
        else {
            databaseReference.child(deal.getId()).removeValue();
        }
    }

    private void backToList(){
        startActivity(new Intent(this, ListActivity.class));
    }
}
