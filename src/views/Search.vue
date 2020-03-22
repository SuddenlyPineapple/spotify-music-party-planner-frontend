<template>
  <v-container>
    <v-row>
      <v-col cols="12">
        <Header text="Search" />
      </v-col>

      <v-col cols="12" v-if="error">
        <ErrorMessage
          message="There is a problem with Spotify API or our backend. Please contact Administraor."
        />
      </v-col>

      <v-col cols="12">
        <v-text-field
          v-model="query"
          @keyup="searchMusic"
          solo
          placeholder="Type to search music..."
        />
      </v-col>

      <v-col cols="12">
        <SongList :tracks="tracks" />
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import Header from "../components/Header";
import SongList from "../components/SongList";
import ErrorMessage from "../components/ErrorMessage";
import axios from "axios";
import { apiUrl } from "../config/backend";

export default {
  name: "Search",
  components: {
    Header,
    SongList,
    ErrorMessage
  },
  data: () => ({
    query: "",
    tracks: [],
    error: ""
  }),
  methods: {
    searchMusic() {
      this.error = "";
      if (this.query) {
        axios
          .get(apiUrl + "explore/search?query=" + this.query)
          .then(response => {
            if (response.status != 200) this.error = "Response error";
            else this.tracks = response.data.tracks;
          })
          .catch(error => {
            this.error = error;
          });
      }
    }
  }
};
</script>
