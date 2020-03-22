<template>
  <v-container>
    <v-row>
      <v-col cols="12">
        <Header text="Dashboard" />
      </v-col>
      <v-col cols="12">
        <h1 v-if="auth && auth.token">token {{ auth.token }}</h1>
        <LogonButton v-else :handleAuth="authHandler" />
        <!-- <EventManger v-if="token" :data="data" /> -->
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import Header from "../components/Header";
import LogonButton from "../components/LogonButton";
import {
  redirectSpotifyAuth,
  checkAutorization
} from "../helpers/authorization";

export default {
  name: "Dashboard",
  components: {
    Header,
    LogonButton
  },
  data: () => ({
    authHandler: redirectSpotifyAuth
  }),
  computed: {
    auth: () => checkAutorization()
  },
  created() {}
};
</script>
