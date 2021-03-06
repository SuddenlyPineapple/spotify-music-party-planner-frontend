import Vue from "vue";
import VueRouter from "vue-router";
import Dashboard from "../views/Dashboard.vue";
import { GITHUB_DEPLOY } from "../config/auth";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "Dashboard",
    component: Dashboard
  },
  {
    path: "/search",
    name: "Search",
    component: () => import("../views/Search.vue")
  },
  {
    path: "/search/:id",
    name: "Search for event",
    props: true,
    component: () => import("../views/Search.vue")
  },
  {
    path: "/event/:id",
    name: "Event",
    props: true,
    component: () => import("../views/Event.vue")
  },
  {
    path: "/about",
    name: "About",
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/About.vue")
  },
  {
    path: "*",
    name: "404",
    component: () => import("../views/404.vue")
  }
];

const router = new VueRouter({
  routes,
  mode: "history",
  base:  process.env.NODE_ENV === "production" && GITHUB_DEPLOY
      ? "/spotify-music-party-planner-frontend/"
      : "/"
});

export default router;
