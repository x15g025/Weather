package jp.ac.x15g025chiba_fjb.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

/**
 * Created by x15g025 on 2017/06/15.
 */
@JsonIgnoreProperties(ignoreUnknown=true) //クラス定義に含まれていないものは読み飛ばす
public class WeatherData {


        public Date publicTime;
        public String title;

    public static class Description{
        public String text;
        public Date publicTime;

    }
    public Description description;
    public String link;

    public static class Forecasts {
        public String dateLabel;
        public String telop;
        public Date date;

        public static class Temperature {
            public static class Daikyu {
                public int celsius;
                public double fahrenheit;
            }

            public Daikyu min;
            public Daikyu max;
        }

        public Temperature temperature;

        public static class Image {
            public int width;
            public String url;
            public String title;
            public int height;
        }
        public Image image;
    }
    public Forecasts [] forecasts;

}
