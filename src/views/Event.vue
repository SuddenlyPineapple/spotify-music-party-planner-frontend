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
        <v-container cols="12" style="background-color: #2E2E2E; color:#FFF;">
          <v-row>
            <v-col style="margin:0; padding:0; padding-left:20px;">
              <v-img
                v-if="notBigEnough()"
                contain
                :src="getTrackThumbnail(0)"
                cols="4"
              />
              <v-row v-else v-for="m in 2" :key="m" style="margin:0; padding:0">
                <v-col
                  v-for="n in 2"
                  :key="n"
                  style="margin:0; padding:0"
                  md="auto"
                >
                  <v-img
                    v-if="m == 1"
                    :src="getTrackThumbnail(m + n - 2)"
                    width="100"
                  >
                  </v-img>
                  <v-img v-else :src="getTrackThumbnail(m + n - 1)" width="100">
                  </v-img>
                </v-col>
              </v-row>
            </v-col>
            <v-divider vertical></v-divider>
            <v-col cols="7">
              <div
                style="font-size:9px; letter-spacing: 10px; font-weight: 600;"
              >
                EVENT
              </div>
              <div
                style="font-size:27px; font-weight: 900; letter-spacing: 2px;"
              >
                {{ event.name }}
              </div>
              <div
                style="font-size:13px; letter-spacing: 3px; font-weight: 500; margin-top:-7px; margin-bottom:7px;"
              >
                Tracks: {{ tracks.length }}
              </div>
              <div
                style="font-size:13px; letter-spacing: 3px; font-weight: 500;"
              >
                Location:
              </div>
              <div
                style="font-size:15px; font-weight: 700; letter-spacing: 2px; margin-bottom:5px;"
              >
                {{ event.location }}
              </div>
              <div
                style="font-size:13px; letter-spacing: 3px; font-weight: 500;"
              >
                Date:
              </div>
              <div
                style="font-size:15px; font-weight: 700; letter-spacing: 2px; margin-bottom:5px;"
              >
                {{ event.date }}
              </div>
              <div
                style="font-size:13px; letter-spacing: 3px; font-weight: 500;"
              >
                Host:
              </div>
              <div
                style="font-size:15px; font-weight: 700; letter-spacing: 2px; margin-bottom:5px;"
              >
                {{ event.hostId }}
              </div>
            </v-col>
          </v-row>
        </v-container>
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
    ErrorMessage
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
    },
    getTrackThumbnail(n) {
      if (this.tracks.length != 0) {
        if (this.tracks[n].album.img.length >= 2) {
          console.log(this.tracks[n].album.img[1].url);
          return this.tracks[n].album.img[1].url;
        } else return this.tracks[n].album.img[0].url;
      } else return "../assets/default-album-artwork.png";
    },
    notBigEnough() {
      if (this.tracks.length < 4) return true;
      else return false;
    }
  },
  mounted() {
    this.getEvent();
  }
};
</script>
