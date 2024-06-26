import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

class WeatherApp extends JFrame {
    private JTextField cityField;
    private JButton getWeatherButton;
    private JTextArea weatherInfoArea;

    private static final String API_KEY = "99a02205e43544b7049518008d0fa3a6";  // Replace with your API key

    public WeatherApp() {
        setTitle("Weather App");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        cityField = new JTextField(15);
        getWeatherButton = new JButton("Get Weather");
        panel.add(new JLabel("City:"));
        panel.add(cityField);
        panel.add(getWeatherButton);

        weatherInfoArea = new JTextArea();
        weatherInfoArea.setEditable(false);

        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(weatherInfoArea), BorderLayout.CENTER);

        getWeatherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String city = cityField.getText().trim();
                if (!city.isEmpty()) {
                    fetchWeatherData(city);
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a city name.");
                }
            }
        });

        setVisible(true);
    }

    private void fetchWeatherData(String city) {
        try {
            String urlString = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY + "&units=metric";
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            connection.disconnect();

            parseWeatherData(content.toString());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching weather data. Please check the city name and try again.");
        }
    }

    private void parseWeatherData(String jsonData) {
        JSONObject jsonObject = new JSONObject(jsonData);
        String cityName = jsonObject.getString("name");
        JSONObject main = jsonObject.getJSONObject("main");
        double temp = main.getDouble("temp");
        double feelsLike = main.getDouble("feels_like");
        int humidity = main.getInt("humidity");

        StringBuilder weatherInfo = new StringBuilder();
        weatherInfo.append("City: ").append(cityName).append("\n");
        weatherInfo.append("Temperature: ").append(temp).append(" °C\n");
        weatherInfo.append("Feels Like: ").append(feelsLike).append(" °C\n");
        weatherInfo.append("Humidity: ").append(humidity).append(" %\n");

        weatherInfoArea.setText(weatherInfo.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WeatherApp());
    }
}
