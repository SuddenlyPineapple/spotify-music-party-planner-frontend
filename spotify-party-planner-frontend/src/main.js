import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import vuetify from "./plugins/vuetify";
import VueClipboard from "vue-clipboard2";
import "roboto-fontface/css/roboto/roboto-fontface.css";
import "@mdi/font/css/materialdesignicons.css";

import Adsense from "vue-google-adsense/dist/Adsense.min.js";
import InFeedAdsense from "vue-google-adsense/dist/InFeedAdsense.min.js";

Vue.use(require("vue-script2"));

Vue.use(Adsense);
Vue.use(InFeedAdsense);

Vue.use(VueClipboard);
VueClipboard.config.autoSetContainer = true;

Vue.config.productionTip = false;

new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
}).$mount("#app");
