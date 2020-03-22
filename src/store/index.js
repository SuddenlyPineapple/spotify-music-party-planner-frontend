import Vue from "vue";
import Vuex from "vuex";
import axios from "axios";
import { checkAutorization } from "../helpers/authorization";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    drawer: false,
    userData: undefined,
    token: "",
    routes: [
      {
        text: "Dashboard",
        to: "/"
      },
      {
        text: "Search",
        to: "/search"
      },
      {
        text: "About",
        to: "/about"
      }
    ]
  },
  getters: {
    routes: state => state.routes,
    getUserData: state => state.userData,
    getToken: state => {
      if (state.token) return state.token;
      const authCheck = checkAutorization();
      return authCheck.token;
    }
  },
  mutations: {
    setDrawer: (state, payload) => (state.drawer = payload),
    toggleDrawer: state => (state.drawer = !state.drawer),
    setUserData: (state, payload) => (state.userData = payload),
    setToken: (state, payload) => (state.token = payload)
  },
  actions: {
    getUserData(context) {
      return new Promise((resolve, reject) => {
        if (typeof this.state.userData === "undefined") {
          const settings = {
            async: true,
            crossDomain: true,
            url: "https://api.spotify.com/v1/me",
            method: "GET",
            headers: {
              Authorization: "Bearer " + this.getters.getToken
            }
          };

          axios(settings)
            .then(response => {
              context.commit("setUserData", response.data);
              resolve(response.data);
            })
            .catch(error => {
              reject(error);
            });
        } else {
          resolve(this.state.userData);
        }
      });
    }
  }
});
