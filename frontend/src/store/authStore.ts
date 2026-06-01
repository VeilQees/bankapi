import { create } from "zustand";

interface AuthState {

    accessToken: string | null;

    setToken: (token: string | null) => void;

    logout: () => void;
}

export const useAuthStore = create<AuthState>((set) => ({

    accessToken: localStorage.getItem("accessToken"),

    setToken: (token) => {

        if (token) {
            localStorage.setItem("accessToken", token);
        } else {
            localStorage.removeItem("accessToken");
        }

        set({
            accessToken: token,
        });
    },

    logout: () => {

        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");

        set({
            accessToken: null,
        });
    },
}));