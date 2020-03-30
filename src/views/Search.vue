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

      <v-col cols="12" class="pb-0 mb-0">
        <v-text-field
          v-model="query"
          @keyup="searchMusic"
          solo
          placeholder="Type to search music..."
        />
      </v-col>

      <v-col cols="12" v-if="query" class="pt-0">
        <SongList :tracks="tracks" :loading="loading" :eventId="id" />
      </v-col>

      <div v-else>
        <v-card-title class="pt-0 less-margin-top">
          <img src="../assets/arrow.png" class="arrow-position" />
          <h1 class="title">Search for songs!</h1>
        </v-card-title>
      </div>
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
  props: ["id"],
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

<style scope lang="scss">
img {
  margin-top: 5px;
}
.arrow-position {
  max-height: 64px;
  margin-right: 10px;
}
.less-margin-top {
  margin-top: -15px;
}
</style>
