package org.example;

import org.w3c.dom.html.HTMLImageElement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DisplayForm {

    JFrame frame = new JFrame("Current Weather");
    JPanel contentPanel = new JPanel();
    JPanel formPanel = new JPanel();
    JPanel leftFormPanel = new JPanel();
    JPanel rightFormPanel = new JPanel();
    JPanel leftFillerPanel = new JPanel();
    JPanel rightFillerPanel = new JPanel();
    JPanel centerPanel = new JPanel(new BorderLayout());
    JLabel imageLabel;
    JLabel title = new JLabel("Weather Fetcher");
    JButton firstButton = new JButton("switch to second panel");
    CardLayout cardLayout = new CardLayout();
    HintTextField cityField = new HintTextField("Enter a city");
    HintTextField countryField = new HintTextField("Enter the country initials");

    String[] CONTINENTS = {"America", "Europe", "Asia", "Africa", "Australia"};
    String cityName, countryName, continentName;
    JComboBox<String> continentDropDown = new JComboBox<>(CONTINENTS);

    DisplayForm() {

        // frame settings
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(5, 5)); // sets panels to have a 5 pixel margin
        frame.setSize(600, 500);

        // card layout allows for different panels to be shown at different times
        // basically allows different screens
        contentPanel.setLayout(cardLayout);

        // ------------------ creating a form on the first screen -----------------------

        // main panel settings
        formPanel.setLayout(new BorderLayout(0, 0));
        formPanel.setSize(600, 500);

        // set dimensions for left and right side of main panel
        leftFormPanel.setPreferredSize(new Dimension(300, 500));
        leftFormPanel.setFocusable(true);
        //leftFormPanel.setBackground(Color.BLUE);

        leftFormPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 15, 15, 15); // 15 pixels around items

        title.setFont(new Font("Serif", Font.PLAIN, 40));
        leftFormPanel.add(title);

        // text field settings
        cityField.setMaximumSize(new Dimension(130, 60));
        gbc.gridy = 3;
        leftFormPanel.add(cityField, gbc);

        countryField.setMaximumSize(new Dimension(130, 60));
        gbc.gridy = 2; // 3 in the index of grid bag layout
        leftFormPanel.add(countryField, gbc);

        // dropdown settings
        continentDropDown.setMaximumSize(new Dimension(130, 60));
        continentDropDown.setFocusable(false);
        gbc.gridy = 1; // 2nd index vertically
        leftFormPanel.add(continentDropDown, gbc);

        firstButton.setMaximumSize(new Dimension(130 , 30));
        gbc.gridy = 4; // 5th index vertically
        leftFormPanel.add(firstButton, gbc);

        rightFormPanel.setPreferredSize(new Dimension(300, 500));
        rightFormPanel.setLayout(new BorderLayout());

        leftFillerPanel.setPreferredSize(new Dimension(50, 500));
        rightFillerPanel.setPreferredSize(new Dimension(50, 500));
        centerPanel.setPreferredSize(new Dimension(200, 500));

        rightFillerPanel.setOpaque(false);
        leftFillerPanel.setOpaque(false);
        centerPanel.setOpaque(false);

        rightFormPanel.add(leftFillerPanel, BorderLayout.WEST);
        rightFormPanel.add(rightFillerPanel, BorderLayout.EAST);
        rightFormPanel.add(centerPanel, BorderLayout.CENTER);

        rightFormPanel.setBackground(Color.decode("#FBA998"));

        ImageIcon loginImage = new ImageIcon("C:\\Users\\n_lon\\loginPng.png");
        Image image = loginImage.getImage(); // get image
        Image newImg = image.getScaledInstance(500,400, Image.SCALE_SMOOTH); // resizes the image
        loginImage = new ImageIcon(newImg); // puts resized image into original icon

        imageLabel = new JLabel(loginImage);
        centerPanel.add(imageLabel, BorderLayout.CENTER);

        // add panels
        formPanel.add(leftFormPanel, BorderLayout.WEST);
        formPanel.add(rightFormPanel, BorderLayout.EAST);

        // ------------------------------------------------------------------------------

        // add screens to main panel
        contentPanel.add(formPanel, "1");
        cardLayout.show(contentPanel, "1");

        frame.add(contentPanel);


        firstButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                cityName = cityField.getText();
                countryName = countryField.getText();
                continentName = continentDropDown.getSelectedItem().toString();

                System.out.println("Continent entered: " + continentName);
                System.out.println("Country entered: " + countryName);
                System.out.println("City entered: " + cityName);

                // get weather variables
                GetWeatherData weatherData = new GetWeatherData(cityName, countryName, continentName);

                // store weather data
                float temp = weatherData.Ftemp;
                float celsius = weatherData.Ctemp;
                int hum = weatherData.humidity;
                float feelsLike = weatherData.feelsTemp;
                String desc = weatherData.weatherDesc;
                String city = weatherData.cityName;
                String country = weatherData.countryName;
                String continent = weatherData.continent;

                // get weatherPanel which displays all weather information
                DisplayWeather display = new DisplayWeather((int) temp, desc, city, country, continent, hum, (int) feelsLike);

                contentPanel.add(display.weatherPanel, "2"); // add weather data to second screen

                display.backButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        cardLayout.show(contentPanel, "1");
                    }
                });

                display.celsiusButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                    }
                });

                cardLayout.show(contentPanel, "2"); // switch to second screen
            }
        });

        // so you can click off the text fields at any time
        contentPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                contentPanel.requestFocusInWindow();
            }
        });

        // ending frame settings
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);

        // nothing is in focus when the app is opened
        leftFormPanel.requestFocusInWindow();

    }

}
