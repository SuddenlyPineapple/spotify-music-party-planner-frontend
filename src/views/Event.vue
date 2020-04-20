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
            Add Songs
          </v-btn>
          <v-btn @click="getEvent" color="accent" dark class="ma-2">
            Refresh Playlist
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
        <v-col cols="12">
          <DeleteEventModal
            v-if="event && event.id && event.name && !!userToken"
            :eventId="event.id"
            :eventName="event.name"
          />
        </v-col>
      </v-row>
      <v-row>
        <Header text="Suggested Songs" />
        <SuggestedSongs :event="event" />
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
    loading: false,
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
    EventInfo
  },
  methods: {
    getEvent() {
      this.loading = true;
      axios
        .get(apiUrl + "events/" + this.id)
        .then(response => {
          if (response.status == 200) this.event = response.data;
          this.loading = false;
        })
        .catch(this.setError);
    },
    syncPlaylist() {
      this.syncLoading = true;
      axios
        .put(apiUrl + "events/" + this.id + "/synchronize", {
          userToken: this.userToken
        })
        .then(response => {
          if (response.status == 200) {
            this.syncLoading = false;
            this.showInfo = true;
          } else throw "Request Failed";
        })
        .catch(this.setError);
    },
    setError(error) {
      if (error == "Error: Request failed with status code 404")
        this.error =
          "Event that you are referring to by URL doesn't exist anymore, go to dashboard to create new one or try refresh page.";
      else
        this.error =
          "There is a problem with Spotify API or our backend. Try refresh page or contact Administrator.";
      this.loading = false;
      this.syncLoading = false;
    }
  },
  async mounted() {
    this.getEvent();
    this.userToken = await this.getUserToken;
  }
};
</script>
