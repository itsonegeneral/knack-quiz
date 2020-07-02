package com.rstudio.knackquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rstudio.knackquiz.datastore.DataStore;
import com.rstudio.knackquiz.helpers.DBClass;
import com.rstudio.knackquiz.helpers.DBKeys;
import com.rstudio.knackquiz.models.Player;

import java.util.HashMap;
import java.util.Map;

public class RedeemActivity extends AppCompatActivity {

    Player player;
    private TextInputEditText etAmount;
    private int balance = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem);
        setToolbar();
        initValues();
        Toast.makeText(this, player.getPlayerID(), Toast.LENGTH_SHORT).show();

        findViewById(R.id.bt_redeemAmount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = etAmount.getText().toString();
                if (amount.isEmpty() || Integer.valueOf(amount) > balance) {
                    etAmount.setError("Invalid");
                    etAmount.requestFocus();
                } else {
                    uploadData(amount);
                }
            }
        });

    }

    private void uploadData(final String amount) {

        StringRequest request = new StringRequest(StringRequest.Method.POST, DBClass.urlCreatePaymentRequest, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("userid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                params.put("amount",amount);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DBKeys.KEY_USERS).child("");
        long diamonds = player.getDiamonds() - (Integer.parseInt(amount) * 10);
        player.setDiamonds(diamonds);
        ref.setValue(player).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    DataStore.setCurrentPlayer(player, RedeemActivity.this);
                    Snackbar.make(findViewById(android.R.id.content), "Request Sent!", Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Failed to place request", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initValues() {
        player = DataStore.getCurrentPlayer(getApplicationContext());
        TextView tvBalance = findViewById(R.id.tv_availableBalanceRedeem);
        etAmount = findViewById(R.id.et_amountRedeem);

        balance = (int) player.getDiamonds() / 10;
        tvBalance.setText(balance+ "");
        etAmount.setHint("Max " + balance);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.tb_redeemActivity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView tv = findViewById(R.id.tv_toolbarHeadingSimple);
        tv.setText("Redeem");
    }
}
