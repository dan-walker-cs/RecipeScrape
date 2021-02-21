package main.java.com.example.RecipeScrape.Controller;


import com.example.RecipeScrape.Model.Recipe;
import main.java.com.example.RecipeScrape.Service.RecipeScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.Serializable;

// Class to handle exposing REST endpoints for the application
@RestController
public class RESTEndpoints {

    // Service singleton
    @Autowired
    RecipeScraperService recipeScraperService;

    // Default json response
    @RequestMapping(value="/", method=RequestMethod.GET, produces="application/json")
    public String defaultPage() {
        return "Nice to see you! If you would like to find a recipe, " +
                "simply enter your search query into the url bar. " +
                "We'll get those results to you right away!";
    }

    // json response for invalid searches, or searches that yield no results
    @RequestMapping(value="/error", method=RequestMethod.GET, produces="application/json")
    public String errorPage() {
        return "Why, hello there. " +
                "You seem to have entered an invalid search query.. " +
                "Please try again by typing your query into the url bar.";
    }

    // json response for valid searches, displays all recipe information
    @RequestMapping(value="/{recipeTitle}", method=RequestMethod.GET, produces="application/json")
    public Serializable displayRecipeInformation(@PathVariable("recipeTitle") String recipeTitle) throws IOException {
        Recipe targetRecipe = recipeScraperService.searchRecipeByTitle(recipeTitle);
        if(targetRecipe == null) {
            return errorPage();
        }
        return recipeScraperService.searchRecipeByTitle(recipeTitle);
    }
}
