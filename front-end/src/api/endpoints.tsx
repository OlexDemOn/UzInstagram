import { fetchClient } from "./fetch";

const SERVER = import.meta.env.VITE_SERVER_URL;

const LOGIN_URL = `${SERVER}/user/login`;
const REGISTER_URL = `${SERVER}/user/register`;

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
