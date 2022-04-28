export const getters = {
  isLoggedIn(state) {
    return state.auth.loggedIn
  },

  username(state) {
    return state.auth.user.username
  }


}
