<template>
  <v-container>
    <v-row>
      <v-col cols="12">
        <Header text="About" />
        <p>
          <strong>
            <span class="accent--text">SPOTIFY</span>
            PARTY
            <span class="accent--text">MUSIC</span>
            PLANNER
          </strong>
          is an application that allows you to jointly plan playlists for social
          gatherings. The application is integrated with Spotify's REST API.
        </p>
        <p>
          It also team project created by IT students at
          <strong>Poznan University of Technology.</strong>
        </p>
        <strong>Authors:</strong>
        <ul>
          <li>Wojciech Kasperski</li>
          <li>Jakub Riegel</li>
          <li>Gracjan Trawiński</li>
          <li>Łukasz Wełnic</li>
        </ul>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import Header from "../components/Header";

export default {
  components: {
    Header
  }
};
</script>
