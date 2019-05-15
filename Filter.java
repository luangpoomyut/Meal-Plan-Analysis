/**
 * Filename:   Filter.java
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

import java.util.ArrayList;
import java.util.List;

/**
 * A class for prompting for a filter and adding it to the rules
 */
public class Filter {

    /**
     * display the window for adding filters
     *
     * @param title     the title of the window
     * @param foodData  the instance of the foodData
     * @param rules     the list of the filters
     * @param foodTable the list of the FoodItems
     */
    public static void display(String title, FoodData foodData, List<String> rules, TableView<FoodItem> foodTable) {
        // a new window
        Stage secondaryStage = new Stage();

        // Block input events with other windows until this one is taken care
        secondaryStage.initModality(Modality.APPLICATION_MODAL);

        // change the properties of the window
        secondaryStage.setTitle(title);
        secondaryStage.setWidth(800);
        secondaryStage.setHeight(390);

        // button to add new filters
        Button addRuleButton = new Button("Add Rule");
        // button to apply all of the filters
        Button applyButton = new Button("Apply Query");

        // add the a choice box with the comparison operators
        ChoiceBox<String> comparatorChoiceBox = new ChoiceBox();
        comparatorChoiceBox.getItems().addAll(">=", "<=", "==");
        comparatorChoiceBox.setValue(">=");

        // add a choice box with the filter categories
        ChoiceBox<String> filterChoiceBox = new ChoiceBox();
        filterChoiceBox.getItems().addAll("calories", "fat", "fiber", "carbohydrate", "protein");
        filterChoiceBox.setValue("calories");

        // create a textField
        TextField valueTextField = new TextField();
        valueTextField.setPromptText("Value to filter");

        VBox showRulesLayout = new VBox();

        // make sure to create new labels and buttons for the filters
        for (int i = 0; i < rules.size(); ++i) {
            // get the rule
            String filter = rules.get(i);
            Label filterLabel = new Label(rules.get(i));
            Button closeButton = new Button("x");
            closeButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    // if click the x button, remove the filter from the rules
                    rules.remove(filter);
                    showRulesLayout.getChildren().remove(filterLabel);

                }
            });

            // set the label to the x button
            filterLabel.setGraphic(closeButton);
            filterLabel.setContentDisplay(ContentDisplay.RIGHT);
            // add it to the layout
            showRulesLayout.getChildren().add(filterLabel);
        }

        addRuleButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // get the value from the choiceBoxes and textField
                String nutrientFilter = filterChoiceBox.getSelectionModel().getSelectedItem();
                String comparisonFilter = comparatorChoiceBox.getSelectionModel().getSelectedItem();
                String filterValue = valueTextField.getText().trim();

                try {
                    if (Double.parseDouble(filterValue) < 0) {
                        throw new Exception();
                    }
                } catch (Exception e) {
                    // create an AlertBox if they type a nonegative value or letter
                    Alert invalidValue = new Alert(Alert.AlertType.WARNING,
                            "You must enter a non-negative number for the value.");
                    invalidValue.showAndWait();
                    return;
                }

                // concatenate the string from the values from the choiceBox and textField
                String filter = nutrientFilter + " " + comparisonFilter + " " + filterValue;

                // create filter object
                Label filterLabel = new Label(filter);
                Button closeButton = new Button("x");

                // when click on the x button of the filter label remove from the rules list
                closeButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        rules.remove(filter);
                        showRulesLayout.getChildren().remove(filterLabel);

                    }
                });
                filterLabel.setGraphic(closeButton);
                filterLabel.setContentDisplay(ContentDisplay.RIGHT);
                showRulesLayout.getChildren().add(filterLabel);

                // add the filters to the rules
                rules.add(filter);
                valueTextField.clear();
            }
        });
        // when the user clicks the apply button filter the list based on the rules
        applyButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (rules.isEmpty())
                    foodTable.setItems(FXCollections.observableArrayList(foodData.getAllFoodItems()));
                else
                    foodTable.setItems(FXCollections.observableArrayList(foodData.filterByNutrients(rules)));
                secondaryStage.close();
            }
        });

        // add the all of the filter categories to a layout
        HBox propertiesLayout = new HBox(10, filterChoiceBox, comparatorChoiceBox, valueTextField, addRuleButton, applyButton);
        propertiesLayout.setAlignment(Pos.CENTER);

        // add the layouts to the root
        VBox root = new VBox(propertiesLayout, showRulesLayout);
        root.setPadding(new Insets(10, 10, 10, 10));

        // add the root to the main window
        Scene scene = new Scene(root);
        secondaryStage.setScene(scene);
        secondaryStage.showAndWait();
    }
}

