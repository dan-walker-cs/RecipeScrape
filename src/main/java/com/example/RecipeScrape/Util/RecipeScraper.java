package com.example.RecipeScrape.Util;

import com.example.RecipeScrape.Model.Recipe;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// This class parses an html document for specific recipe information, specially for the allrecipes.com domain
public class RecipeScraper {

    Recipe recipe = new Recipe();

    private Document currentPage;

    private ArrayList<String> recipeMetaDataList;
    private ArrayList<String> ingredientList;
    private ArrayList<String> instructionList;

    RecipeScraper() {

    }

    public RecipeScraper(Document currentPage) {
        this.currentPage = currentPage;
        ingredientList = new ArrayList<>();
        instructionList = new ArrayList<>();
    }

    // returns the url of the results of the search query
    public String search(String targetTitle) throws IOException {
        String targetURL;

        FormElement searchElement = (FormElement) currentPage.select("form.primary-nav-search-form").get(0);
        searchElement.select("input[name=wt]").val(targetTitle);
        Document resultsPage = searchElement.submit().get();
        targetURL = resultsPage.location();

        return targetURL;
    }

    // returns the url of the first recipe matching the search query
    public String fetchResult() {
        String recipeURL;

        Element searchResultLink = currentPage.getElementsByClass("fixed-recipe-card__title-link").first();

        if(searchResultLink == null) {
            return null;
        }

        recipeURL = searchResultLink.attr("href");

        return recipeURL;
    }

    // sets all the fields for a recipe object
    public void buildRecipe() {
        scrapeTitle();
        scrapeRecipeMetaData();
        scrapePrepTime();
        scrapeCookTime();
        scrapeServingCount();
        scrapeCaloriesPerServing();
        scrapeIngredientList();
        scrapeInstructionList();
        scrapeDishImageURL();
    }

    // parses the html document for the name of the recipe
    private void scrapeTitle() {
        Element title = currentPage.getElementsByClass("headline heading-content").get(0);
        if(title == null) {
            recipe.setTitle("Untitled Recipe");
        }
        recipe.setTitle(title.text());
    }

    // collects recipe metadata to be parsed for specific fields
    private void scrapeRecipeMetaData() {
        recipeMetaDataList = new ArrayList<>();
        Elements recipeMetaData = currentPage.getElementsByClass("recipe-meta-item");
        for(Element e : recipeMetaData) {
            recipeMetaDataList.add(e.text());
        }
    }

    // parses the metadata for prep time
    private void scrapePrepTime() {
        for(String str : recipeMetaDataList) {
            if(str.contains("prep")) {
                recipe.setPrepTime(str.substring(6));
                recipeMetaDataList.remove(str);
                break;
            }
        }
    }

    // parses the metadata for cook time
    private void scrapeCookTime() {
        for(String str : recipeMetaDataList) {
            if(str.contains("cook")) {
                recipe.setCookTime(str.substring(6));
                recipeMetaDataList.remove(str);
                break;
            }
        }
    }

    // parses the metadata for number of servings recipe makes
    private void scrapeServingCount() {
        for(String str : recipeMetaDataList) {
            if(str.contains("Servings")) {
                recipe.setServingCount(str.substring(10));
                recipeMetaDataList.remove(str);
                break;
            }
        }
    }

    // parses the html document for the recipe's nutritional information, then extracts the calorie information
    private void scrapeCaloriesPerServing() {
        Elements nutritionContainer = currentPage.getElementsByClass("nutrition-section");
        String nutritionInfo = nutritionContainer.get(0).text();

        // regex to match calorie count
        String calorieRegex = "(\\d+ calories)";
        Pattern pattern = Pattern.compile(calorieRegex);
        Matcher matcher = pattern.matcher(nutritionInfo);

        if(matcher.find()) {
            recipe.setCaloriesPerServing(matcher.group(1));
        } else {
            recipe.setCaloriesPerServing("unknown");
        }
    }

    // parses the html document for the list of recipe ingredients
    private void scrapeIngredientList() {
        Elements ingredients = currentPage.getElementsByClass("ingredients-item-name");

        for(Element e : ingredients) {
            ingredientList.add(e.text());
        }
        recipe.setIngredientList(ingredientList);
    }

    // parses the html document for the list of recipe instructions
    private void scrapeInstructionList() {
        Elements instructions = currentPage.getElementsByClass("paragraph");

        for(Element e : instructions) {
            instructionList.add(e.text());
        }
        recipe.setInstructionList(instructionList);
    }

    // returns the url for recipe's video tutorial if available, and finished product image if not
    private void scrapeDishImageURL() {
        // div containing the image information
        Element imageContainer = currentPage.selectFirst("div.inner-container.js-inner-container.image-overlay");

        // heading down the document hierarchy to where the image/video links are found
        imageContainer = imageContainer.child(0);
        imageContainer = imageContainer.child(0);

        String dishImageURL;
        // at this level, the container's child will either be a link if it's an image, or a label if it's a video
        if(imageContainer.hasAttr("href")) {
            dishImageURL = imageContainer.attr("href");
        } else {
            dishImageURL = imageContainer.child(0).attr("href");
        }

        recipe.setDishImageURL(dishImageURL);
    }

    // exposes recipe
    public Recipe getRecipe() {
        return recipe;
    }
}