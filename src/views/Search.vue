<template>
  <v-container>
    <v-row>
      <v-col cols="12">
        <Header text="Search" />
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import Header from "../components/Header";

export default {
  name: "Search",
  components: {
    Header
  }
};
</script>
