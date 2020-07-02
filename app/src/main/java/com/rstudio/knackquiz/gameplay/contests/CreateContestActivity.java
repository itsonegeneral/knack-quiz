package com.rstudio.knackquiz.gameplay.contests;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.rstudio.knackquiz.R;
import com.rstudio.knackquiz.adapters.PrizeDistributionAdapter;
import com.rstudio.knackquiz.helpers.DBClass;
import com.rstudio.knackquiz.helpers.DBKeys;
import com.rstudio.knackquiz.helpers.DateHelper;
import com.rstudio.knackquiz.helpers.KeyStore;
import com.rstudio.knackquiz.helpers.RandomString;
import com.rstudio.knackquiz.models.Category;
import com.rstudio.knackquiz.models.Contest;
import com.rstudio.knackquiz.models.Question;
import com.shawnlin.numberpicker.NumberPicker;
import com.tiper.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class CreateContestActivity extends AppCompatActivity {

    private static final String TAG = "CreateContestActivity";

    //Buttons and TextViews
    private Button btDate, btTime, btCreate;
    private TextView tvSelectedDate, tvSelectedTime;

    //Pickers
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private NumberPicker durationPicker;

    //Edittexts
    private TextInputLayout layoutMaxWinners;
    private TextInputEditText etTotalPlayers, etEntryCoins, etWinners;

    private MaterialSpinner spinner;

    private Calendar date = Calendar.getInstance();
    private Contest contest = new Contest();
    private ArrayList<Category> categoryArrayList = new ArrayList<>();
    private ArrayList<Question> questions = new ArrayList<>();

    //Time
    private int mYear, mMonth, mDay, mHour, mMinute, mSeconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contest);

        initValues();
        setToolbar();
        setDateDialogues();
        setListeners();
        loadCategories();

        findViewById(R.id.bt_createContest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    contest.setCategory(spinner.getSelectedItem().toString());
                    getQuestions(contest.getCategory());
                }
            }
        });

    }

    private void getQuestions(final String cat) {
        //   showLoadingAlert();
        String url = DBClass.urlGetQuestions + "?category=" + cat + "&limit=" + 10;
        Log.d(TAG, "getQuestions: " + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    questions.clear();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("data");
                    Gson gson = new Gson();
                    for (int i = 0; i < array.length(); i++) {
                        Question question = gson.fromJson(array.getJSONObject(i).toString(), Question.class);
                        questions.add(question);
                    }
                    if (questions.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "No Questions Found", Toast.LENGTH_SHORT).show();
                    } else {
                        contest.setQuestions(questions);
                        uploadContest();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.getLocalizedMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("category", cat);
                params.put("limit", "10");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void uploadContest() {
        String key = UUID.randomUUID().toString().replace("-", "").substring(0, 15);
        Date fDate = date.getTime();
        contest.setTotalPlayers(Integer.parseInt(etTotalPlayers.getText().toString()));
        contest.setWinnerCount(Integer.parseInt(etWinners.getText().toString()));
        contest.setId(key);
        contest.setEntryType(KeyStore.COIN);
        contest.setRewardType(KeyStore.DIAMOND);
        contest.setEntryValue(Integer.parseInt(etEntryCoins.getText().toString()));
        contest.setRewardValue((contest.getEntryValue() / 100) * 90);
        contest.setStartTime(DateHelper.getFormattedDate(fDate));
        contest.setEndTime(DateHelper.addHourToDate(contest.getStartTime(), durationPicker.getValue()));
        contest.setQuestionTime(10);
        contest.addPlayer(FirebaseAuth.getInstance().getCurrentUser().getUid() != null ? FirebaseAuth.getInstance().getCurrentUser().getUid() : "kjshdkja");


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DBKeys.KEY_CONTESTS);

        ref.child(key).setValue(contest).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Snackbar.make(findViewById(android.R.id.content), "Contest created", Snackbar.LENGTH_SHORT).show();
                    finish();
                } else {

                }
            }
        });
    }

    private boolean validateInput() {
        int totalPlayers = Integer.parseInt(etTotalPlayers.getText().toString());
        int winners = Integer.parseInt(etWinners.getText().toString());
        if (spinner.getSelectedItem() == null) {
            Toast.makeText(this, "Select Category", Toast.LENGTH_SHORT).show();
            spinner.setError("Pick Category");
            spinner.requestFocus();
        } else if (tvSelectedDate.getText().toString().isEmpty()) {
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        } else if (tvSelectedTime.getText().toString().isEmpty()) {
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        } else if (totalPlayers < 2) {
            etTotalPlayers.setError("Min 2");
            etTotalPlayers.requestFocus();
        } else if (winners > totalPlayers / 2 || winners < 1) {
            etWinners.setError("0 -" + totalPlayers / 2);
        } else if (etEntryCoins.getText().toString().isEmpty()) {
            etEntryCoins.setError("Enter coins");
            etEntryCoins.requestFocus();
        } else {
            return true;
        }
        return false;
    }

    String[] categories;

    private void loadCategories() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, DBClass.urlGetSubCategories, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Gson gson = new Gson();
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                        JSONArray array = jsonObject.getJSONArray("data");
                        categories = new String[array.length()];
                        for (int i = 0; i < array.length(); i++) {
                            categoryArrayList.add(gson.fromJson(array.getJSONObject(i).toString(), Category.class));
                            categories[i] = gson.fromJson(array.getJSONObject(i).toString(), Category.class).getCategoryName();
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, categories);
                        spinner.setAdapter(adapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void setListeners() {
        btDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        tvSelectedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        btTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show();
            }
        });

        tvSelectedTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show();
            }
        });

    }

    private void setDateDialogues() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        mSeconds = c.get(Calendar.SECOND);

        datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        date.set(year, monthOfYear, dayOfMonth);
                        tvSelectedDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        btDate.setVisibility(View.GONE);
                    }
                }, mYear, mMonth, mDay);

        timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        if (hourOfDay < 12) {
                            tvSelectedTime.setText(hourOfDay + ":" + minute + " AM");
                        } else {
                            tvSelectedTime.setText((hourOfDay % 12) + ":" + minute + " PM");
                        }
                        tvSelectedTime.setVisibility(View.VISIBLE);
                        btTime.setVisibility(View.GONE);
                    }
                }, mHour, mMinute, false);

    }

    private void initValues() {
        btDate = findViewById(R.id.bt_selectDateCreateContest);
        btTime = findViewById(R.id.bt_selectTimeCreateContest);

        tvSelectedDate = findViewById(R.id.tv_dateCreateContest);
        tvSelectedTime = findViewById(R.id.tv_timeCreateContest);

        durationPicker = findViewById(R.id.np_hoursPicker);

        spinner = findViewById(R.id.spinner_pickCategory);

        //Edittexts
        layoutMaxWinners = findViewById(R.id.txtlayout_winnersCreateContest);
        etTotalPlayers = findViewById(R.id.et_totalPlayersCreateContest);
        etEntryCoins = findViewById(R.id.et_entryCoinsCreateContest);
        etWinners = findViewById(R.id.et_winnersCreateContest);

        setInputListeners();

    }


    private void setInputListeners() {
        etTotalPlayers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    layoutMaxWinners.setHint("Max 0");
                    contest.setWinnerCount(0);
                    contest.setTotalPlayers(0);
                    return;
                }
                int total = Integer.parseInt(etTotalPlayers.getText().toString());
                contest.setTotalPlayers(total);
                layoutMaxWinners.setHint("Max " + total / 2);
                contest.setWinnerCount(total / 2);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    /*    final ListView prizeListView = findViewById(R.id.list_priceDistCreateContest);
        etEntryCoins.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {

                    return;
                }
                if (contest.getTotalPlayers() != 0 && contest.getWinnerCount() != 0) {
                    List<Integer> prizes = new ArrayList<>();
                    prizes.add(10);
                    prizes.add(100);
                    prizes.add(1000);
                    PrizeDistributionAdapter adapter = new PrizeDistributionAdapter(getApplicationContext(), R.layout.list_price_distribution, prizes);
                    prizeListView.setAdapter(adapter);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.tb_createContest);
        TextView tv = findViewById(R.id.tv_toolbarHeadingSimple);
        tv.setText("Host Contest");
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }


}
