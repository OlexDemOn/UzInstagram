import ReactDOM from "react-dom/client";
import { RouterProvider, createRouter } from "@tanstack/react-router";
import "./index.css";
import { routeTree } from "./routeTree.gen";
import { ThemeProvider } from "./components/theme-provider";
import { AuthProvider, useAuth } from "../providers/AuthProvider";
import { QueryClient, QueryClientProvider } from "react-query";
import "./i18n";

const router = createRouter({ routeTree });

declare module "@tanstack/react-router" {
    interface Register {
        router: typeof router;
    }
}

function App() {
    const auth = useAuth();
    return <RouterProvider router={router} context={{ auth }} />;
}

const queryClient = new QueryClient();
const rootElement = document.getElementById("root")!;
if (!rootElement.innerHTML) {
    const root = ReactDOM.createRoot(rootElement);
    root.render(
        <QueryClientProvider client={queryClient}>
            <ThemeProvider defaultTheme="dark" storageKey="vite-ui-theme">
                <AuthProvider>
                    <App />
                </AuthProvider>
            </ThemeProvider>
        </QueryClientProvider>
    );
}
