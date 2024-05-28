import {createStore} from "vuex"
import createPersistedState from "vuex-persistedstate"

const store = createStore({
    state: {
        username: '',
    },

    getters: {
        isLoggedIn: state => {
            return state.username !== ''
        }
    },

    mutations: {
        setUsername: (state, username) => {
            state.username = username
        }
    },

    plugins: [
        createPersistedState()
    ]
})

export default store