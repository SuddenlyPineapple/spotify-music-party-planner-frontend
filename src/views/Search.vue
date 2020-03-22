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
    </v-row>
  </v-container>
</template>

<script>
import Header from "../components/Header";
import ErrorMessage from "../components/ErrorMessage";

export default {
  name: "Search",
  components: {
    Header,
    ErrorMessage
  },
  data: () => ({
    query: "",
    error: ""
  }),
  methods: {
    searchMusic() {
      console.log("123");
    }
  }
};
</script>
