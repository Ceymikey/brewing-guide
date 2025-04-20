package dev.ceymikey.interfaces;

/**
 * Interface to expose search field state from the brewing stand screen
 */
public interface ISearchFieldProvider {
    /**
     * Checks if the search field is currently focused
     * @return true if the search field is focused
     */
    boolean isSearchFieldFocused();
}
