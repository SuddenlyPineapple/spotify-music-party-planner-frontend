<template>
  <v-card color="red darken-2" dark class="pa-2">
    <strong>Error: </strong>{{ message }}
  </v-card>
</template>

<script>
export default {
  name: "ErrorMessage",
  props: ["message"]
};
</script>
