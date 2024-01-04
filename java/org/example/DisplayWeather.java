package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.UIManager;

public class DisplayWeather implements ActionListener {

    //JFrame frame;
    JPanel weatherPanel;
    JLabel tempLabel, cityLabel, timeLabel, descLabel, imageLabel, precLabel, feelsLabel, feelsLikeValue;
    ImageIcon weatherImg;
    JPanel topPanel, leftPanel, rightPanel, bottomPanel, centerPanel;
    JProgressBar progressBar;
    JButton celsiusButton, backButton;
    Color titleColor;

    DisplayWeather(int farTemp, String desc, String city, String country, String continent, int humidity, int feelsLike) {

        // -------- sets the background color of the JFrame ---------------------
        try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize theme. Using fallback." );
        }
        // ----------------------------------------------------------------------

        // get current time of city
        String currTime = getCurrTime(city, continent);

        // get a color based on hex values
        Color customColor = Color.decode("#FFF0ED");
        titleColor = Color.decode("#FBA998");

        // panel settings
        weatherPanel = new JPanel();
        weatherPanel.setLayout(new BorderLayout(5, 5)); // sets panels to have a 5 pixel margin
        weatherPanel.setSize(600, 500);
        weatherPanel.setBackground(customColor);

        // add border panels to border layout of JPanel
        addBorderPanels();

        // temperature value settings
        tempLabel = new JLabel(farTemp + "°F");
        centerPanel.add(tempLabel, BorderLayout.NORTH);

        tempLabel.setVerticalAlignment(JLabel.TOP);
        tempLabel.setHorizontalAlignment(JLabel.CENTER);
        tempLabel.setFont(new Font("Serif", Font.PLAIN, 60));

        // description string settings
        descLabel = new JLabel(desc);
        centerPanel.add(descLabel, BorderLayout.CENTER);
        descLabel.setVerticalAlignment(JLabel.TOP);
        descLabel.setHorizontalAlignment(JLabel.CENTER);
        descLabel.setFont(new Font("Serif", Font.ITALIC, 20));

        // get correct image based on weather description
        String filepath = getWeatherImage(desc);

        // weather image resizing settings
        weatherImg = new ImageIcon(filepath);
        Image image = weatherImg.getImage(); // get image
        Image newImg = image.getScaledInstance(400,200, Image.SCALE_SMOOTH); // resizes the image
        weatherImg = new ImageIcon(newImg); // puts resized image into original icon

        // add weather image to frame
        imageLabel = new JLabel(weatherImg);
        centerPanel.add(imageLabel, BorderLayout.SOUTH);
        imageLabel.setVerticalAlignment(JLabel.TOP);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);

        // city name container settings
        JPanel cityContainer = new JPanel(new BorderLayout());
        cityContainer.setPreferredSize(new Dimension(200, 50));
        cityContainer.setBackground(titleColor);

        // makes city title in center of screen
        JPanel rightFiller = new JPanel();
        rightFiller.setPreferredSize(new Dimension(75, 50));
        rightFiller.setBackground(titleColor);

        // city name settings
        cityLabel = new JLabel(city + ", " +  country);
        cityLabel.setFont(new Font("Serif", Font.BOLD, 40));
        cityLabel.setHorizontalAlignment(JLabel.CENTER);

        // current time settings
        timeLabel = new JLabel(currTime);
        timeLabel.setFont(new Font("Serif", Font.PLAIN, 30));
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // adds container to top left of screen
        JPanel backContainer = new JPanel();
        backContainer.setPreferredSize(new Dimension(75, 50));
        backContainer.setBackground(titleColor);

        // add a back button to return to home screen
        backButton = new JButton("Back");
        backContainer.add(backButton);

        // add city and time to top of screen
        topPanel.add(timeLabel, BorderLayout.SOUTH);
        topPanel.add(backContainer, BorderLayout.WEST);
        topPanel.add(cityContainer, BorderLayout.CENTER);
        topPanel.add(rightFiller, BorderLayout.EAST);
        cityContainer.add(cityLabel, BorderLayout.CENTER);

        // add precipitation label
        precLabel = new JLabel("Humidity");
        precLabel.setFont(new Font("Serif", Font.BOLD, 20));
        precLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // add bar for precipitation probability at right of screen
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setOrientation(JProgressBar.VERTICAL);
        progressBar.setValue(humidity);
        progressBar.setFont(new Font("Serif", Font.ITALIC, 35));
        progressBar.setPreferredSize(new Dimension(50, 200));

        // put the progress bar into a container panel, so it does not take up the whole
        // width of the right panel
        JPanel progressBarContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        progressBarContainer.setOpaque(false);
        progressBarContainer.add(progressBar);

        // add precipitation chance bar and precipitation label
        rightPanel.add(precLabel, BorderLayout.NORTH);
        rightPanel.add(progressBarContainer, BorderLayout.CENTER);

        // contains the feels like temperatures
        JPanel feelsContainer = new JPanel(new BorderLayout());
        feelsContainer.setPreferredSize(new Dimension(50, 100));
        feelsContainer.setOpaque(false);
        leftPanel.add(feelsContainer, BorderLayout.NORTH);

        // add feels like temperature to left of screen
        feelsLabel = new JLabel("Feels like: ");
        feelsLabel.setFont(new Font("Serif", Font.BOLD, 20));
        feelsLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // add feels like temperature value
        feelsLikeValue = new JLabel(feelsLike + "°F");
        feelsLikeValue.setFont(new Font("Serif", Font.BOLD, 35));
        feelsLikeValue.setHorizontalAlignment(SwingConstants.CENTER);

        // add feels like label and temperature to container in left panel
        feelsContainer.add(feelsLabel, BorderLayout.NORTH);
        feelsContainer.add(feelsLikeValue, BorderLayout.CENTER);

        // add button to switch between C and F
        celsiusButton = new JButton("Switch to C");
        celsiusButton.setFocusable(false);
        bottomPanel.add(celsiusButton);

    }

    public void addBorderPanels () {

        // panel settings
        topPanel = new JPanel(new BorderLayout()); // can now position labels within top panel
        leftPanel = new JPanel(new BorderLayout());
        rightPanel = new JPanel(new BorderLayout());
        bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // allows button to be in center of panel
        centerPanel = new JPanel(new BorderLayout());

        // panel sizes
        topPanel.setPreferredSize(new Dimension(100, 100));
        leftPanel.setPreferredSize(new Dimension(100, 100));
        rightPanel.setPreferredSize(new Dimension(100, 100));
        bottomPanel.setPreferredSize(new Dimension(100, 50));
        centerPanel.setPreferredSize(new Dimension(50, 50));

        // make panels transparent so you can change the background color
        topPanel.setBackground(titleColor);
        leftPanel.setOpaque(false);
        rightPanel.setOpaque(false);
        bottomPanel.setOpaque(false);
        centerPanel.setOpaque(false);

        // color coding to see panels better
        //centerPanel.setBackground(Color.BLUE);
        //topPanel.setBackground(Color.green);

        // add panels
        weatherPanel.add(topPanel, BorderLayout.NORTH); // attaches panel to the top of weatherPanel
        weatherPanel.add(leftPanel, BorderLayout.WEST); // attaches to the left of weatherPanel
        weatherPanel.add(rightPanel, BorderLayout.EAST); // attaches to the right of weatherPanel
        weatherPanel.add(bottomPanel, BorderLayout.SOUTH); // attaches to bottom of weatherPanel
        weatherPanel.add(centerPanel, BorderLayout.CENTER); // attaches to the center of weatherPanel
    }

    public String getCurrTime (String cityName, String continentStr) {

        // format cities with spaces in between
        String formatCity = cityName.replaceAll(" ", "_");

        //ZoneId timeZone = ZoneId.of("America/New_York");
        ZoneId timeZone = ZoneId.of(continentStr + "/" + formatCity);

        // Get the current time
        ZonedDateTime currTime = ZonedDateTime.now(timeZone);

        // Define the desired date and time format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a z");

        // Format and print the current time
        String formattedTime = currTime.format(formatter);
        System.out.println("Current time in " + cityName + ": " + formattedTime);

        return formattedTime;
    }

    public String getWeatherImage (String description) {

        if (description.equals("overcast clouds") || description.equals("few clouds") || description.equals("broken clouds")) {
            return "C:\\Users\\n_lon\\partly_clouds.png";
        }
        else if (description.equals("clear sky")) {
            return "C:\\Users\\n_lon\\sun.png";
        }
        else {
            return "C:\\Users\\n_lon\\partly_clouds.png";
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
