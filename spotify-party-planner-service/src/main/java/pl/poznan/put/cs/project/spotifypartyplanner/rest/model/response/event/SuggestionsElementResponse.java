package pl.poznan.put.cs.project.spotifypartyplanner.rest.model.response.event;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SuggestionsElementResponse<T> {
    public T element;
    public int n;

    private SuggestionsElementResponse(T element, int n) {
        this.element = element;
        this.n = n;
    }

    public static <E> List<SuggestionsElementResponse<E>> fromMap(
            Map<String, Integer> data, Function<String, E> makeElement
    ) {
        return data.entrySet()
                .stream()
                .map(e -> new SuggestionsElementResponse<>(makeElement.apply(e.getKey()), e.getValue()))
                .collect(Collectors.toList());
    }

}
