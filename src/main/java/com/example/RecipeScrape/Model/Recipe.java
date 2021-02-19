package com.example.RecipeScrape.Model;


import java.io.Serializable;
import java.util.ArrayList;

// DTO class to hold all of the scraped information for a recipe
// Pretty much just setters and getters
public class Recipe implements Serializable {
    private static final long serialVersionUID = 1L;

    private String title;
    private String prepTime;
    private String cookTime;
    private String servingCount;
    private String caloriesPerServing;
    private ArrayList<String> ingredientList;
    private ArrayList<String> instructionList;
    private String dishImageURL;

    public Recipe() {
        ingredientList = new ArrayList<>();
        instructionList = new ArrayList<>();
    }

    public Recipe(String title, String prepTime, String cookTime, String servingCount,
                  String caloriesPerServing, ArrayList<String> ingredientList,
                  ArrayList<String> instructionList, String dishImageURL) {
        super();
        this.title = title;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.servingCount = servingCount;
        this.caloriesPerServing = caloriesPerServing;
        this.ingredientList = ingredientList;
        this.instructionList = instructionList;
        this.dishImageURL = dishImageURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(String prepTime) {
        this.prepTime = prepTime;
    }

    public String getCookTime() {
        return cookTime;
    }

    public void setCookTime(String cookTime) {
        this.cookTime = cookTime;
    }

    public String getServingCount() {
        return servingCount;
    }

    public void setServingCount(String servingCount) {
        this.servingCount = servingCount;
    }

    public String getCaloriesPerServing() {
        return caloriesPerServing;
    }

    public void setCaloriesPerServing(String caloriesPerServing) {
        this.caloriesPerServing = caloriesPerServing;
    }

    public ArrayList<String> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(ArrayList<String> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public ArrayList<String> getInstructionList() {
        return instructionList;
    }

    public void setInstructionList(ArrayList<String> instructionList) {
        this.instructionList = instructionList;
    }

    public String getDishImageURL() {
        return dishImageURL;
    }

    public void setDishImageURL(String dishImageURL) {
        this.dishImageURL = dishImageURL;
    }
}
