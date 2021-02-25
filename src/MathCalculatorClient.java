// Network Calculator
// Class MathCalculatorClient
// Davis Insua

import javafx.application.Application;
import java.util.ArrayList;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.paint.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import java.io.*;
import java.net.*;

public class MathCalculatorClient extends Application
{

   // Declare server elements
   public static final int LISTENING_PORT = 6000;
   PrintWriter outgoing;
   Socket connection;
   BufferedReader incoming;

   // Declare variables for handleButtonAction function
   private int num1 = -1, num2 = -1;
   private String operator, result;

   // Declare number display box
   TextField displayField = new TextField();

   // Create arraylist for UI buttons using strings, allows for more concise editing of buttons using loops
   String buttonString[] = {"1", "2", "3", "+", "4", "5", "6", "-", "7", "8", "9", "*", "0", "=", "CLR", "/"};
   ArrayList<Button> buttons = new ArrayList<Button>(16);

   // Needed
   public static void main(String[] args)
   {
      launch(args);
   }

   @Override
   public void start(Stage myStage)
   {
     try
     {
         // Define server elements
         connection = new Socket( "localhost", LISTENING_PORT );
         incoming = new BufferedReader(new InputStreamReader(connection.getInputStream()));
         outgoing = new PrintWriter(connection.getOutputStream(), true);
     }

     // Handle errors
     catch (Exception e)
     {
         System.out.println("Error:  " + e);
     }

      // Stage options
      myStage.setTitle("Davis' Calculator");
      myStage.setResizable(false);

      // Top number display options
      displayField.setEditable(false);
      displayField.setPrefWidth(300);
      displayField.setFont(Font.font("Consolas", FontWeight.NORMAL, 20));

      // Loop to declare and modify UI buttons
      for (int i = 0; i < buttonString.length; i++)
      {
          // Holds current button's string, used for handle action
          String temp = buttonString[i];
          // Create button with specified string from buttonString
          buttons.add(new Button(temp));
          // Adjust size of button
          buttons.get(i).setFont(Font.font("Consolas", FontWeight.NORMAL, 20));
          buttons.get(i).setMinWidth(75);
          buttons.get(i).setMinHeight(75);
          // Button event handling
          buttons.get(i).setOnAction(e->handleButtonAction(temp));
      }

      // Hbox options
      HBox topField = new HBox();
      topField.getChildren().addAll(displayField);

      // Gridpane options
      GridPane calc = new GridPane();

      // Add buttons to gridpane calc
      int counter = 0;
      for (int y = 0; y < 4; y++)
      {
          for (int x = 0; x < 4; x++)
          {
              // Add buttons from button arraylist
              calc.add(buttons.get(counter), x, y);
              counter++;
          }
      }

      // Borderpane (root) options
      BorderPane root = new BorderPane();
      root.setTop(topField);
      root.setBottom(calc);

      // Scene options
      Scene scene = new Scene(root, 300, 338, Color.WHITE);
      myStage.setScene(scene);
      myStage.show();
   }

   // Driver function for handling the button presses on calculator
   void handleButtonAction(String st)
   {
       // Rest Display Color
       displayField.setStyle("-fx-background-color: #FFFFFF");

       // Try statement for error handling
      try
      {
          // Switch statement which does the appropriate action based on the arguments passed to the handleButtonAction function (buttons pressed)
         switch(st)
         {
            // User presses an arithmetic operator, call handleOperator function and pass string
            case "+": case "-": case "/": case "*":
               handleOperator(st);
               break;

            // User presses equal button, call handleEqualSign function and pass string
            case "=":
               handleEqualSign(st);
               break;

            // User presses CLR button
            case "CLR":
               // Wipe current num and operator values
               num1 = num2 = -1;
               operator = "N/A";
               // Reset display
               displayField.setText("");
               break;

            // User presses a number button
            default:
               displayField.appendText(st);
               break;
         }
      }

      // Handle the number format exception
      catch(NumberFormatException ex)
      {
         displayField.setText("");
      }
   }

   // Helper function to handle events from the operator buttons (+, -, /, *)
   void handleOperator(String st)
   {
        // Save number entered before this to memory, assign it to num1
        num1 = Integer.parseInt(displayField.getText());
        // Clear top field
        displayField.setText("");
        // Update operator for use in equal switch statement
        operator = st;
    }

   // Helper function to handle events from the equal sign
   void handleEqualSign(String st)
   {
        // save the second number in memory, assign to num2
        num2 = Integer.parseInt(displayField.getText());

        // Handles scenario if user presses 1 number, and then presses equal
        if(num1 == -1)
        {
            displayField.setText("");
            return;
        }

        // Switch based on what operator was pressed before equal sign
        switch(operator)
        {
            case "+":
                result = num1+" + "+num2;
                break;
            case "-":
                result = num1+" - "+num2;
                break;
            case "*":
                result = num1+" * "+num2;
                break;
            case "/":
                result = num1+" / "+num2;
                break;
        }

        // Update display field with calculated result
        try
        {
            // Send Math string to server
            outgoing.println(result);
            // Get result from the server and update the displayfield
            displayField.setText(incoming.readLine());
            displayField.setStyle("-fx-background-color: #07eb31");
        }

        // Error handle
        catch (Exception e)
        {
            System.out.println("Error:  " + e);
        }
    }
}