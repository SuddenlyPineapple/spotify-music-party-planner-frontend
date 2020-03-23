import { redirectToURL } from "../config/auth.js";

const checkTokenExpiration = () => {
  const expire_date = localStorage.getItem("TOKEN_EXPIRES");
  if (expire_date && new Date(expire_date) < new Date()) {
    localStorage.removeItem("ACCESS_TOKEN");
    localStorage.removeItem("TOKEN_EXPIRES");
    return false;
  }
  return true;
};

export const checkAutorization = () => {
  let tokenValue = null;
  let errorValue = "";

  const token = localStorage.getItem("ACCESS_TOKEN");
  if (token && checkTokenExpiration()) {
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

    let expire_date = new Date();
    expire_date.setHours(expire_date.getHours() + 1);
    localStorage.setItem("TOKEN_EXPIRES", expire_date);

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
