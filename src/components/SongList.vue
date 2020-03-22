<template>
  <v-container>
    <v-row>
      <v-col cols="12">
        {{ tracks }}
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  name: "SongList",
  props: ["tracks"]
};
</script>
