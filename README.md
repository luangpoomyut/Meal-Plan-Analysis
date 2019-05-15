# Meal-Plan-Analysis

This application serves to tell users how many calories and macronutrients are in their current or planned diet based on food items that they select.

Authors: Kevin Luangpoomyut, Sheung Chan, Jiahui Zhou, Matthew Kesler, Michael Thompson

## Design

*Front End*\
The graphical user interface (GUI) is fully designed with JavaFX.

*Back End*\
We perform macronutrient and calorie retrievals for a user-created food list via indexing, similar to that of database management systems.

Our indexing is implemented through a B+ Tree, which accounted for the heaviest portion of our coding design and allows us to retrieve records for range searches efficiently, which is crucial for range filtering (e.g., calories >= 100 and protein > 20).

## Considerations

*Food Data*\
We designed our program to take an input file that will populate the bank of food items and their nutrient content. The program accepts .csv or .txt files.

The format of entries in the loaded file is as follows: 

id, name, "calories" label, calories value, "fat" label, fat value, "carbohydrate" label, carbohydrate value, "fiber" label, fiber value, "protein" label, protein value

Example:\
556540ff5d613c9d5f5935a9,Stewarts_PremiumDarkChocolatewithMintCookieCrunch,calories,280,fat,18,carbohydrate,34,fiber,3,protein,3

*Storage*\
Considering is onloaded by the user, we keep this data in memory for the duration of the program (non-persistent). We have provided a .txt file with a preset list of food items to use.

## How It Works

You can run this program if you download the entirety of this folder to your local computer and run the Main class. You should see a blank slate of the GUI.

To populate the program with data, click on File->Open and designate which file you would like to load your data from.

You can additionally add more food items to your list manually by clicking on the "Create Food" button.

Once data has been loaded or manually added, you can select food items by clicking on them in the list and they should become highlighted. You can then click on the "Add to Meal" button to add it to your meal plan.

You can delete your meal plan all at once by clicking on the "Clear Meal" button.

To speed up selecting food items you may already know of or if you are looking for items of interest, there are two ways to search for food items: name searching or filtering by macronutrients.

There is a text field that you can search by name and you can also set up search criterion based on macronutrients using the "Filter" button.

Once you are done selecting the food items you would like for your meal plan, you can click on the "Analyze Meal" button to see more detailed information about the meal plan you designed.

You can save out the bank of food items in your program externally onto your local drive if you click on File->Save. The file should be saved locally in the folder where the program files are.

**Enjoy planning your diet to meet your goals!**
