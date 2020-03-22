<template>
  <v-container>
    <v-row>
      <v-col cols="12">
        <Header text="Dashboard - Your Events" />
      </v-col>

      <v-col cols="12" v-if="auth.error || error">
        <ErrorMessage
          message="There is a problem with Spotify API or our backend. Try refresh or contact Administraor."
        ></ErrorMessage>
      </v-col>

      <v-col cols="12">
        <EventList
          v-if="auth && auth.token"
          :events="events"
          :userId="userId"
          :loading="loading"
        />
        <LogonButton v-else :handleAuth="authHandler" />
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import Header from "../components/Header";
import LogonButton from "../components/LogonButton";
import ErrorMessage from "../components/ErrorMessage";
import EventList from "../components/EventList";

import { mapActions } from "vuex";
import { apiUrl } from "../config/backend";
import axios from "axios";

import {
  redirectSpotifyAuth,
  checkAutorization
} from "../helpers/authorization";

export default {
  name: "Dashboard",
  components: {
    Header,
    LogonButton,
    ErrorMessage,
    EventList
  },
  data: () => ({
    authHandler: redirectSpotifyAuth,
    events: [],
    userId: "",
    loading: false,
    error: ""
  }),
  computed: {
    auth: () => checkAutorization(),
    ...mapActions(["getUserData"])
  },
  async mounted() {
    if (this.auth && this.auth.token)
      try {
        const user = await this.getUserData;

        if (user.id) {
          this.loading = true;
          axios
            .get(apiUrl + "events?userId=" + user.id)
            .then(response => {
              this.events = response.data.events;
              this.loading = false;
            })
            .catch(() => {
              this.error = true;
              this.loading = false;
            });
        } else {
          this.auth = {
            ...this.auth,
            error: true
          };
        }
      } catch {
        this.error = true;
      }
  }
};
</script>
