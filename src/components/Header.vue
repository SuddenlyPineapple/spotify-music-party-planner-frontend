<template>
  <h1 v-if="!secondary">{{ text }}</h1>
  <h2 v-else>{{ text }}</h2>
</template>

<script>
export default {
  name: "Header",
  props: ["text", "secondary"]
};
</script>
