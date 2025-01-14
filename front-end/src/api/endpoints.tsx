import { fetchClient } from "./fetch";

// eslint-disable-next-line react-refresh/only-export-components
const SERVER = import.meta.env.VITE_SERVER_URL;

const LOGIN_URL = `${SERVER}/user/login`;
const REGISTER_URL = `${SERVER}/user/register`;
const UPDATEPROFILE_URL = `${SERVER}/user/profile`;
const GET_PROFILE_URL = `${SERVER}/user/profile`;

const POST_URL = `${SERVER}/post`;

export async function test() {
    console.log(SERVER);
    return await fetchClient(`${SERVER}`, "GET");
}

export async function login({
    usernameOrEmail,
    password,
}: {
    usernameOrEmail: string;
    password: string;
}) {
    return await fetchClient(LOGIN_URL, "POST", { usernameOrEmail, password });
}

export async function register({
    email,
    username,
    password,
}: {
    email: string;
    username: string;
    password: string;
}) {
    return await fetchClient(REGISTER_URL, "POST", {
        email,
        username,
        password,
    });
}

export async function updateProfile({
    username,
    fullName,
    bio,
    profileImg,
}: {
    username: string;
    fullName: string;
    bio: string;
    profileImg: string;
}) {
    return await fetchClient(UPDATEPROFILE_URL, "PUT", {
        username,
        fullName,
        bio,
        profileImg,
    });
}

export async function getProfile(username: string) {
    return await fetchClient(GET_PROFILE_URL, "GET", null, username);
}

export async function getPost() {
    return await fetchClient(POST_URL, "GET");
}
