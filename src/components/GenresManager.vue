<template>
  <div v-if="eventId">
    <v-autocomplete
      v-model="selectedGenres"
      :items="genresList"
      placeholder="Genres recomendations..."
      chips
      multiple
      label="Recomendations genres list"
      @input="updateGenre"
    >
    </v-autocomplete>
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
    genresPristine: []
  }),
  methods: {
    getGenres() {
      axios
        .get(apiUrl + "explore/genres")
        .then(response => {
          if (response.status === 200) this.genresList = response.data.genres;
          else throw "Request Failed";
        })
        .catch(err => {
          console.log(err);
        });
    },
    updateGenre(value) {
      const diffAdded = _.difference(value, this.genresPristine);
      const diffDeleted = _.difference(this.genresPristine, value);
      const endpoint = apiUrl + "events/" + this.eventId + "/suggestions";

      console.log(diffAdded, diffDeleted);

      if (diffAdded.length)
        axios
          .put(endpoint, {
            genres: diffAdded
          })
          .then(response => {
            this.genresPristine = this.getSelectedGenres(
              response.data.playlist.suggestions.fromGuests.genres
            );
          });

      if (diffDeleted.length)
        axios
          .delete(endpoint, {
            data: {
              genres: diffDeleted
            }
          })
          .then(response => {
            this.genresPristine = this.getSelectedGenres(
              response.data.playlist.suggestions.fromGuests.genres
            );
          });
    },
    getSelectedGenres(genres) {
      return genres.map(genre => (genre.n > 0 ? genre.element : null));
    }
  },
  mounted() {
    this.getGenres();
    this.genresPristine = this.getSelectedGenres(this.genres);
    this.selectedGenres = [...this.genresPristine];
  }
};
</script>
