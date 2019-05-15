/**
 * Filename:   YourMealPlanAnalysis.java
 * Project:    FoodQuery
 * Authors:    Kevin Luangpoomyut, Sheung Chan, Jiahui Zhou, Matthew Kesler,
 *             Michael Thompson
 *
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * A class that shows an analysis of the current FoodItems in the mealPlan
 */
public class YourMealPlanAnalysis {

    /**
     * to display the analysis window
     *
     * @param title the title of the window
     * @param message the string of the message to deliver to the end user
     * @param mealPlanList the list of the mealPlanList
     */
    public static void display(String title, String message, ObservableList<FoodItem> mealPlanList){

        // variables to store the total number of nutrients
        double calories = 0;
        double fat = 0;
        double carbohydrate = 0;
        double fiber = 0;
        double protein = 0;
        Stage secondaryStage = new Stage();

        // block input events with other windows until this one is taken care
        secondaryStage.initModality(Modality.APPLICATION_MODAL);
        secondaryStage.setTitle(title);
        secondaryStage.setMinWidth(1000);
        secondaryStage.setMinHeight(900);

        // the Label to display message to let the users know there is no meal within the meal list
        Label label = new Label();
        label.setText(message);
        Button closeButton = new Button("Close the window.");
        closeButton.setOnAction(e -> secondaryStage.close());

        // the root
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        // if the meal_list that users create is not empty
        if (mealPlanList.size() != 0){

            // calculate the total number for nutrients
            for (FoodItem food: mealPlanList) {
                calories += food.getNutrientValue("calories");
                fat += food.getNutrientValue("fat");
                carbohydrate += food.getNutrientValue("carbohydrate");
                fiber += food.getNutrientValue("fiber");
                protein += food.getNutrientValue("protein");
            }

            // to create the pie chart
            ObservableList<PieChart.Data> pieChartData =
                    FXCollections.observableArrayList(
                            new PieChart.Data("fat", fat),
                            new PieChart.Data("fiber", fiber),
                            new PieChart.Data("protein", protein),
                            new PieChart.Data("carbohydrate", carbohydrate));

            final PieChart chart = new PieChart(pieChartData);

            // the label for the chart
            chart.setLegendSide(Side.TOP);

            // set title for the chart
            Label chart_title = new Label();
            chart_title.setFont(new Font(30));
            chart_title.setText("Nutrients");

            // total nutrients HBox
            HBox total_nutrients = new HBox(20);
            total_nutrients.setAlignment(Pos.CENTER);

            // the labels to show each nutrient's total value
            Label total_calories = new Label();
            Label total_fat = new Label();
            Label total_carbohydrate = new Label();
            Label total_fiber = new Label();
            Label total_protein = new Label();


            // to set the labels
            total_calories.setFont(new Font(16));
            total_calories.setText("Total Calories:  " + calories + " kcal");
            total_nutrients.getChildren().add(total_calories);

            total_fat.setFont(new Font(16));
            total_fat.setText("Total Fat: " + fat + "g");
            total_nutrients.getChildren().add(total_fat);

            total_fiber.setFont(new Font(16));
            total_fiber.setText("Total Fiber: " + fiber + "g");
            total_nutrients.getChildren().add(total_fiber);

            total_protein.setFont(new Font(16));
            total_protein.setText("Total Protein: " + protein + "g");
            total_nutrients.getChildren().add(total_protein);

            total_carbohydrate.setFont(new Font(16));
            total_carbohydrate.setText("Total Carbohydrate: " + carbohydrate  + "g");
            total_nutrients.getChildren().add(total_carbohydrate);

            // adding all the parts into the root
            layout.getChildren().add(chart_title);
            layout.getChildren().add(chart);
            layout.getChildren().add(total_nutrients);
            layout.getChildren().addAll(closeButton);
        }
        else{
            // if the meal plan is empty
            layout.getChildren().addAll(label, closeButton);
        }


        // to show it on the screen
        Scene scene = new Scene(layout);
        secondaryStage.setScene(scene);
        secondaryStage.showAndWait();

    }
}
