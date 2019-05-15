/**
 * Filename:   NewFoodEntry.java
 * Project:    FoodQuery
 * Authors:    Kevin Luangpoomyut, Sheung Chan, Jiahui Zhou, Matthew Kesler,
 *             Michael Thompson
 *
 */

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * A class for holding the value from the textField and holding the description for value
 */
public class NewFoodEntry {
    private Label description;
    private TextField textField;
    private String value;

    /**
     * Constructor that creates an object with a description and TextField
     * @param description the description of the value to type in
     * @param prompt the string of the prompt of the TextField
     */
    public NewFoodEntry(String description, String prompt) {
        // the description of the value to enter
        this.description = new Label(description);

        // the TextField to enters values into
        this.textField = new TextField();
        textField.setPromptText(prompt);
    }

    /**
     * Setter for the string value
     * @param getValue the textField that holds the value
     */
    public void setValue(TextField getValue) {
        value = textField.getText();
    }

    /**
     * getter for the string value
     * @return the string value in the TextField
     */
    public String getValue() {
        return value;
    }

    /**
     * Getter for the description
     * @return the description
     */
    public Label getDescription() {
        return description;
    }


    /**
     * getter for the actual TextField object
     * @return the textFieldObject that holds
     */
    public TextField getTextField() {
        return textField;
    }
}
