<template>
  <v-container>
    <BackButton v-if="id" :path="'/event/' + id" />

    <v-row>
      <v-col cols="12">
        <Header text="Search" />
      </v-col>

      <v-col cols="12" v-if="error">
        <ErrorMessage
          message="There is a problem with Spotify API or our backend. Please contact Administrator."
        />
      </v-col>

      <v-col cols="12" class="pb-0 mb-0">
        <v-text-field
          v-model="query"
          solo
          placeholder="Type to search music..."
        />
      </v-col>

      <v-col cols="12" v-if="query" class="pt-0">
        <SongList :tracks="tracks" :loading="loading" :eventId="id" />
      </v-col>

      <v-col cols="12" v-else>
        <v-row justify="center">
          <v-col cols="12">
            <v-card-title class="pt-0 less-margin-top">
              <img src="../assets/arrow.png" class="arrow-position" />
              <h1 class="title">Search for songs!</h1>
            </v-card-title>
          </v-col>
          <v-col cols="10">
            <Adsense
              data-ad-client="ca-pub-1122450262680065"
              data-ad-slot="7842943805"
              data-ad-format="auto"
              data-full-width-responsive="true"
            />
          </v-col>
        </v-row>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import Header from "../components/Header";
import SongList from "../components/SongList";
import ErrorMessage from "../components/ErrorMessage";
import BackButton from "../components/BackButton";
import debounce from "lodash/debounce";
import axios from "axios";
import { apiUrl } from "@/config/backend";

export default {
  name: "Search",
  props: ["id"],
  components: {
    Header,
    SongList,
    BackButton,
    ErrorMessage
  },
  data: () => ({
    query: "",
    tracks: [],
    loading: false,
    error: ""
  }),
  watch: {
    // Answer why all defined methods are "function" instead of lambdas is here:
    // https://stackoverflow.com/questions/53041171/lodashs-debounce-not-working-in-vue-js
    query: function() {
      this.debouncedSearch();
    }
  },
  created: function() {
    this.debouncedSearch = debounce(this.searchMusic, 500);
  },
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
          })
          .catch(error => {
            this.error = error;
          })
          .finally(() => {
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
