/**
 * Filename:   CreateFood.java
 * Project:    FoodQuery
 * Authors:    Kevin Luangpoomyut, Sheung Chan, Jiahui Zhou, Matthew Kesler,
 *             Michael Thompson
 *
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.logging.Filter;

/**
 * A class for displaying a prompt for the user to create a new FoodItem to add to the foodDataList
 */
public class CreateFood {
    // static variable for the id that always just increments by 1 when adding
    static String id = "1";

    /**
     * Displays a new window prompting the user to create a new FoodItem
     * @param title the title of the window
     * @param message the message for the end user to see
     * @param foodData the instance of the foodData
     * @param foodTable the table that contains all of the FoodItems in the list
     * @param foodCounter a label that keeps track of the total number of foodItems (not the filtered amount)
     * @param rules a list that contains the String version of the rules
     */
    public static void display(String title, String message, FoodData foodData, TableView<FoodItem> foodTable, Label foodCounter, List<String> rules) {
        // create a new window
        Stage secondaryStage = new Stage();

        // Block input events with other windows until this one is taken care
        secondaryStage.initModality(Modality.APPLICATION_MODAL);

        // change the properties of the window
        secondaryStage.setTitle(title);
        secondaryStage.setWidth(300);
        secondaryStage.setHeight(390);
        secondaryStage.setResizable(false);

        // Make a table with text fields
        TableView<NewFoodEntry> table = new TableView<NewFoodEntry>();
        table.setEditable(true);

        // Create label and textField for end user input values

        // description column
        TableColumn descriptionCol = new TableColumn("");
        descriptionCol.setCellValueFactory(
                new PropertyValueFactory<NewFoodEntry, Label>("description"));
        descriptionCol.setMinWidth(120);
        descriptionCol.setMaxWidth(120);

        // textfield column
        TableColumn getValueCol = new TableColumn("");
        getValueCol.setCellValueFactory(
                new PropertyValueFactory<NewFoodEntry, TextField>("textField"));

        // This label is for the close button
        Label label = new Label();
        label.setText(message);

        // Close button functionality
        Button closeButton = new Button("Close the window.");
        label.setTranslateX(0);
        closeButton.setOnAction(e -> secondaryStage.close());

        // The various AddEntries
        NewFoodEntry nameEntry = new NewFoodEntry("Name", "name of food");
        NewFoodEntry caloriesEntry = new NewFoodEntry("Calories", "# of calories");
        NewFoodEntry fatsEntry = new NewFoodEntry("Fats", "# of fats");
        NewFoodEntry fiberEntry = new NewFoodEntry("Fiber", "# of fiber");
        NewFoodEntry carbohydratesEntry = new NewFoodEntry("Carbohydrates", "# of carbohydrates");
        NewFoodEntry proteinsEntry = new NewFoodEntry("Proteins", "# of proteins");

        // This list holds the various entry textFields
        ObservableList<NewFoodEntry> addEntries = FXCollections.observableArrayList(nameEntry, caloriesEntry, fatsEntry, fiberEntry, carbohydratesEntry, proteinsEntry);

        // Adds the entries to the table
        table.setItems(addEntries);
        table.getColumns().addAll(descriptionCol, getValueCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Add button functionality
        Button addButton = new Button("Add Item.");

        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Get the value of each textField and add it to the AddEntry item
                for (NewFoodEntry entry : addEntries)
                    entry.setValue(entry.getTextField());

                // Values are parsed and gotten here for readability
                try {
                    // get the nutrients from the TextField
                    String newName = addEntries.get(0).getValue();
                    double newCalories = Double.parseDouble(addEntries.get(1).getValue());
                    double newFat = Double.parseDouble(addEntries.get(2).getValue());
                    double newFiber = Double.parseDouble(addEntries.get(3).getValue());
                    double newCarbohydrate = Double.parseDouble(addEntries.get(4).getValue());
                    double newProtein = Double.parseDouble(addEntries.get(5).getValue());

                    // if the values are negative throw an exception
                    if (newFiber < 0) throw new Exception();
                    if (newProtein < 0) throw new Exception();
                    if (newFat < 0) throw new Exception();
                    if (newCalories < 0) throw new Exception();
                    if (newCarbohydrate < 0) throw new Exception();

                    // add the nutrients from the text field to the foodItem
                    FoodItem newFood = new FoodItem(id, newName);
                    newFood.addNutrient("fiber", newFiber);
                    newFood.addNutrient("protein", newProtein);
                    newFood.addNutrient("fat", newFat);
                    newFood.addNutrient("calories", newCalories);
                    newFood.addNutrient("carbohydrate", newCarbohydrate);

                    // Add the new foodItem
                    foodData.addFoodItem(newFood);

                    // update the foodCounter after adding a foodItem
                    foodCounter.setText("Total Number of Food Items: " + Integer.toString(foodData.getAllFoodItems().size()));

                    // update the table to the foodData
                    foodTable.setItems(FXCollections.observableArrayList(foodData.getAllFoodItems()));

                    // if there are filters in the rules list, update the table to filteredFoodList
                    if (!rules.isEmpty()) {
                        foodTable.setItems(FXCollections.observableArrayList(foodData.filterByNutrients(rules)));
                    }

                    // Revert back to original AddFood display
                    for (NewFoodEntry entry : addEntries)
                        entry.getTextField().clear();
                    // increment the id
                    id = id + 1;
                } catch (Exception e) {
                    // create an AlertBox
                    Alert invalidValue = new Alert(Alert.AlertType.WARNING,
                            "You must enter a non-negative number for all nutrient values.");
                    // wait until user clicks no and yes
                    invalidValue.showAndWait();
                    return;
                }
            }
        });
        // create a root to hold the various elements
        VBox root = new VBox(10);
        root.getChildren().addAll(label, table, new HBox(10, closeButton, addButton));
        root.setPadding(new Insets(10, 10, 10, 10));
        label.setTranslateX(50);
        // add the root to main scene
        Scene scene = new Scene(root);
        secondaryStage.setScene(scene);
        // wait until user enters value
        secondaryStage.showAndWait();
    }
}

