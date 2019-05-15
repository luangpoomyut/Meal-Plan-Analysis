/**
 * Filename:   Main.java
 * Project:    FoodQuery
 * Authors:    Kevin Luangpoomyut, Sheung Chan, Jiahui Zhou, Matthew Kesler,
 *             Michael Thompson
 *
 */

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.*;
import javafx.scene.image.Image;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This class starts the application and runs it
 */
public class Main extends Application {
    // the various different buttons for the class
    private Button analyzeButton, createButton, addButton, clearButton, filterButton;
    // the table that has the food list
    TableView<FoodItem> foodTable;
    // the table that has the meal plan in it
    TableView<FoodItem> mealPlanTable;
    // the list of the food list
    ObservableList<FoodItem> foodDataList;
    // the list of the food meal plan
    ObservableList<FoodItem> mealPlanList = FXCollections.observableArrayList(new ArrayList<FoodItem>());
    // the instance of the food data
    FoodData foodData = new FoodData();
    // a list of all of the rules
    List<String> rules = new ArrayList<String>();
    // the file path to save to
    String stringChosenFile;
    // the root layout for the main scene
    VBox root;
    // a label that for the food list above the foodTable
    Label foodTableLabel;
    // a label that changes based on the amount of FoodItems
    Label foodCounter;


    /**
     * The main class that launches the application
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The program that starts the application and adds various buttons and layouts to the scene
     * @param primaryStage the main window that starts the GUI
     */
    @Override
    public void start(Stage primaryStage) {
        stringChosenFile = "";

        primaryStage.setTitle("Meal Plan");
        // get the bounds of the screen for the window
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        // set the window for the stage to maximum values
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        // Create button for analysis based on items in mealPlanList
        analyzeButton = new Button("Analyze Meal");
        // Custom class that handles creating a chart for the analysis
        analyzeButton.setOnAction(e -> YourMealPlanAnalysis.display("Your Meal Plan Analysis", "You have no items in your meal plan yet.", mealPlanList));

        // Create search by name functionality
        TextField searchField = new TextField();
        searchField.setPromptText("Search food by name!");
        // Button for searching, does not work dynamically
        Button searchButton = new Button("Search");
        Button clearSearchButton = new Button("Clear Search");

        // Search based on text in search text field when search button is pressed
        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                foodTable.setItems(FXCollections.observableArrayList(foodData.filterByName(searchField.getText().trim())));
            }
        });

        // Clear the search and revert back to original list
        clearSearchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // reset the search field
                searchField.clear();
                foodTable.setItems(FXCollections.observableArrayList(foodData.getAllFoodItems()));
            }
        });

        // Layout that contains the all of the various search nodes
        HBox searchByNameLayout = new HBox(10, searchField, searchButton, clearSearchButton);
        searchByNameLayout.setPadding(new Insets(5, 10, 5, 10));

        // create a tableView from the foodDataList values
        foodTable = createFoodTable(foodDataList);
        // create a layout for the foodTable so it can be spaced out
        HBox foodTableLayout = new HBox(foodTable);
        foodTableLayout.setPadding(new Insets(5, 10, 5, 10));

        // create a a tableView from the mealPlanList values
        mealPlanTable = createFoodTable(mealPlanList);
        // create a layout for the foodTable so it can be spaced out
        HBox mealPlanLayout = new HBox(mealPlanTable);
        mealPlanLayout.setPadding(new Insets(5, 10, 15, 10));

        // button for adding FoodItems from the foodList to the mealPlanList
        addButton = new Button("Add to Meal");
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // if something is actually selected from the foodList
                if (foodTable.getSelectionModel().getSelectedItem() != null) {
                    // get the selected FoodItem
                    FoodItem selectedFood = foodTable.getSelectionModel().getSelectedItem();
                    // add the FoodItem to the mealPlanList
                    mealPlanList.add(selectedFood);
                    // update the mealPlanTable
                    mealPlanTable.setItems(mealPlanList);
                }
            }
        });

        // label for the foodListTable
        foodTableLabel = new Label("Food List");
        foodTableLabel.setFont(new Font(24));
        foodTableLabel.setPadding(new Insets(0, 10, 5, 10));

        // label for the mealPlanTable
        Label mealPlanLabel = new Label("Meal Plan");
        mealPlanLabel.setFont(new Font(24));
        mealPlanLabel.setPadding(new Insets(0, 10, 5, 10));

        // filter button
        filterButton = new Button("Filter");
        // once button is pressed display a window based on a custom class
        filterButton.setOnAction(e -> Filter.display("Filter Food", foodData, rules, foodTable));

        // clear all FoodItems from the mealPlanList and the mealPlanTable
        clearButton = new Button("Clear Meal");
        // once button is pressed display a window based on a custom class
        clearButton.setOnAction(e -> ClearMeal.display(mealPlanList, mealPlanTable));

        // count the total number of FoodItems from the unfiltered foodDataList
        foodCounter = new Label("Total Number of Food Items: " + foodData.getAllFoodItems().size());
        // change the position
        foodCounter.setTranslateX(bounds.getWidth() - 400);

        // button that creates new FoodItem typed in by end user
        createButton = new Button("Create Food");
        // once button is pressed open new window prompt user using textFields
        createButton.setOnAction(e -> CreateFood.display("Create New Food", "Add properties to the food!", foodData, foodTable, foodCounter, rules));

        // create a counter for the amount of foodItems
        HBox foodListCounter = new HBox(foodTableLabel, foodCounter);
        // layout that holds all the buttons
        HBox buttonsLayout = new HBox(analyzeButton, createButton, addButton, clearButton, filterButton);
        // adjust the spacing and padding
        buttonsLayout.setSpacing(5);
        buttonsLayout.setPadding(new Insets(5, 10, 0, 10));

        // Create the final layout that holds all of the components of the main GUI
        root = new VBox();
        // Add all  the layouts to the root
        root.getChildren().addAll(createMenuBar(primaryStage), buttonsLayout, searchByNameLayout, foodListCounter, foodTableLayout, mealPlanLabel, mealPlanLayout);
        // Add the root to the main scene
        Scene mainScene = new Scene(root);

        // Set the stage to the mainScene
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    /**
     * Method that creates a MenuBar for the application for opening, saving, showing, hiding, and about
     * @param primaryStage the main window for the initial starting GUI
     * @return a MenuBar with opening, saving, showing, hiding, and about
     */
    private MenuBar createMenuBar(Stage primaryStage) {
        // File Menu functionality
        Menu fileMenu = new Menu("File");

        // Open functionality
        MenuItem fileOpen = new MenuItem("Open...");
        createOpenFunction(fileOpen, primaryStage);

        // Save functionality
        MenuItem fileSave = new MenuItem("Save");
        createSaveFunction(fileSave, primaryStage);

        // add fileOpen and fileSave to the main File menu
        fileMenu.getItems().add(fileOpen);
        fileMenu.getItems().add(fileSave);

        // View Menu functionality
        Menu viewMenu = new Menu("View");

        // Add a showFoodList dropdown
        MenuItem viewShowFoodList = new MenuItem("Show Food List");
        viewShowFoodList.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // make the foodList visible
                foodTable.setVisible(true);
                foodTableLabel.setText("Food List");
            }
        });

        // Add a hideFoodList dropdown
        MenuItem viewHideFoodList = new MenuItem("Hide Food List");

        viewHideFoodList.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // make the foodList hidden
                foodTable.setVisible(false);
                foodTableLabel.setText("Food List (hidden)");

            }
        });

        // Add both the show and hide FoodList dropdowns to the main view menu
        viewMenu.getItems().add(viewShowFoodList);
        viewMenu.getItems().add(viewHideFoodList);

        // Help Menu functionality
        Menu helpMenu = new Menu("Help");

        // Add an about dropdown
        MenuItem helpAbout = new MenuItem("About");
        helpMenu.getItems().add(helpAbout);

        helpAbout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage secondaryStage = new Stage();
                // Block input events with other windows until this one is taken care
                secondaryStage.initModality(Modality.APPLICATION_MODAL);

                // change the properties of the window
                secondaryStage.setTitle("About");
                secondaryStage.setWidth(700);
                secondaryStage.setHeight(700);
                secondaryStage.setResizable(false);

                // the text of instructions
                Label instructions = new Label("Created by: Sheung Chan, Jiahui Zhou, Matthew Kesler, Kevin Luangpoomyut, Michael Thompson\n" +
                        "\n" +
                        "How to Import a List of Food\n" +
                        "Click on File -> Open and select a file that is either in .txt or .csv format.\n" +
                        "\n" +
                        "How to Toggle to Hide or Show Food List\n" +
                        "Click on View -> Show Food List/Hide Food List\n" +
                        "\n" +
                        "How to Add Your Own Custom Food to the Food List\n" +
                        "Click on the ‘Create Food’ button and fill in the necessary information.\n" +
                        "\n" +
                        "How to Add a Food Item to Your Meal Plan\n" +
                        "Click on an item on the table and then click on the ‘Add to Meal’ button.\n" +
                        "\n" +
                        "How to Filter the Food List\n" +
                        "Click on the ‘Filter’ button and then specify filter criterion.\n" +
                        "\n" +
                        "How to Saves Your Food List\n" +
                        "Click on File -> Save and specify the name and location of the file you would like to save.");
                VBox aboutRoot = new VBox(instructions);
                aboutRoot.setAlignment(Pos.CENTER);
                Scene scene = new Scene(aboutRoot);
                secondaryStage.setScene(scene);
                // wait until user enters value
                secondaryStage.showAndWait();
            }
        });

        // Main menu bar
        MenuBar menuBar = new MenuBar();
        // adds all the menus to the menu bar
        menuBar.getMenus().addAll(fileMenu, viewMenu, helpMenu);
        return menuBar;
    }

    /**
     * Creates a tableView of FoodItems that users can select from for the foodList and mealPlanList
     * @param foodMembers the list of FoodItems to create a tableView from
     * @return the table created from the input list of FoodItems
     */
    private TableView<FoodItem> createFoodTable(ObservableList<FoodItem> foodMembers) {
        TableView<FoodItem> table = new TableView<FoodItem>();

        // set the items to the list of FoodItems
        table.setItems(foodMembers);

        // populate the cells based on the name and the nutrients from the Food Items

        TableColumn<FoodItem, String> nameCol = new TableColumn<FoodItem, String>("Name");
        nameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        nameCol.setMinWidth(400);

        TableColumn<FoodItem, String> caloriesCol = new TableColumn<FoodItem, String>("Calories");
        caloriesCol.setCellValueFactory(cellData -> new SimpleStringProperty(Double.toString(cellData.getValue().getNutrientValue("calories"))));
        caloriesCol.setMinWidth(300);
        caloriesCol.setMaxWidth(300);

        TableColumn<FoodItem, String> fatCol = new TableColumn<FoodItem, String>("Fat");
        fatCol.setCellValueFactory(cellData -> new SimpleStringProperty(Double.toString(cellData.getValue().getNutrientValue("fat"))));
        fatCol.setMinWidth(300);
        fatCol.setMaxWidth(300);

        TableColumn<FoodItem, String> carbohydrateCol = new TableColumn<FoodItem, String>("Carbohydrate");
        carbohydrateCol.setCellValueFactory(cellData -> new SimpleStringProperty(Double.toString(cellData.getValue().getNutrientValue("carbohydrate"))));
        carbohydrateCol.setMinWidth(300);
        carbohydrateCol.setMaxWidth(300);

        TableColumn<FoodItem, String> fiberCol = new TableColumn<FoodItem, String>("Fiber");
        fiberCol.setCellValueFactory(cellData -> new SimpleStringProperty(Double.toString(cellData.getValue().getNutrientValue("fiber"))));
        fiberCol.setMinWidth(300);
        fiberCol.setMaxWidth(300);

        TableColumn<FoodItem, String> proteinCol = new TableColumn<FoodItem, String>("Protein");
        proteinCol.setCellValueFactory(cellData -> new SimpleStringProperty(Double.toString(cellData.getValue().getNutrientValue("protein"))));
        proteinCol.setMinWidth(300);
        proteinCol.setMaxWidth(300);

        // Add all the populated columns to the table
        table.getColumns().setAll(nameCol, caloriesCol, fatCol, carbohydrateCol, fiberCol, proteinCol);
        return table;

    }


    /**
     * Create the functionality of opening and loading the file to the foodData instance
     * @param fileOpen the menu item for Open
     * @param primaryStage the main window for the GUI when it starts
     */
    private void createOpenFunction(MenuItem fileOpen, Stage primaryStage) {
        // Create instance of FileChooser
        FileChooser fileChooser = new FileChooser();

        // Set clicking on Open to open a File Chooser
        fileOpen.setOnAction(e -> {
            File chosenFile = fileChooser.showOpenDialog(primaryStage);
            try {
                // get the string filePath
                stringChosenFile = chosenFile.toPath().toString().substring(0);
            } catch (Exception a) {
            }
            // load the new file to the instance of the FoodData class
            foodData.loadFoodItems(stringChosenFile);
            // update the foodDataList
            foodDataList = FXCollections.observableArrayList(foodData.getAllFoodItems());

            // make the table editable
            foodTable.setEditable(true);

            // update foodTable
            foodTable.setItems(foodDataList);
            // update the foodCounter label
            foodCounter.setText("Total Number of Food Items: " + foodData.getAllFoodItems().size());
        });

    }

    /**
     * Create the functionality of the save dropdown
     * @param fileSave the menu item for Save
     * @param primaryStage the main window for the GuI when it starts
     */
    private void createSaveFunction(MenuItem fileSave, Stage primaryStage) {
        // create an instance of a FileChooser
        FileChooser fileChooser = new FileChooser();
        // once menu item is selected open fileChooser
        fileSave.setOnAction(e -> {
            // open Save dialog
            File chosenFile = fileChooser.showSaveDialog(primaryStage);
            try {
                // get the string file path
                stringChosenFile = chosenFile.toPath().toString().substring(0);
                foodData.saveFoodItems(stringChosenFile);
            } catch (Exception a) {
                System.out.println("Save failed");
            }
        });
    }
}
