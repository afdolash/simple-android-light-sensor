package com.codesch.afdolash.simplelightsensor;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private LinearLayout linearLayout;
    private ImageView img_lamp;
    private TextView tv_title;
    private TextView tv_message;
    private TextView tv_current;

    private SensorManager mSensorManager;
    private Sensor mSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Membuat status bar/notification bar transparan
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        changeStatusBarColor();

        // Isinialisasi widget
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        img_lamp = (ImageView) findViewById(R.id.img_lamp);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_message = (TextView) findViewById(R.id.tv_message);
        tv_current = (TextView) findViewById(R.id.tv_current);

        // Inisialisasi sensor light
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        // Pengecekan apakah smartphone support sensor light
        if (mSensor == null) {
            Toast.makeText(MainActivity.this, "Your device not supported Light Sensor", Toast.LENGTH_LONG).show();
        } else {
            // Menampilkan nilai maksimum
            // float mMax = mSensor.getMaximumRange();
            // tv_message.setText(mMax +" lx.");

            // Mulai menjalankan sensor
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // Light sensor hanya mengeluarkan 1 nilai return
        // Kebanyakan sensor mempunyai 3 nilai return, 1 nilai untuk setiap axis
        if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {
            // Mendapatkan nilai sensor light
            float mCurrentLight = sensorEvent.values[0];
            tv_current.setText(mCurrentLight +" lx.");

            // Menentukan hasil keluaran
            if (mCurrentLight < 1) {
                linearLayout.setBackgroundColor(getColor(R.color.colorNoLight));
                img_lamp.setImageDrawable(getDrawable(R.drawable.img_lamp_nolight));
                tv_title.setText("No Light");
                tv_title.setTextColor(getColor(R.color.colorWhite));
                tv_message.setText("Hidupkan lampu! Ruangan sangat gelap.");
                tv_message.setTextColor(getColor(R.color.colorWhite));
                tv_current.setTextColor(getColor(R.color.colorWhite));

            } else if (mCurrentLight < 10) {
                linearLayout.setBackgroundColor(getColor(R.color.colorDim));
                img_lamp.setImageDrawable(getDrawable(R.drawable.img_lamp_dim));
                tv_title.setText("Dim");
                tv_title.setTextColor(getColor(R.color.colorWhite));
                tv_message.setText("Ruangan kurang penerangan. Butuh penerang.");
                tv_message.setTextColor(getColor(R.color.colorWhite));
                tv_current.setTextColor(getColor(R.color.colorWhite));

            } else if (mCurrentLight < 50) {
                linearLayout.setBackgroundColor(getColor(R.color.colorNormal));
                img_lamp.setImageDrawable(getDrawable(R.drawable.img_lamp_normal));
                tv_title.setText("R");
                tv_title.setTextColor(getColor(R.color.colorBlack));
                tv_message.setText("Ruangan sangat nyaman. Penerangan sangat cukup.");
                tv_message.setTextColor(getColor(R.color.colorBlack));
                tv_current.setTextColor(getColor(R.color.colorBlack));

            } else if (mCurrentLight < 100) {
                linearLayout.setBackgroundColor(getColor(R.color.colorBright));
                img_lamp.setImageDrawable(getDrawable(R.drawable.img_lamp_bright));
                tv_title.setText("Penerangan berlebih. Redupkan lampu!");
                tv_title.setTextColor(getColor(R.color.colorBlack));
                tv_message.setTextColor(getColor(R.color.colorBlack));
                tv_current.setTextColor(getColor(R.color.colorBlack));

            } else {
                linearLayout.setBackgroundColor(getColor(R.color.colorSun));
                img_lamp.setImageDrawable(getDrawable(R.drawable.img_lamp_sun));
                tv_title.setText("#@!Explode#@!");
                tv_title.setTextColor(getColor(R.color.colorBlack));
                tv_message.setText("Ahhkkk... Terlalu terang. Mataku terbakar!@#$%.");
                tv_message.setTextColor(getColor(R.color.colorBlack));
                tv_current.setTextColor(getColor(R.color.colorBlack));
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    /**
     * Membuat notification bar transparant
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
}
