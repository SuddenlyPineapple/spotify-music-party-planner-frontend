<template>
  <v-container>
    <v-row>
      <v-col cols="12">
        <HelloWorld msg="HomePage" />
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import HelloWorld from "../components/HelloWorld";

export default {
  components: {
    HelloWorld
  }
};
</script>
