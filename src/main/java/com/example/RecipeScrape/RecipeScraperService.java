package com.example.RecipeScrape;

import java.io.IOException;

// Interface to be implemented by the Spring Service
public interface RecipeScraperService {
    public void loadContents(String url) throws IOException;
    public Recipe listRecipeInformation();
    public Recipe searchRecipeByTitle(String recipeTitle) throws IOException;
}
