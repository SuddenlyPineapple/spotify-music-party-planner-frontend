module.exports = {
  transpileDependencies: ["vuetify"],
  publicPath:
    process.env.NODE_ENV === "production"
      ? "/spotify-music-party-planner/"
      : "/"
};
