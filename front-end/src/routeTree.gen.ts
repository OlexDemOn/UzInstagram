/* prettier-ignore-start */

/* eslint-disable */

// @ts-nocheck

// noinspection JSUnusedGlobalSymbols

// This file is auto-generated by TanStack Router

// Import Routes

import { Route as rootRoute } from "./routes/__root";
import { Route as RegisterImport } from "./routes/register";
import { Route as LoginImport } from "./routes/login";
import { Route as AuthImport } from "./routes/_auth";
import { Route as AuthIndexImport } from "./routes/_auth.index";
import { Route as AuthTopTenImport } from "./routes/_auth.topTen";
import { Route as AuthSavedImport } from "./routes/_auth.saved";
import { Route as AuthProfileInfoImport } from "./routes/_auth.profileInfo";
import { Route as AuthCreatePostImport } from "./routes/_auth.createPost";
import { Route as AuthProfileUsernameImport } from "./routes/_auth.profile.$username";

// Create/Update Routes

const RegisterRoute = RegisterImport.update({
    path: "/register",
    getParentRoute: () => rootRoute,
} as any);

const LoginRoute = LoginImport.update({
    path: "/login",
    getParentRoute: () => rootRoute,
} as any);

const AuthRoute = AuthImport.update({
    id: "/_auth",
    getParentRoute: () => rootRoute,
} as any);

const AuthIndexRoute = AuthIndexImport.update({
    path: "/",
    getParentRoute: () => AuthRoute,
} as any);

const AuthTopTenRoute = AuthTopTenImport.update({
    path: "/topTen",
    getParentRoute: () => AuthRoute,
} as any);

const AuthSavedRoute = AuthSavedImport.update({
    path: "/saved",
    getParentRoute: () => AuthRoute,
} as any);

const AuthProfileInfoRoute = AuthProfileInfoImport.update({
    path: "/profileInfo",
    getParentRoute: () => AuthRoute,
} as any);

const AuthCreatePostRoute = AuthCreatePostImport.update({
    path: "/createPost",
    getParentRoute: () => AuthRoute,
} as any);

const AuthProfileUsernameRoute = AuthProfileUsernameImport.update({
    path: "/profile/$username",
    getParentRoute: () => AuthRoute,
} as any);

// Populate the FileRoutesByPath interface

declare module "@tanstack/react-router" {
    interface FileRoutesByPath {
        "/_auth": {
            id: "/_auth";
            path: "";
            fullPath: "";
            preLoaderRoute: typeof AuthImport;
            parentRoute: typeof rootRoute;
        };
        "/login": {
            id: "/login";
            path: "/login";
            fullPath: "/login";
            preLoaderRoute: typeof LoginImport;
            parentRoute: typeof rootRoute;
        };
        "/register": {
            id: "/register";
            path: "/register";
            fullPath: "/register";
            preLoaderRoute: typeof RegisterImport;
            parentRoute: typeof rootRoute;
        };
        "/_auth/createPost": {
            id: "/_auth/createPost";
            path: "/createPost";
            fullPath: "/createPost";
            preLoaderRoute: typeof AuthCreatePostImport;
            parentRoute: typeof AuthImport;
        };
        "/_auth/profileInfo": {
            id: "/_auth/profileInfo";
            path: "/profileInfo";
            fullPath: "/profileInfo";
            preLoaderRoute: typeof AuthProfileInfoImport;
            parentRoute: typeof AuthImport;
        };
        "/_auth/saved": {
            id: "/_auth/saved";
            path: "/saved";
            fullPath: "/saved";
            preLoaderRoute: typeof AuthSavedImport;
            parentRoute: typeof AuthImport;
        };
        "/_auth/topTen": {
            id: "/_auth/topTen";
            path: "/topTen";
            fullPath: "/topTen";
            preLoaderRoute: typeof AuthTopTenImport;
            parentRoute: typeof AuthImport;
        };
        "/_auth/": {
            id: "/_auth/";
            path: "/";
            fullPath: "/";
            preLoaderRoute: typeof AuthIndexImport;
            parentRoute: typeof AuthImport;
        };
        "/_auth/profile/$username": {
            id: "/_auth/profile/$username";
            path: "/profile/$username";
            fullPath: "/profile/$username";
            preLoaderRoute: typeof AuthProfileUsernameImport;
            parentRoute: typeof AuthImport;
        };
    }
}

// Create and export the route tree

interface AuthRouteChildren {
    AuthCreatePostRoute: typeof AuthCreatePostRoute;
    AuthProfileInfoRoute: typeof AuthProfileInfoRoute;
    AuthSavedRoute: typeof AuthSavedRoute;
    AuthTopTenRoute: typeof AuthTopTenRoute;
    AuthIndexRoute: typeof AuthIndexRoute;
    AuthProfileUsernameRoute: typeof AuthProfileUsernameRoute;
}

const AuthRouteChildren: AuthRouteChildren = {
    AuthCreatePostRoute: AuthCreatePostRoute,
    AuthProfileInfoRoute: AuthProfileInfoRoute,
    AuthSavedRoute: AuthSavedRoute,
    AuthTopTenRoute: AuthTopTenRoute,
    AuthIndexRoute: AuthIndexRoute,
    AuthProfileUsernameRoute: AuthProfileUsernameRoute,
};

const AuthRouteWithChildren = AuthRoute._addFileChildren(AuthRouteChildren);

export interface FileRoutesByFullPath {
    "": typeof AuthRouteWithChildren;
    "/login": typeof LoginRoute;
    "/register": typeof RegisterRoute;
    "/createPost": typeof AuthCreatePostRoute;
    "/profileInfo": typeof AuthProfileInfoRoute;
    "/saved": typeof AuthSavedRoute;
    "/topTen": typeof AuthTopTenRoute;
    "/": typeof AuthIndexRoute;
    "/profile/$username": typeof AuthProfileUsernameRoute;
}

export interface FileRoutesByTo {
    "/login": typeof LoginRoute;
    "/register": typeof RegisterRoute;
    "/createPost": typeof AuthCreatePostRoute;
    "/profileInfo": typeof AuthProfileInfoRoute;
    "/saved": typeof AuthSavedRoute;
    "/topTen": typeof AuthTopTenRoute;
    "/": typeof AuthIndexRoute;
    "/profile/$username": typeof AuthProfileUsernameRoute;
}

export interface FileRoutesById {
    __root__: typeof rootRoute;
    "/_auth": typeof AuthRouteWithChildren;
    "/login": typeof LoginRoute;
    "/register": typeof RegisterRoute;
    "/_auth/createPost": typeof AuthCreatePostRoute;
    "/_auth/profileInfo": typeof AuthProfileInfoRoute;
    "/_auth/saved": typeof AuthSavedRoute;
    "/_auth/topTen": typeof AuthTopTenRoute;
    "/_auth/": typeof AuthIndexRoute;
    "/_auth/profile/$username": typeof AuthProfileUsernameRoute;
}

export interface FileRouteTypes {
    fileRoutesByFullPath: FileRoutesByFullPath;
    fullPaths:
        | ""
        | "/login"
        | "/register"
        | "/createPost"
        | "/profileInfo"
        | "/saved"
        | "/topTen"
        | "/"
        | "/profile/$username";
    fileRoutesByTo: FileRoutesByTo;
    to:
        | "/login"
        | "/register"
        | "/createPost"
        | "/profileInfo"
        | "/saved"
        | "/topTen"
        | "/"
        | "/profile/$username";
    id:
        | "__root__"
        | "/_auth"
        | "/login"
        | "/register"
        | "/_auth/createPost"
        | "/_auth/profileInfo"
        | "/_auth/saved"
        | "/_auth/topTen"
        | "/_auth/"
        | "/_auth/profile/$username";
    fileRoutesById: FileRoutesById;
}

export interface RootRouteChildren {
    AuthRoute: typeof AuthRouteWithChildren;
    LoginRoute: typeof LoginRoute;
    RegisterRoute: typeof RegisterRoute;
}

const rootRouteChildren: RootRouteChildren = {
    AuthRoute: AuthRouteWithChildren,
    LoginRoute: LoginRoute,
    RegisterRoute: RegisterRoute,
};

export const routeTree = rootRoute
    ._addFileChildren(rootRouteChildren)
    ._addFileTypes<FileRouteTypes>();

/* prettier-ignore-end */

/* ROUTE_MANIFEST_START
{
  "routes": {
    "__root__": {
      "filePath": "__root.tsx",
      "children": [
        "/_auth",
        "/login",
        "/register"
      ]
    },
    "/_auth": {
      "filePath": "_auth.tsx",
      "children": [
        "/_auth/createPost",
        "/_auth/profileInfo",
        "/_auth/saved",
        "/_auth/topTen",
        "/_auth/",
        "/_auth/profile/$username"
      ]
    },
    "/login": {
      "filePath": "login.tsx"
    },
    "/register": {
      "filePath": "register.tsx"
    },
    "/_auth/createPost": {
      "filePath": "_auth.createPost.tsx",
      "parent": "/_auth"
    },
    "/_auth/profileInfo": {
      "filePath": "_auth.profileInfo.tsx",
      "parent": "/_auth"
    },
    "/_auth/saved": {
      "filePath": "_auth.saved.tsx",
      "parent": "/_auth"
    },
    "/_auth/topTen": {
      "filePath": "_auth.topTen.tsx",
      "parent": "/_auth"
    },
    "/_auth/": {
      "filePath": "_auth.index.tsx",
      "parent": "/_auth"
    },
    "/_auth/profile/$username": {
      "filePath": "_auth.profile.$username.tsx",
      "parent": "/_auth"
    }
  }
}
ROUTE_MANIFEST_END */
