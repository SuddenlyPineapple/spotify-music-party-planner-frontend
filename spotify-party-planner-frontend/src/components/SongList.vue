<template>
  <v-container>
    <v-row>
      <v-col cols="12" class="text-center" v-if="loading">
        <v-progress-circular
          :size="70"
          :width="7"
          color="accent"
          indeterminate
        ></v-progress-circular>
      </v-col>
      <v-col cols="12" v-else>
        <v-data-table
          :items="tracks"
          :headers="headers"
          :items-per-page="10"
          class="elevation-1"
          multi-sort
        >
          <template v-slot:item.album="{ item }">
            <img
              v-if="item.album.img.length == 3"
              :src="item.album.img[2].url"
              height="64"
              width="64"
            />
            <img
              v-else
              src="../assets/default-album-artwork.png"
              height="64"
              width="64"
            />
          </template>

          <template v-slot:item.id="{ item }">
            <v-btn
              @click="addSong(item.id)"
              color="accent"
              dark
              icon
              :ripple="false"
            >
              <v-icon v-if="inPlaylist(item.id)">mdi-heart</v-icon>
              <v-icon v-else>mdi-heart-outline</v-icon>
            </v-btn>
          </template>
        </v-data-table>
      </v-col>
    </v-row>

    <v-snackbar
      v-model="showError"
      bottom
      color="red"
      multi-line
      right
      :timeout="6000"
    >
      {{ error }}
      <v-btn dark text @click="showError = false" fab>
        <v-icon>mdi-close</v-icon>
      </v-btn>
    </v-snackbar>
  </v-container>
</template>

<script>
import axios from "axios";
import { apiUrl } from "../config/backend";

export default {
  name: "SongList",
  props: ["tracks", "loading", "eventId", "eventView"],
  data: () => ({
    selected: [],
    error: "",
    showError: false
  }),
  computed: {
    headers() {
      return this.eventId
        ? [
            { text: "Cover", sortable: false, value: "album" },
            { text: "Title", value: "name" },
            { text: "Artist", value: "artist" },
            { text: "Album", value: "album.name" },
            { text: "Duration", value: "duration" },
            { text: "Add to playlist", sortable: false, value: "id" }
          ]
        : [
            { text: "Cover", sortable: false, value: "album" },
            { text: "Title", value: "name" },
            { text: "Artist", value: "artist" },
            { text: "Album", value: "album.name" },
            { text: "Duration", value: "duration" }
          ];
    }
  },
  methods: {
    setError(error) {
      this.showError = true;
      if (error == "Error: Request failed with status code 404")
        this.error =
          "Error: 404. Probably your Event ID in URL is invalid or event you refering to doesn't exists anymore.";
      else this.error = error;
    },
    addSong(songId) {
      const endpoint = apiUrl + "events/" + this.eventId + "/tracks";

      if (this.inPlaylist(songId)) {
        axios
          .delete(endpoint, {
            data: {
              trackIds: [songId]
            }
          })
          .then(response => {
            if (response.status == 200)
              this.selected = this.selected.filter(song => song != songId);
          })
          .catch(this.setError);
      } else {
        axios
          .put(endpoint, {
            trackIds: [songId]
          })
          .then(response => {
            if (response.status == 200) this.selected.push(songId);
          })
          .catch(this.setError);
      }
    },
    inPlaylist(songId) {
      return !!this.selected.filter(song => song == songId).length;
    },
    getSelected() {
      axios
        .get(apiUrl + "events/" + this.eventId)
        .then(response => {
          this.selected = response.data.playlist.tracks.map(track => track.id);
        })
        .catch(this.setError);
    }
  },
  mounted() {
    if (this.eventId && !this.eventView) this.getSelected();
    else this.selected = this.tracks.map(track => track.id);
  },
  updated() {
    if (this.eventId && this.eventView)
      this.selected = this.tracks.map(track => track.id);
  }
};
</script>
