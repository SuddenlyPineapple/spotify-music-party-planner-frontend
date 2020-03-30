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
    </v-row>
  </v-container>
</template>

<script>
import Header from "../components/Header";
import SongList from "../components/SongList";
import axios from "axios";
import { apiUrl } from "../config/backend";

export default {
  name: "Event",
  props: ["id"],
  data: () => ({
    event: null,
    loading: false,
    error: ""
  }),
  components: {
    Header,
    SongList
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
    setError(err) {
      this.error = err;
      this.loading = false;
    }
  },
  mounted() {
    this.getEvent();
  }
};
</script>
