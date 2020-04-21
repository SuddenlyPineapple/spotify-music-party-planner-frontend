<template>
  <div>
    <v-container>
      <v-row v-if="error">
        <v-col cols="12">
          <ErrorMessage :message="error" />
        </v-col>
      </v-row>
      <v-row v-else>
        <v-col cols="12">
          <Header text="Event" />
          <EventInfo :event="event" />
        </v-col>
        <v-col cols="12" class="py-0">
          <v-btn
            @click="$router.push('/search/' + id)"
            color="accent"
            dark
            class="ma-2"
          >
            <v-icon>mdi-plus</v-icon> Add Songs
          </v-btn>
          <v-btn @click="getEvent" color="accent" dark class="ma-2">
            <v-icon>mdi-refresh</v-icon> Refresh Data
          </v-btn>
          <v-btn
            v-if="!!userToken"
            @click="syncPlaylist"
            color="accent"
            :disabled="syncLoading"
            :dark="!syncLoading"
            class="ma-2"
          >
            <v-icon v-if="syncLoading">mdi-sync mdi-spin</v-icon>
            <v-icon v-else>mdi-sync</v-icon>
            Sync With Spotify
          </v-btn>
          <DeleteEventModal
            v-if="event && event.id && event.name && !!userToken"
            :eventId="event.id"
            :eventName="event.name"
          />
        </v-col>
        <v-col cols="12" class="pb-0">
          <span
            class="theme--light v-label v-label--active caption small-spacing"
          >
            Song list - based on participants and yours suggestions
          </span>
        </v-col>
        <v-col cols="12" v-if="event" class="py-0">
          <SongList
            :tracks="
              event && event.playlist && event.playlist.tracks
                ? event.playlist.tracks
                : []
            "
            :eventId="event.id"
            :eventView="true"
            :loading="loading"
          />
        </v-col>
      </v-row>
      <v-row>
        <v-col cols="12" class="pt-0">
          <Header text="Recomendations" :secondary="true" />
        </v-col>
        <v-col cols="12">
          <GenresManager
            v-if="event"
            :eventId="id"
            :genres="event.playlist.suggestions.fromGuests.genres"
          />
        </v-col>
        <v-col cols="12">
          <SuggestedSongs
            v-if="event"
            :eventId="id"
            :recomendations="
              event.playlist.suggestions.fromRecommendations.tracks
            "
            :pristineSelected="event.playlist.tracks.map(track => track.id)"
            :loading="loading"
            @eventUpdated="fillEventData"
            @refreshData="getEvent"
          />
        </v-col>
      </v-row>
    </v-container>
    <v-snackbar
      v-model="showInfo"
      bottom
      right
      color="accent"
      multi-line
      :timeout="2000"
    >
      Playlist synchronization completed!
      <v-btn dark text @click="showInfo = false" fab>
        <v-icon>mdi-close</v-icon>
      </v-btn>
    </v-snackbar>
  </div>
</template>

<script>
import Header from "../components/Header";
import SongList from "../components/SongList";
import EventInfo from "../components/EventInfo";
import SuggestedSongs from "../components/SuggestedSongs";
import GenresManager from "../components/GenresManager";
import DeleteEventModal from "../components/DeleteEventModal";
import ErrorMessage from "../components/ErrorMessage";
import { mapActions } from "vuex";
import axios from "axios";
import { apiUrl } from "../config/backend";

export default {
  name: "Event",
  props: ["id"],
  data: () => ({
    event: null,
    loading: true,
    error: "",
    userToken: null,
    syncLoading: false,
    showInfo: false
  }),
  computed: {
    ...mapActions(["getUserToken"])
  },
  components: {
    Header,
    SongList,
    SuggestedSongs,
    DeleteEventModal,
    ErrorMessage,
    EventInfo,
    GenresManager
  },
  methods: {
    getEvent() {
      this.loading = true;
      axios
        .get(apiUrl + "events/" + this.id)
        .then(response => {
          if (response.status == 200) this.event = response.data;
        })
        .catch(this.setError)
        .finally(() => {
          this.loading = false;
        });
    },
    syncPlaylist() {
      this.syncLoading = true;
      axios
        .put(apiUrl + "events/" + this.id + "/synchronize", {
          userToken: this.userToken
        })
        .then(response => {
          if (response.status == 200) {
            this.showInfo = true;
          } else throw "Request Failed";
        })
        .catch(this.setError)
        .finally(() => {
          this.syncLoading = false;
        });
    },
    setError(error) {
      if (error == "Error: Request failed with status code 404")
        this.error =
          "Event that you are referring to by URL doesn't exist anymore, go to dashboard to create new one or try refresh page.";
      else
        this.error =
          "There is a problem with Spotify API or our backend. Try refresh page or contact Administrator.";
    },
    async fillEventData(eventData) {
      this.loading = true;
      this.event.playlist.tracks = eventData.playlist.tracks;
      this.loading = false;
    }
  },
  async mounted() {
    this.getEvent();
    this.userToken = await this.getUserToken;
  }
};
</script>
