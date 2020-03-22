<template>
  <div id="login" class="text-center">
    <h3>
      Plese login to your spotify account to be able define events, manage them
      & planning party playlist!
    </h3>
    <v-btn color="accent" @click="handleAuth">ZALOGUJ SIĘ</v-btn>
  </div>
</template>

<script>
export default {
  name: "LogonButton",
  props: ["handleAuth"]
};
</script>
