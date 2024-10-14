import Header from "@/components/header";
import Footer from "@/components/footer";
import { createRootRoute, Outlet } from "@tanstack/react-router";

export const Route = createRootRoute({
    component: () => (
        <div className="w-full flex flex-1 flex-col h-full">
            <Header />
            <Outlet />
            <Footer />
        </div>
        // <TanStackRouterDevtools />
    ),
});
