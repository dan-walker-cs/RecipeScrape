package com.example.RecipeScrape.Service;

import com.example.RecipeScrape.Model.Recipe;
import com.example.RecipeScrape.Service.RecipeScraperService;
import com.example.RecipeScrape.Util.RecipeScraper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;


// Implementation class for the Spring Service
@Service
public class RecipeScraperServiceImpl implements RecipeScraperService {

    private String baseURL = "https://www.allrecipes.com/";
    private Integer parseTimeoutMillis = 10000;
    private Recipe recipe;;

    public RecipeScraperServiceImpl() {

    }

    // Scrapes all relevant recipe information from a page on allrecipes.com
    @Override
    public void loadContents(String url) throws IOException {
        Document currentRecipePage = Jsoup.connect(url).timeout(parseTimeoutMillis).get();
        RecipeScraper scraper = new RecipeScraper(currentRecipePage);
        scraper.buildRecipe();
        recipe = scraper.getRecipe();
    }

    // exposes recipe object
    @Override
    public Recipe listRecipeInformation() {
        return recipe;
    }

    // searches allrecipes.com for recipeTitle, selects the first recipe from the results, and passes back its url
    @Override
    public Recipe searchRecipeByTitle(String recipeTitle) throws IOException {
        // allrecipes.com home page manipulation
        Document basePage = Jsoup.connect(baseURL).timeout(parseTimeoutMillis).get();
        RecipeScraper baseScraper = new RecipeScraper(basePage);
        String searchResultsURL = baseScraper.search(recipeTitle);

        // allrecipes.com search results page manipulation
        Document resultsPage = Jsoup.connect(searchResultsURL).timeout(parseTimeoutMillis).get();
        RecipeScraper searchScraper = new RecipeScraper(resultsPage);
        String targetRecipeURL = searchScraper.fetchResult();

        if(targetRecipeURL == null) {
            return null;
        }

        // scrapes the recipe information desired
        loadContents(targetRecipeURL);

        return recipe;
    }
}
