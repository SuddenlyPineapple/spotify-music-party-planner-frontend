const CLIENT_ID = "a9671d2e406d49fe86ab96e94f4e68a5";

const SCOPES = encodeURIComponent(
  "user-library-read playlist-modify-private playlist-read-private playlist-modify-public playlist-read-collaborative user-library-modify user-library-read"
);

export const GITHUB_DEPLOY = false

const REDIRECT_URI = process.env.NODE_ENV === "production"
    ? encodeURIComponent(GITHUB_DEPLOY ? "https://suddenlypineapple.github.io" : "http://jrie.eu")
    : encodeURIComponent("http://localhost:8080");

export const redirectToURL = route => `https://accounts.spotify.com/authorize?client_id=${CLIENT_ID}&response_type=token&scope=${SCOPES}&redirect_uri=${REDIRECT_URI}${route}`;
