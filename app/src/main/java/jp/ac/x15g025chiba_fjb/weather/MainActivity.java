package jp.ac.x15g025chiba_fjb.weather;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        load(130010);
    }



    void load(final int city){
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL("http://weather.livedoor.com/forecast/webservice/json/v1?city="+city);
                    ObjectMapper mapper = new ObjectMapper();
                    WeatherData wd = mapper.readValue(url,WeatherData.class);
                    onLoad(wd);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    void onLoad(final WeatherData wd){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");

                TextView date = (TextView) findViewById(R.id.textDate);
                TextView title = (TextView)findViewById(R.id.textTitle);
                TextView description = (TextView)findViewById(R.id.textDescription);
                date.setText(sdf.format(wd.publicTime));
                title.setText(wd.title);
                description.setText(wd.description.text);

                LinearLayout layout1 = (LinearLayout)findViewById(R.id.layout1);
                setWeather(layout1,wd.forecasts[0]);
                LinearLayout layout2 = (LinearLayout)findViewById(R.id.layout2);
                setWeather(layout2,wd.forecasts[1]);
                LinearLayout layout3 = (LinearLayout)findViewById(R.id.layout3);
                setWeather(layout3,wd.forecasts[2]);
            }
        });

    }

    private void setWeather(LinearLayout layout1, WeatherData.Forecasts forecast) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");

        TextView date2 = (TextView) layout1.findViewById(R.id.textDate);
        ImageView image = (ImageView)layout1.findViewById(imageView);
        TextView temp = (TextView)layout1.findViewById(R.id.textTemperature);

        date2.setText(sdf.format(forecast.date));
        setImageURL(image,forecast.image.url);
        if(forecast.temperature.min == null) {
            if(forecast.temperature.max == null)
                temp.setText("--/--");
            else
                temp.setText(String.format("--/%d",
                        forecast.temperature.max.celsius));
        }
        else
            temp.setText(String.format("%d/%d",
                    forecast.temperature.min.celsius,
                    forecast.temperature.max.celsius));
    }

    private void setImageURL(ImageView image, String url) {
        new Thread(){
            @Override
            public void run() {

                try {
                    URL aURL = new URL(url);
                    URLConnection conn = aURL.openConnection();
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    final Bitmap bm = BitmapFactory.decodeStream(is);
                    is.close();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(bm);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

}
