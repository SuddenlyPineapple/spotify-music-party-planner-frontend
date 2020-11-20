package pl.poznan.put.cs.project.spotifypartyplanner.model.event;

public class Suggestions {
    private SuggestionsFrom fromGuests = new SuggestionsFrom();
    private SuggestionsFrom fromRecommendations = new SuggestionsFrom();

    public SuggestionsFrom getFromGuests() {
        return fromGuests;
    }

    public void setFromGuests(SuggestionsFrom fromGuests) {
        this.fromGuests = fromGuests;
    }

    public SuggestionsFrom getFromRecommendations() {
        return fromRecommendations;
    }

    public void setFromRecommendations(SuggestionsFrom fromRecommendations) {
        this.fromRecommendations = fromRecommendations;
    }
}
