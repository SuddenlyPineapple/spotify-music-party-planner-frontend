<template>
  <v-app-bar app dark color="primary">
    <v-app-bar-nav-icon class="hidden-md-and-up" @click="toggleDrawer" />
    <v-toolbar-title class="headline text-uppercase">
      <span class="accent--text">SPOTIFY</span>
      <span class="font-weight-light">PARTY</span>
      <span class="accent--text">MUSIC</span>
      <span class="font-weight-light">PLANNER</span>
    </v-toolbar-title>
    <v-spacer></v-spacer>
    <v-toolbar-items class="hidden-sm-and-down">
      <v-btn
        v-for="(route, i) in routes"
        :key="i"
        :to="route.to"
        class="ml-0 hidden-sm-and-down"
      >
        {{ route.text }}
      </v-btn>
    </v-toolbar-items>
  </v-app-bar>
</template>

<script>
import { mapGetters, mapMutations } from "vuex";

export default {
  computed: {
    ...mapGetters(["routes"])
  },
  methods: {
    ...mapMutations(["toggleDrawer"])
  }
};
</script>
