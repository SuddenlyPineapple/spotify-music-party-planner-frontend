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
          gatherings. The application is integrated with Spotify's REST API and
          GoogleAds.
        </p>
        <p>
          It's also team project created by IT students at
          <strong>Poznan University of Technology.</strong>
        </p>
        <strong>Authors:</strong>
        <ul class="mb-4">
          <li>Wojciech Kasperski</li>
          <li>Jakub Riegel</li>
          <li>Rafał Ewiak</li>
          <li>Gracjan Trawiński</li>
          <li>Łukasz Wełnic</li>
        </ul>
        <p>
          Whole project is open source and you can contribute to it on
          <a
            href="https://github.com/SuddenlyPineapple/spotify-music-party-planner-frontend"
            alt="github link"
            >GitHub</a
          >.
        </p>
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
