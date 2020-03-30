<template>
  <v-container>
    <v-row>
      <v-col cols="12">
        <Header text="Event" />
        {{ id }}
      </v-col>
      <v-col cols="12">
        <v-btn @click="$router.push('/search/' + id)" color="accent" dark>
          Add Songs
        </v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import Header from "../components/Header";

export default {
  name: "Event",
  props: ["id"],
  components: {
    Header
  }
};
</script>
