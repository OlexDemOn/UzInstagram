/* prettier-ignore-start */

/* eslint-disable */

// @ts-nocheck

// noinspection JSUnusedGlobalSymbols

// This file is auto-generated by TanStack Router

// Import Routes

import { Route as rootRoute } from './routes/__root'
import { Route as RegisterImport } from './routes/register'
import { Route as LoginImport } from './routes/login'
import { Route as AuthImport } from './routes/_auth'
import { Route as AuthIndexImport } from './routes/_auth.index'

// Create/Update Routes

const RegisterRoute = RegisterImport.update({
  path: '/register',
  getParentRoute: () => rootRoute,
} as any)

const LoginRoute = LoginImport.update({
  path: '/login',
  getParentRoute: () => rootRoute,
} as any)

const AuthRoute = AuthImport.update({
  id: '/_auth',
  getParentRoute: () => rootRoute,
} as any)

const AuthIndexRoute = AuthIndexImport.update({
  path: '/',
  getParentRoute: () => AuthRoute,
} as any)

// Populate the FileRoutesByPath interface

declare module '@tanstack/react-router' {
  interface FileRoutesByPath {
    '/_auth': {
      id: '/_auth'
      path: ''
      fullPath: ''
      preLoaderRoute: typeof AuthImport
      parentRoute: typeof rootRoute
    }
    '/login': {
      id: '/login'
      path: '/login'
      fullPath: '/login'
      preLoaderRoute: typeof LoginImport
      parentRoute: typeof rootRoute
    }
    '/register': {
      id: '/register'
      path: '/register'
      fullPath: '/register'
      preLoaderRoute: typeof RegisterImport
      parentRoute: typeof rootRoute
    }
    '/_auth/': {
      id: '/_auth/'
      path: '/'
      fullPath: '/'
      preLoaderRoute: typeof AuthIndexImport
      parentRoute: typeof AuthImport
    }
  }
}

// Create and export the route tree

interface AuthRouteChildren {
  AuthIndexRoute: typeof AuthIndexRoute
}

const AuthRouteChildren: AuthRouteChildren = {
  AuthIndexRoute: AuthIndexRoute,
}

const AuthRouteWithChildren = AuthRoute._addFileChildren(AuthRouteChildren)

export interface FileRoutesByFullPath {
  '': typeof AuthRouteWithChildren
  '/login': typeof LoginRoute
  '/register': typeof RegisterRoute
  '/': typeof AuthIndexRoute
}

export interface FileRoutesByTo {
  '/login': typeof LoginRoute
  '/register': typeof RegisterRoute
  '/': typeof AuthIndexRoute
}

export interface FileRoutesById {
  __root__: typeof rootRoute
  '/_auth': typeof AuthRouteWithChildren
  '/login': typeof LoginRoute
  '/register': typeof RegisterRoute
  '/_auth/': typeof AuthIndexRoute
}

export interface FileRouteTypes {
  fileRoutesByFullPath: FileRoutesByFullPath
  fullPaths: '' | '/login' | '/register' | '/'
  fileRoutesByTo: FileRoutesByTo
  to: '/login' | '/register' | '/'
  id: '__root__' | '/_auth' | '/login' | '/register' | '/_auth/'
  fileRoutesById: FileRoutesById
}

export interface RootRouteChildren {
  AuthRoute: typeof AuthRouteWithChildren
  LoginRoute: typeof LoginRoute
  RegisterRoute: typeof RegisterRoute
}

const rootRouteChildren: RootRouteChildren = {
  AuthRoute: AuthRouteWithChildren,
  LoginRoute: LoginRoute,
  RegisterRoute: RegisterRoute,
}

export const routeTree = rootRoute
  ._addFileChildren(rootRouteChildren)
  ._addFileTypes<FileRouteTypes>()

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
        "/_auth/"
      ]
    },
    "/login": {
      "filePath": "login.tsx"
    },
    "/register": {
      "filePath": "register.tsx"
    },
    "/_auth/": {
      "filePath": "_auth.index.tsx",
      "parent": "/_auth"
    }
  }
}
ROUTE_MANIFEST_END */
