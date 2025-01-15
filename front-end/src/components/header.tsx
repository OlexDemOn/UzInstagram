import { Link, useNavigate } from "@tanstack/react-router";
import React from "react";
import { twMerge } from "tailwind-merge";
import { ModeToggle } from "./mode-toggle";
import { useAuth } from "../../providers/AuthProvider";
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuSeparator,
    DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { FaNetworkWired } from "react-icons/fa";
const menuItems = [
    { label: "Main", href: "/" },
    { label: "Create post", href: "/createPost" },
    { label: "Saved posts", href: "/saved" },
];

const Header = React.forwardRef<
    HTMLDivElement,
    React.HTMLProps<HTMLDivElement>
>((props, ref) => {
    return (
        <header
            className={twMerge(
                "flex items-center justify-between sticky w-full z-50 bg-background top-0 h-16 p-4",
                props.className
            )}
            ref={ref}
            {...props}
        >
            <Logo />
            <div className="absolute left-1/2 transform -translate-x-1/2">
                <Navigation />
            </div>
            <div className="flex gap-x-4 w-30">
                <ModeToggle />
                <User />
            </div>
        </header>
    );
});

const Logo = React.forwardRef<HTMLDivElement, React.HTMLProps<HTMLDivElement>>(
    (props, ref) => {
        return (
            <div
                className={twMerge("flex items-center w-40", props.className)}
                ref={ref}
                {...props}
            >
                <span className="font-semibold flex items-center gap-x-2">
                    <FaNetworkWired size={30} />
                    Social Network
                </span>
            </div>
        );
    }
);

const Navigation = React.forwardRef<
    HTMLDivElement,
    React.HTMLProps<HTMLDivElement>
>((props, ref) => {
    return (
        <nav
            className={twMerge(
                "flex items-center gap-x-3 justify-center",
                props.className
            )}
            ref={ref}
            {...props}
        >
            {menuItems.map((item, index) => (
                <Link
                    className="hover:text-primary transition-all"
                    key={index}
                    to={item.href}
                >
                    {item.label}
                </Link>
            ))}
        </nav>
    );
});

const User = React.forwardRef<HTMLDivElement, React.HTMLProps<HTMLDivElement>>(
    (props, ref) => {
        const auth = useAuth();
        const navigate = useNavigate();

        const handleLogout = () => {
            auth.logout();
            navigate("/");
            window.location.reload();
        };

        return (
            <div
                className={twMerge("flex items-center", props.className)}
                ref={ref}
                {...props}
            >
                <DropdownMenu>
                    <DropdownMenuTrigger className="hover:text-primary transition-all">
                        Profile
                    </DropdownMenuTrigger>
                    <DropdownMenuContent>
                        <DropdownMenuLabel>Username: </DropdownMenuLabel>
                        <Link to={`/profile/${auth.user?.username}`}>
                            <DropdownMenuItem className="hover:cursor-pointer">
                                {auth.user?.username}
                            </DropdownMenuItem>
                        </Link>
                        <Link to="/profileinfo">
                            <DropdownMenuItem className="hover:cursor-pointer">
                                Settings
                            </DropdownMenuItem>
                        </Link>
                        <DropdownMenuSeparator />
                        <DropdownMenuItem
                            onClick={handleLogout}
                            className="hover:cursor-pointer"
                        >
                            Logout
                        </DropdownMenuItem>
                    </DropdownMenuContent>
                </DropdownMenu>
            </div>
        );
    }
);

export default Header;
