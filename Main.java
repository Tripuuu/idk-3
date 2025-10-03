package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.swing.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class Main {

   private static final String FILE_NAME = "events.json";
   private static final ObjectMapper objectMapper = new ObjectMapper();

    //  Metodo para cargar eventos
   public static List<Event> loadEvents () {
        try (FileReader reader = new FileReader(FILE_NAME)) {
            Event[] eventsArray = objectMapper.readValue(reader, Event[].class);
            List<Event> events = new ArrayList<>();
            for (Event e : eventsArray) {
                events.add(e);
            }
            return events;
        } catch (IOException e) {
            return new ArrayList<>(); // si no existe el archivo, lista vacía
        }
    }

    // Metodo para guardar eventos
   public static void saveEvents (List < Event > events) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(FILE_NAME))) {
            objectMapper.writeValue(writer, events);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        JFrame frame = new JFrame("Calendario");
        frame.setSize(400, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTextField descriptionField = new JTextField(20);
        JTextField startField = new JTextField(10);
        JTextField endField = new JTextField(10);
        JButton saveButton = new JButton("Guardar Evento");

        JPanel panel = new JPanel();
        panel.add(new JLabel("Descripción:"));
        panel.add(descriptionField);
        panel.add(new JLabel("Hora inicio:"));
        panel.add(startField);
        panel.add(new JLabel("Hora fin:"));
        panel.add(endField);
        panel.add(saveButton);

        frame.add(panel);
        frame.setVisible(true);

        saveButton.addActionListener(e -> {
            String description = descriptionField.getText();
            String startTime = startField.getText();
            String endTime = endField.getText();

            if (!description.isEmpty() && !startTime.isEmpty() && !endTime.isEmpty()) {
                List<Event> events = loadEvents();  // carga eventos existentes
                events.add(new Event(description, startTime, endTime));
                saveEvents(events);
                JOptionPane.showMessageDialog(frame, "Evento guardado en " + FILE_NAME);
                descriptionField.setText("");
                startField.setText("");
                endField.setText("");
            } else {
                JOptionPane.showMessageDialog(frame, "Por favor llena todos los campos");
            }
        });

    }
}