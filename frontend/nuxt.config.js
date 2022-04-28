import colors from "vuetify/es5/util/colors";

let authBaseUrl = process.env.AUTH_BASE_URL || "https://ni-bucket.auth.eu-west-1.amazoncognito.com";
let authClientId = process.env.AUTH_CLIENT_ID || "";
let selfUrl = "https://ni-bucket.5rfafrfgg8ba2.eu-west-1.cs.amazonlightsail.com";
// let selfUrl = "http://localhost:8081";


export default {
  ssr: false,

  target: "static",

  mode: 'spa',

  head: {
    titleTemplate: "%s - NI Bucket List",
    htmlAttrs: {
      lang: "en"
    },
    meta: [
      {charset: "utf-8"},
      {name: "viewport", content: "width=device-width, initial-scale=1"},
      {hid: "description", name: "description", content: ""}
    ],
    link: [{rel: "icon", type: "image/x-icon", href: "/favicon.ico"}]
  },

  css: [],

  plugins: [{
    src: "~/plugins/amplify.js",
  }],

  components: true,

  buildModules: ["@nuxtjs/vuetify"],

  modules: ["@nuxtjs/axios", "@nuxtjs/auth-next"],

  axios: {
    baseURL: `${selfUrl}/api`,
  },

  auth: {
    strategies: {
      awsCognito: {
        scheme: "oauth2",
        endpoints: {
          authorization: `${authBaseUrl}/login`,
          token: `${authBaseUrl}/oauth2/token`,
          userInfo: `${authBaseUrl}/oauth2/userInfo`,
          logout: `${authBaseUrl}/logout`
        },
        token: {
          property: "access_token",
          type: "Bearer",
          maxAge: 3600
        },
        refreshToken: {
          property: "refresh_token",
          maxAge: 60 * 60 * 24 * 30
        },
        responseType: "token",
        redirectUri: `${selfUrl}/login`,
        logoutRedirectUri: selfUrl,
        clientId: authClientId,
        scope: ["email", "openid", "profile"],
        codeChallengeMethod: "S256"
      }
    }
  },

  vuetify: {
    customVariables: ["~/assets/variables.scss"],
    theme: {
      dark: false,
      themes: {
        light: {
          primary: "#42b883",
          accent: colors.grey.darken3,
          secondary: colors.amber.darken3,
          info: colors.teal.lighten1,
          warning: colors.amber.base,
          error: colors.deepOrange.accent4,
          success: colors.green.accent3
        }
      }
    }
  },

  build: {},

  router: {
    middleware: ["auth"]
  }
};
