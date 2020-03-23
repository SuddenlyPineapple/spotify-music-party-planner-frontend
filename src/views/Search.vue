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
        <SongList :tracks="tracks" :loading="loading" />
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
    loading: false,
    error: ""
  }),
  methods: {
    searchMusic() {
      this.error = "";

      if (this.query) {
        this.loading = true;
        axios
          .get(apiUrl + "explore/search?query=" + encodeURI(this.query))
          .then(response => {
            if (response.status == 200) this.tracks = response.data.tracks;
            else if (response.status == 204) this.tracks = [];
            else this.error = "Response error";
            this.loading = false;
          })
          .catch(error => {
            this.error = error;
            this.loading = false;
          });
      }
    }
  }
};
</script>
