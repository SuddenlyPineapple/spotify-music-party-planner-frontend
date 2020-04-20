<template>
  <div v-if="eventId">
    <v-autocomplete
      v-model="selectedGenres"
      :items="genresList"
      placeholder="Click to add genres recomendations list..."
      chips
      multiple
      label="Recomendations genres list"
      @input="updateGenre"
    >
    </v-autocomplete>
    <v-snackbar
      v-model="showError"
      bottom
      right
      color="red"
      multi-line
      :timeout="60000"
    >
      {{ errorMessage }}
      <v-btn dark text @click="showError = false" fab>
        <v-icon>mdi-close</v-icon>
      </v-btn>
    </v-snackbar>
  </div>
</template>

<script>
import axios from "axios";
import _ from "lodash";
import { apiUrl } from "../config/backend";

export default {
  name: "GenresManager",
  props: ["genres", "eventId"],
  data: () => ({
    genresList: [],
    selectedGenres: [],
    genresPristine: [],
    showError: false,
    errorMessage: ""
  }),
  methods: {
    setError(message, error) {
      this.showError = true;
      this.errorMessage = message + " (" + error + ")";
    },
    getGenres() {
      axios
        .get(apiUrl + "explore/genres")
        .then(response => {
          if (response.status === 200) this.genresList = response.data.genres;
          else throw "Request Failed";
        })
        .catch(err => {
          this.setError("Cannot fetch genres list. Please refresh page!", err);
        });
    },
    async updateGenre(value) {
      const diffAdded = _.difference(value, this.genresPristine);
      const diffDeleted = _.difference(this.genresPristine, value);
      const endpoint = apiUrl + "events/" + this.eventId + "/suggestions";
      const errorGenreChangeMessage =
        "Cannot add or remove genre from list. There is problem with our API. Try refresh or contact administrator!";

      if (diffAdded.length)
        await axios
          .put(endpoint, {
            genres: diffAdded
          })
          .then(response => {
            this.genresPristine = this.getSelectedGenres(
              response.data.playlist.suggestions.fromGuests.genres
            );
          })
          .catch(err => {
            this.setError(errorGenreChangeMessage, err);
          });

      if (diffDeleted.length)
        await axios
          .delete(endpoint, {
            data: {
              genres: diffDeleted
            }
          })
          .then(response => {
            this.genresPristine = this.getSelectedGenres(
              response.data.playlist.suggestions.fromGuests.genres
            );
          })
          .catch(err => {
            this.setError(errorGenreChangeMessage, err);
          });
    },
    getSelectedGenres(genres) {
      return genres.reduce((filtered, genre) => {
        if (genre.n > 0) filtered.push(genre.element);
        return filtered;
      }, []);
    }
  },
  mounted() {
    this.getGenres();
    this.genresPristine = this.getSelectedGenres(this.genres);
    this.selectedGenres = [...this.genresPristine];
  }
};
</script>
