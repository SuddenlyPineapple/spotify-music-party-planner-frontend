<template>
  <v-container>
    <v-row v-if="error">
      <v-col cols="12">
        <ErrorMessage :message="error" />
      </v-col>
    </v-row>
    <v-row v-else>
      <v-col cols="12">
        <Header text="Event" />
        <EventInfo :event="event" :tracks="tracks" />
      </v-col>
      <v-col cols="12">
        <v-btn @click="$router.push('/search/' + id)" color="accent" dark>
          Add Songs
        </v-btn>
        <v-btn @click="getEvent" color="accent" dark class="ml-5">
          Refresh Playlist
        </v-btn>
      </v-col>
      <v-col cols="12" v-if="event">
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
          v-if="event && event.id && event.name"
          :eventId="event.id"
          :eventName="event.name"
        />
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import Header from "../components/Header";
import SongList from "../components/SongList";
import EventInfo from "../components/EventInfo";
import DeleteEventModal from "../components/DeleteEventModal";
import ErrorMessage from "../components/ErrorMessage";
import axios from "axios";
import { apiUrl } from "../config/backend";

export default {
  name: "Event",
  props: ["id"],
  data: () => ({
    event: null,
    loading: false,
    tracks: [],
    error: ""
  }),
  components: {
    Header,
    SongList,
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
          this.tracks =
            this.event && this.event.playlist && this.event.playlist.tracks
              ? this.event.playlist.tracks
              : [];
        })
        .catch(this.setError);
    },
    setError(error) {
      if (error == "Error: Request failed with status code 404")
        this.error =
          "Event that you are referring to by URL doesn't exist anymore, go to dashboard to create new one or try refresh page.";
      else
        this.error =
          "There is a problem with Spotify API or our backend. Try refresh page or contact Administraor.";
      this.loading = false;
    }
  },
  mounted() {
    this.getEvent();
  }
};
</script>
