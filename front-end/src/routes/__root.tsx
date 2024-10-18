import { createRootRoute, Outlet } from "@tanstack/react-router";

export const Route = createRootRoute({
    component: () => (
        <div className="flex relative flex-1">
            <Outlet />
        </div>
    ),
});
