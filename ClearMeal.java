/**
 * Filename:   ClearMeal.java
 * Project:    FoodQuery
 * Authors:    Kevin Luangpoomyut, Sheung Chan, Jiahui Zhou, Matthew Kesler,
 *             Michael Thompson
 *
 */

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;

import java.util.Optional;

/**
 * A class for displaying a Warning message when a user wants to clear their meal plan
 */
public class ClearMeal {
    /**
     * Displays the warning message for clearing the mealPlan
     * @param mealPlanList the list of the FoodItems in the mealPlan
     * @param foodTable the table of the FoodItems for the mealPlan
     */
    public static void display(ObservableList<FoodItem> mealPlanList, TableView<FoodItem> foodTable) {
        // Create an alert warning box
        Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure you want to clear your meal plan?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Warning");
        alert.setHeaderText("Clear Meal Plan?");
        // Get the result between choosing yes and no
        Optional<ButtonType> result = alert.showAndWait();
        // if the result is no do nothing
        if (result.get() == ButtonType.NO)
            return;
        else {
            // if the result is yes clear the mealPlanList
            mealPlanList.clear();
            // also update the foodTable to nothing
            foodTable.setItems(null);
        }
    }
}
