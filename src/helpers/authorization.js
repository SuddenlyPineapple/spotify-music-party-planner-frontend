import { redirectToURL } from "../config/auth.js";

export const checkAutorization = () => {
  let tokenValue = null;
  let errorValue = "";

  const token = localStorage.getItem("ACCESS_TOKEN");
  if (token) {
    tokenValue = token;
  }

  const error = /error=/.exec(window.location.hash);
  if (error) {
    errorValue = "Error signing in. Please try again later.";
  }

  const match = /access_token=([^&]*)/.exec(window.location.hash);
  if (match) {
    const accessToken = decodeURIComponent(match[1].replace(/\+g/, " "));

    tokenValue = accessToken;
    localStorage.setItem("ACCESS_TOKEN", accessToken);

    history.pushState(
      "",
      document.title,
      window.location.pathname + window.location.search
    );
  }

  return {
    token: tokenValue,
    error: errorValue
  };
};

export const redirectSpotifyAuth = () => {
  window.location = redirectToURL(window.location.pathname);
};
