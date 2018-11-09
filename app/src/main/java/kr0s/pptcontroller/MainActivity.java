package kr0s.pptcontroller;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {
    private String ip = "";
    private int port = 8080;
    private RequestQueue queue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        queue = Volley.newRequestQueue(this);

        // Progress Bar
        ProgressBar pg = findViewById(R.id.progressBar);
        pg.setIndeterminate(true);


        Button fb = findViewById(R.id.forwardsButton);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity c = (MainActivity) view.getContext();
                c.forwards();
            }
        });

        Button bb = findViewById(R.id.backButton);
        bb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity c = (MainActivity) view.getContext();
                c.backwards();
            }
        });
        showStartupDialogue();

    }

    private void showStartupDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        SharedPreferences sp = getSharedPreferences("PPTRemote", Activity.MODE_PRIVATE);
        final SharedPreferences.Editor ed = sp.edit();

        final String ipFromSettings = sp.getString("ip", "");
        int portFromSettings = sp.getInt("port", 8080);

        builder.setTitle("Enter the IP/port to connect to.");

        //clear when you click these
        final EditText ipInput = new EditText(this);
        ipInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ipInput.getText().clear();
            }
        });

        final EditText portInput = new EditText(this);
        portInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                portInput.getText().clear();
            }
        });

        ipInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
        ipInput.setText(ipFromSettings);
        portInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        portInput.setText(String.valueOf(portFromSettings));

        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(ipInput);
        ll.addView(portInput);
        builder.setView(ll);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ip = ipInput.getText().toString();
                ed.putString("ip", ip);
                port = Integer.valueOf(portInput.getText().toString());
                ed.putInt("port", port);
                ed.commit();
            }
        });
        builder.show();
        getSlideNo();
    }

    //good fucking christ why does everything have to be so bad in java
    public void getSlideNo() {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int slides = Integer.valueOf(response);
                ProgressBar pg = findViewById(R.id.progressBar);
                pg.setIndeterminate(false);
                pg.setMin(0);
                pg.setMax(slides-1);
                pg.setProgress(0);
            }
        };
        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getSlideNo();
            }
        };
        String url = "http://" + ip + ":" + port + "/start";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, listener, error);
        queue.add(stringRequest);
    }

    public void forwards() {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ProgressBar pg = findViewById(R.id.progressBar);
                pg.setProgress(pg.getProgress()+1);
            }
        };
        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                forwards();
            }
        };

        String url = "http://" + ip + ":" + port + "/forwards";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, listener, error);
        queue.add(stringRequest);
    }

    public void backwards() {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ProgressBar pg = findViewById(R.id.progressBar);
                pg.setProgress(pg.getProgress()-1);
            }
        };

        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        };

        String url = "http://" + ip + ":" + port + "/backwards";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, listener, error);
        queue.add(stringRequest);
    }
}
