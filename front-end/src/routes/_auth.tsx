import Header from "@/components/header";
import Footer from "@/components/footer";
import { Outlet, createFileRoute, redirect } from "@tanstack/react-router";
import React from "react";

export const Route = createFileRoute("/_auth")({
    beforeLoad: ({ context, location }) => {
        // @ts-expect-error - auth is not in the types
        if (!context?.auth?.isAuthenticated) {
            throw redirect({
                to: "/login",
                search: {
                    redirect: location.href,
                },
            });
        }
    },
    component: AuthLayout,
});

function AuthLayout() {
    return (
        <React.Fragment>
            <main className="flex flex-1 flex-col gap-10 min-h-screen">
                <Header />
                <div className="flex-1">
                    <Outlet />
                </div>
                <Footer />
            </main>
        </React.Fragment>
    );
}
