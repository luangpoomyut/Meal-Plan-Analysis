/**
 * Filename:   FoodData.java
 * Project:    FoodQuery
 * Authors:    Kevin Luangpoomyut, Sheung Chan, Jeff Chou, Matthew Kesler,
 *             Michael Thompson
 *
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * This class represents the backend for managing all
 * the operations associated with FoodItems
 *
 * @author sapan (sapan@cs.wisc.edu)
 */
public class FoodData implements FoodDataADT<FoodItem> {

    // The following constants represent indices in the array that is returned
    // from calling the split() function

    // Index in array corresponding to ID
    private static final int ID_INDEX = 0;
    // Index in array corresponding to name
    private static final int NAME_INDEX = 1;
    // Index in array corresponding to first nutrient type listed in file
    private static final int NUTR1_LBL_INDEX = 2;
    // Index in array corresponding to value of first nutrient type
    private static final int NUTR1_VAL_INDEX = 3;
    // Index in array corresponding to second nutrient type listed in file
    private static final int NUTR2_LBL_INDEX = 4;
    // Index in array corresponding to value of second nutrient type
    private static final int NUTR2_VAL_INDEX = 5;
    // Index in array corresponding to third nutrient type listed in file
    private static final int NUTR3_LBL_INDEX = 6;
    // Index in array corresponding to value of third nutrient type
    private static final int NUTR3_VAL_INDEX = 7;
    // Index in array corresponding to fourth nutrient type listed in file
    private static final int NUTR4_LBL_INDEX = 8;
    // Index in array corresponding to value of fourth nutrient type
    private static final int NUTR4_VAL_INDEX = 9;
    // Index in array corresponding to fifth nutrient type listed in file
    private static final int NUTR5_LBL_INDEX = 10;
    // Index in array corresponding to value of fifth nutrient type
    private static final int NUTR5_VAL_INDEX = 11;
    // Branching factor of any instance of B+ tree instantiated
    private static final int BRANCHING_FACTOR = 3;

    // List of all the food items
    private List<FoodItem> foodItemList;

    // Map of nutrients and their corresponding index
    private HashMap<String, BPTree<Double, FoodItem>> indexes;

    /**
     * Public constructor
     */
    public FoodData() {
        foodItemList = new ArrayList<FoodItem>();
        indexes = new HashMap<String, BPTree<Double, FoodItem>>();
        indexes.put("calories", new BPTree<Double, FoodItem>(BRANCHING_FACTOR));
        indexes.put("fat", new BPTree<Double, FoodItem>(BRANCHING_FACTOR));
        indexes.put("carbohydrate", new BPTree<Double, FoodItem>(BRANCHING_FACTOR));
        indexes.put("fiber", new BPTree<Double, FoodItem>(BRANCHING_FACTOR));
        indexes.put("protein", new BPTree<Double, FoodItem>(BRANCHING_FACTOR));
    }

    /**
     * Defines ordering for FoodItem objects.
     *
     * @author kevinluangpoomyut
     */
    /*class FoodItemComparator implements Comparator<FoodItem> {

        *//**
         * Two food items are compared by their name values.
         *
         * @param f1 an instance of FoodItem
         * @param f2 an instance of FoodItem
         * @return 0 if the names are equal (case insensitive), a negative value
         * if the first argument comes before the second argument in
         * alphabetical order, a positive value if the first argument comes
         * after the second argument in alphabetical order.
         *//*
        @Override
        public int compare(FoodItem f1, FoodItem f2) {
            return f1.getName().toUpperCase().compareTo(f2.getName().toUpperCase());
        }

    }*/

    /**
     * Loads data from a .csv or .txt file.
     *
     * @param filePath path of the food item data file
     */
    @Override
    public void loadFoodItems(String filePath) {

        try {

            // Input stream
            BufferedReader input = new BufferedReader(new FileReader(filePath));

            // Clear current list (after file load, because we don't want to clear
            // out the list if the file is invalid or unable to be loaded)
            foodItemList = null;

            // Explicitly alters the reference to a new list
            foodItemList = new ArrayList<FoodItem>();

            // Holds current line being parsed
            String currLine;

            // Is the resulting String array after fields have been separated out
            String[] lineSplit;

            // Format of line is acceptable or not
            boolean formatMatch = true;

            try {

                // While there is a line to read form the input file
                while ((currLine = input.readLine()) != null) {

                    // Returns an array of with each comma separated term in its own
                    // element of the array
                    lineSplit = currLine.split(",");
                    // Set or reset boolean to true, if format is not matched, boolean
                    // is set to false
                    formatMatch = true;

                    // Only parse line if the split function returned a non-zero
                    // length array
                    if (lineSplit != null && lineSplit.length > 0) {

                        // Code in try block is to check if the nutrient values parsed
                        // are valid ones: can be represented as a double, and is a
                        // non-negative value
                        try {
                            Double.parseDouble(lineSplit[NUTR1_VAL_INDEX].trim());
                            Double.parseDouble(lineSplit[NUTR2_VAL_INDEX].trim());
                            Double.parseDouble(lineSplit[NUTR3_VAL_INDEX].trim());
                            Double.parseDouble(lineSplit[NUTR4_VAL_INDEX].trim());
                            Double.parseDouble(lineSplit[NUTR5_VAL_INDEX].trim());
                            if (   !(Double.parseDouble(lineSplit[NUTR1_VAL_INDEX].trim()) >= 0
                                    && Double.parseDouble(lineSplit[NUTR2_VAL_INDEX].trim()) >= 0
                                    && Double.parseDouble(lineSplit[NUTR3_VAL_INDEX].trim()) >= 0
                                    && Double.parseDouble(lineSplit[NUTR4_VAL_INDEX].trim()) >= 0
                                    && Double.parseDouble(lineSplit[NUTR5_VAL_INDEX].trim()) >= 0)   ) {
                                throw new Exception();
                            }
                        }
                        // Boolean set to false because at least one nutrient type's
                        // value is not valid
                        catch (Exception e) {
                            formatMatch = false;
                        }

                        // If format of line parsed is valid, we proceed to create the
                        // FoodItem with its information
                        if (formatMatch) {
                            // Instance of FoodItem to be added to foodItemList
                            FoodItem currItem = new FoodItem(lineSplit[ID_INDEX].trim(), lineSplit[NAME_INDEX].trim());
                            currItem.addNutrient(lineSplit[NUTR1_LBL_INDEX].trim().toLowerCase(), Double.parseDouble(lineSplit[NUTR1_VAL_INDEX].trim()));
                            currItem.addNutrient(lineSplit[NUTR2_LBL_INDEX].trim().toLowerCase(), Double.parseDouble(lineSplit[NUTR2_VAL_INDEX].trim()));
                            currItem.addNutrient(lineSplit[NUTR3_LBL_INDEX].trim().toLowerCase(), Double.parseDouble(lineSplit[NUTR3_VAL_INDEX].trim()));
                            currItem.addNutrient(lineSplit[NUTR4_LBL_INDEX].trim().toLowerCase(), Double.parseDouble(lineSplit[NUTR4_VAL_INDEX].trim()));
                            currItem.addNutrient(lineSplit[NUTR5_LBL_INDEX].trim().toLowerCase(), Double.parseDouble(lineSplit[NUTR5_VAL_INDEX].trim()));
                            // FoodItem is added to list
                            foodItemList.add(currItem);
                            // Add the FoodItem with its nutrients to its respective B+ trees
                            indexes.get(lineSplit[NUTR1_LBL_INDEX].trim().toLowerCase()).insert(Double.parseDouble(lineSplit[NUTR1_VAL_INDEX].trim()), currItem);
                            indexes.get(lineSplit[NUTR2_LBL_INDEX].trim().toLowerCase()).insert(Double.parseDouble(lineSplit[NUTR2_VAL_INDEX].trim()), currItem);
                            indexes.get(lineSplit[NUTR3_LBL_INDEX].trim().toLowerCase()).insert(Double.parseDouble(lineSplit[NUTR3_VAL_INDEX].trim()), currItem);
                            indexes.get(lineSplit[NUTR4_LBL_INDEX].trim().toLowerCase()).insert(Double.parseDouble(lineSplit[NUTR4_VAL_INDEX].trim()), currItem);
                            indexes.get(lineSplit[NUTR5_LBL_INDEX].trim().toLowerCase()).insert(Double.parseDouble(lineSplit[NUTR5_VAL_INDEX].trim()), currItem);
                            // Reset value
                            currItem = null;
                        }

                    }

                    // Reset value
                    lineSplit = null;

                }

                // Sort list alphabetically
                sortFoodList(foodItemList);

            }
            catch (IOException e) {
                e.getClass();
                e.getMessage();
                e.printStackTrace();
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("The file: '" + filePath + "' does not exist.");
        }

    }


    /**
     * Gets all the food items that have name containing the substring.
     *
     * @param substring substring to be searched
     * @return list of filtered food items; if no food item matched, return
     * empty list
     */
    @Override
    public List<FoodItem> filterByName(String substring) {

        // List that will hold any instance of FoodItem whose name contains the
        // substring
        List<FoodItem> nameFiltered = new ArrayList<FoodItem>();

        // For each item in foodItemList
        for (FoodItem item : foodItemList) {
            // Add item to nameFiltered if there is a match
            if (item.getName().toLowerCase().contains(substring.toLowerCase())) {
                nameFiltered.add(item);
            }
        }

        return nameFiltered;

    }

    /**
     * Gets all the food items that fulfill ALL the provided rules
     *
     * Format of a rule:
     *     "<nutrient> <comparator> <value>"
     *
     * Definition of a rule:
     *     A rule is a string which has three parts separated by a space:
     *         1. <nutrient>: Name of one of the 5 nutrients [CASE-INSENSITIVE]
     *         2. <comparator>: One of the following comparison operators: <=, >=, ==
     *         3. <value>: a double value
     *
     * Note:
     *     1. Multiple rules can contain the same nutrient.
     *         E.g. ["calories >= 50.0", "calories <= 200.0", "fiber == 2.5"]
     *     2. A FoodItemADT object MUST satisfy ALL the provided rules
     *        to be returned in the filtered list.
     *
     * @param rules list of rules
     * @return list of filtered food items; if no food item matched, return
     * empty list
     */
    @Override
    public List<FoodItem> filterByNutrients(List<String> rules) {

        // Holds the food items that fulfill all the applied rules
        List<FoodItem> nutrFiltered = new ArrayList<FoodItem>();

        // An array that holds the individual components of a rule
        String[] ruleSplit = new String[3];

        // A list of food item lists that satisfy each rule
        ArrayList<List<FoodItem>> filterResults = new ArrayList<List<FoodItem>>();

        String nutrient;      // represents nutrient component of a rule
        String comparator;    // represents comparator component of a rule
        Double value;         // represents value component of a rule

        // For each rule listed
        for (int i = 0; i < rules.size(); i++) {
            // Populate ruleSplit with the rule split by a whitespace
            ruleSplit = rules.get(i).split(" ");
            // Initialize variables associated with components of rules
            nutrient = ruleSplit[0];
            comparator = ruleSplit[1];
            value = Double.parseDouble(ruleSplit[2]);
            filterResults.add(indexes.get(nutrient).rangeSearch(value, comparator));
            nutrient = null;
            comparator = null;
            value = 0.0;
        }

        // Set nutrFiltered to list of filtered items from first rule applied
        nutrFiltered = filterResults.get(0);

        // For each list produced from the rules applied, we apply the intersect
        // between nutrFiltered and the next list until all the lists have been
        // compared
        for (int i = 0; i < filterResults.size(); i++) {
            if (i == filterResults.size() - 1) {
                break;
            }
            nutrFiltered.retainAll(filterResults.get(i + 1));
        }

        // Sort the list with all the rules applied
        sortFoodList(nutrFiltered);

        return nutrFiltered;

    }


    /**
     * Adds a food item to the loaded data.
     *
     * @param foodItem the food item instance to be added
     */
    @Override
    public void addFoodItem(FoodItem foodItem) {

        // Add foodItem passed as an argument into the food list
        foodItemList.add(foodItem);

        // Add nutrient values to its respective B+ tree
        indexes.get("calories").insert(foodItem.getNutrientValue("calories"), foodItem);
        indexes.get("fat").insert(foodItem.getNutrientValue("fat"), foodItem);
        indexes.get("carbohydrate").insert(foodItem.getNutrientValue("carbohydrate"), foodItem);
        indexes.get("fiber").insert(foodItem.getNutrientValue("fiber"), foodItem);
        indexes.get("protein").insert(foodItem.getNutrientValue("protein"), foodItem);

        // Sort list since update to the list has been made
        sortFoodList(foodItemList);

    }

    /**
     * Gets the list of all food items.
     *
     * @return list of FoodItem
     */
    @Override
    public List<FoodItem> getAllFoodItems() {

        return foodItemList;

    }

    /**
     * Save the list of food items in ascending order by name.
     *
     * @param filename name of the file where the data needs to be saved
     */
    @Override
    public void saveFoodItems(String filename) {

        try {

            // Instance of PrintWriter
            PrintWriter pwrite = new PrintWriter(new File(filename));
            // Instance of StringBuilder
            StringBuilder sbuild = new StringBuilder();

            // Assembles the line for each food item to be outputted to the file
            for (FoodItem foodItem : foodItemList) {
                sbuild.append(foodItem.getID());
                sbuild.append(",");
                sbuild.append(foodItem.getName());
                sbuild.append(",");
                sbuild.append("calories,");
                sbuild.append(foodItem.getNutrientValue("calories"));
                sbuild.append(",");
                sbuild.append("fat,");
                sbuild.append(foodItem.getNutrientValue("fat"));
                sbuild.append(",");
                sbuild.append("carbohydrate,");
                sbuild.append(foodItem.getNutrientValue("carbohydrate"));
                sbuild.append(",");
                sbuild.append("fiber,");
                sbuild.append(foodItem.getNutrientValue("fiber"));
                sbuild.append(",");
                sbuild.append("protein,");
                sbuild.append(foodItem.getNutrientValue("protein"));
                sbuild.append("\n");
            }

            pwrite.write(sbuild.toString());
            // Close resources
            pwrite.close();

        } catch (FileNotFoundException e) {
            System.out.println("ERROR: FileNotFoundException was thrown trying to "
                    + "save food items to a file.");
        }

    }

    /**
     * Sorts a list of food item in alphabetical order according to its name.
     * Sorting is case insensitive.
     *
     * @param listFood list of food items
     */
    private void sortFoodList(List<FoodItem> listFood) {

        // Instance of foodItemComparator
        //Comparator<FoodItem> foodItemComparator = new FoodItemComparator();

        /*final Comparator<FoodItem> FOOD_ITEM_COMPARATOR = new Comparator<FoodItem>() {
          @Override
          public int compare(FoodItem f1, FoodItem f2) {
            return f1.getName().toUpperCase().compareTo(f2.getName().toUpperCase());
          }
        };
      
        // Sort list passed as an argument
        Collections.sort(listFood, FOOD_ITEM_COMPARATOR);*/
      
      Collections.sort(listFood, (f1, f2) -> f1.getName().toUpperCase().compareTo(f2.getName().toUpperCase()));

    }
    
    public static void main(String[] args) {
      FoodData t = new FoodData();
      t.loadFoodItems("foodItems.txt");
      for (FoodItem f : t.getAllFoodItems()) {
        System.out.println(f.getName());
      }
    }
    
}
