<template>
  <v-container>
    <v-row>
      <v-col cols="12">
        <Header text="Dashboard - Your Events" />
      </v-col>

      <v-col cols="12" v-if="auth.error || error">
        <ErrorMessage
          message="There is a problem with Spotify API or our backend. Try refresh page or contact Administrator."
        />
      </v-col>

      <v-col cols="12">
        <EventList
          v-if="auth && auth.token"
          :events="events"
          :loading="loading"
        />
        <LogonButton v-else :handleAuth="authHandler" />
      </v-col>
      <v-col>
        <AddEventModal
          v-if="auth && auth.token"
          :userId="userId"
          @saved="getEvents"
        />
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import Header from "../components/Header";
import LogonButton from "../components/LogonButton";
import ErrorMessage from "../components/ErrorMessage";
import EventList from "../components/EventList";
import AddEventModal from "../components/AddEventModal";

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
    EventList,
    AddEventModal
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
  methods: {
    async getEvents() {
      if (this.auth && this.auth.token)
        try {
          const user = await this.getUserData;

          if (user.id) {
            this.userId = user.id;
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
  },
  mounted() {
    let redirect = sessionStorage.redirect;
    delete sessionStorage.redirect;
    if (redirect && redirect != location.pathname) {
      const path = redirect.match(/\/(event|search|about|404)\/?[\d\w]{0,24}/);
      if (path && path.length > 0) this.$router.push(path[0]);
    }

    this.getEvents();
  }
};
</script>
